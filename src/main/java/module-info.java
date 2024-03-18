module dsa_visualisation.dsa_visualisation {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.ionicons4;
    requires org.kordamp.ikonli.bootstrapicons;
    requires com.almasb.fxgl.all;

    opens com.dsa_visualisation to javafx.fxml;
    exports com.dsa_visualisation;
}