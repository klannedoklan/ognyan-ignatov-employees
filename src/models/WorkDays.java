package models;

import java.time.LocalDate;

public class WorkDays {
    private LocalDate startDate;
    private LocalDate endDate;

    public WorkDays(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
