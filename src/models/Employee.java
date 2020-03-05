package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Employee {

    private Integer employeeId;
    private Map<Integer, Collection<WorkDays>> projectDays;

    public Employee(Integer employeeId) {
        this.employeeId = employeeId;
        this.projectDays = new HashMap<>();
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Map<Integer, Collection<WorkDays>> getProjectDays() {
        return projectDays;
    }

    public void addProjectDays(Integer employeeId, WorkDays workDays){
        if (!this.projectDays.containsKey(employeeId)){
            this.projectDays.put(employeeId, new ArrayList<>());
        }
        this.projectDays.get(employeeId).add(workDays);
    }
}
