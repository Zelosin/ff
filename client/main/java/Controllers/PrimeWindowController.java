package Controllers;

import Communication.CommunicationService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logger.Logger;
import lombok.SneakyThrows;
import lombok.var;
import tracker.TimeTracker;

import java.io.IOException;
import java.util.Objects;

public class PrimeWindowController {

    @FXML
    private TextField dUpperIntervalTextFiled;

    @FXML
    private TextField dDownIntervalTextField;

    @FXML
    private TextField dLoadPercent;

    @FXML
    private ListView<String> dLogList;

    @FXML
    private ListView<String> dClientList;

    @FXML
    private Label dEstText;
    @FXML
    private Label dSecLabel;

    @FXML
    private ProgressBar dProgressBar;

    private CommunicationService communicationService;

    @SneakyThrows
    public void initialize() {
        Logger.setdLogger(dLogList);
        TimeTracker.setTimeLabel(dSecLabel);
        TimeTracker.setProgressBar(dProgressBar);
        dLoadPercent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                changeLoadPercent(null);
            }
        });
        dUpperIntervalTextFiled.setText("2");
        dDownIntervalTextField.setText("1");
        new Thread(() -> {
            try {
                communicationService = new CommunicationService();
                CommunicationService.dClientList = dClientList;
                communicationService.runServiceFlow();
            } catch (IOException ignore) {
            }
        }).start();
    }

    public void changeLoadPercent(KeyEvent keyEvent) {
        var selectedIndex = dClientList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            var client = CommunicationService.clientSockets.get(selectedIndex);
            if(dLoadPercent.getText().equals("")){
                client.setLoadPercent(1);
            } else {
                client.setLoadPercent(Integer.parseInt(dLoadPercent.getText()));
            }
        }
    }

    protected void createAndShowModalWithMessage(String text){
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(new Label(text));
        root.setSpacing(40);
        var okButton = new Button();
        okButton.setOnMouseClicked(event -> {
            stage.close();
        });
        okButton.setText("Ок");
        okButton.setMinWidth(60);
       // okButton.setM(new Insets(50, 0, 0, 0));
        root.getChildren().add(okButton);
        stage.setScene(new Scene(root,400, 100));
        stage.setTitle("Сообщение системы.");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    public void start(MouseEvent mouseEvent) {
        if(CommunicationService.dClientList.getItems().isEmpty()){
            createAndShowModalWithMessage("Нет клиентов для выполнения задачи");
        } else {
            dEstText.setVisible(true);
            communicationService.start(Double.parseDouble(dUpperIntervalTextFiled.getText()), Double.parseDouble(dDownIntervalTextField.getText()));
        }
    }

    public void selectClient(MouseEvent mouseEvent) {
        var selectedIndex = dClientList.getSelectionModel().getSelectedIndex();
        if (Objects.nonNull(selectedIndex) && selectedIndex != -1) {
            var client = CommunicationService.clientSockets.get(selectedIndex);
            dLoadPercent.setText(String.valueOf(client.getLoadPercent()));
        }
    }
}
