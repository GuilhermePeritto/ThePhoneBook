package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.Usuario;
import com.example.ThePhoneBook.Repository.UsuarioRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static javafx.stage.StageStyle.UNDECORATED;

@Component
public class RegistrarController {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @FXML
    private Button BtnFechar;

    @FXML
    private Button BtnMinimizar;

    @FXML
    private Button BtnVoltar;

    @FXML
    private Button BtnVoltarTelaEntrar;

    @FXML
    private Button btnFinalizarRegistro;

    @FXML
    private Button btnLoginFacebook;

    @FXML
    private Button btnLoginGoogle;

    @FXML
    private Label lblAviso;

    @FXML
    public TextField lblEmail;

    @FXML
    public TextField lblNome;

    @FXML
    public PasswordField lblSenha;

    public static Scene registrarView;

    private ConfigurableApplicationContext springContext = Main.getContext();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void pressed(javafx.scene.input.MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void dragged(javafx.scene.input.MouseEvent event) {
        Window stage = BtnFechar.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }


    @FXML
    void minimizar(ActionEvent event) {
        Stage stage = (Stage) BtnMinimizar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void finalizarRegistro() throws Exception {
        AvisoController avisoController = new AvisoController();
        try {
            if (lblNome.getText().isEmpty()) {
                lblAviso.setText("Você não preencheu o campo Nome Completo, favor tentar novamente!");
            } else if (lblEmail.getText().isEmpty()) {
                lblAviso.setText("Você não preencheu o campo E-mail, favor tentar novamente!");
            } else if (lblSenha.getText().isEmpty()) {
                lblAviso.setText("Você não preencheu o campo Senha, favor tentar novamente!");
            } else {
                try {
                    Usuario novoUsuario = new Usuario(null, lblNome.getText(), lblEmail.getText(), passwordEncoder.encode(lblSenha.getText()));
                    usuarioRepository.save(novoUsuario);
                    avisoController.showAlerta(new Stage(), "Cadastro realizado com sucesso!", true);
                } catch (Exception e) {
                    avisoController.showAlerta(new Stage(), e.getLocalizedMessage(), false);
                }
                lblEmail.setText("");
                lblNome.setText("");
                lblSenha.setText("");
            }
        } catch (Exception e) {
            avisoController.showAlerta(new Stage(),e.getMessage(), true);
        }

    }
    @FXML
    void voltarTelaEntrar(ActionEvent event) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLogin(Main.stage);
    }

    @FXML
    void voltar(ActionEvent event) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLogin(Main.stage);
    }

    @FXML
    void fechar(ActionEvent event) {
        Stage stage = (Stage) BtnFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginFacebook(ActionEvent event) {
        System.out.println("Login Facebook");
    }

    @FXML
    void loginGoogle(ActionEvent event) {
        System.out.println("Login Google");
    }

    public void showRegistrar(Stage stage) throws Exception {
        FXMLLoader fxmlLoaderRegistrar = new FXMLLoader(getClass().getClassLoader().getResource("View/Registrar.fxml"));
        fxmlLoaderRegistrar.setControllerFactory(springContext::getBean);
        Parent fxmlRegistrarView = fxmlLoaderRegistrar.load();
        registrarView = new Scene(fxmlRegistrarView, 975, 501);
        stage.setScene(registrarView);
        stage.initStyle(UNDECORATED);

        stage.show();
    }



}