package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Model.Category;
import com.mazenfahim.YallaBudget.Service.ExpenseService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for rapid expense logging.
 */
public class ExpenseEntryController {
    private final ExpenseService expenseService = new ExpenseService();

    @FXML
    private TextField amountInput;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        List<Category> categories = expenseService.getCategories();
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }

    @FXML
    public void onSubmitExpenseClicked() {
        try {
            double amount = Double.parseDouble(amountInput.getText().trim());
            Category selectedCategory = categoryComboBox.getValue();

            if (!expenseService.validateExpense(amount, selectedCategory)) {
                showValidationError("Please enter a valid amount and select a category.");
                return;
            }

            expenseService.addExpense(amount, selectedCategory);
            navigateTo("DashboardView.fxml");
        } catch (NumberFormatException e) {
            showValidationError("Please enter a valid number.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            showValidationError(e.getMessage());
        } catch (IOException e) {
            showValidationError("Expense saved, but dashboard could not be opened.");
        }
    }

    @FXML
    public void onCancelClicked() {
        try {
            navigateTo("DashboardView.fxml");
        } catch (IOException e) {
            showValidationError("Could not return to dashboard.");
        }
    }

    public void showValidationError(String message) {
        errorLabel.setText(message);
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) amountInput.getScene().getWindow();
        stage.setScene(scene);
    }
}
