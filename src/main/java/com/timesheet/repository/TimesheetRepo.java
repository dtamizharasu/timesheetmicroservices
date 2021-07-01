package com.timesheet.repository;

import com.timesheet.model.ProjectHrs;
import com.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TimesheetRepo extends JpaRepository<Timesheet,Integer> {

    @Query(value = "select t from Timesheet t where t.employeeId =?1 and t.taskDate in ?2")
    List<Timesheet> getEmployeeByIdAndDate(Integer employeeId, Date taskDate);

//    @Query(value = "select t.* from Timesheet t where t.employeeId =?1 and t.taskDate between ?2 and ?3")
//    List<ProjectHrs> getProjectListByEmpIdAndDate(Integer employeeId, String startDate, String endDate);

    @Query(value = "select t from Timesheet t where t.employeeId =?1 and t.taskDate between ?2 and ?3")
    List<Timesheet> getProjectListByEmpIdAndDate(Integer employeeId, Date startDate, Date endDate);

}
