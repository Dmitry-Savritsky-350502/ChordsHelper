/**
 * Created by ������� on 29.10.2015.
 */
import java.lang.*;
public class Chord {
   private String name;
   private String tableName;
   private Integer[] applicature;
   private Boolean valid;
   public Chord()
    {   name="Unknown";
        applicature=new Integer[6];
        for(int i=0;i<6;i++)
            applicature[i]=0;
    }
   public Chord(String n,String tn,Integer[] app)
   {   name=n;
       tableName=tn;
       applicature=app;
   }
    public Chord(String n,String tn)
    {   name=n;
        tableName=tn;
        valid=true;
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
