package com.timesheet.service;

import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;
import com.timesheet.repository.TimesheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepo repo;


    public String addHrs(Timesheet timesheet){
        String message = "";
        List<Timesheet> exitsEntry = repo.getEmployeeByIdAndDate(timesheet.getEmployeeId(),timesheet.getTaskDate());
        int overAllHrs = exitsEntry.stream().mapToInt(Timesheet::getContributionHrs).sum();
        int remainingHrs = 8 - overAllHrs;
        if(timesheet.getContributionHrs() < 8){
            if(remainingHrs == 0){
                repo.save(timesheet);
                message = "Hours Has been locked successfully";
            }else if(remainingHrs !=0  && timesheet.getContributionHrs() <= remainingHrs){
                repo.save(timesheet);
                message = "Hours Has been locked successfully";
            }else {
                message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
            }
        }else{
            if(remainingHrs == 8){
                remainingHrs = 0;
            }
            message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
        }
        return message;
    }

    public List<ProjectHrs> getAllProjectDetailsByEmpId(Integer employeeId, String startDate, String endDate) throws ParseException {
        List<Timesheet> timesheetList;
        List<ProjectHrs> projectHrsList = new ArrayList<>();
        // Formatting the String into Date
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDates = formatter.parse(startDate);
        Date endDates = formatter.parse(endDate);
        java.sql.Date sqlStDate = new java.sql.Date(startDates.getTime());
        java.sql.Date sqlEdDate = new java.sql.Date(endDates.getTime());

        timesheetList  = repo.getProjectListByEmpIdAndDate(employeeId, sqlStDate, sqlEdDate);
        if(!timesheetList.isEmpty()){
            timesheetList.forEach(time ->{
                projectHrsList.add(new ProjectHrs(time.getProjectId(),time.getEmployeeId(), time.getContributionHrs(),
                        time.getProjectName(), String.valueOf(time.getTaskDate())));
            });
        }

        return projectHrsList;
    }
}
