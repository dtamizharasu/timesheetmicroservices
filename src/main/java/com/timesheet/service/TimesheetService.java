package com.timesheet.service;

import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;
import com.timesheet.repository.TimesheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepo repo;


    public String addHrs(Timesheet timesheet){
        String message = "";
        List<Timesheet> exitsEntry = repo.getEmployeeByIdAndDate(timesheet.getEmployeeId(),timesheet.getTaskDate());
        int remainingHrs = exitsEntry.stream().mapToInt(Timesheet::getContributionHrs).sum();
        if(timesheet.getContributionHrs() < 8 && timesheet.getContributionHrs() == remainingHrs){
            repo.save(timesheet);
            message = "Hours Has been locked successfully";
        }else{
            if(remainingHrs == 8){
                remainingHrs = 0;
            }
            message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
        }
        return message;
    }

    public List<ProjectHrs> getAllProjectDetailsByEmpId(Integer employeeId, String startDate, String endDate){
        List<ProjectHrs> timesheetList;

        timesheetList  = repo.getProjectListByEmpIdAndDate(employeeId, startDate, endDate);


        return timesheetList;
    }
}
