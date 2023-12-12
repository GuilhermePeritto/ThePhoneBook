package com.example.ThePhoneBook.Controller;

import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.Contato;
import com.example.ThePhoneBook.Repository.ContatoRepository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class ContatoController {

    @FXML
    private TextField observacaoLbl;

    @FXML
    private ImageView imgContato;

    @FXML
    private DatePicker dataNLbl;

    @FXML
    private TextField nomeLbl;

    @FXML
    private BorderPane backgroundImagem;

    private ConfigurableApplicationContext springContext = Main.getContext();

    private static Contato contato = new Contato();

    private BigInteger idContato;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private String LocalDaImagem;

    public Boolean desabilitar = false;

    @Autowired
    ContatoRepository contatoRepository;


    @FXML
    public void initialize() {
        backgroundImagem.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                uploadImagem(); // Chame o método uploadImagem quando a imagem for clicada
            }
        });
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderContato = new FXMLLoader(getClass().getClassLoader().getResource("View/Contato.fxml"));
        fxmlLoaderContato.setControllerFactory(springContext::getBean);
        Parent parent = fxmlLoaderContato.load();
        // Obtém a instância do controlador
        ContatoController contatoController = fxmlLoaderContato.getController();
        contatoController.alterarDados(contato);
        contatoController.desabilitarCampos(desabilitar);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    private void alterarDados(Contato contato) {
        if(contato == null) {
            return;
        }
        nomeLbl.setText(contato.getDescricao());
        observacaoLbl.setText(contato.getObservacao());
        imgContato.setImage(new Image("file:" + contato.getLocalImagem()));
        idContato = contato.getIdContato();
        dataNLbl.setValue(contato.getDataNascimento());
        setData(contato);
    }

    public void setData(Contato contato) {
        this.contato = contato;
    }

    @FXML
    public void uploadImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Abrir a janela de diálogo para seleção do arquivo
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Diretório onde as imagens serão salvas localmente
            String localDirectory = "C:/PurpleGym/Imagens/Contatos/" + idContato + "/";

            // Verificar se o diretório existe, se não, criar
            File directory = new File(localDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Copiar a imagem para o diretório local
            Path localDaImagem = new File(localDirectory + idContato + ".png").toPath();
            try {
                Files.copy(selectedFile.toPath(), localDaImagem, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace(); // Tratar a exceção conforme necessário
            }
            LocalDaImagem = (localDaImagem.toString());

            // Atualizar a imagem exibida no aplicativo
            Image image = new Image("file:" + localDaImagem.toString());
            imgContato.setImage(image);
        }
    }

    @FXML
    public void salvarContato(ActionEvent event) throws IOException {
        contato.setDescricao(nomeLbl.getText());
        contato.setObservacao(observacaoLbl.getText());
        contato.setLocalImagem(LocalDaImagem);
        contato.setDataNascimento(dataNLbl.getValue());
        contatoRepository.save(contato);
        AvisoController avisoController = new AvisoController();
        avisoController.showAlerta(new Stage(), "Contato salvo com sucesso!", false);
        contato = new Contato();
        voltar(event);
    }

    private void voltar(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        contato = new Contato();
        stage.close();
        DashBoardController dashBoardController = new DashBoardController();
        dashBoardController.getDashBoardController().PesquisarTelefoneContatosBtnEvent(new ActionEvent());
    }

    @FXML
    public void cancelarContato(ActionEvent event) throws IOException {
        voltar(event);
    }

    public void excluiContato() {
        AvisoConfirmacaoController avisoConfirmacaoController = new AvisoConfirmacaoController();
        try {
            AvisoConfirmacaoController.ConfirmacaoCallback callback = resultado -> {
                if (resultado) {
                    springContext.getBean(ContatoRepository.class).deleteById(contato.getIdContato());
                    AvisoController avisoController = new AvisoController();
                    avisoController.showAlerta(new Stage(), "Contato excluído com sucesso!", false);
                    contato = new Contato();
                }
            };

            avisoConfirmacaoController.showAlertaConfirmacao(new Stage(), "Você tem certeza que deseja excluir este contato?", callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desabilitarCampos(Boolean desabilitar) {
        if (desabilitar) {
            nomeLbl.setDisable(true);
            observacaoLbl.setDisable(true);
            btnSalvar.setDisable(true);
            dataNLbl.setDisable(true);
        }
    }

    public static Contato getContato() {
        return contato;
    }

    public static void setContato(Contato contato) {
        ContatoController.contato = contato;
    }
}
