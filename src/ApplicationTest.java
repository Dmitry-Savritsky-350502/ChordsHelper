/**
 * Created by ִלטענטי on 30.11.2015.
 */
import org.junit.Test;
import java.util.Vector;
import static org.junit.Assert.assertEquals;

public class ApplicationTest {
    @Test
    public void testIncreasingTone()
    {
        ChordControl a=new ChordControl();
        a.increaseTone();
        assertEquals((long) 1, (long) a.getTone());
        a.setTone(4);
        assertEquals((long) 4,(long) a.getTone());
    }

    @Test
    public void testDecreasingTone()
    {
        ChordControl a=new ChordControl();
        a.decreaseTone();
        assertEquals((long) -1, (long) a.getTone());
        a.setTone(-11);
        a.decreaseTone();
        assertEquals((long) 0, (long) a.getTone());
    }

    @Test
    public void testParsingNames()
    {
        MainApplication m=new MainApplication();
        String l="Am C Dm F";
        Vector<Chord> vect=m.parseEnteredNames(l);
        assertEquals(vect.get(0).getName(),"Am");
        assertEquals(vect.get(1).getName(),"C");
        assertEquals(vect.get(2).getName(),"Dm");
        assertEquals(vect.get(3).getName(),"F");
    }

    @Test
    public void testChangeNames()
    {
        ChordControl a=new ChordControl();
        Chord ch=new Chord("A5","F#");
        a.changeNameOfChord(ch);
        assertEquals(ch.getName(),"F#5");
    }

}
