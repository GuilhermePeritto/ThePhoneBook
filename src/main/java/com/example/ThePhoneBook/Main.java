package com.example.ThePhoneBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static javafx.stage.StageStyle.*;


@SpringBootApplication
public class Main extends Application {

    public static ConfigurableApplicationContext springContext;
    public static Parent rootNode;
    public static Stage stage;
    public static FXMLLoader fxmlLoaderLogin;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        fxmlLoaderLogin = new FXMLLoader(getClass().getClassLoader().getResource("View/Login.fxml"));
        fxmlLoaderLogin.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoaderLogin.load();
    }

    @Override
    public void start(Stage startStage) throws Exception {
        stage = startStage;
        startStage.setTitle("PurpleGym");
        startStage.setScene(new Scene(rootNode, 975, 501));
        startStage.initStyle(UNDECORATED);
        startStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }

    public static ConfigurableApplicationContext getContext() {
        return springContext;
    }

}
