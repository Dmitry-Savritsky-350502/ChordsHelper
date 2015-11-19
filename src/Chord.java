import java.lang.*;
/**
 * Chord class is used to store attributes of guitar chord
 *
 * @author Dmitry Savritsky
 */
public class Chord {
    /**
     * Name of the chord
     */
   private String name;
    /**
     * Name of the table(note) for which chord is belongs to
     */
   private String tableName;
    /**
     * Applicature of chord
     */
   private Integer[] applicature;
    /**
     * Flag of chord validation
     */
   private Boolean valid;
    /**
     *
     * @param n name of chord
     * @param tn table name(name of note, for which chord is belongs to)
     */
    public Chord(String n,String tn)
    {   name=n;
        tableName=tn;
        valid=true;
        applicature=new Integer[6];
        applicature[0]=0;
        applicature[1]=0;
        applicature[2]=0;
        applicature[3]=0;
        applicature[4]=0;
        applicature[5]=0;
    }
    public String getName()
    {
        return name;
    }
    public String getTableName()
    {
        return tableName;
    }
    public Integer[] getApplicature()
    {
        return applicature;
    }
    public Boolean getValid()
    {
        return valid;
    }
    public void setName(String n)
    {
        name=n;
    }
    public void setTableName(String tn)
    {
        tableName=tn;
    }
    public void setApplicature(Integer[] app)
    {
        applicature=app;
    }
    public void setValid(Boolean v)
    {
        valid=v;
    }
}
