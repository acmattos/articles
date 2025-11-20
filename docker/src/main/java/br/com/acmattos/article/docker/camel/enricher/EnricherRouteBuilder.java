package br.com.acmattos.article.docker.camel.enricher;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

@Component
public class EnricherRouteBuilder extends RouteBuilder {
    private static final String TIMER_ENRICHER_PERIOD_ENRICHER_TIMER_PERIOD =
        "timer://enricher?period={{enricher.timer.period}}";
    private static final String ENRICHER = "enricher";
    public static final String STARTING_D_ROUTE_ID = "Starting: ${routeId}";
    public static final String ASC = "asc";
    private static final String DIRECT_FIND_BRANCHES_WITH_MISSING_DATA =
        "direct:findBranchesWithMissingData";

    private final ExecutorService executorService;
    private final boolean enable;

    public EnricherRouteBuilder(
        @Qualifier("threadPoolExecutorService") ExecutorService executorService,
        @Value("${enricher.enable}") boolean enable) {
        this.executorService = executorService;
        this.enable = enable;
    }

    @Override
    public void configure() throws Exception {
        if(enable) {
            from(TIMER_ENRICHER_PERIOD_ENRICHER_TIMER_PERIOD)
                .routeId(ENRICHER)
                .setBody(constant(Arrays.asList(true, false)))
                .split(body())
                    .parallelProcessing()
                    .executorService(executorService)
                    .setHeader(ASC, simple("${body}"))
                    .to(DIRECT_FIND_BRANCHES_WITH_MISSING_DATA)
                .end();
        } else {
            from("timer:enricher?repeatCount=1")
                .routeId(ENRICHER)
                .log("Enricher Disabled!")
                .end();
        }
    }
}
