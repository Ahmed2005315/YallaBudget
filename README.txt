YallaBudget - SDS-Compatible JavaFX Maven Project

Architecture used in this version:
- Controller package: JavaFX screen controllers only.
- Service package: business logic and validation.
- Manager package: database persistence and SQLite query/update operations.
- Model package: pure entity/data classes.

Main flow:
1. YallaBudgetApplication creates the SQLite tables.
2. If no local user exists, the app opens PinSetupView.fxml.
3. If a user exists, the app opens PinUnlockView.fxml.
4. After PIN setup/unlock, the app opens SetupView.fxml if no budget cycle exists, otherwise DashboardView.fxml.

Implemented SRS/SDS features:
- Local PIN setup and unlock.
- Initial allowance/budget cycle setup.
- Safe daily limit calculation.
- Expense logging with categories.
- Remaining balance update after expense logging.
- Spending dashboard with pie chart data.
- 80% threshold warning.
- Dynamic daily limit recalculation based on current date and remaining balance.
- Transaction history with category/date filtering.
- Settings screen with PIN change and cycle reset.

Important files:
- src/main/java/com/mazenfahim/YallaBudget/Controller
- src/main/java/com/mazenfahim/YallaBudget/Service
- src/main/java/com/mazenfahim/YallaBudget/Manager
- src/main/java/com/mazenfahim/YallaBudget/Model
- src/main/resources/com/mazenfahim/YallaBudget

Run from project root:
./mvnw javafx:run

On Windows:
mvnw.cmd javafx:run
