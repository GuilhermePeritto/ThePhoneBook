package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.TelefoneContato;
import com.example.ThePhoneBook.Repository.TelefoneContatoRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TelefoneContatoController {

    private ConfigurableApplicationContext springContext = Main.getContext();

    @FXML
    private TextField emailLbl;

    @FXML
    private TextField dddLbl;

    @FXML
    private TextField telefoneLbl;

    @Autowired
    TelefoneContatoRepository telefoneContatoRepository;

    public static TelefoneContato telefoneContato;

    public Boolean desabilitar = false;

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderTelefoneContato = new FXMLLoader(getClass().getClassLoader().getResource("View/TelefoneContato.fxml"));
        fxmlLoaderTelefoneContato.setControllerFactory(springContext::getBean);
        Parent parent = fxmlLoaderTelefoneContato.load();

        // Obtém a instância do controlador
        TelefoneContatoController telefoneContatoController = fxmlLoaderTelefoneContato.getController();
        telefoneContatoController.desabilitarCampos(desabilitar);
        // Chama o método alterarCampos com o telefoneContato fornecido
        telefoneContatoController.alterarCampos(telefoneContato);

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    public void setData(TelefoneContato telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    @FXML
    public void fecharTela(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void salvar(ActionEvent event) {
        AvisoController avisoController = new AvisoController();
        try {
            telefoneContato.setTelefone(telefoneLbl.getText());
            telefoneContato.setDdd(dddLbl.getText());
            telefoneContato.setEmail(emailLbl.getText());
            telefoneContatoRepository.save(telefoneContato);
            telefoneContato = new TelefoneContato(); //nao remover pelo amor de deus, se tirar isso, o telefoneContato vai ser criado com o nome do ultimo telefoneContato selecionado
            fecharTela(event);
            avisoController.showAlerta(new Stage(), "TelefoneContato salvo com sucesso!", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void alterarCampos(TelefoneContato telefoneContato) {

        if(telefoneContato == null) {
            return;
        }
        telefoneLbl.setText(telefoneContato.getTelefone());
        dddLbl.setText(telefoneContato.getDdd());
        emailLbl.setText(telefoneContato.getEmail());
    }

    public void excluiTelefoneContato() {
        AvisoConfirmacaoController avisoConfirmacaoController = new AvisoConfirmacaoController();
        try {
            AvisoConfirmacaoController.ConfirmacaoCallback callback = resultado -> {
                if (resultado) {
                    springContext.getBean(TelefoneContatoRepository.class).deleteById(telefoneContato.getIdTelefoneContato());
                    AvisoController avisoController = new AvisoController();
                    avisoController.showAlerta(new Stage(), "TelefoneContato excluído com sucesso!", false);
                    telefoneContato = new TelefoneContato();
                }
            };

            avisoConfirmacaoController.showAlertaConfirmacao(new Stage(), "Você tem certeza que deseja excluir este telefoneContato?", callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desabilitarCampos(Boolean desabilitar) {
        if (desabilitar) {
            telefoneLbl.setDisable(true);
            dddLbl.setDisable(true);
            emailLbl.setDisable(true);
        }
    }

    public static TelefoneContato getTelefoneContato() {
        return telefoneContato;
    }

    public static void setTelefoneContato(TelefoneContato telefoneContato) {
        TelefoneContatoController.telefoneContato = telefoneContato;
    }
}
