package com.timesheet.repository;

import com.timesheet.models.EmployeeReports;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimesheetMongoDb extends MongoRepository<EmployeeReports,String> {
}
