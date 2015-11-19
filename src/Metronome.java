
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Metronome class used for repeating sound with chosen delay
 *
 * @author Dmitry Savritsky
 */
public class Metronome {
    /**
     * Chosen speed of sound in hits/min
     */
    private Double chosenSpeed;
    /**
     * MediaPlayer for playing sound
     */
    private MediaPlayer tickPlayer;
    /**
     * Timer for repeating
     */
    private Timer timer;
    /**
     * Plays music with chosen speed and music path
     * @param speed Speed in hit/min
     * @param path Path to the chosen music sample
     */
    public void play (double speed,String path)  {
        chosenSpeed = speed;
        Media chosenSound = new Media(new File(path).toURI().toString());
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
    /**
     * Stops music playing
     */
    public void stop()
    {
        if(tickPlayer!=null && timer!=null)
            {
                timer.cancel();
                tickPlayer.stop();
            }
    }
}
