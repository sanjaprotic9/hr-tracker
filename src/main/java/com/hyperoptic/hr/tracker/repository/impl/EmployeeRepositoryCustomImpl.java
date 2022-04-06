package com.hyperoptic.hr.tracker.repository.impl;

import com.hyperoptic.hr.tracker.model.entity.Employee;
import com.hyperoptic.hr.tracker.model.filter.EmployeeSearchFilter;
import com.hyperoptic.hr.tracker.repository.EmployeeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.COLUMN_NAME;
import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.PARAM_FILTER_TEAM;
import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.PARAM_FILTER_TEAM_LEAD;
import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.PARAM_NAME;
import static com.hyperoptic.hr.tracker.service.employeeService.EmployeeService.PARAM_PERSONAL_ID;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Implementation of {@link EmployeeRepositoryCustom}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<Employee> searchEmployees(EmployeeSearchFilter filter, Pageable pageable) {
        log.debug("Searching for employees matching [{}] and on page [{}].", filter, pageable);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = query.from(Employee.class);
        employeeRoot.alias("employee");

        query.select(employeeRoot)
                .where(where(criteriaBuilder, query, employeeRoot, filter))
                .orderBy(orderings(criteriaBuilder, pageable, employeeRoot));

        Long size = getCount(criteriaBuilder, query);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(query)
                .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());
        var resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList, pageable, size);
    }

    private Predicate[] where(CriteriaBuilder criteriaBuilder, CriteriaQuery<Employee> query,
                              Root<Employee> employeeRoot, EmployeeSearchFilter filter) {
        var predicates = new ArrayList<Predicate>();

        if (filter.getPersonalId() != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(employeeRoot.get(PARAM_PERSONAL_ID), filter.getPersonalId())));
        }

        if (filter.getName() != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(employeeRoot.get(PARAM_NAME)).as(String.class),
                    "%" + filter.getName() + "%")));
        }

        if (filter.getTeams() != null && !filter.getTeams().isEmpty()) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.in(employeeRoot.get(PARAM_FILTER_TEAM)).value(filter.getTeams())));
        }

        if (filter.getTeamLeaders() != null && !filter.getTeamLeaders().isEmpty()) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.in(employeeRoot.get(PARAM_FILTER_TEAM_LEAD))
                    .value(filter.getTeamLeaders())));
        }

        return predicates.toArray(new Predicate[]{});
    }

    private Long getCount(CriteriaBuilder criteriaBuilder, CriteriaQuery<Employee> searchQuery) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> employeeRoot = countQuery.from(Employee.class);
        employeeRoot.alias("employee");
        countQuery.select(criteriaBuilder.count(employeeRoot));

        Predicate restriction = searchQuery.getRestriction();
        if (restriction != null) {
            countQuery.where(restriction);
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Order> orderings(CriteriaBuilder criteriaBuilder, Pageable pageable, Root<Employee> employeeRoot) {
        List<Order> orderings = pageable.getSort().stream()
                .map(sort -> sort.getDirection().equals(ASC)
                        ? criteriaBuilder.asc(employeeRoot.get(sort.getProperty()))
                        : criteriaBuilder.desc(employeeRoot.get(sort.getProperty())))
                .collect(toList());

        orderings.add(criteriaBuilder.desc(employeeRoot.get(COLUMN_NAME)));

        return orderings;
    }
}
