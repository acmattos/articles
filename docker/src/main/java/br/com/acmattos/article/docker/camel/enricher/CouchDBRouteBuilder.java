package br.com.acmattos.article.docker.camel.enricher;

import br.com.acmattos.article.docker.camel.config.DbServerConfig;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static br.com.acmattos.article.docker.camel.enricher.ElasticsearchRouteBuilder.DIRECT_UPDATE_BRANCH_MISSING_DATA;
import static br.com.acmattos.article.docker.camel.enricher.ElasticsearchRouteBuilder.DUNS;

@Component
public class CouchDBRouteBuilder extends RouteBuilder {
    public static final String DIRECT_FIND_HEADQUARTER_DATA =
        "direct:findHeadquarterData";
    private static final String FIND_HEADQUARTER_DATA = "findHeadquarterData";
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String GZIP = "gzip";
    private static final String GET = "GET";
    private static final String D_COMPANY_NAME = "$.company_name";
    private static final String COMPANY = "company";
    private static final String D_BODY = "${body}";
    public static final String BULK_UPDATE_BUCKET = "bulkUpdateBucket";
    public static final String D_EXCHANGE_PROPERTY_HQ_DUNS = "${exchangeProperty.hqDuns}?";

    private final DbServerConfig dbServerConfig;
    private final int bulkUpdateBuckets;

    public CouchDBRouteBuilder(DbServerConfig dbServerConfig,
    @Value("${enricher.route.bulk-update.buckets}") int bulkUpdateBuckets) {
        this.dbServerConfig = dbServerConfig;
        this.bulkUpdateBuckets = bulkUpdateBuckets;
    }

    @Override
    public void configure() throws Exception {
        // Each 'duns' received will trigger a DB search for 'company_name'
        from(DIRECT_FIND_HEADQUARTER_DATA)
            .routeId(FIND_HEADQUARTER_DATA)
            .setHeader(ACCEPT_ENCODING, constant(GZIP))
            .setHeader(Exchange.HTTP_METHOD, constant(GET))
            .toD(dbServerConfig.toUrl(D_EXCHANGE_PROPERTY_HQ_DUNS))
            .choice()
               .when()
                  .jsonpath(D_COMPANY_NAME)
                     .split()
                         .jsonpath(D_COMPANY_NAME)
                         .streaming()
                         .setProperty(COMPANY, simple(D_BODY))
                         .process(this::generateBulkBucket)
                     .to(DIRECT_UPDATE_BRANCH_MISSING_DATA)
                .end()
            .endChoice()
            .end();
    }

    private void generateBulkBucket(Exchange exchange) {
        exchange.getMessage().setHeader(
            BULK_UPDATE_BUCKET, calculateBulkUpdateBucket(exchange));
        exchange.getMessage().setBody(null);
    }

    private int calculateBulkUpdateBucket(Exchange exchange) {
        return Math.floorMod(
            exchange.getProperty(DUNS, String.class).hashCode(),
            Math.max(1, bulkUpdateBuckets));
    }
}
