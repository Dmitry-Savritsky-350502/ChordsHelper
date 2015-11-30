import java.util.Vector;

/**
 * ChordControl class used for manipulations with guitar chords, such as increasing/decreasing tone
 *
 * @author Dmitry Savritsky
 */
public class ChordControl {
    /**
     * Current tone offset of the transponed chords
     */
    private Integer tone;
    /**
     * Vector of the default chords
     */
    private Vector<Chord> defaultChords;
    /**
     * Vector of the transponed chords
     */
    private Vector<Chord> transponedChords;
    /**
     * Vector of names of the possible guitar notes
     */
    private Vector<String> standard;
    /**
     * Creates class object with initialized vector of the possible guitar notes
     */
    public ChordControl()
    {
        tone=0;
        defaultChords=new Vector<>();
        transponedChords=new Vector<>();
        standard=new Vector<>();
        standard.add("A");
        standard.add("A#");
        standard.add("B");
        standard.add("C");
        standard.add("C#");
        standard.add("D");
        standard.add("D#");
        standard.add("E");
        standard.add("F");
        standard.add("F#");
        standard.add("G");
        standard.add("G#");
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
        transponedChords=new Vector<>() ;
        for(Chord tempElem : a)
        {
            Chord temp=new Chord(tempElem.getName(),tempElem.getTableName());
            temp.setApplicature(tempElem.getApplicature());
            transponedChords.add(temp);
        }
    }

    public Integer getTone() {
        return tone;
    }
    /**
     * Increases tones of all chords in transponedChords class field by 1 by changing its names to next one
     *
     */
    public void increaseTone()
    {
        tone++;
        if(tone==12) tone=0;
        for (Chord transponedChord : transponedChords) {
            //set table name of transponed chord
            int index = standard.indexOf(transponedChord.getTableName());
            if (index == 11) transponedChord.setTableName(standard.get(0));
            else transponedChord.setTableName(standard.get(index + 1));
            //change name of chord
           changeNameOfChord(transponedChord);
        }
    }
    /**
     * Decreases tones of all chords in transponedChords class field by 1 by changing its names to previous one
     *
     */
    public void decreaseTone()
    {
        tone--;
        if(tone==-12) tone=0;
        for (Chord transponedChord : transponedChords) {
            //set table name of transponed chord
            int index = standard.indexOf(transponedChord.getTableName());
            if (index == 0) transponedChord.setTableName(standard.get(11));
            else transponedChord.setTableName(standard.get(index - 1));
            //change name of chord
            changeNameOfChord(transponedChord);
        }
    }
    /**
     * Changes names of the chord due to the name of the note of the table
     *
     * @param transponedChord chord, which name we need to change
     */
    public void changeNameOfChord(Chord transponedChord)
    {   String temp;
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
    public void setTone(int t)
    {
        if(tone >= -11 && tone <=11)
        tone=t;
    }
    public Vector<String> getStandardTones()
    {
        return standard;
    }

}
