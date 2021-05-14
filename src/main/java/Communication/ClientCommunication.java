package Communication;

import lombok.Data;

import java.net.Socket;
import java.util.Objects;
import java.util.UUID;

@Data
public class ClientCommunication {

    private Socket socket;
    private int loadPercent;
    private String uuid = UUID.randomUUID().toString();

    public ClientCommunication(Socket socket, int loadPercent) {
        this.socket = socket;
        this.loadPercent = loadPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientCommunication that = (ClientCommunication) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
