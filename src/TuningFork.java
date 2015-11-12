/**
 * Created by ִלטענטי on 29.10.2015.
 */
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

 public class TuningFork {
     private SourceDataLine line;
     private Timer timer;
     private byte[] buffer;
     public  TuningFork()
        {

        }

     public boolean play(double freq,int dur) throws LineUnavailableException
        {
            AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true, true);
            line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
            line.start();
            buffer = makeSinWave(audioFormat, freq, dur, TimeUnit.SECONDS);

            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    line.write(buffer, 0, buffer.length);
                    line.drain();
                }
            };
            timer=new Timer(true);
            timer.scheduleAtFixedRate(task, 0, dur*1000);
            return true;

        }

    public boolean stop()
        {
            if(timer!=null && line!=null)
            {
                timer.cancel();
                if (line.isRunning())
                    line.close();
            }
            return true;
        }

     private byte[] makeSinWave (AudioFormat audioFormat, double frequency, long duration, TimeUnit timeUnit)
        {
         byte[] buffer = new byte[(int) (timeUnit.toSeconds(duration) * audioFormat.getSampleRate())];
         double period = audioFormat.getSampleRate() / frequency;
         for (int i = 0; i < buffer.length; i++) {
             double angle = 2d * Math.PI * i / period;
             buffer[i] = (byte) (Math.sin(angle) * 127d);
            }
         return buffer;
        }
}
