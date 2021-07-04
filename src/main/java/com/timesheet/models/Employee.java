package com.timesheet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Integer empId;
    private String empName;
    private String designation;
    private String emailId;
    private String mobileNum;
    private String availability;
}
