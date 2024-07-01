module dsa_visualisation.dsa_visualisation {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.ionicons4;
    requires org.kordamp.ikonli.bootstrapicons;
    requires com.almasb.fxgl.all;
    requires com.gluonhq.charm.glisten;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires okhttp3;
    requires retrofit2;
    requires service;
    requires api;
//    requires retrofit2.converter.jackson;
//    requires com.theokanning.openai_gpt3_java;
//    requires com.theokanning.openai_gpt3_java.client;
//    requires com.theokanning.openai_gpt3_java.api;



    opens com.dsa_visualisation to javafx.fxml;
    exports com.dsa_visualisation;
}