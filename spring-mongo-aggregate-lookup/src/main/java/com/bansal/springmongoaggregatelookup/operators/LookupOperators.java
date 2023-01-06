package com.bansal.springmongoaggregatelookup.operators;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationSpELExpression;

public class LookupOperators {

    public static class Lookup implements AggregationSpELExpression {

        @Override
        public Document toDocument() {
            return AggregationExpression.super.toDocument();
        }

        @Override
        public Document toDocument(AggregationOperationContext context) {
            return null;
        }
    }
}
