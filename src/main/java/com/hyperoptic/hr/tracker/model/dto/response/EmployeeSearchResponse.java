package com.hyperoptic.hr.tracker.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.ROWS_PER_PAGE;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchResponse {

    private Integer rowsPerPage = ROWS_PER_PAGE;
    private Integer numOfPages;
    private Integer numOfAllEmployees;
    private List<EmployeeSearchRecord> employeeSearchRecords = new ArrayList<>();
}
