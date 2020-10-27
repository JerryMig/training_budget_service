import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BudgetServiceTest {

    private BudgetRepo budgetRepo = new BudgetRepo();;

    @Test
    public void query_2020_0101_to_2020_0131() {
        budgetRepo.setAll(Arrays.asList(new Budget(){{
            setAmount(31);
            setYearMonth("202001");
        }}));

        BudgetService budgetService = new BudgetService(budgetRepo);
        LocalDate startTime = LocalDate.of(2020, 1, 1);
        LocalDate endTime = LocalDate.of(2020, 1, 31);
        double budget = budgetService.query(startTime, endTime);
        assertEquals(budget, 31d, 0);
    }

    @Test
    public void query_2019_1231_to_2020_0201() {
        budgetRepo.setAll(Arrays.asList(new Budget(){{
            setAmount(31);
            setYearMonth("202001");
        }}, new Budget() {{
            setYearMonth("201912");
            setAmount(155);
        }}, new Budget() {{
            setYearMonth("202002");
            setAmount(290);
        }}));

        BudgetService budgetService = new BudgetService(budgetRepo);
        LocalDate startTime = LocalDate.of(2020, 1, 1);
        LocalDate endTime = LocalDate.of(2020, 1, 10);
        double budget = budgetService.query(startTime, endTime);
        assertEquals(budget, 46d, 0);
    }


    private static class BudgetRepo implements IBudgetRepo  {

        private final List<Budget> budgetList = new ArrayList<>();

        public void setAll(List<Budget> budgets) {
            budgetList.clear();
            budgetList.addAll(budgets);
        }

        @Override
        public List<Budget> getAll() {
            return budgetList;
        }
    }
}