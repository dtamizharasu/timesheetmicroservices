package com.timesheet.controller;

import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;
import com.timesheet.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {

    @Autowired
    private TimesheetService service;

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
    * */
    @GetMapping("/get/{id}/{startDate}/{endDate}")
    public List<ProjectHrs> getAllProjectHrs(@PathVariable("id") Integer id, @PathVariable("startDate") String startDate,
                                             @PathVariable("endDate") String endDate){

        return service.getAllProjectDetailsByEmpId(id,startDate,endDate);

    }
}
