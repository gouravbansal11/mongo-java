package com.bansal.springmongoaggregatelookup.controller;

import com.bansal.springmongoaggregatelookup.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.AggregationSpELExpression;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class EmployeeController {

    @Autowired
    MongoTemplate template;

    @GetMapping(value = "/employee/{id}")
    public Employee getEmployee(@PathVariable("id") String id) {

        LookupOperation lookupOperation = Aggregation.lookup("Department", "departId", "did", "departments");
        MatchOperation matchOperation = Aggregation.match(Criteria.where("id").is(id));
        //target is from db perspective. name is from pojo class perspective.
        Fields fields = Fields.from(Fields.field("name", "empName"), Fields.field("departNames", "departments.name"));
        ProjectionOperation projectionOperation = Aggregation.project(fields);
        Aggregation combined = Aggregation.newAggregation(matchOperation, lookupOperation, projectionOperation);
        AggregationResults<Employee> employees = template.aggregate(combined, "Employee", Employee.class);
        return employees.getMappedResults().get(0);
    }

    @GetMapping(value = "/emp/{id}")
    public Employee getEmployeeSpecial(@PathVariable("id") String id) {
       /* AggregationSpELExpression.expressionOf("");
        Aggregation.lookup()*/

        LookupOperation lookupOperation = Aggregation.lookup("Department", "departId", "did", "departments");
        MatchOperation matchOperation = Aggregation.match(Criteria.where("id").is(id));
        //target is from db perspective. name is from pojo class perspective.
        Fields fields = Fields.from(Fields.field("name", "empName"), Fields.field("departNames", "departments.name"));
        ProjectionOperation projectionOperation = Aggregation.project(fields);
        Aggregation combined = Aggregation.newAggregation(matchOperation, lookupOperation, projectionOperation);
        AggregationResults<Employee> employees = template.aggregate(combined, "Employee", Employee.class);
        return employees.getMappedResults().get(0);
    }
}
