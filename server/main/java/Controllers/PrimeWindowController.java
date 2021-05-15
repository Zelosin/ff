package Controllers;

import communication.CommunicationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import logger.Logger;
import lombok.SneakyThrows;

public class PrimeWindowController {
    @FXML
    private TextField dIPTextField;

    @FXML
    private TextField dPortTextField;

    @FXML
    private ListView<String> dLogList;

    @FXML
    private Button dConnectButton;

    private CommunicationService communicationService;

    public void initialize() {
        Logger.setdLogger(dLogList);
        dIPTextField.setText("localhost");
        dPortTextField.setText("4004");
    }

    @SneakyThrows
    public void connect(MouseEvent mouseEvent) {
        communicationService = new CommunicationService();
        new Thread(()->{
            communicationService.runClientFlow(dIPTextField.getText(), Integer.parseInt(dPortTextField.getText()), dConnectButton);
        }).start();
    }
}
