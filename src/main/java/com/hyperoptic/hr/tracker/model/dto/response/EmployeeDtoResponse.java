package com.hyperoptic.hr.tracker.model.dto.response;

import lombok.Data;

@Data
public class EmployeeDtoResponse {

    private Long id;
    private Integer personalId;
    private String name;
    private String team;
    private String teamLead;
}
