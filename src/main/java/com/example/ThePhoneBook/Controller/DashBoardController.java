package com.example.ThePhoneBook.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.MonthPage;
import com.example.ThePhoneBook.Main;
import com.example.ThePhoneBook.Model.Contato;
import com.example.ThePhoneBook.Model.TelefoneContato;
import com.example.ThePhoneBook.Repository.TelefoneContatoRepository;
import com.example.ThePhoneBook.Repository.ContatoRepository;
import com.example.ThePhoneBook.Repository.UsuarioRepository;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.fortuna.ical4j.model.property.Url;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Component
public class DashBoardController {
    @FXML
    public MonthPage agenda;

    public static DashBoardController dashBoardController;

    @FXML
    private VBox menuSlider;

    @FXML
    private Button homeBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button configuracaoBtn;

    @FXML
    private Line lineEspacamento;

    @FXML
    private Button telefoneContatosBtn;

    @FXML
    private Button perfilBtn;

    @FXML
    private Button contatosBtn;

    @FXML
    private AnchorPane dashBoardTelefoneContato;

    @FXML
    private AnchorPane dashBoardConfiguracao;

    @FXML
    private AnchorPane dashBoardContato;

    @FXML
    private AnchorPane dashBoardListContato;

    @FXML
    private AnchorPane dashBoardHome;

    @FXML
    private AnchorPane dashBoardPerfil;

    @FXML
    private Label moduloLbl;

    @FXML
    private Label usuarioLogadoLbl;

    @FXML
    public AnchorPane dashBoardListTelefoneContato;

    @FXML
    private Button adicionarTelefoneContatoBtn;

    @FXML
    private Button adicionarContatoBtn;

    @FXML
    private AnchorPane listTelefoneContato;

    @FXML
    private TextField pesquisaContatoTf;

    @FXML
    private Pagination paginacaoTelefoneContato;

    @FXML
    private TextField pesquisaTelefoneContatoTf;

    private Integer itensPorPaginaTelefoneContato = 15;
    private Integer itensPorPaginaContato = 15;
    private Integer contatosPorPagina = 8;

    private ObservableList<Node> paginatedTelefoneContatos;

    private ObservableList<Node> paginatedContatos;

    @FXML
    private Pagination paginacaoContato;

    @Autowired
    public TelefoneContatoRepository telefoneContatoRepository;

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public ContatoRepository contatoRepository;

    @FXML
    public AnchorPane dashBoardAgenda;

    @FXML
    public Button agendaBtn;

    public static Scene dashBoardView;

    private ContatoListController contatoListController;

    private static final double ORIGINAL_WIDTH = 23.0;
    private static final double EXPANDED_WIDTH = 250.0;

    private static final double ORIGINAL_WIDTH_MOUSE = 54;
    private static final double EXPANDED_WIDTH_MOUSE = 155;

    @FXML
    public void initialize() {

        // Adicione ou ajuste outros inicializadores aqui, se necessário
        menuSlider.setPrefWidth(ORIGINAL_WIDTH);
        setMouseEvents();

        paginatedTelefoneContatos = FXCollections.observableArrayList();
        paginacaoTelefoneContato.setPageFactory(this::createPageTelefoneContato);
        paginatedContatos = FXCollections.observableArrayList();
        paginacaoContato.setPageFactory(this::createPageContato);

        // Calcula dinamicamente o número de itens por página para telefonecontatos com base no tamanho do componente
        double telefoneContatoNodeHeight = 150.0; // Ajuste o tamanho médio desejado para cada nó de telefoneContato
        int numLinhasTelefoneContatos = (int) (dashBoardListTelefoneContato.getHeight() / telefoneContatoNodeHeight);

        // Define o número de itens por página para telefonecontatos com base no número de linhas
        int itensPorPaginaTelefoneContatos = numLinhasTelefoneContatos;
        paginacaoTelefoneContato.setPageCount(1); // Apenas uma página para telefonecontatos

        // Calcula dinamicamente o número de itens por página para contatos com base na altura da tela
        double contatoNodeHeight = 150.0; // Ajuste o tamanho médio desejado para cada nó de contato
        int numLinhasContatos = (int) (Screen.getPrimary().getVisualBounds().getHeight() / contatoNodeHeight);

        itensPorPaginaTelefoneContato = 15;// Define o número de itens por página para contatos com base no número de linhas
        itensPorPaginaContato = numLinhasContatos;
        paginacaoContato.setPageCount(1); // Apenas uma página para contatos

    }

    private void setMouseEvents() {
        menuSlider.setOnMouseEntered(event -> expandMenuSlider());
        menuSlider.setOnMouseExited(event -> shrinkMenuSlider());

        setButtonMouseEvents(adicionarTelefoneContatoBtn);
        setButtonMouseEvents(adicionarContatoBtn);
    }

    private void setButtonMouseEvents(Button button) {
        button.setOnMouseEntered(event -> expandirBotaoCadastro(button));
        button.setOnMouseExited(event -> encolherBotaoCadastro(button));
    }

    private void expandirBotaoCadastro(Button button) {
        animateButtonWidth(button, EXPANDED_WIDTH_MOUSE);
    }

    private void encolherBotaoCadastro(Button button) {
        animateButtonWidth(button, ORIGINAL_WIDTH_MOUSE);
    }

    private void animateButtonWidth(Button button, double targetWidth) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(button.prefWidthProperty(), targetWidth);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void expandMenuSlider() {
        animateWidth(EXPANDED_WIDTH);
        contatosBtn.setText("Contatos");
        configuracaoBtn.setText("Configurações");
        perfilBtn.setText("Perfil");
        telefoneContatosBtn.setText("Telefones");
        homeBtn.setText("Home");
        logoutBtn.setText("Logout");
        agendaBtn.setText("Aniversariantes");
    }

    private void shrinkMenuSlider() {
        animateWidth(60.0);
        contatosBtn.setText("");
        configuracaoBtn.setText("");
        perfilBtn.setText("");
        telefoneContatosBtn.setText("");
        homeBtn.setText("");
        logoutBtn.setText("");
        agendaBtn.setText("");
    }

    @FXML
    private void TelefoneContatosBtnEvent(ActionEvent event) {
        this.showPane(dashBoardTelefoneContato);
        moduloLbl.setText("Contatos");
    }

    @FXML
    private void ConfiguracaoBtnEvent(ActionEvent event) {
        this.showPane(dashBoardConfiguracao);
        moduloLbl.setText("Configurações");

    }

    @FXML
    private void HomeBtnEvent(ActionEvent event) {
        this.showPane(dashBoardHome);
        moduloLbl.setText("Home");
    }

    @FXML
    private void LogoutBtnEvent(ActionEvent event) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLogin(Main.stage);
        fecharDashBoard();

    }

    @FXML
    private void PerfilBtnEvent(ActionEvent event) {
        this.showPane(dashBoardPerfil);
        moduloLbl.setText("Perfil");
    }

    @FXML
    private void ContatosBtnEvent(ActionEvent event) {
        this.showPane(dashBoardContato);
        moduloLbl.setText("Telefones");
    }

    @FXML
    private void AgendaBtnEvent(ActionEvent event) {
        this.showPane(dashBoardAgenda);
        moduloLbl.setText("Aniversariantes");
        AgendaController agendaController = new AgendaController();
        agendaController.Calcular(contatoRepository.findAllByOrderByDescricaoAsc(), agenda);
    }

    @FXML
    private void ListaTelefoneContatosBtnEvent(ActionEvent event) throws IOException {
        this.showPane(dashBoardListTelefoneContato);
        paginacaoTelefoneContato.setVisible(false);

    }

    @FXML
    private void ListaContatosBtnEvent(ActionEvent event) throws IOException {
        this.showPane(dashBoardListContato);
        paginacaoTelefoneContato.setVisible(false);
        paginacaoContato.setVisible(true);

    }

    @FXML
    private void AdicionarContatoBtnEvent(ActionEvent event) throws IOException {
        ContatoController contatoController = new ContatoController();
        contatoController.start(new Stage());
    }

    @FXML
    public void PesquisarContatosBtnEvent(ActionEvent event) throws IOException {
        LoadingController loadingController = new LoadingController();
        Stage loadingStage = new Stage();

        // Inicia o loading
        loadingController.iniciaLoading(loadingStage);

        // Cria uma thread para realizar a consulta em segundo plano
        Thread consultaThread = new Thread(() -> {
            List<Contato> listaContatos;
            if (!pesquisaContatoTf.getText().isEmpty()) {
                listaContatos = contatoRepository.findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(pesquisaContatoTf.getText());
            } else {
                listaContatos = contatoRepository.findAllByOrderByDescricaoAsc();
            }

            // Atualiza a UI na thread principal após a consulta
            Platform.runLater(() -> {
                try {
                    // Preenche a lista paginada
                    paginatedContatos.clear();
                    for (int i = 0; i < listaContatos.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/ContatoList.fxml"));
                        BorderPane borderPane = fxmlLoader.load();

                        ContatoListController contatoListController = fxmlLoader.getController();
                        contatoListController.setData((Contato) listaContatos.get(i));

                        paginatedContatos.add(borderPane);
                    }

                    int pageCount = (int) Math.ceil((double) paginatedContatos.size() / contatosPorPagina);
                    paginacaoContato.setPageCount(pageCount);
                    paginacaoContato.setCurrentPageIndex(0);
                    paginacaoContato.setPageFactory(this::createPageContato);

                    // Mostra a paginação
                    paginacaoContato.setVisible(true);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Fecha o loading após a consulta
                    loadingController.fecharLoading(loadingStage);
                    trocarPaginas(paginacaoContato);
                }
            });
        });

        // Inicia a thread de consulta
        consultaThread.start();
    }



    @FXML
    public void PesquisarTelefoneContatosBtnEvent(ActionEvent event) throws IOException {
        LoadingController loadingController = new LoadingController();
        Stage loadingStage = new Stage();

        // Inicia o loading
        loadingController.iniciaLoading(loadingStage);

        // Cria uma thread para realizar a consulta em segundo plano
        Thread consultaThread = new Thread(() -> {
            List<TelefoneContato> listaTelefoneContatos;
            if (!pesquisaTelefoneContatoTf.getText().isEmpty()) {
                List<Contato> listaDeIds = contatoRepository.findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(pesquisaTelefoneContatoTf.getText());
                listaTelefoneContatos = telefoneContatoRepository.findByContatoInOrderByTelefoneAsc(listaDeIds);
            } else {
                listaTelefoneContatos = telefoneContatoRepository.findAllByOrderByTelefoneAsc();
            }

            // Atualiza a UI na thread principal após a consulta
            Platform.runLater(() -> {
                try {
                    // Preenche a lista paginada
                    paginatedTelefoneContatos.clear();
                    for (int i = 0; i < listaTelefoneContatos.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/TelefoneContatoList.fxml"));
                        BorderPane borderPane = fxmlLoader.load();

                        TelefoneContatoListController telefoneContatosListController = fxmlLoader.getController();
                        telefoneContatosListController.setData((TelefoneContato) listaTelefoneContatos.get(i));

                        paginatedTelefoneContatos.add(borderPane);
                    }

                    int pageCount = (int) Math.ceil((double) paginatedTelefoneContatos.size() / itensPorPaginaTelefoneContato);
                    paginacaoTelefoneContato.setPageCount(pageCount);
                    paginacaoTelefoneContato.setCurrentPageIndex(0);
                    paginacaoTelefoneContato.setPageFactory(this::createPageTelefoneContato);

                    // Mostra a paginação
                    paginacaoTelefoneContato.setVisible(true);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Fecha o loading após a consulta
                    loadingController.fecharLoading(loadingStage);
                    trocarPaginas(paginacaoTelefoneContato);
                }
            });
        });

        // Inicia a thread de consulta
        consultaThread.start();
    }

    private void trocarPaginas(Pagination paginacao) {
        paginacao.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            paginacao.setCurrentPageIndex(newIndex.intValue());
        });
    }

    @FXML
    private void AdicionarTelefoneContatoBtnEvent(ActionEvent event) throws IOException {
        TelefoneContatoController telefoneContatoController = new TelefoneContatoController();
        telefoneContatoController.start(new Stage());
    }

    private Node createPageTelefoneContato(int pageIndex) {
        int fromIndex = pageIndex * itensPorPaginaTelefoneContato;
        int toIndex = Math.min(fromIndex + itensPorPaginaTelefoneContato, paginatedTelefoneContatos.size());

        ListView<Node> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(paginatedTelefoneContatos.subList(fromIndex, toIndex)));
        listView.setStyle("-fx-background-color: transparent;");

        AnchorPane anchorPane = new AnchorPane(listView);
        AnchorPane.setTopAnchor(listView, 0.0);
        AnchorPane.setBottomAnchor(listView, 0.0);
        AnchorPane.setLeftAnchor(listView, 0.0);
        AnchorPane.setRightAnchor(listView, 0.0);

        return anchorPane;
    }

    private Node createPageContato(int pageIndex) {
        int fromIndex = pageIndex * contatosPorPagina;
        int toIndex = Math.min(fromIndex + contatosPorPagina, paginatedContatos.size());

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        double contatoNodeWidth = 150.0;
        int numColunasContatos = (int) (dashBoardListContato.getPrefWidth() / (contatoNodeWidth + 20));

        for (int i = fromIndex; i < toIndex; i++) {
            Node contatoNode = paginatedContatos.get(i);
            int col = (i - fromIndex) % numColunasContatos;
            int row = (i - fromIndex) / numColunasContatos;
            gridPane.add(contatoNode, col, row);
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");

        // Centralize o GridPane na tela
        gridPane.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(scrollPane);
        anchorPane.setStyle("-fx-background-color: transparent;");

        // Configure as âncoras do ScrollPane dentro da AnchorPane
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);

        return anchorPane;
    }



    public void showPane(AnchorPane pane) {
        dashBoardTelefoneContato.setVisible(pane == dashBoardTelefoneContato);
        dashBoardConfiguracao.setVisible(pane == dashBoardConfiguracao);
        dashBoardContato.setVisible(pane == dashBoardContato);
        dashBoardHome.setVisible(pane == dashBoardHome);
        dashBoardPerfil.setVisible(pane == dashBoardPerfil);
        dashBoardListTelefoneContato.setVisible(pane == dashBoardListTelefoneContato);
        dashBoardListContato.setVisible(pane == dashBoardListContato);
        dashBoardAgenda.setVisible(pane == dashBoardAgenda);
        agenda.setVisible(pane == dashBoardAgenda);

    }


    private void animateWidth(double targetWidth) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(menuSlider.prefWidthProperty(), targetWidth);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        KeyValue keyValuesTelefoneContatos = new KeyValue(telefoneContatosBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameTelefoneContatos = new KeyFrame(Duration.millis(300), keyValuesTelefoneContatos);
        timeline.getKeyFrames().add(keyFrameTelefoneContatos);

        KeyValue keyValuesPerfil = new KeyValue(perfilBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFramePerfil = new KeyFrame(Duration.millis(300), keyValuesPerfil);
        timeline.getKeyFrames().add(keyFramePerfil);

        KeyValue keyValuesConfiguracao = new KeyValue(configuracaoBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameConfiguracao = new KeyFrame(Duration.millis(300), keyValuesConfiguracao);
        timeline.getKeyFrames().add(keyFrameConfiguracao);


        KeyValue keyValuesContatos = new KeyValue(contatosBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameContatos = new KeyFrame(Duration.millis(300), keyValuesContatos);
        timeline.getKeyFrames().add(keyFrameContatos);

        KeyValue keyValuesHome = new KeyValue(homeBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameHome = new KeyFrame(Duration.millis(300), keyValuesHome);
        timeline.getKeyFrames().add(keyFrameHome);

        KeyValue keyValuesLogout = new KeyValue(logoutBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameLogout = new KeyFrame(Duration.millis(300), keyValuesLogout);
        timeline.getKeyFrames().add(keyFrameLogout);

        KeyValue keyValuesLine = new KeyValue(lineEspacamento.endXProperty(), targetWidth);
        KeyFrame keyFrameLine = new KeyFrame(Duration.millis(300), keyValuesLine);
        timeline.getKeyFrames().add(keyFrameLine);

        KeyValue keyValuesAgenda = new KeyValue(agendaBtn.prefWidthProperty(), targetWidth);
        KeyFrame keyFrameAgenda = new KeyFrame(Duration.millis(300), keyValuesAgenda);
        timeline.getKeyFrames().add(keyFrameAgenda);

        timeline.play();
    }

    public void showDashBoard(Stage stage, LoginController loginController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/DashBoard.fxml"));
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent fxmlDashBoardView = fxmlLoader.load();
        dashBoardView = new Scene(fxmlDashBoardView);
        stage.setScene(dashBoardView);
        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(true);
        stage.show();
        dashBoardController = fxmlLoader.getController();
        dashBoardController.usuarioLogadoLbl.setText("Olá, " + loginController.getUsuarioLogado().getNome());
    }

    public void fecharDashBoard() {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        stage.close();
    }

    public DashBoardController getDashBoardController(){
        return dashBoardController;
    }

    public void filtrarTelefoneContato(Contato contato) throws IOException {
        showPane(dashBoardListTelefoneContato);
        moduloLbl.setText("Telefones");
        PesquisarTelefoneContatoFiltrado(contato);

    }

    @FXML
    public void PesquisarTelefoneContatoFiltrado(Contato contato) throws IOException {
        LoadingController loadingController = new LoadingController();
        Stage loadingStage = new Stage();

        // Inicia o loading
        loadingController.iniciaLoading(loadingStage);

        // Cria uma thread para realizar a consulta em segundo plano
        Thread consultaThread = new Thread(() -> {
            List<TelefoneContato> listaTelefoneContatos;
           listaTelefoneContatos = telefoneContatoRepository.findByContatoOrderByTelefoneAsc(contato);

            // Atualiza a UI na thread principal após a consulta
            Platform.runLater(() -> {
                try {
                    // Preenche a lista paginada
                    paginatedTelefoneContatos.clear();
                    for (int i = 0; i < listaTelefoneContatos.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/TelefoneContatoList.fxml"));
                        BorderPane borderPane = fxmlLoader.load();

                        TelefoneContatoListController telefoneContatosListController = fxmlLoader.getController();
                        telefoneContatosListController.setData((TelefoneContato) listaTelefoneContatos.get(i));

                        paginatedTelefoneContatos.add(borderPane);
                    }

                    int pageCount = (int) Math.ceil((double) paginatedTelefoneContatos.size() / itensPorPaginaTelefoneContato);
                    paginacaoTelefoneContato.setPageCount(pageCount);
                    paginacaoTelefoneContato.setCurrentPageIndex(0);
                    paginacaoTelefoneContato.setPageFactory(this::createPageTelefoneContato);

                    // Mostra a paginação
                    paginacaoTelefoneContato.setVisible(true);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Fecha o loading após a consulta
                    loadingController.fecharLoading(loadingStage);
                    trocarPaginas(paginacaoTelefoneContato);
                }
            });
        });

        // Inicia a thread de consulta
        consultaThread.start();
    }
}