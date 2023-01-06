package com.bansal.springmongoaggregatelookup.operators;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAggregationExpression {
    private final Object value;

    protected AbstractAggregationExpression(Object value) {
        this.value = value;
    }

    public Document toDocument(AggregationOperationContext context) {
        return this.toDocument(this.value, context);
    }

    public Document toDocument(Object value, AggregationOperationContext context) {
        return new Document(this.getMongoMethod(), this.unpack(value, context));
    }

    protected static List<Field> asFields(String... fieldRefs) {
        return ObjectUtils.isEmpty(fieldRefs) ? Collections.emptyList() : Fields.fields(fieldRefs).asList();
    }

    private Object unpack(Object value, AggregationOperationContext context) {
        if (value instanceof AggregationExpression) {
            return ((AggregationExpression)value).toDocument(context);
        } else if (value instanceof Field) {
            return context.getReference((Field)value).toString();
        } else if (value instanceof List) {
            List<Object> sourceList = (List)value;
            List<Object> mappedList = new ArrayList(sourceList.size());
            sourceList.stream().map((item) -> {
                return this.unpack(item, context);
            }).forEach(mappedList::add);
            return mappedList;
        } else if (value instanceof Map) {
            Document targetDocument = new Document();
            Map<String, Object> sourceMap = (Map)value;
            sourceMap.forEach((k, v) -> {
                targetDocument.append(k, this.unpack(v, context));
            });
            return targetDocument;
        } else {
            return value instanceof Aggregation.SystemVariable ? value.toString() : value;
        }
    }

    protected List<Object> append(Object value,  AbstractAggregationExpression.Expand expandList) {
        if (!(this.value instanceof List)) {
            return Arrays.asList(this.value, value);
        } else {
            List<Object> clone = new ArrayList((List)this.value);
            if (value instanceof Collection &&  AbstractAggregationExpression.Expand.EXPAND_VALUES.equals(expandList)) {
                clone.addAll((Collection)value);
            } else {
                clone.add(value);
            }

            return clone;
        }
    }

    protected List<Object> append(Object value) {
        return this.append(value,  AbstractAggregationExpression.Expand.EXPAND_VALUES);
    }

    protected Map<String, Object> append(String key, Object value) {
        Assert.isInstanceOf(Map.class, this.value, "Value must be a type of Map!");
        Map<String, Object> clone = new LinkedHashMap((Map)this.value);
        clone.put(key, value);
        return clone;
    }

    protected Map<String, Object> remove(String key) {
        Assert.isInstanceOf(Map.class, this.value, "Value must be a type of Map!");
        Map<String, Object> clone = new LinkedHashMap((Map)this.value);
        clone.remove(key);
        return clone;
    }

    protected Map<String, Object> appendAt(int index, String key, Object value) {
        Assert.isInstanceOf(Map.class, this.value, "Value must be a type of Map!");
        Map<String, Object> clone = new LinkedHashMap();
        int i = 0;

        for(Iterator var6 = ((Map)this.value).entrySet().iterator(); var6.hasNext(); ++i) {
            Map.Entry<String, Object> entry = (Map.Entry)var6.next();
            if (i == index) {
                clone.put(key, value);
            }

            if (!((String)entry.getKey()).equals(key)) {
                clone.put(entry.getKey(), entry.getValue());
            }
        }

        if (i <= index) {
            clone.put(key, value);
        }

        return clone;
    }

    protected List<Object> values() {
        if (this.value instanceof List) {
            return new ArrayList((List)this.value);
        } else {
            return this.value instanceof Map ? new ArrayList(((Map)this.value).values()) : new ArrayList(Collections.singletonList(this.value));
        }
    }

    protected <T> T get(int index) {
        return this.values().get(index);
    }

    protected <T> T get(Object key) {
        Assert.isInstanceOf(Map.class, this.value, "Value must be a type of Map!");
        return ((Map)this.value).get(key);
    }

    protected Map<String, Object> argumentMap() {
        Assert.isInstanceOf(Map.class, this.value, "Value must be a type of Map!");
        return Collections.unmodifiableMap((Map)this.value);
    }

    protected boolean contains(Object key) {
        return !(this.value instanceof Map) ? false : ((Map)this.value).containsKey(key);
    }

    protected abstract String getMongoMethod();

    protected static enum Expand {
        EXPAND_VALUES,
        KEEP_SOURCE;

        private Expand() {
        }
    }
}
