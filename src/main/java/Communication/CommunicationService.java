package Communication;

import gfg.CalculatingScheduler;
import gfg.CalculatingTask;
import gfg.GFG;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import logger.Logger;
import lombok.var;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class CommunicationService {

    private static final int port = 4004;
    private static Socket serverReceivedClientSocket;
    private static ServerSocket serverSocket;
    private static final Boolean isClientWaiting = true;

    private static final Deque<CalculatingTask> tasks = new ArrayDeque();
    public static final List<ClientCommunication> clientSockets = new ArrayList<>();
    public static ListView<String> dClientList;

    public synchronized static int getTaskCount(){
        return tasks.size();
    }

    public synchronized static void pushTask(CalculatingTask task){
        tasks.push(task);
    }

    public synchronized static CalculatingTask getTask() {
        return tasks.pollFirst();
    }

    public synchronized static void removeClient(ClientCommunication clientCommunication){
        var clientIndex = clientSockets.indexOf(clientCommunication);
        clientSockets.remove(clientCommunication);
        Platform.runLater(()->{
            dClientList.getItems().remove(clientIndex);
            dClientList.refresh();
        });
        Logger.log(String.format("От сервера отключился клиент (%s). Текущее число клиентов: %d", clientCommunication.getSocket(), clientSockets.size()));
    }

    public synchronized static boolean hasTask() {
        return !tasks.isEmpty();
    }

    public void start(double upperInterval, double downInterval){
        new Thread(() -> {
            Logger.log("Начато выполнение задачи.\nКоличество клиентов: " + clientSockets.size());
            var calculatedPeriod = (upperInterval - downInterval) / CalculatingScheduler.SUBTASK_COUNT;
            var taskInterval = downInterval;
            CalculatingScheduler.reset();
            tasks.clear();
            CalculatingScheduler.setEstimatedTaskCount(CalculatingScheduler.SUBTASK_COUNT);
            for (var i = 0; i < CalculatingScheduler.SUBTASK_COUNT; i++) {
                tasks.addFirst(new CalculatingTask(taskInterval + calculatedPeriod, taskInterval));
                taskInterval = taskInterval + calculatedPeriod;
            }
            clientSockets.forEach(client -> {
                new Thread(new ServerDataConsumer(client, null)).start();
            });
        }).start();
    }

    public void runServiceFlow() throws IOException {
        serverSocket = new ServerSocket(port);
        Logger.log("Сервер успешно сконфигурирован. IP: " + InetAddress.getByName("localhost") + " порт: " + port);
        Logger.log("Нажмите Старт, чтобы начать выполнение задачи");
        Logger.log("Сервер перешел в режим ожидания подключений");
        while (isClientWaiting) {
            serverReceivedClientSocket = serverSocket.accept();
            //if (!clientSockets.stream().anyMatch((client -> client.getSocket().getLocalAddress().equals(serverReceivedClientSocket.getLocalAddress())))) {
                clientSockets.add(new ClientCommunication(serverReceivedClientSocket, 50));
                Platform.runLater(()->{
                    dClientList.getItems().add(serverReceivedClientSocket.toString());
                    dClientList.refresh();
                });
                Logger.log("К серверу подключился клиент (" + serverReceivedClientSocket.getInetAddress() + "). Текущее число клиентов: " + clientSockets.size());
           // }
        }
    }
}
