package com.timesheet.service;

import com.timesheet.model.MappedProjects;
import com.timesheet.model.Project;
import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;

import com.timesheet.models.Employee;
import com.timesheet.models.EmployeeReports;
import com.timesheet.models.ProjectDetails;

import com.timesheet.repository.TimesheetMongoDb;
import com.timesheet.repository.TimesheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepo repo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TimesheetMongoDb mongoDb;


    public String addHrs(Timesheet timesheet){
        String message = "";
        List<Timesheet> exitsEntry = repo.getEmployeeByIdAndDate(timesheet.getEmployeeId(),timesheet.getTaskDate());
        int overAllHrs = exitsEntry.stream().mapToInt(Timesheet::getContributionHrs).sum();
        int remainingHrs = 8 - overAllHrs;
        if(timesheet.getContributionHrs() < 8){
            if(remainingHrs == 0){
                message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
            }else if(remainingHrs !=0  && timesheet.getContributionHrs() <= remainingHrs){
                repo.save(timesheet);
                saveReportJsonToMongoDb(timesheet);
                message = "Hours Has been locked successfully";
            }else {
                message = "Per Day Hours Limit is 8 and available limit is :"+remainingHrs;
            }
        }else{
            message = "Per Day Hours Limit is 8 and entered Hour is :"+timesheet.getContributionHrs()+" will not accepted";
        }
        return message;
    }

    public List<ProjectHrs> getAllProjectDetailsByEmpId(Integer employeeId, String startDate, String endDate) throws ParseException {
        List<Timesheet> timesheetList;
        List<ProjectHrs> projectHrsList = new ArrayList<>();
        // Formatting the String into Date
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDates = formatter.parse(startDate);
//        Date endDates = formatter.parse(endDate);
//        java.sql.Date sqlStDate = new java.sql.Date(startDates.getTime());
//        java.sql.Date sqlEdDate = new java.sql.Date(endDates.getTime());

        timesheetList  = repo.getProjectListByEmpIdAndDate(employeeId, startDate, endDate);
        if(!timesheetList.isEmpty()){
            timesheetList.forEach(time ->{
                projectHrsList.add(new ProjectHrs(time.getProjectId(),time.getEmployeeId(), time.getContributionHrs(),
                        time.getProjectName(), time.getTaskDate()));
            });
        }

        return projectHrsList;
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
        mappedProjects.setContributionHrs(0);
        mappedProjects.setTaskDate(String.valueOf(new Date()));

    return mappedProjects;
    }

//    public void saveEmployeeDetailsToMongoDb(Timesheet timesheet){
//
//        Set<ProjectDetails> projectList = new HashSet<>();
//        Set<ProjectDetails> updatedList = new HashSet<>();
//        String num = timesheet.getTaskDate().substring(5,7);
//
//        EmployeeReports employeeReports = new EmployeeReports();
//        String docId = timesheet.getEmployeeId() +num;
//        Optional<EmployeeReports> projectDetails = mongoDb.findById(docId);
//        if (projectDetails.isPresent()){
//            projectList = projectDetails.get().getProjectList();
//            if(!projectList.isEmpty()){
//                projectList.forEach(project->{
//
//                    if(project.getProjectId().equals(timesheet.getProjectId())){
//
//                        int overAllHrs = project.getOverAllHrs()+timesheet.getContributionHrs();
//                        updatedList.add(new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
//                                timesheet.getCategory(),timesheet.getBillable(),overAllHrs));
//                    }else{
//                        updatedList.add(new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
//                                timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
//                    }
//                });
//            }else {
//                updatedList.add(new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
//                        timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
//            }
//        }else {
//
//            updatedList.add(new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
//                    timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
//        }
//
//        if (!projectList.isEmpty()){
//        updatedList.addAll(projectList);
//        }
//        int overAllHrs = updatedList.stream().mapToInt(ProjectDetails::getOverAllHrs).sum();
//        employeeReports.setEmployeeId(timesheet.getEmployeeId());
//        employeeReports.setId(docId);
//        employeeReports.setTotalHrs(overAllHrs);
//        employeeReports.setEmployeeName(getEmpName(timesheet.getEmployeeId()));
//        employeeReports.setMonth(num);
//        employeeReports.setProjectList(updatedList);
//
//        mongoDb.save(employeeReports);
//
//    }

    public void saveReportJsonToMongoDb(Timesheet timesheet){

        Set<ProjectDetails> updatedList = new HashSet<>();
        Map<Integer,ProjectDetails> projectDetailsMap = new HashMap<>();
        String num = timesheet.getTaskDate().substring(5,7);

        EmployeeReports employeeReports = new EmployeeReports();
        String docId = timesheet.getEmployeeId() +num;
        Optional<EmployeeReports> projectDetails = mongoDb.findById(docId);
        if (projectDetails.isPresent()){
            projectDetailsMap = getProjectDetailsMap(projectDetails.get().getProjectList());
            if(!projectDetailsMap.isEmpty()){

                    if(projectDetailsMap.containsKey(timesheet.getProjectId())){

                        int overAllHrs = projectDetailsMap.get(timesheet.getProjectId()).getOverAllHrs()
                                +timesheet.getContributionHrs();
                        projectDetailsMap.put(timesheet.getProjectId(),
                                new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
                                        timesheet.getCategory(),timesheet.getBillable(),overAllHrs));
                    }else{
                        projectDetailsMap.put(timesheet.getProjectId(),
                                new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
                                        timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
                    }
            }else {
                projectDetailsMap.put(timesheet.getProjectId(),
                        new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
                                timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
            }
        }else {

            projectDetailsMap.put(timesheet.getProjectId(),
                    new ProjectDetails(timesheet.getProjectId(),timesheet.getProjectName(),
                            timesheet.getCategory(),timesheet.getBillable(),timesheet.getContributionHrs()));
        }

        updatedList = new HashSet<>(projectDetailsMap.values());
        int overAllHrs = updatedList.stream().mapToInt(ProjectDetails::getOverAllHrs).sum();
        updatedList = mapDefaultProjects(updatedList);
        employeeReports.setEmployeeId(timesheet.getEmployeeId());
        employeeReports.setId(docId);
        employeeReports.setTotalHrs(overAllHrs);
        employeeReports.setEmployeeName(getEmpName(timesheet.getEmployeeId()));
        employeeReports.setMonth(num);
        employeeReports.setProjectList(updatedList);

        mongoDb.save(employeeReports);

    }

    public Map<Integer,ProjectDetails> getProjectDetailsMap(Set<ProjectDetails> projectDetails){
        Map<Integer,ProjectDetails> projectDetailsMap = new HashMap<>();

        if(!projectDetails.isEmpty()){
            projectDetails.forEach(project ->{
                projectDetailsMap.put(project.getProjectId(),project);
            });
        }
        return projectDetailsMap;
    }

    public String getEmpName(Integer empId){
        String empName = null;

        Employee employee = restTemplate
                .getForObject("http://Employee-Service/api/employee/"+empId,Employee.class);
        if (employee != null){
            empName = employee.getEmpName();
        }
        return empName;
    }

    public EmployeeReports getEmpReport(String id){
        EmployeeReports employeeReports = new EmployeeReports();
                if(mongoDb.existsById(id)){
                    employeeReports= mongoDb.findById(id).get();
                }
        return employeeReports;
    }

    public Set<ProjectDetails> mapDefaultProjects(Set<ProjectDetails> projectDetails){
        Set<ProjectDetails> updatedSet = projectDetails;
        List<Integer> projectIds = projectDetails.stream()
                .map(ProjectDetails::getProjectId).collect(Collectors.toList());
        ParameterizedTypeReference<List<Project>> typeRef =
                new ParameterizedTypeReference<List<Project>>() {};
        ResponseEntity<List<Project>> response =
                restTemplate.
                        exchange("http://Project-Service/api/project//get/all/projects",
                                HttpMethod.GET,null,
                                typeRef);
        Set<Project> projectSet = new HashSet<>(response.getBody());
        if(!projectSet.isEmpty()){
            projectSet.forEach(project -> {
                if(!projectIds.contains(project.getProjectId())){
                    updatedSet.add(new ProjectDetails(project.getProjectId(),project.getProjectName(),
                            "NA","NA",0));
                }
            });
        }
        return updatedSet;
    }
}
