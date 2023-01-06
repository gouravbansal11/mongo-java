package com.bansal.springmongoaggregatelookup.model;

import java.util.List;

public class Employee {
    String id;
    String name;
    String departId;
    List<String> departNames;


    public List<String> getDepartNames() {
        return departNames;
    }

    public void setDepartNames(List<String> departNames) {
        this.departNames = departNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }
}
