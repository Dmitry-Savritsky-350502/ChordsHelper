import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Tuning fork class is used for the generation of sin wave to tune guitar strings
 *
 * @author Dmitry Savritsky
 */
 public class TuningFork {
    /**
     * Data line of the generated sound
     */
     private SourceDataLine line;
    /**
     * Timer, which is used to repeat playing of generated sound
     */
     private Timer timer;
    /**
     * Buffer for the generated sound
     */
     private byte[] buffer;
    /**
     *
     * @param freq frequency of sound, which we need to generate
     * @param dur duration of the sound
     */
     public void play(double freq,int dur) throws LineUnavailableException,IllegalArgumentException
        {
            if(freq<0 || dur<0) throw new IllegalArgumentException("Illegal arguments of frequency or duration params");
            AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true, true);
            line = AudioSystem.getSourceDataLine(audioFormat);
            if(line==null) throw new LineUnavailableException("Line is unavailable for the sound generation");
            line.open(audioFormat);
            line.start();
            buffer = makeSinWave(audioFormat, freq, dur);
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    line.write(buffer, 0, buffer.length);
                    line.drain();
                }
            };
            timer=new Timer(true);
            timer.scheduleAtFixedRate(task, 0, dur*1000);
        }
    /**
     * Stops the sound of generated sound
     */
    public void stop()
        {
            if(timer!=null && line!=null)
            {
                timer.cancel();
                if (line.isRunning())
                    line.close();
            }
        }
    /**
     * Generates the sin wave with chosen params and returns byte array of the audio data
     * @param audioFormat chosen format of audio
     * @param frequency frequency of the sound
     * @param duration duration of the sound
     * @return array of values of generated sin wave
     */
     private byte[] makeSinWave (AudioFormat audioFormat, double frequency, long duration)
        {TimeUnit timeUnit =TimeUnit.SECONDS;
         byte[] buffer = new byte[(int) (timeUnit.toSeconds(duration) * audioFormat.getSampleRate())];
         double period = audioFormat.getSampleRate() / frequency;
         for (int i = 0; i < buffer.length; i++) {
             double angle = 2d * Math.PI * i / period;
             buffer[i] = (byte) (Math.sin(angle) * 127d);
            }
         return buffer;
        }
}
