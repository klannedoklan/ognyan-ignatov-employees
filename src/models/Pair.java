package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;

public class Pair implements Comparable<Pair>{

    private Employee left;
    private Employee right;
    private Integer projectId;

    public Pair(Employee left, Employee right, Integer projectId) {
        this.left = left;
        this.right = right;
        this.projectId = projectId;
    }

    public Employee getLeft() {
        return left;
    }

    public Employee getRight() {
        return right;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public int getDaysWorkedTogether() {
        int result = 0;
        Map<Integer, Collection<WorkDays>> projectDaysLeft = this.getLeft().getProjectDays();
        Map<Integer, Collection<WorkDays>> projectDaysRight = this.getRight().getProjectDays();

        for (Integer projectsLeft : projectDaysLeft.keySet()){
            for (Integer projectsRight : projectDaysRight.keySet()){
                if (projectsLeft.equals(projectsRight)){
                    result += calculateColabDays(projectsLeft);
                }
            }
        }
        return result;
    }

    private int calculateColabDays(Integer projectId) {
        int result = 0;
        Collection<WorkDays> workDaysLeft = this.getLeft().getProjectDays().get(projectId);
        Collection<WorkDays> workDaysRight = this.getRight().getProjectDays().get(projectId);
        for (WorkDays leftDay : workDaysLeft){
            for (WorkDays rightDay : workDaysRight){
               result += getOverlapSum(leftDay.getStartDate(), leftDay.getEndDate(),
                                       rightDay.getStartDate(), rightDay.getEndDate());
            }
        }
        return result;
    }

    private int getOverlapSum(LocalDate date1Start, LocalDate date1End,
                               LocalDate date2Start, LocalDate date2End) {
        if (date1Start.isBefore(date2Start) || date1Start.isEqual(date2End) &&
               (date1End.isAfter(date2End) || date1End.isEqual(date2Start))){
            LocalDate firstDate = date1End.equals(date2End) ? date1End :
                                  date1End.isBefore(date2End) ? date1End : date2End;
            LocalDate secondDate = date1Start.equals(date2Start) ? date1Start :
                                   date1Start.isAfter(date2Start) ? date1Start : date2Start;
            return (int) ChronoUnit.DAYS.between(firstDate, secondDate);
        } else return 0;
    }

    @Override
    public int compareTo(Pair other) {
        return Integer.compare(this.getDaysWorkedTogether(), other.getDaysWorkedTogether());
    }

    @Override
    public String toString() {
        return String.format("EmployeeId: %d and EmployeeId: %d have worked together for total %d "
                             + "days on ProjectId: %d",left.getEmployeeId(),right.getEmployeeId(),
                             this.getDaysWorkedTogether(),this.getProjectId());
    }
}
