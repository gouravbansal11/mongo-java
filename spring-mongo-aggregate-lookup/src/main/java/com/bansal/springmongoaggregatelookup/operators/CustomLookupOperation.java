package com.bansal.springmongoaggregatelookup.operators;

import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.util.Assert;

import java.util.List;

public class CustomLookupOperation extends LookupOperation {

    public CustomLookupOperation(final List<String> lookUpStr) {
        Assert.notEmpty(lookUpStr, "From must not be null!");

        super("from", "","","");
    }
    public CustomLookupOperation(Field from, Field localField, Field foreignField, Field as) {
        super(from, localField, foreignField, as);
    }
}
