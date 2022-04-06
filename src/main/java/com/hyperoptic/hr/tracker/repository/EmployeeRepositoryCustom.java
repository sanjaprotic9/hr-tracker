package com.hyperoptic.hr.tracker.repository;

import com.hyperoptic.hr.tracker.model.entity.Employee;
import com.hyperoptic.hr.tracker.model.filter.EmployeeSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Base for custom employee repository
 */
public interface EmployeeRepositoryCustom {

    /**
     * Search for employees matching the provided filter and pagination data.
     *
     * @param filter   Search filter.
     * @param pageable Pagination data.
     * @return Page containing matching employees.
     */
    Page<Employee> searchEmployees(EmployeeSearchFilter filter, Pageable pageable);
}
