package communication;

import gfg.CalculatingScheduler;
import gfg.CalculatingTask;
import javafx.scene.control.Button;
import logger.Logger;
import lombok.SneakyThrows;
import lombok.var;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

public class CommunicationService {

    private static Socket clientSocket;
    public static boolean connected = false;

    @SneakyThrows
    public void runClientFlow(String ip, int port, Button button) {
        try {
            clientSocket = new Socket(ip, port);
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            Logger.log("Соединение с сервером установлено");
            connected = true;
            button.setVisible(false);
            while (connected) {
                if (br.ready()) {
                    var task = CalculatingTask.deserialize(br.readLine());
                    if(task.getStatus() == 1) {
                        Logger.log("Сервер завершил расчеты");
                        continue;
                    }
                    Logger.log(String.format("Поступило задание для вычисление интеграла от %f до %f%n", task.getUpperInterval(), task.getDownInterval()));
                    var timeStart = System.currentTimeMillis();
                    var result =
                            new CalculatingScheduler()
                                    .init(task)
                                    .start()
                                    .join();
                    task.setLocalResult(result);
                    var elapsedTime = System.currentTimeMillis() - timeStart;
                    task.setElapsedTime(elapsedTime);
                    Logger.log(String.format("Интервал от %f до %f был вычислен за %d милисекунд%n", task.getUpperInterval(), task.getDownInterval(), elapsedTime));
                    pw.println(task.serialize());
                }
            }
            Logger.log("Осуществляется закрытие соединения с сервором");
        } catch (ConnectException e){
            Logger.log("Не удалось подключиться к серверу");
        }
        finally {
            if(Objects.nonNull(clientSocket)) {
                clientSocket.close();
                Logger.log("Соединение успешно закрыто");
            }
        }
    }
}
