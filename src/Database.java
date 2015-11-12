
import javafx.scene.control.Alert;

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
    private Statement statement;

    public Database(String p) throws SQLException,ClassNotFoundException
    {   path=p;
        initialize();
    }

    public Vector<Chord> getChords(Vector<Chord> chords) throws SQLException
    {
        Vector<Chord> tempChordsArray=new Vector<>();
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

        for (Chord chord1 : chords) {
            if (tones.indexOf(chord1.getTableName()) == -1)
                chord1.setValid(false);
        }

        for (Chord chord : chords) {
            if(chord.getValid()) {
                ResultSet res = statement.executeQuery("SELECT * FROM [" + chord.getTableName() + "] WHERE Chord='" + chord.getName() + "'");
                if (res.next()) {
                    Integer temp[] = new Integer[6];
                    temp[0] = res.getInt("Str1");
                    temp[1] = res.getInt("Str2");
                    temp[2] = res.getInt("Str3");
                    temp[3] = res.getInt("Str4");
                    temp[4] = res.getInt("Str5");
                    temp[5] = res.getInt("Str6");
                    chord.setApplicature(temp);
                } else chord.setValid(false);
            }
        }
        for (Chord chord : chords) {
            if (chord.getValid())
                tempChordsArray.add(chord);
        }

        checkChords(chords);

        return tempChordsArray;
    }

    private boolean initialize() throws SQLException,ClassNotFoundException
    {   Class.forName("org.sqlite.JDBC");
        Connection bd = DriverManager.getConnection("jdbc:sqlite:" + path);
        statement= bd.createStatement();
        return true;
    }

    public void checkChords(Vector<Chord> ch)
    {
        String notFound="";
        int flag=0;
        for(Chord chord : ch)
        {
            if(!chord.getValid()) {
                if(flag<4) notFound += chord.getName() + ",";
                flag++;
            }
        }

        if(notFound.length()!=0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            String text = "Some chords wasn't found: " + notFound;
            if (flag >= 4)
                text += " and " + (flag - 4) + " more. Check the spelling of chords.";
            alert.setContentText(text);
            alert.showAndWait();
        }
    }

    /*public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }*/
}
