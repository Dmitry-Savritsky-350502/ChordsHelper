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
        String temp;
        Vector<String> tones=new Vector<>();
        tones.add("A");
        tones.add("A#");
        tones.add("B");
        tones.add("C");
        tones.add("C#");
        tones.add("D");
        tones.add("D#");
        tones.add("E");
        tones.add("F");
        tones.add("F#");
        tones.add("G");
        tones.add("G#");
        tone++;
        if(tone==12) tone=0;
        for (Chord transponedChord : transponedChords) {
            //set table name of transponed chord
            int index = tones.indexOf(transponedChord.getTableName());
            if (index == 11) transponedChord.setTableName(tones.get(0));
            else transponedChord.setTableName(tones.get(index + 1));
            //change name of chord
            temp = transponedChord.getName();
            if (transponedChord.getName().length() > 1) {
                if (transponedChord.getName().charAt(1) == '#') {
                    if (transponedChord.getName().length() > 2) {
                        String tmp = transponedChord.getTableName() + temp.substring(2);
                        transponedChord.setName(tmp);
                    } else transponedChord.setName(transponedChord.getTableName());
                } else transponedChord.setName(transponedChord.getTableName() + temp.substring(1));
            } else transponedChord.setName(transponedChord.getTableName());
        }
    }
    public void decreaseTone()
    {
        String temp;
        Vector<String> tones=new Vector<>();
        tones.add("A");
        tones.add("A#");
        tones.add("B");
        tones.add("C");
        tones.add("C#");
        tones.add("D");
        tones.add("D#");
        tones.add("E");
        tones.add("F");
        tones.add("F#");
        tones.add("G");
        tones.add("G#");
        tone--;
        if(tone==-12) tone=0;
        for (Chord transponedChord : transponedChords) {
            //set table name of transponed chord
            int index = tones.indexOf(transponedChord.getTableName());
            if (index == 0) transponedChord.setTableName(tones.get(11));
            else transponedChord.setTableName(tones.get(index - 1));
            //change name of chord
            temp = transponedChord.getName();
            if (transponedChord.getName().length() > 1) {
                if (transponedChord.getName().charAt(1) == '#') {
                    if (transponedChord.getName().length() > 2) {
                        String tmp = transponedChord.getTableName() + temp.substring(2);
                        transponedChord.setName(tmp);
                    } else transponedChord.setName(transponedChord.getTableName());
                } else transponedChord.setName(transponedChord.getTableName() + temp.substring(1));
            } else transponedChord.setName(transponedChord.getTableName());
        }
    }
    public void setTone(int t)
    {
        tone=t;
    }

}
