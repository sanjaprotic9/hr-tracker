package com.hyperoptic.hr.tracker.service.employeeService;

import com.hyperoptic.hr.tracker.model.dto.response.EmployeeSearchResponse;
import com.hyperoptic.hr.tracker.model.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    int ROWS_PER_PAGE = 10;

    String ENCODED_SPACE = "%20";
    String ENCODED_COMMA = "%2C";
    String FUZZY_SEARCH_CHAR = "%";

    String PARAM_PERSONAL_ID = "personalId";
    String PARAM_NAME = "name";
    String PARAM_FILTER_TEAM = "team";
    String PARAM_FILTER_TEAM_LEAD = "teamLead";
    String PARAM_PAGE = "page";

    String COLUMN_NAME = "name";

    List<Employee> getAllEmployees();

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long employeeId, Employee employee);

    void deleteEmployee(Long employeeId);

    Employee getEmployeeById(Long employeeId);

    EmployeeSearchResponse search(Map<String, String> requestParameters);
}
