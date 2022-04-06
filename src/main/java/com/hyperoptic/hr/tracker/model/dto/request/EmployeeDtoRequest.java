package com.hyperoptic.hr.tracker.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeDtoRequest {

    @NotNull
    private Integer personalId;

    @NotEmpty
    private String name;

    private String team;

    private String teamLead;
}
