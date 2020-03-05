import models.Employee;
import models.Pair;
import models.WorkDays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Application {

    public static void main(String[] args) throws IOException {
        Map<Integer, Employee> employees = new HashMap<>();
        Map<Integer, Set<Integer>> projectsByEmployee = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(
                new File("src//resources//employees.txt").getAbsolutePath()))) {
            for (String line; (line = br.readLine()) != null; ) {
                processEmployee(employees, projectsByEmployee, line);
            }
        }
        List<Pair> pairs = new ArrayList<>();
        for (Map.Entry<Integer, Employee> employeeEntry : employees.entrySet()) {
            Integer currentEmployeeId = employeeEntry.getKey();
            Employee currentEmployee = employeeEntry.getValue();
            Map<Integer, Collection<WorkDays>> currentEmployeeProjectDays = currentEmployee.getProjectDays();
            Set<Integer> currentEmployeeProjects = currentEmployeeProjectDays.keySet();
            for (Integer employeeProject : currentEmployeeProjects) {
                for (Map.Entry<Integer, Set<Integer>> entry : projectsByEmployee.entrySet()) {
                    if (!currentEmployeeId.equals(entry.getKey())) {
                        if (entry.getValue().contains(employeeProject)) {
                            pairs.add(new Pair(employees.get(currentEmployeeId),
                                    employees.get(entry.getKey()),
                                    employeeProject));
                        }
                    }
                }
            }
        }
        pairs.sort(Collections.reverseOrder());
        System.out.println(pairs.isEmpty() ? "Not found. Exiting!" : pairs.get(0));
    }


    private static void processEmployee(Map<Integer, Employee> employees,
                                        Map<Integer, Set<Integer>> projectsByEmployee,
                                        String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] lineArgs = line.split(",");
        Integer employeeId = Integer.valueOf(lineArgs[0].trim());
        Integer projectId = Integer.valueOf(lineArgs[1].trim());
        LocalDate dateFrom;
        try {
            dateFrom = LocalDate.parse(lineArgs[2].trim(), formatter);
        } catch (DateTimeParseException e) {
            dateFrom = LocalDate.now();
        }

        LocalDate dateTo;
        try {
            dateTo = LocalDate.parse(lineArgs[3].trim(), formatter);
        } catch (DateTimeParseException e) {
            dateTo = LocalDate.now();
        }
        WorkDays workDays = new WorkDays(dateFrom, dateTo);
        Employee employee = new Employee(employeeId);

        if (!employees.containsKey(employeeId)) {
            employees.put(employeeId, employee);
        }
        employees.get(employeeId).addProjectDays(projectId, workDays);

        if (!projectsByEmployee.containsKey(employeeId)) {
            projectsByEmployee.put(employeeId, new HashSet<>());
        }
        projectsByEmployee.get(employeeId).add(projectId);
    }

}
