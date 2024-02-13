package com.login.loginform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;



    @FXML
    protected void handleLogin(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidAdmin(username, password)){
            System.out.println("Admin login successful");
            handleSuccessfulLogin(event);
        }
        else {
            System.out.println("Invalid login credentials");
            //Implement actual function
        }
    }

    private void handleSuccessfulLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();

            //Get the controller of the adminDashboard.fxml file
            AdminDashboardController adminDashboardController = loader.getController();

            // Pass any necessary data to the AdminDashboardController
            // For example, you might pass the username of the logged-in admin


            //Show the  admin dashboard window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            //Close the login Window
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch(IOException e){
            e.printStackTrace();
            //Handle the exception (e.g. show an error message)
        }

    }

    private boolean isValidAdmin(String username, String password){
        try {
            // Get a database connection
            try (Connection connection = DatabaseConnector.getConnection()) {
                // Perform database operations (e.g., login validation)
                String query = "SELECT * FROM login WHERE username = ? AND password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next(); // Return true if a matching admin is found
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return false;
        }
    }


}