module com.mazenfahim.YallaBudget {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.mazenfahim.YallaBudget to javafx.fxml;
    opens com.mazenfahim.YallaBudget.Controller to javafx.fxml;

    exports com.mazenfahim.YallaBudget;
    exports com.mazenfahim.YallaBudget.Controller;
    exports com.mazenfahim.YallaBudget.Service;
    exports com.mazenfahim.YallaBudget.Manager;
    exports com.mazenfahim.YallaBudget.Model;
}
