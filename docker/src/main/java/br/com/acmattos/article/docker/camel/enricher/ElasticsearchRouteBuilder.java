package br.com.acmattos.article.docker.camel.enricher;

import br.com.acmattos.article.docker.camel.config.EsServerConfig;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static br.com.acmattos.article.docker.camel.enricher.CouchDBRouteBuilder.BULK_UPDATE_BUCKET;
import static br.com.acmattos.article.docker.camel.enricher.CouchDBRouteBuilder.DIRECT_FIND_HEADQUARTER_DATA;
import static br.com.acmattos.article.docker.camel.enricher.EnricherRouteBuilder.ASC;

@Component
public class ElasticsearchRouteBuilder extends RouteBuilder {
    private static final String ID = "{ \"update\": { \"_id\": \"%s\" } }\n";
    private static final String PAYLOAD =
        "{ \"doc\": { \"company\": \"%s\", \"trade\": \"%s\" } }\n";
    private static final String ES_UPDATE_FAILURE_HTTP_RESPONSE_BODY =
        "ES Update Failure: [HTTP {}] – Response body:\n{}\n{}";
    private static final String DIRECT_FIND_BRANCHES_WITH_MISSING_DATA =
        "direct:findBranchesWithMissingData";
    private static final String FIND_BRANCHES_WITH_MISSING_DATA =
        "findBranchesWithMissingData";
    private static final String SEARCH_Q_COMPANY_NO_COMPANY_SIZE =
        "_search?q=company:NO_COMPANY%s&size=%d&";
    private static final String EMPTY = "";
    private static final String SORT_DUNS_KEYWORD_DESC = "&sort=duns.keyword:desc";
    public static final String DUNS = "duns";
    private static final String COMPANY = "company";
    private static final String HQ_DUNS = "hqDuns";
    private static final String HITS_HITS_SOURCE = "$.hits.hits[*]._source";
    private static final String D_DUNS = "$.duns";
    private static final String D_COMPANY = "$.company";
    protected static final String DIRECT_UPDATE_BRANCH_MISSING_DATA =
        "direct:updateBranchMissingData";
    private static final String UPDATE_BRANCH_MISSING_DATA =
        "updateBranchMissingData";
    private static final String BULK = "_bulk?filter_path=errors,took,items.*.status";

    private static final String _ID = "_id";
    private static final String POST = "POST";
    private static final String DUNS_SIZE = "Duns' Size: {}";

    private final EsServerConfig esServerConfig;
    private final ExecutorService esSearchPoolEecutorService;
    private final ExecutorService esBulkPoolEecutorService;
    private int completionSize;
    private int completionTimeout;
    private int bulkUpdateBuckets;

    public ElasticsearchRouteBuilder(EsServerConfig esServerConfig,
        @Qualifier("esSearchPoolExecutorService") ExecutorService esSearchPoolEecutorService,
        @Qualifier("esBulkPoolExecutorService") ExecutorService esBulkPoolExecutorService,
        @Value("${enricher.aggregation.completion-size}") int completionSize,
        @Value("${enricher.aggregation.completion-timeout}") int completionTimeout,
        @Value("${enricher.route.bulk-update.buckets}") int bulkUpdateBuckets) {
        this.esServerConfig = esServerConfig;
        this.esSearchPoolEecutorService = esSearchPoolEecutorService;
        this.esBulkPoolEecutorService = esBulkPoolExecutorService;
        this.completionSize = completionSize;
        this.completionTimeout = completionTimeout;
        this.bulkUpdateBuckets = bulkUpdateBuckets;
    }

    @Override
    public void configure() throws Exception {
        onException(HttpOperationFailedException.class)
            .handled(true)
            .process(exchange -> {
                HttpOperationFailedException cause =
                    exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                        HttpOperationFailedException.class);
                int status = cause.getStatusCode();
                String body = cause.getResponseBody();
                String payload = exchange.getMessage().getBody(String.class);
                log.error(ES_UPDATE_FAILURE_HTTP_RESPONSE_BODY,
                    status, body, payload, cause);
            })
            .stop();

        from(DIRECT_FIND_BRANCHES_WITH_MISSING_DATA)
            .routeId(FIND_BRANCHES_WITH_MISSING_DATA)
            .choice()
                .when(header(ASC).isEqualTo(true))
                    .to(esServerConfig.toUrl(SEARCH_Q_COMPANY_NO_COMPANY_SIZE
                        .formatted(EMPTY, esServerConfig.size())))
                .otherwise()
                    .to(esServerConfig.toUrl(SEARCH_Q_COMPANY_NO_COMPANY_SIZE
                        .formatted(SORT_DUNS_KEYWORD_DESC, esServerConfig.size())))
            .end()
            .split()
            .jsonpath(HITS_HITS_SOURCE)
            .streaming()
            .parallelProcessing()
            .executorService(esSearchPoolEecutorService)
            .setProperty(_ID, jsonpath(D_DUNS))
            .setProperty(DUNS, jsonpath(D_DUNS))
            .setProperty(COMPANY, jsonpath(D_COMPANY))
            .process(exchange -> {
                String duns = exchange.getProperty(DUNS, String.class);
                if (duns != null && duns.length() >= 11) {
                    String hqDuns = duns.substring(0, 11)
                        .replaceAll("-", "");
                    exchange.setProperty(HQ_DUNS, hqDuns);
                    exchange.setProperty(DUNS, duns);
                }
                exchange.getMessage().setBody(null);
            })
            .to(DIRECT_FIND_HEADQUARTER_DATA);

        final int minBulkSize = 200;
        final int effectiveCompletionSize =
            Math.max(minBulkSize, Math.max(1, completionSize / Math.max(1, bulkUpdateBuckets)));

        from(DIRECT_UPDATE_BRANCH_MISSING_DATA)
            .routeId(UPDATE_BRANCH_MISSING_DATA)
            .aggregate(header(BULK_UPDATE_BUCKET), new ExchangeAggregationStrategy())
            .completionSize(effectiveCompletionSize)
            .completionTimeout(completionTimeout)
            .eagerCheckCompletion()
            .ignoreInvalidCorrelationKeys()
            .parallelProcessing()
            .executorService(esBulkPoolEecutorService)
            .process(exchange -> {
                @SuppressWarnings("unchecked")
                List<Exchange> exchanges = exchange.getProperty(
                    Exchange.GROUPED_EXCHANGE, List.class);
                log.info(DUNS_SIZE, exchanges.size());
                StringBuilder bulkPayload = new StringBuilder();
                for (Exchange e : exchanges) {
                    String duns = e.getProperty(DUNS, String.class);
                    String company = e.getProperty(COMPANY, String.class);
                    bulkPayload.append(ID.formatted(duns.replaceAll("-", "")))
                        .append(PAYLOAD.formatted(company, company));
                }
                exchange.getIn().setBody(bulkPayload.toString());
                bulkPayload.setLength(0);
                exchanges.clear();
            })
            .setHeader(Exchange.HTTP_METHOD, constant(POST))
            .toD(esServerConfig.toUrl(BULK))
            .log("Update result: ${body}")
            .process(this::freeResources);
    }

    private void freeResources(Exchange exchange) {
        exchange.removeProperties("*");
        exchange.getMessage().removeHeaders("*");
        exchange.getMessage().setBody(null);
        exchange.setMessage(null);
    }
}
