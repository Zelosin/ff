package Communication;

import gfg.CalculatingScheduler;
import gfg.CalculatingTask;
import logger.Logger;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.var;
import tracker.TimeTracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@AllArgsConstructor
public class ServerDataConsumer implements Runnable {

    private final ClientCommunication clientCommunication;
    private CalculatingTask clientTask;

    @Override
    @SneakyThrows
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(clientCommunication.getSocket().getInputStream()));
            PrintWriter pw = new PrintWriter(clientCommunication.getSocket().getOutputStream(), true);
            while (CommunicationService.hasTask()){
                clientTask =  CommunicationService.getTask();
                clientTask.setLoadPercent(clientCommunication.getLoadPercent());
                Logger.log(String.format("Клиенту (%s) выдан интервал от %f до %f%n", clientCommunication.getSocket(), clientTask.getUpperInterval(), clientTask.getDownInterval()));
                pw.println(clientTask.serialize());
                var clientResponse = CalculatingTask.deserialize(br.readLine());
                Logger.log(String.format("Получен ответ от клиента (%s) для интервала %f до %f : %f %n", clientCommunication.getSocket(), clientTask.getUpperInterval(), clientTask.getDownInterval(), clientResponse.getLocalResult()));
                TimeTracker.update(clientResponse.getElapsedTime());
                CalculatingScheduler.add(clientResponse.getLocalResult());
            }
            pw.println(new CalculatingTask(0, 0).serialize(1));
            Logger.log("Общий результат вычислений: " + CalculatingScheduler.getResult());
            return;
        } catch (Exception e){
            CommunicationService.pushTask(clientTask);
            CommunicationService.removeClient(clientCommunication);
            Logger.log("Соединение с клиентом (" + clientCommunication.getSocket().getInetAddress().getHostAddress() + ") закрыто, его задача возвращена в очередь");
            clientCommunication.getSocket().close();
        }
    }
}
