package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AvisoController {

    @FXML
    private Button btnOk;
    @FXML
    private Label lblMensagem;

    public static Boolean voltarAoMenu = false;

    private ConfigurableApplicationContext springContext = Main.getContext();


    public void showAlerta(Stage stage, String mensagem, Boolean voltarAoMenu) throws IOException {
        this.voltarAoMenu = voltarAoMenu;
        FXMLLoader fxmlLoaderAviso = new FXMLLoader(getClass().getClassLoader().getResource("View/Aviso.fxml"));
        fxmlLoaderAviso.setControllerFactory(Main.getContext()::getBean);
        Parent parent = fxmlLoaderAviso.load();
        AvisoController avisoController = fxmlLoaderAviso.getController();
        avisoController.alterarMensagem(mensagem);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    public void alterarMensagem(String mensagem) {
        lblMensagem.setText(mensagem);
    }

    @FXML
    void onClickBtnOk(ActionEvent event) throws Exception {
        if (!voltarAoMenu) {
            fecharAviso();
            return;
        }
        LoginController loginController = new LoginController();
        loginController.showLogin(Main.stage);
        fecharAviso();
    }

    public void fecharAviso() {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }


}
