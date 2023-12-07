package com.example.ThePhoneBook.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AvisoConfirmacaoController {

    @FXML
    private Button btnNao;

    @FXML
    private Button btnSim;

    @FXML
    private Label lblMensagem;

    private Boolean resultadoConfirmacao;
    private ConfirmacaoCallback confirmacaoCallback;

    public void showAlertaConfirmacao(Stage stage, String mensagem, ConfirmacaoCallback callback) throws IOException {
        FXMLLoader fxmlLoaderAvisoConfirmacao = new FXMLLoader(getClass().getClassLoader().getResource("View/AvisoConfirmacao.fxml"));
        Parent parent = fxmlLoaderAvisoConfirmacao.load();
        AvisoConfirmacaoController avisoController = fxmlLoaderAvisoConfirmacao.getController();
        avisoController.alterarMensagem(mensagem);

        // Configurar os callbacks
        avisoController.setConfirmacaoCallback(callback);

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.showAndWait();

        // Após o fechamento da janela, retorna o resultado da confirmação
        this.resultadoConfirmacao = avisoController.getResultadoConfirmacao();
    }

    public void alterarMensagem(String mensagem) {
        lblMensagem.setText(mensagem);
    }

    @FXML
    void onClickBtnNao() throws IOException {
        fecharAviso(false);
    }

    @FXML
    void onClickBtnSim() throws IOException {
        fecharAviso(true);
    }

    public void fecharAviso(Boolean resultado) throws IOException {
        Stage stage = (Stage) btnNao.getScene().getWindow();
        this.resultadoConfirmacao = resultado;

        // Chamar o callback ao fechar a janela
        if (confirmacaoCallback != null) {
            confirmacaoCallback.onConfirmacao(resultado);
        }

        stage.close();
    }

    public Boolean getResultadoConfirmacao() {
        return resultadoConfirmacao;
    }

    public void setConfirmacaoCallback(ConfirmacaoCallback confirmacaoCallback) {
        this.confirmacaoCallback = confirmacaoCallback;
    }

    public interface ConfirmacaoCallback {
        void onConfirmacao(Boolean resultado) throws IOException;
    }
}