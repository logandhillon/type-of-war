module com.logandhillon.typeofwar {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires org.apache.logging.log4j.core;
    requires jdk.compiler;
    requires java.desktop;
    requires javafx.media;

    opens com.logandhillon.typeofwar to javafx.fxml;
    exports com.logandhillon.typeofwar;

    exports com.logandhillon.typeofwar.engine;
    exports com.logandhillon.typeofwar.entity;
    exports com.logandhillon.typeofwar.entity.ui;
    exports com.logandhillon.typeofwar.game;
}