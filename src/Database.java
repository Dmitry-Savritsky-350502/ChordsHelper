
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by Дмитрий on 29.10.2015.
 */
public class Database {
    private String path;
    private Connection bd;
    private Statement statement;
    private ResultSet res;

    public Database(String p) throws SQLException,ClassNotFoundException
    {   path=p;
        initialize();
    }

    public Vector<Chord> getChords(Vector<Chord> chords) throws SQLException
    {
        for(int i=0;i<chords.size();i++) {
            res = statement.executeQuery("SELECT * FROM [" +chords.get(i).getTableName()+"] WHERE Chord='" + chords.get(i).getName()+"'");
            while (res.next()) {
                Integer temp[]=new Integer[6];
                temp[0]=res.getInt("Str1");
                temp[1]=res.getInt("Str2");
                temp[2]=res.getInt("Str3");
                temp[3]=res.getInt("Str4");
                temp[4]=res.getInt("Str5");
                temp[5]=res.getInt("Str6");
                chords.get(i).setApplicature(temp);
            }
        }
        return chords;
    }

    private boolean initialize() throws SQLException,ClassNotFoundException
    {   Class.forName("org.sqlite.JDBC");
        bd=DriverManager.getConnection("jdbc:sqlite:"+path);
        statement=bd.createStatement();
        return true;
    }

    /*public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }*/
}
