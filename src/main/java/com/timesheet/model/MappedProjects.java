package com.timesheet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedProjects {

    private Integer employeeId;
    private List<Integer> projectId;
    private List<String> projectName;
    private List<String> category;
    private Integer contributionHrs;
    private String taskDate;
}
