import java.util.Vector;

/**
 * Created by ִלטענטי on 29.10.2015.
 */
public class ChordControl {
    private Integer tone;
    private Vector<Chord> defaultChords;
    private Vector<Chord> transponedChords;
    public ChordControl()
    {
        tone=0;
        defaultChords=new Vector<>();
        transponedChords=new Vector<>();
    }
    public Vector<Chord> getDefaultChords()
    {
        return defaultChords;
    }

    public void  setDefaultChords(Vector<Chord> a)
    {
        defaultChords=a;
    }

    public  Vector<Chord> getTransponedChords()
    {
        return transponedChords;
    }

    public void  setTransponedChords(Vector<Chord> a)
    {
        transponedChords=a;
    }

    public Integer getTone() {
        return tone;
    }
    public void increaseTone()
    {
        tone++;
        if(tone==12) tone=0;
    }
    public void decreaseTone()
    {
        tone--;
        if(tone==-12) tone=0;
    }
    public void setTone(Integer a)
    {
        tone=a;
    }


}
