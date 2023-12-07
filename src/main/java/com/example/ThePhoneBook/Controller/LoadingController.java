package com.example.ThePhoneBook.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoadingController {

    @FXML
    private ProgressIndicator progressIndicator;

    void iniciaLoading(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderLoading = new FXMLLoader(getClass().getClassLoader().getResource("View/Loading.fxml"));
        Parent parent = fxmlLoaderLoading.load();
        BorderPane borderPane = (BorderPane) parent;
        LoadingController loadingController = fxmlLoaderLoading.getController();
        loadingController.progressIndicator.setStyle("-fx-progress-color: #cd3bca;");

        // Configura o estilo transparente apenas para a cor de fundo
        borderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        stage.show();
        stage.toFront();
    }

    void fecharLoading(Stage stage) {
        stage.close();
    }

}
