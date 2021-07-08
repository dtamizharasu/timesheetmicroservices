package com.timesheet.service;

import com.timesheet.model.MappedProjects;
import com.timesheet.model.Project;
import com.timesheet.model.Timesheet;
import com.timesheet.repository.TimesheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepo repo;

    @Autowired
    private RestTemplate restTemplate;


    public String addHrs(Timesheet timesheet){
        String message = "Hours Has been locked successfully";
        ResponseEntity<int[]> response =
                restTemplate.getForEntity("http://ALLOCATION-SERVICE/api/allocation/projectIds/"+timesheet.getEmployeeId(),int[].class);
        int[] projIds = response.getBody();
        List<Integer> ids = Arrays.stream(projIds).boxed().collect(Collectors.toList());
        if(ids.contains(timesheet.getProjectId())){
            List<Timesheet> exitsEntry = repo.getEmployeeByIdAndDate(timesheet.getEmployeeId(),timesheet.getTaskDate());
            int overAllHrs = exitsEntry.stream().mapToInt(Timesheet::getContributionHrs).sum();
            int remainingHrs = 8 - overAllHrs;
            if(timesheet.getContributionHrs() <=8){
                if(remainingHrs == 0){
                    message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
                }else if(remainingHrs !=0  && timesheet.getContributionHrs() <= remainingHrs){
                    repo.save(timesheet);
                    message = "Hours Has been locked successfully";
                }else {
                    message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
                }
            }else{
                message = "Per Day Hours Limit is 8 and entered Hour is :"+timesheet.getContributionHrs()+" will not accepted";
            }
            //repo.save(timesheet);
        }else{
            message = "Given Project Id You Are not Mapped Please Provide Valid ID";
        }

        return message;
    }

    public List<Timesheet> getAllProjectDetailsByEmpId(Integer employeeId, String startDate, String endDate){
        List<Timesheet> timesheetList;
//        List<Timesheet> projectHrsList = new ArrayList<>();
        // Formatting the String into Date
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDates = formatter.parse(startDate);
//        Date endDates = formatter.parse(endDate);
//        java.sql.Date sqlStDate = new java.sql.Date(startDates.getTime());
//        java.sql.Date sqlEdDate = new java.sql.Date(endDates.getTime());

        timesheetList  = repo.getProjectListByEmpIdAndDate(employeeId, startDate, endDate);
//        if(!timesheetList.isEmpty()){
//            timesheetList.forEach(time ->{
//                projectHrsList.add(new ProjectHrs(time.getProjectId(),time.getEmployeeId(), time.getContributionHrs(),
//                        time.getProjectName(), time.getTaskDate()));
//            });
//        }

        return timesheetList;
    }

    public MappedProjects getAllMappedProjects(Integer empId){
        MappedProjects mappedProjects = new MappedProjects();
        List<Integer> projectIdList =  new ArrayList<>();
        List<String> projectNameList =  new ArrayList<>();

        ResponseEntity<int[]> response =
                restTemplate.getForEntity("http://Allocation-Service/api/allocation/projectIds/"+empId,int[].class);
        int[] projIds = response.getBody();
        if(projIds!=null){
            for(int ids: projIds){
                Project project =
                        restTemplate.getForObject("http://Project-Service/api/project/"+ids,Project.class);
                projectIdList.add(ids);
                    assert project != null;
                    projectNameList.add(project.getProjectName());
            }
        }
        mappedProjects.setEmployeeId(empId);
        mappedProjects.setProjectId(projectIdList);
        mappedProjects.setProjectName(projectNameList);
        mappedProjects.setCategory(Arrays.asList("Training","Remote","OffShore","OnShore","Leaves"));

    return mappedProjects;
    }

}
