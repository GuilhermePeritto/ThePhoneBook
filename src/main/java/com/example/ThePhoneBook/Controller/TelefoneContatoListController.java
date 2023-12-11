package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Model.TelefoneContato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class TelefoneContatoListController {

    @FXML
    private Button abrirTelefoneContatoBtn;

    @FXML
    private Button editarTelefoneContatoBtn;

    @FXML
    private Button excluirTelefoneContatoBtn;

    @FXML
    private Label contatoLbl;

    @FXML
    private Label dddTelefoneContatoLbl;

    @FXML
    private Label telefoneTelefoneContatoLbl;

    @FXML
    private Label emailTelefoneContatoLbl;

    private TelefoneContato telefoneContato;

    public void setData(TelefoneContato telefoneContato) {
        this.telefoneContato = telefoneContato;
        dddTelefoneContatoLbl.setText(telefoneContato.getDdd());
        telefoneTelefoneContatoLbl.setText(telefoneContato.getTelefone());
        emailTelefoneContatoLbl.setText(telefoneContato.getEmail());
        contatoLbl.setText(telefoneContato.getContato().getDescricao());
    }

    @FXML
    private void InspecionarTelefoneContatoBtnEvent(ActionEvent event) throws IOException {
        TelefoneContatoController telefoneContatoController = new TelefoneContatoController();
        telefoneContatoController.setData(telefoneContato);
        telefoneContatoController.desabilitar = true;
        telefoneContatoController.start(new Stage());
        telefoneContatoController.setTelefoneContato(null);
    }

    @FXML
    private void EditarTelefoneContatoBtnEvent(ActionEvent event) throws IOException {
        TelefoneContatoController telefoneContatoController = new TelefoneContatoController();
        telefoneContatoController.setData(telefoneContato);
        telefoneContatoController.start(new Stage());
    }

    @FXML
    private void ExcluirTelefoneContatoBtnEvent(ActionEvent event) throws IOException {
        TelefoneContatoController telefoneContatoController = new TelefoneContatoController();
        DashBoardController dashBoardController = new DashBoardController();
        telefoneContatoController.setData(telefoneContato);
        telefoneContatoController.excluiTelefoneContato();
    }
}
