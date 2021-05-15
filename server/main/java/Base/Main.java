package Base;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mPrimaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mPrimaryStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> {
            System.exit(1);
        });
        mPrimaryStage.setTitle("Высокопроизводительные вычисления. Вариант №6. Клиент.");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/PrimeWindowFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
