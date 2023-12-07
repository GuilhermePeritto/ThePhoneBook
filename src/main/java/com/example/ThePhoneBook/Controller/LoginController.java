package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.Usuario;
import com.example.ThePhoneBook.Repository.UsuarioRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static javafx.scene.paint.Color.RED;

@Component
public class LoginController {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @FXML
    public TextField lblemail;
    @FXML
    public PasswordField lblsenha;

    @FXML
    private Button BtnEntrar;

    @FXML
    private Button BtnRegistrar;

    @FXML
    private Button BtnMinimize;

    @FXML
    private Button BtnSair;

    @FXML
    private Label LblAviso;

    private double xOffset = 0;
    private double yOffset = 0;

    private Usuario usuarioLogado = new Usuario();

    public static Scene loginView;

    private ConfigurableApplicationContext springContext = Main.getContext();


    @FXML
    void sair(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void minimizar(ActionEvent event) {
        Stage stage = (Stage) BtnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void entrar(ActionEvent event) throws IOException {
        LoginController loginController = Main.fxmlLoaderLogin.getController();
        Usuario usuario = usuarioRepository.findByEmail(lblemail.getText());

        if (usuario != null && passwordEncoder.matches(lblsenha.getText(), usuario.getSenha())) {
            usuarioLogado = usuario;
            DashBoardController dashBoardController = new DashBoardController();
            Main.stage.close();
            dashBoardController.showDashBoard(new Stage(), loginController);
        } else {
            // Senha incorreta ou usuário não encontrado
            loginController.LblAviso.setTextFill(RED);
            loginController.LblAviso.setText("Usuário ou senha incorretos!");
        }
    }

    @FXML
    void registrar(ActionEvent event) throws Exception {
        try {
            RegistrarController registrarController = new RegistrarController();
            registrarController.showRegistrar(Main.stage);
            fecharLogin();
            LblAviso.setText("");
        } catch (Exception e) {
            LblAviso.setTextFill(RED);
            LblAviso.setText(e.getMessage());
        }
    }

    public void showLogin(Stage stage) throws Exception {
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getClassLoader().getResource("View/Login.fxml"));
        fxmlLoaderLogin.setControllerFactory(springContext::getBean);
        Parent fxmlLoginView = fxmlLoaderLogin.load();
        loginView = new Scene(fxmlLoginView, 975, 501);
        stage.setScene(loginView);
        stage.show();
    }

    public void fecharLogin() {
        Stage stage = (Stage) BtnSair.getScene().getWindow();
        stage.close();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    @FXML
    void pressed(MouseEvent event) {
        Stage stage = (Stage) BtnEntrar.getScene().getWindow();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) BtnEntrar.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void released(MouseEvent event) {
        Stage stage = (Stage) BtnEntrar.getScene().getWindow();
    }
}