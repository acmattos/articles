package br.com.acmattos.article.docker.camel.enricher;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class ExchangeAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            List<Exchange> list = new ArrayList<>();
            newExchange.setProperty(Exchange.GROUPED_EXCHANGE, list);
            list.add(newExchange);
            return newExchange;
        } else {
            List<Exchange> list = oldExchange.getProperty(
                Exchange.GROUPED_EXCHANGE, List.class);
            list.add(newExchange);
            return oldExchange;
        }
    }
}
