package gfg;

import com.google.gson.Gson;
import lombok.*;

@RequiredArgsConstructor
public class CalculatingTask {

    @Setter
    @Getter
    private int status;

    @NonNull
    @Getter
    private final double upperInterval;

    @NonNull
    @Getter
    private final double downInterval;

    @Setter
    private int loadPercent;

    @Getter
    private double elapsedTime;

    @Getter
    private double localResult;

    public String serialize(int status){
        this.setStatus(status);
        return new Gson().toJson(this);
    }

    public String serialize(){
        return new Gson().toJson(this);
    }

    public static CalculatingTask deserialize(String data){
        return new Gson().fromJson(data, CalculatingTask.class);
    }
}