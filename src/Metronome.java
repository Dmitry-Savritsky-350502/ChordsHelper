
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ������� on 29.10.2015.
 */
public class Metronome {
    private Double chosenSpeed;
    private MediaPlayer tickPlayer;
    private Media chosenSound;
    private String tickPath;
    private Timer timer;
    public Metronome()
    {
    }

    public void play (double speed,String name)  {
        chosenSpeed = speed;
        tickPath=name;
        chosenSound = new Media(new File(tickPath).toURI().toString());
        tickPlayer = new MediaPlayer(chosenSound);
        tickPlayer.setVolume(1.0);
        tickPlayer.setCycleCount(1);
        long delay=(long) (60000 / chosenSpeed);

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                tickPlayer.stop();
                tickPlayer.play();
            }
        };
        timer=new Timer(true);
        timer.scheduleAtFixedRate(task, 0, delay);
    }

    public boolean stop()
    {
        if(tickPlayer!=null && timer!=null)
            {
                timer.cancel();
                tickPlayer.stop();
                return true;
            }
        return false;
    }
}
