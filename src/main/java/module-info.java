module com.logandhillon.typeofwar {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.logging.log4j.core;
    requires com.google.protobuf;
    requires java.desktop;

    opens com.logandhillon.typeofwar to javafx.fxml;
    exports com.logandhillon.typeofwar;

    exports com.logandhillon.typeofwar.engine;
    exports com.logandhillon.typeofwar.entity;
    exports com.logandhillon.typeofwar.entity.ui;
    exports com.logandhillon.typeofwar.game;
    exports com.logandhillon.typeofwar.networking.proto;
}