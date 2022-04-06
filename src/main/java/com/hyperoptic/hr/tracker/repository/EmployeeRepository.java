package com.hyperoptic.hr.tracker.repository;

import com.hyperoptic.hr.tracker.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {

}
