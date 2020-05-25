import java.util.Timer;
import java.util.TimerTask;

public class ClientRequest {
    public ClientRequest() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };

        timer.schedule(task, 2000);
    }
}

