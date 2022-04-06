package com.hyperoptic.hr.tracker.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeeSearchRecord {

    private Long id;
    private Integer personalId;
    private String name;
    private String team;
    private String teamLead;
}
