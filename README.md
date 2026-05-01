# YallaBudget 💰

A local-first personal budgeting desktop application built with JavaFX and SQLite. YallaBudget helps you track your monthly allowance, log daily expenses, and stay within your budget — all stored privately on your machine with PIN protection.

---

## Features

- 🔐 **PIN Protection** — Secure local PIN setup and unlock
- 💵 **Budget Cycle Setup** — Define your monthly allowance and start date
- 📊 **Spending Dashboard** — Visual pie chart breakdown of your expenses
- ⚠️ **80% Threshold Warning** — Get alerted before you overspend
- 📝 **Expense Logging** — Log expenses by category with timestamps
- 🔄 **Dynamic Daily Limit** — Recalculates safe daily spending based on current date and remaining balance
- 📅 **Transaction History** — Filter past expenses by category or date
- ⚙️ **Settings** — Change your PIN or reset your budget cycle

---

## Architecture

YallaBudget follows a layered architecture designed for clarity and separation of concerns:

```
com.mazenfahim.YallaBudget
├── Controller/   → JavaFX screen controllers (UI logic only)
├── Service/      → Business logic and validation
├── Manager/      → SQLite database queries and persistence
└── Model/        → Pure data/entity classes
```

### App Flow

```
Launch
  └── No user? → PinSetupView
  └── User exists? → PinUnlockView
        └── No budget cycle? → SetupView
        └── Budget cycle exists? → DashboardView
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI Framework | JavaFX 21.0.6 with FXML |
| Database | SQLite via `sqlite-jdbc` |
| Build Tool | Maven |
| Language | Java 21 |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven (or use the included wrapper)

### Run the app

**On Windows:**
```bash
mvnw.cmd javafx:run
```

**On macOS/Linux:**
```bash
./mvnw javafx:run
```

---

## Project Structure

```
YallaBudget/
├── src/
│   ├── main/
│   │   ├── java/com/mazenfahim/YallaBudget/
│   │   │   ├── Controller/
│   │   │   ├── Service/
│   │   │   ├── Manager/
│   │   │   ├── Model/
│   │   │   └── YallaBudgetApplication.java
│   │   └── resources/com/mazenfahim/YallaBudget/
│   │       ├── *.fxml         (screen layouts)
│   │       └── css/           (stylesheets)
├── pom.xml
└── yallabudget.db             (auto-created on first run)
```

---

## Screens

| Screen | Description |
|---|---|
| `PinSetupView` | First-time PIN creation |
| `PinUnlockView` | PIN entry on subsequent launches |
| `SetupView` | Initial allowance and budget cycle configuration |
| `DashboardView` | Spending overview with chart and daily limit |
| `ExpenseEntryView` | Log a new expense with category |
| `HistoryView` | Browse and filter past transactions |
| `SettingsView` | Change PIN or reset budget cycle |
