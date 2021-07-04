package com.timesheet.controller;

import com.timesheet.model.MappedProjects;
import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;
import com.timesheet.models.EmployeeReports;
import com.timesheet.service.TimesheetService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Api(value = "Timesheet", tags = {"Timesheet"})
@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {

    @Autowired
    private TimesheetService service;

    /**
     * @param id - Get the Mapped Projects Details From Allocation Service
     * @return String message
     */

    @GetMapping("/get/mapped/projects/{empid}")
    public MappedProjects getAllMappedProjectsDetails(@PathVariable("empid") Integer id){

        MappedProjects emp = service.getAllMappedProjects(id);

        return emp;
    }

    /**
     * @param timesheet - Get the Timesheet Details
     * @return String message
     */

    @PostMapping("/addHrs")
    public String addHrs(@RequestBody Timesheet timesheet){
        return service.addHrs(timesheet);
    }

    /**
    * @param id employee id
     * @param startDate start date
     * @param endDate end date
     * Date Formats should be in yyyy-MM-dd
    * */
    @GetMapping("/get/{empid}/{startDate}/{endDate}")
    public List<ProjectHrs> getAllProjectHrs(@PathVariable("empid") Integer id, @PathVariable("startDate") String startDate,
                                             @PathVariable("endDate") String endDate) throws ParseException {

        return service.getAllProjectDetailsByEmpId(id,startDate,endDate);

    }

    @GetMapping("/get/report/{empid}/{month}")
    public EmployeeReports getAllMappedProjects(@PathVariable("empid") Integer id,
                                                  @PathVariable("month") String num){
//        ResponseEntity<?>	re= null;
//        try {
//          EmployeeReports response = service.getEmpReport(id+num);
//            if (response.isPresent()) {
//                re= new ResponseEntity<EmployeeReports>(response, HttpStatus.CREATED);
//            }else {
//                re= new ResponseEntity<String>("Conflict Data",HttpStatus.CONFLICT);
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//            re= new ResponseEntity<String>("Internal Error",HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        EmployeeReports response = service.getEmpReport(id+num);

        return response;

    }



}
