module com.logandhillon.typeofwar {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.logandhillon.typeofwar to javafx.fxml;
    exports com.logandhillon.typeofwar;
    exports com.logandhillon.typeofwar.engine;
}