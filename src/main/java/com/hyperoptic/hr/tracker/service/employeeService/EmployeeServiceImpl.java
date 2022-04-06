package com.hyperoptic.hr.tracker.service.employeeService;

import com.hyperoptic.hr.tracker.exception.BadRequestException;
import com.hyperoptic.hr.tracker.exception.ResourceNotFoundException;
import com.hyperoptic.hr.tracker.model.dto.response.EmployeeSearchRecord;
import com.hyperoptic.hr.tracker.model.dto.response.EmployeeSearchResponse;
import com.hyperoptic.hr.tracker.model.entity.Employee;
import com.hyperoptic.hr.tracker.model.filter.EmployeeSearchFilter;
import com.hyperoptic.hr.tracker.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static Set<String> searchParameters = Stream.of(
                    PARAM_PERSONAL_ID,
                    PARAM_NAME,
                    PARAM_FILTER_TEAM,
                    PARAM_FILTER_TEAM_LEAD,
                    PARAM_PAGE)
            .collect(Collectors.toCollection(HashSet::new));

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long employeeId, Employee employeeRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id:" + employeeId + " is not found."));

        employee.setName(employeeRequest.getName());
        employee.setTeam(employeeRequest.getTeam());
        employee.setTeamLead(employeeRequest.getTeamLead());

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id:" + employeeId + " is not found."));

        employeeRepository.delete(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id:" + employeeId + " is not found."));

        return employee;
    }

    @Override
    public EmployeeSearchResponse search(Map<String, String> requestParameters) {
        validateSearchRequestParameters(requestParameters);
        Pageable pageRequest = getRequestedPage(requestParameters);

        String name = requestParameters.get(PARAM_NAME);
        if (StringUtils.isNoneBlank(name)) {
            name = StringUtils.replace(name.toUpperCase(), ENCODED_SPACE, FUZZY_SEARCH_CHAR);
        }

        String personalIdStr = requestParameters.get(PARAM_PERSONAL_ID);
        Integer personalId = null;
        if (StringUtils.isNoneBlank(personalIdStr)) {
            personalId = Integer.valueOf(personalIdStr);
        }

        // search filters
        List<String> teams = getFilterValues(requestParameters, PARAM_FILTER_TEAM);
        List<String> teamLeaders = getFilterValues(requestParameters, PARAM_FILTER_TEAM_LEAD);

         EmployeeSearchFilter employeeSearchFilter = new EmployeeSearchFilter()
                    .setPersonalId(personalId)
                    .setName(name)
                    .setTeams(teams)
                    .setTeamLeaders(teamLeaders);

         var employees = employeeRepository.searchEmployees(employeeSearchFilter, pageRequest);

         return getEmployeeSearchResponse(employees);
    }

    private EmployeeSearchResponse getEmployeeSearchResponse(Page<Employee> employeesPage) {
        return new EmployeeSearchResponse()
                .setNumOfAllEmployees(employeeRepository.findAll().size())
                .setNumOfPages(employeesPage.getTotalPages())
                .setEmployeeSearchRecords(getEmployeeSearchResponseRecords(employeesPage.getContent()));
    }

    private List<EmployeeSearchRecord> getEmployeeSearchResponseRecords(List<Employee> employees) {
        List<EmployeeSearchRecord> recordsList = new ArrayList<>();

        if (employees != null && !employees.isEmpty()) {
            employees.forEach(employee -> recordsList.add(getEmployeeSearchRecord(employee)));
        }

        return recordsList;
    }

    private EmployeeSearchRecord getEmployeeSearchRecord(Employee employee) {
        return new EmployeeSearchRecord()
                .setId(employee.getId())
                .setPersonalId(employee.getPersonalId())
                .setName(employee.getName())
                .setTeam(employee.getTeam())
                .setTeamLead(employee.getTeamLead());
    }

    private void validateSearchRequestParameters(Map<String, String> requestParameters) {
        if (!searchParameters.containsAll(requestParameters.keySet())
                || (requestParameters.containsKey(PARAM_PERSONAL_ID) && requestParameters.containsKey(PARAM_NAME))) {
            throw new BadRequestException("Invalid set of search parameters is provided: " + String.join(", ", requestParameters.keySet()));
        }
    }

    private Pageable getRequestedPage(Map<String, String> requestParameters) {
        String pageStr = requestParameters.get(PARAM_PAGE);
        int page = StringUtils.isNoneBlank(pageStr) ? Integer.parseInt(pageStr) - 1 : 0;

        return PageRequest.of(page, ROWS_PER_PAGE, Sort.by(COLUMN_NAME).descending());
    }

    private List<String> getFilterValues(Map<String, String> requestParameters, String filterParameter) {
        if (requestParameters.containsKey(filterParameter)) {
            String filterParameterValue = requestParameters.get(filterParameter);

            return Arrays.stream(StringUtils.split(filterParameterValue, ENCODED_COMMA)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
