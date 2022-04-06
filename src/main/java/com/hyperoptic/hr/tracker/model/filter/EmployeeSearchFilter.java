package com.hyperoptic.hr.tracker.model.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * DTO representing filter for employees
 */
@Getter
@Setter
@Accessors(chain = true)
public class EmployeeSearchFilter {

    Integer personalId;
    String name;
    List<String> teams;
    List<String> teamLeaders;
}
