package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.Contato;
import com.example.ThePhoneBook.Model.TelefoneContato;
import com.example.ThePhoneBook.Repository.ContatoRepository;
import com.example.ThePhoneBook.Repository.TelefoneContatoRepository;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TelefoneContatoController {

    private ConfigurableApplicationContext springContext = Main.getContext();

    @FXML
    private TextField emailLbl;

    @FXML
    private TextField dddLbl;

    @FXML
    private TextField telefoneLbl;

    @FXML
    private ComboBox<Contato> comboContato;

    @Autowired
    TelefoneContatoRepository telefoneContatoRepository;

    @Autowired
    ContatoRepository contatoRepository;

    public static TelefoneContato telefoneContato = new TelefoneContato();

    public Boolean desabilitar = false;

    @FXML
    public void initialize() {
        // Adiciona um ouvinte para o evento onShowing do ComboBox
        comboContato.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // O ComboBox está prestes a ser expandido, agora você pode carregar os dados
                    carregarDadosNoComboBox();
                }
            }
        });
    }

    private void carregarDadosNoComboBox() {
        // Buscar todos os contatos do banco de dados
        List<Contato> contatos = contatoRepository.findAll();

        // Criar uma lista observável de contatos para exibição no ComboBox
        ObservableList<Contato> contatosObservable = FXCollections.observableArrayList(contatos);

        // Definir a lista de contatos no ComboBox
        comboContato.setItems(contatosObservable);
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
        telefoneContato = new TelefoneContato();
        stage.close();
    }

    @FXML
    public void salvar(ActionEvent event) {
        AvisoController avisoController = new AvisoController();
        try {
            telefoneContato.setTelefone(telefoneLbl.getText());
            telefoneContato.setDdd(dddLbl.getText());
            telefoneContato.setEmail(emailLbl.getText());

            if(comboContato.getValue() == null) {
                avisoController.showAlerta(new Stage(), "Selecione um contato!", false);
                return;
            }
            telefoneContato.setContato(comboContato.getValue());
            telefoneContatoRepository.save(telefoneContato);
            telefoneContato = new TelefoneContato(); //nao remover pelo amor de deus, se tirar isso, o telefoneContato vai ser criado com o nome do ultimo telefoneContato selecionado
            fecharTela(event);
            avisoController.showAlerta(new Stage(), "TelefoneContato salvo com sucesso!", false);
        } catch (Exception e) {

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
        comboContato.setValue(telefoneContato.getContato());
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
            comboContato.setDisable(true);
        }
    }

    public static TelefoneContato getTelefoneContato() {
        return telefoneContato;
    }

    public static void setTelefoneContato(TelefoneContato telefoneContato) {
        TelefoneContatoController.telefoneContato = telefoneContato;
    }
}
