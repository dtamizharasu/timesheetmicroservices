//package com.timesheet.service;
//
//import com.timesheet.model.Timesheet;
//import com.timesheet.repository.TimesheetMongoDb;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TimesheetServiceV2 {
//
//    @Autowired
//    private TimesheetMongoDb repo;
//
//    public String addHrs(Timesheet timesheet){
//        String message = "";
//        List<Timesheet> exitsEntry = repo.getEmployeeByIdAndDate(timesheet.getEmployeeId(),timesheet.getTaskDate());
//        int overAllHrs = exitsEntry.stream().mapToInt(Timesheet::getContributionHrs).sum();
//        int remainingHrs = 8 - overAllHrs;
//        if(timesheet.getContributionHrs() < 8){
//            if(remainingHrs == 0){
//                repo.save(timesheet);
//                message = "Hours Has been locked successfully";
//            }else if(remainingHrs !=0  && timesheet.getContributionHrs() <= remainingHrs){
//                repo.save(timesheet);
//                message = "Hours Has been locked successfully";
//            }else {
//                message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
//            }
//        }else{
//            if(remainingHrs == 8){
//                remainingHrs = 0;
//            }
//            message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
//        }
//        return message;
//    }
//
//
//}
