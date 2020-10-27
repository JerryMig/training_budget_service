import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BudgetService {

    private final IBudgetRepo iBudgetRepo;

    public BudgetService(IBudgetRepo iBudgetRepo) {
        this.iBudgetRepo = iBudgetRepo;
    }

    public double query(LocalDate startTime, LocalDate endTime) {
        if (endTime.isBefore(startTime)) return 0d;

        List<Budget> budgets = iBudgetRepo.getAll();

        double sum = 0;
        LocalDate tempStartDate = startTime;
        while (isDateWithinRange(tempStartDate, endTime)) {
            Optional<Budget> monthBudget = getMonthBudget(budgets, tempStartDate);
            sum += getDayBudget(monthBudget, tempStartDate);

            tempStartDate = tempStartDate.plusDays(1);
        }
        return sum;
    }

    private boolean isDateWithinRange(LocalDate startDate, LocalDate endTime) {
        return !startDate.isAfter(endTime);
    }

    private Optional<Budget> getMonthBudget(List<Budget> budgets, LocalDate date) {
        return budgets.stream().filter(budget -> isSameMonth(budget, date)).findFirst();
    }

    private double getDayBudget(Optional<Budget> budgetOptional, LocalDate date) {
        if (!budgetOptional.isPresent()) return 0;

        return (double) budgetOptional.get().amount / date.getMonth().maxLength();
    }

//    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
//    private boolean isSameMonth(Budget budget, LocalDate currentDate) {
//        LocalDate date = LocalDate.parse(budget.yearMonth, formatter);
//        System.out.println("year : " + date.getYear());
//        System.out.println("daysInMonth : " + date.getMonth().maxLength());
//        return date.getYear() == currentDate.getYear() && date.getMonth() == currentDate.getMonth();
//    }

    private boolean isSameMonth(Budget budget, LocalDate currentDate) {
        int year = Integer.parseInt(budget.yearMonth.substring(0, 4));
        int month = Integer.parseInt(budget.yearMonth.substring(4, 6));
        return year == currentDate.getYear() && month == currentDate.getMonth().getValue();
    }
}
