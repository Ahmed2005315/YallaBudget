package com.mazenfahim.YallaBudget;

import com.mazenfahim.YallaBudget.Manager.SQLiteManager;
import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX application class.
 *
 * The startup flow follows the project SDS/SRS direction:
 * 1. Prepare the local SQLite database.
 * 2. Ask the user to create a local PIN if no user exists.
 * 3. Otherwise ask for the PIN and continue to the budget flow.
 */
public class YallaBudgetApplication extends Application {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 680;

    @Override
    public void start(Stage stage) throws IOException {
        SQLiteManager.createTables();

        BudgetService budgetService = new BudgetService();
        budgetService.ensureDefaultCategories();

        UserService userService = new UserService();
        String initialView = userService.userExists() ? "PinUnlockView.fxml" : "PinSetupView.fxml";

        FXMLLoader loader = new FXMLLoader(YallaBudgetApplication.class.getResource(initialView));
        Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("YallaBudget");
        stage.setScene(scene);
        stage.show();
    }
}
