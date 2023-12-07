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
    private TextField cepLbl;

    @FXML
    private ComboBox<String> cidadeCb;

    @FXML
    private TextField complementoLbl;

    @FXML
    private TextField cpfLbl;

    @FXML
    private DatePicker dataNascLbl;

    @FXML
    private TextField emailLbl;

    @FXML
    private TextField enderecoLbl;

    @FXML
    private TextField nomeLbl;

    @FXML
    private TextField numeroLbl;

    @FXML
    private TextField outroContatoLbl;

    @FXML
    private ComboBox<String> ufCb;

    @FXML
    private TextField whatsLbl;

    @Autowired
    TelefoneContatoRepository telefoneContatoRepository;

    public static TelefoneContato telefoneContato;

    public Boolean desabilitar = false;

    @FXML
    public void initialize() {
        // Preencher o ComboBox de cidades
        cidadeCb.getItems().addAll(
                "São Paulo",
                "Rio de Janeiro",
                "Belo Horizonte",
                "Salvador",
                "Brasília"
        );

        // Preencher o ComboBox de UFs
        ufCb.getItems().addAll(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE",
                "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );
    }

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
            telefoneContato.setNome(nomeLbl.getText());
            telefoneContato.setCpf(cpfLbl.getText());
            telefoneContato.setWhatsapp(whatsLbl.getText());
            telefoneContato.setOutroContato(outroContatoLbl.getText());
            telefoneContato.setEmail(emailLbl.getText());
            telefoneContato.setDataNascimento(dataNascLbl.getValue());
            telefoneContato.setEndereco(enderecoLbl.getText());
            telefoneContato.setNumero(numeroLbl.getText());
            telefoneContato.setComplemento(complementoLbl.getText());
            telefoneContato.setCep(cepLbl.getText());
            telefoneContato.setCidade(cidadeCb.getValue());
            telefoneContato.setUf(ufCb.getValue());
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
        nomeLbl.setText(telefoneContato.getNome());
        cpfLbl.setText(telefoneContato.getCpf());
        whatsLbl.setText(telefoneContato.getWhatsapp());
        outroContatoLbl.setText(telefoneContato.getOutroContato());
        emailLbl.setText(telefoneContato.getEmail());
        dataNascLbl.setValue(telefoneContato.getDataNascimento());
        enderecoLbl.setText(telefoneContato.getEndereco());
        numeroLbl.setText(telefoneContato.getNumero());
        complementoLbl.setText(telefoneContato.getComplemento());
        cepLbl.setText(telefoneContato.getCep());
        // Define os valores nos ComboBoxes
        cidadeCb.setValue(telefoneContato.getCidade());
        ufCb.setValue(telefoneContato.getUf());
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
            nomeLbl.setDisable(true);
            cpfLbl.setDisable(true);
            whatsLbl.setDisable(true);
            outroContatoLbl.setDisable(true);
            emailLbl.setDisable(true);
            dataNascLbl.setDisable(true);
            enderecoLbl.setDisable(true);
            numeroLbl.setDisable(true);
            complementoLbl.setDisable(true);
            cepLbl.setDisable(true);
            cidadeCb.setDisable(true);
            ufCb.setDisable(true);
        }
    }

    public static TelefoneContato getTelefoneContato() {
        return telefoneContato;
    }

    public static void setTelefoneContato(TelefoneContato telefoneContato) {
        TelefoneContatoController.telefoneContato = telefoneContato;
    }
}
