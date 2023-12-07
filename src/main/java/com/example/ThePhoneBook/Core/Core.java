package com.example.ThePhoneBook.Core;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Core {

    private double xOffset = 0;
    private double yOffset = 0;

    public void enableDrag(Stage stage) {
        stage.getScene().setOnMousePressed(this::pressed);
        stage.getScene().setOnMouseDragged(this::dragged);
        stage.getScene().setOnMouseReleased(this::released);
    }

    private void pressed(MouseEvent event) {
        Stage stage = (Stage) event.getSource();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
        stage.getScene().setCursor(Cursor.CLOSED_HAND);
    }

    private void dragged(MouseEvent event) {
        Stage stage = (Stage) event.getSource();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void released(MouseEvent event) {
        Stage stage = (Stage) event.getSource();
        stage.getScene().setCursor(Cursor.DEFAULT);
    }
}
