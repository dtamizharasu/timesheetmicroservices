package com.timesheet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Integer projectId;
    private String projectName;
    private String clientName;
    private String durationInDays;
    private String startDate;
    private String endDate;
}
