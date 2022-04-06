package com.hyperoptic.hr.tracker.controller;

import com.hyperoptic.hr.tracker.model.dto.request.EmployeeDtoRequest;
import com.hyperoptic.hr.tracker.model.dto.response.EmployeeDtoResponse;
import com.hyperoptic.hr.tracker.model.dto.response.EmployeeSearchResponse;
import com.hyperoptic.hr.tracker.model.entity.Employee;
import com.hyperoptic.hr.tracker.service.employeeService.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
@Api("Set of endpoints for employee.")
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @GetMapping()
    @ApiOperation("Find all employees.")
    public List<EmployeeDtoResponse> getAllEmployees() {
        log.info("Finding all employees");

        return employeeService.getAllEmployees().stream().map(employee -> modelMapper.map(employee, EmployeeDtoResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ApiOperation("Find employee by id.")
    public EmployeeDtoResponse getEmployeeById(@PathVariable(name = "id") Long id) {
        log.info("Finding employee with id: " + id);
        Employee employee = employeeService.getEmployeeById(id);

        return modelMapper.map(employee, EmployeeDtoResponse.class);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Save employee.")
    public EmployeeDtoResponse saveEmployee(@Valid @RequestBody EmployeeDtoRequest employeeDtoRequest) {
        log.info("Saving new employee");
        Employee employeeRequest = modelMapper.map(employeeDtoRequest, Employee.class);
        Employee employee = employeeService.createEmployee(employeeRequest);

        return modelMapper.map(employee, EmployeeDtoResponse.class);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update employee.")
    public EmployeeDtoResponse updateEmployee(@PathVariable(name = "id") Long id, @Valid @RequestBody EmployeeDtoRequest employeeDtoRequest) {
        log.info("Updating employee with id: " + id);
        Employee employeeRequest = modelMapper.map(employeeDtoRequest, Employee.class);
        Employee employee = employeeService.updateEmployee(id, employeeRequest);

        return modelMapper.map(employee, EmployeeDtoResponse.class);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete employee.")
    public void deleteEmployee(@PathVariable(name = "id") Long id) {
        log.info("Deleting employee with id: " + id);
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/search")
    @ApiOperation("Search employees by personal id, name, team or team lead.")
    public EmployeeSearchResponse search(@RequestParam Map<String, String> requestParameters) {
        log.info("Searching employees based on following properties: " + String.join(", ", requestParameters.keySet()));

        return employeeService.search(requestParameters);
    }

}
