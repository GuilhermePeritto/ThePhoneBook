package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Model.Contato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ContatoListController {

    @FXML
    private Button btnInspecionarContato;

    @FXML
    private ImageView imgContato;

    @FXML
    private MenuButton menuButtonMaisOp;

    @FXML
    private Label nomeContatoLbl;

    private Contato contato;

    public void initialize() {
        // Configurar os itens do MenuButton
        MenuItem editarItem = new MenuItem("Editar");
        MenuItem excluirItem = new MenuItem("Excluir");

        // Configurar os eventos dos itens
        editarItem.setOnAction(event -> {
            try {
                handleEditarContato();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        excluirItem.setOnAction(event -> {
            try {
                handleExcluirContato();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Adicionar os itens ao MenuButton
        menuButtonMaisOp.getItems().addAll(editarItem, excluirItem);

        // ... (outros códigos)
    }

    private void handleEditarContato() throws IOException {
        // Lógica para editar o contato
        EditarContatoBtnEvent(new ActionEvent());

        // Ocultar o MenuButton após a seleção
        menuButtonMaisOp.hide();
    }

    private void handleExcluirContato() throws IOException {
        // Lógica para excluir o contato
        ExcluirContatoBtnEvent(new ActionEvent());

        // Ocultar o MenuButton após a seleção
        menuButtonMaisOp.hide();
    }

    public void setData(Contato contato) {
        this.contato = contato;
        nomeContatoLbl.setText(contato.getDescricao());
        imgContato.setImage(new Image("File:" + contato.getLocalImagem()));
    }

    @FXML
    public void EditarContatoBtnEvent(ActionEvent event) throws IOException {
        ContatoController contatoController = new ContatoController();
        contatoController.setData(contato);
        contatoController.start(new Stage());
    }
    @FXML
    public void ExcluirContatoBtnEvent(ActionEvent event) throws IOException {
        ContatoController contatoController = new ContatoController();
        contatoController.setData(contato);
        contatoController.excluiContato();
    }
    @FXML
    public void InspecionarContatoBtnEvent(ActionEvent event) throws IOException {
        ContatoController contatoController = new ContatoController();
        contatoController.setData(contato);
        contatoController.desabilitar = true;
        contatoController.start(new Stage());
        contatoController.setContato(null);
    }

}
