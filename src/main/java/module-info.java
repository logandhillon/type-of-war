module com.logandhillon.typeofwar {
    requires javafx.base;
    requires javafx.media;
    requires javafx.graphics;

    requires org.apache.logging.log4j.core;
    requires com.google.protobuf;

    opens com.logandhillon.typeofwar to javafx.fxml;
    exports com.logandhillon.typeofwar;

    exports com.logandhillon.typeofwar.engine;
    exports com.logandhillon.typeofwar.entity;
    exports com.logandhillon.typeofwar.entity.ui;
    exports com.logandhillon.typeofwar.scene;
    exports com.logandhillon.typeofwar.networking.proto;
    exports com.logandhillon.typeofwar.entity.core;
    exports com.logandhillon.typeofwar.entity.ui.component;
    exports com.logandhillon.typeofwar.scene.menu;
    exports com.logandhillon.typeofwar.scene.component;
}