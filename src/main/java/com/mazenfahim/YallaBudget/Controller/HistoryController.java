package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Model.BudgetCycle;
import com.mazenfahim.YallaBudget.Model.Category;
import com.mazenfahim.YallaBudget.Model.Expense;
import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.ExpenseService;
import com.mazenfahim.YallaBudget.Service.HistoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for transaction history review, filtering, and deletion.
 */
public class HistoryController {
    private final HistoryService historyService = new HistoryService();
    private final ExpenseService expenseService = new ExpenseService();
    private final BudgetService budgetService = new BudgetService();
    private final Map<String, Integer> categoryNameToId = new HashMap<>();

    @FXML
    private TableView<Expense> expenseTableView;

    @FXML
    private ComboBox<String> categoryFilterComboBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button deleteButton;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTableColumns();
        populateCategoryFilter();
        showHistory();
        deleteButton.disableProperty().bind(expenseTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    public void showHistory() {
        loadAndDisplayExpenses();
    }

    private void setupTableColumns() {
        TableColumn<Expense, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp().toLocalDate().toString()));

        TableColumn<Expense, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory().getCategoryName()));

        TableColumn<Expense, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f EGP", data.getValue().getAmount())));

        expenseTableView.getColumns().setAll(dateColumn, categoryColumn, amountColumn);
    }

    private void populateCategoryFilter() {
        categoryNameToId.clear();
        categoryFilterComboBox.getItems().clear();
        categoryFilterComboBox.getItems().add("All Categories");

        for (Category category : expenseService.getCategories()) {
            categoryNameToId.put(category.getCategoryName(), category.getId());
            categoryFilterComboBox.getItems().add(category.getCategoryName());
        }

        categoryFilterComboBox.setValue("All Categories");
    }

    private void loadAndDisplayExpenses() {
        BudgetCycle cycle = budgetService.getCurrentCycleData();
        if (cycle == null) {
            expenseTableView.setItems(FXCollections.observableArrayList());
            messageLabel.setText("No active budget cycle.");
            return;
        }

        List<Expense> expenses = historyService.getTransactions(cycle.getId());
        String selectedCategory = categoryFilterComboBox.getValue();

        if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
            Integer categoryId = categoryNameToId.get(selectedCategory);
            if (categoryId != null) {
                expenses = historyService.filterByCategory(expenses, categoryId);
            }
        }

        expenses = historyService.filterByDateRange(expenses, fromDatePicker.getValue(), toDatePicker.getValue());
        expenses = historyService.sortTransactionsByDate(expenses);

        expenseTableView.setItems(FXCollections.observableArrayList(expenses));
        messageLabel.setText(expenses.isEmpty() ? "No transactions found." : "");
    }

    @FXML
    public void onFilterChanged() {
        loadAndDisplayExpenses();
    }

    @FXML
    public void onDeleteExpenseClicked() {
        Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            messageLabel.setText("Please select an expense first.");
            return;
        }

        expenseService.deleteExpense(selectedExpense);
        loadAndDisplayExpenses();
        messageLabel.setText("Transaction deleted.");
    }

    @FXML
    public void onBackClicked() {
        try {
            navigateTo("DashboardView.fxml");
        } catch (IOException e) {
            messageLabel.setText("Could not return to dashboard.");
        }
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) expenseTableView.getScene().getWindow();
        stage.setScene(scene);
    }
}
