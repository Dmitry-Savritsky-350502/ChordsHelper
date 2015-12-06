import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Vector;

/**
 * MainApplication class is the main class for JavaFX application
 *
 * @author Dmitry Savritsky
 */
public class MainApplication extends Application {
    /**
     * Main stage of the application
     */
    private Stage mainStage;
    /**
     * Scene used to work in transponer mode
     */
    private Scene trasponerScene;
    /**
     * Scene used to work in tuning-fork mode
     */
    private Scene tuningForkScene;
    /**
     * Scene used to work in metronome mode
     */
    private Scene metronomeScene;
    private TuningFork tuningFork;
    private Metronome metronome;
    /**
     * Default path to music files
     */
    final private String MUSIC_PATH="Resources/Metronome/";
    /**
     * Default path to music files
     */
    private Database db;
    private ChordControl chordControl;

    public void setActiveTrasponerScene()//переходим на сцену транспонирования аккордов
    {mainStage.setScene(trasponerScene);
    }

    public void setActiveTuningForkScene()//переходим на сцену камертона
    {mainStage.setScene(tuningForkScene);
    }

    public void setActiveMetronomeScene()//переходим на сцену метронома
    {mainStage.setScene(metronomeScene);
    }
    /**
     * Initializes scene to work in trasponer mode
     * @return generated scene
     */
    private Scene initTransponerScene() throws SQLException
    {   Button increaseTone,decreaseTone,search;
        TextField typeChords;
        VBox vertical,chordsVbox;
        Label defaultChords,transposedChords,enterChords;
        Label transposeModeLabel,metronomeModeLabel,tuningForkModeLabel;
        Tab transposeMode,metronomeMode,tuningForkMode;
        TabPane tabs;

        ScrollPane defaultChordsViewScrollPane=new ScrollPane();
        defaultChordsViewScrollPane.setMaxSize(560,280);
        defaultChordsViewScrollPane.setPrefSize(560,280);

        ScrollPane transposedChordsViewScrollPane=new ScrollPane();
        transposedChordsViewScrollPane.setPrefSize(560,280);
        transposedChordsViewScrollPane.setMaxSize(560,280);

        try {
            db=new Database("Resources/Database/Chords.db");
        }
        catch (SQLException e) {
           Alert al=new Alert(Alert.AlertType.ERROR);
            al.setTitle("Database error!");
            al.setContentText(e.getMessage());
            al.showAndWait();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        chordControl=new ChordControl();

        typeChords=new TextField();
        typeChords.setAlignment(Pos.TOP_LEFT);
        typeChords.setPrefSize(200, 300);
        typeChords.setLayoutX(200);
        typeChords.setLayoutY(90);
        typeChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        increaseTone=new Button("Tone +1");
        decreaseTone=new Button("Tone -1");

        search=new Button("Search chords");
        search.setPrefSize(180, 40);
        search.setLayoutX(240);
        search.setLayoutY(420);
        search.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        increaseTone.setDisable(true);
        increaseTone.setPrefSize(180, 40);
        increaseTone.setLayoutX(240);
        increaseTone.setLayoutY(500);
        increaseTone.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        increaseTone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    chordControl.increaseTone();
                    chordControl.setTransponedChords(db.getChords(chordControl.getTransponedChords(),chordControl.getStandardTones()));
                    transposedChordsViewScrollPane.setContent(drawTransponed());//draw default chords
                    transposedChordsViewScrollPane.setHvalue(0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        decreaseTone.setDisable(true);
        decreaseTone.setPrefSize(180, 40);
        decreaseTone.setLayoutX(240);
        decreaseTone.setLayoutY(560);
        decreaseTone.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        decreaseTone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    chordControl.decreaseTone();
                    chordControl.setTransponedChords(db.getChords(chordControl.getTransponedChords(),chordControl.getStandardTones()));
                    transposedChordsViewScrollPane.setContent(drawTransponed());//draw default chords
                    transposedChordsViewScrollPane.setHvalue(0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        defaultChords=new Label("Default chords:");
        defaultChords.setPrefSize(200, 50);
        defaultChords.setLayoutX(600);
        defaultChords.setLayoutY(50);
        defaultChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));

        transposedChords=new Label("Transposed chords:");
        transposedChords.setPrefSize(200, 50);
        transposedChords.setLayoutX(600);
        transposedChords.setLayoutY(400);
        transposedChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));

        enterChords=new Label("Enter chords here:");
        enterChords.setAlignment(Pos.CENTER);
        enterChords.setPrefSize(200, 50);
        enterChords.setLayoutX(250);
        enterChords.setLayoutY(30);
        enterChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));

        transposeModeLabel=new Label("Transpose");
        transposeModeLabel.setAlignment(Pos.CENTER_LEFT);
        transposeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        metronomeModeLabel=new Label("Metronome");
        metronomeModeLabel.setAlignment(Pos.CENTER_LEFT);
        metronomeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        tuningForkModeLabel=new Label("Tuning-fork");
        tuningForkModeLabel.setAlignment(Pos.CENTER_LEFT);
        tuningForkModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        transposeMode=new Tab();
        transposeMode.setId("Transpose");
        transposeMode.setGraphic(transposeModeLabel);
        transposeMode.setClosable(false);

        metronomeMode=new Tab();
        metronomeMode.setId("Metronome");
        metronomeMode.setGraphic(metronomeModeLabel);
        metronomeMode.setClosable(false);

        tuningForkMode=new Tab();
        tuningForkMode.setId("Tuning-fork");
        tuningForkMode.setGraphic(tuningForkModeLabel);
        tuningForkMode.setClosable(false);

        vertical=new VBox();
        vertical.setLayoutX(200);
        vertical.setLayoutY(0);
        vertical.setSpacing(30);
        vertical.setAlignment(Pos.TOP_CENTER);

        vertical.getChildren().add(enterChords);
        vertical.getChildren().add(typeChords);
        vertical.getChildren().add(search);
        vertical.getChildren().add(increaseTone);
        vertical.getChildren().add(decreaseTone);

        tabs=new TabPane();
        tabs.setLayoutX(0);
        tabs.setSide(Side.LEFT);
        tabs.setPrefSize(200, 700);
        tabs.setTabMinHeight(180);
        tabs.setTabMinWidth(50);
        tabs.getTabs().add(transposeMode);
        tabs.getTabs().add(metronomeMode);
        tabs.getTabs().add(tuningForkMode);

        SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
        selectionModel.select(transposeMode);

        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (newValue.getId().equals("Metronome")) {
                            setActiveMetronomeScene();
                            selectionModel.select(transposeMode);
                        }
                        if (newValue.getId().equals("Tuning-fork")) {
                            setActiveTuningForkScene();
                            selectionModel.select(transposeMode);
                        }

                    }
                }
        );

        chordsVbox=new VBox();
        chordsVbox.setLayoutX(420);
        chordsVbox.setLayoutY(0);
        chordsVbox.getChildren().add(defaultChords);
        chordsVbox.getChildren().add(defaultChordsViewScrollPane);
        chordsVbox.getChildren().add(transposedChords);
        chordsVbox.getChildren().add(transposedChordsViewScrollPane);

        AnchorPane pane=new AnchorPane();
        pane.setPrefSize(1000, 700);

        pane.getChildren().add(tabs);
        pane.getChildren().add(vertical);
        pane.getChildren().add(chordsVbox);

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    increaseTone.setDisable(false);
                    decreaseTone.setDisable(false);
                    chordControl.setDefaultChords(parseEnteredNames(typeChords.getText()));
                    chordControl.setDefaultChords(db.getChords(chordControl.getDefaultChords(),chordControl.getStandardTones()));//get default chords
                    chordControl.setTransponedChords(chordControl.getDefaultChords());
                    chordControl.setTone(0);
                    defaultChordsViewScrollPane.setContent(drawDefault());//draw default chords
                    defaultChordsViewScrollPane.setHvalue(0);
                    transposedChordsViewScrollPane.setContent(null);
                    transposedChordsViewScrollPane.setHvalue(0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        return new Scene(pane);
    }
    /**
     * Initializes scene to work in tuning-fork mode
     * @return generated scene
     */
    private Scene initTuningForkScene()
    {
        Label transposeModeLabel,metronomeModeLabel,tuningForkModeLabel;
        Tab transposeMode,metronomeMode,tuningForkMode;
        TabPane tabs;
        Button playSound;
        TextArea description;
        CheckBox enableDescription;
        VBox vertical;

        tuningFork=new TuningFork();

        transposeModeLabel=new Label("Transpose");
        transposeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        metronomeModeLabel=new Label("Metronome");
        metronomeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        tuningForkModeLabel=new Label("Tuning-fork");
        tuningForkModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        transposeMode=new Tab();
        transposeMode.setId("Transpose");
        transposeMode.setGraphic(transposeModeLabel);
        transposeMode.setClosable(false);

        metronomeMode=new Tab();
        metronomeMode.setId("Metronome");
        metronomeMode.setGraphic(metronomeModeLabel);
        metronomeMode.setClosable(false);

        tuningForkMode=new Tab();
        tuningForkMode.setId("Tuning-fork");
        tuningForkMode.setGraphic(tuningForkModeLabel);
        tuningForkMode.setClosable(false);

        tabs=new TabPane();
        tabs.setSide(Side.LEFT);
        tabs.setPrefSize(200, 700);
        tabs.setTabMinHeight(180);
        tabs.setTabMinWidth(50);
        tabs.getTabs().add(transposeMode);
        tabs.getTabs().add(metronomeMode);
        tabs.getTabs().add(tuningForkMode);

        SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
        selectionModel.select(tuningForkMode);

        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (newValue.getId().equals("Metronome")) {
                            setActiveMetronomeScene();
                            selectionModel.select(tuningForkMode);
                            tuningFork.stop();
                        }
                        if (newValue.getId().equals("Transpose")) {
                            setActiveTrasponerScene();
                            selectionModel.select(tuningForkMode);
                            tuningFork.stop();
                        }

                    }
                }
        );

        playSound=new Button("Play");
        playSound.setPrefSize(180, 40);
        playSound.setLayoutX(300);
        playSound.setLayoutY(200);
        playSound.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        playSound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playSound.getText().equals("Play")) {

                    try {
                        tuningFork.play(440, 10);
                        playSound.setText("Stop");
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                } else {
                    tuningFork.stop();
                    playSound.setText("Play");
                }
            }
        });

        description=new TextArea();
        description.setPrefSize(600, 160);
        description.setText("Instruction for usage :\n" +
                "1) Hold the first string at the fifth fret and make it tune with the tuning fork sound\n" +
                "2) Hold the second string at the fifth fret and make it tune with the open first.\n" +
                "3) Hold the third string at the fourth fret and make it tune with the open second.\n" +
                "4) Hold the fourth string at the fifth fret and make it tune with the open third.\n" +
                "5) Hold the fifth string at the fifth fret and make it tune with the open fourth.\n" +
                "6) Hold the sixth string at the fifth fret and make it tune with the open fifth.");
        description.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        enableDescription=new CheckBox("Show/Don't show description");
        enableDescription.setPrefSize(300, 50);
        enableDescription.setSelected(true);
        enableDescription.setAlignment(Pos.CENTER);
        enableDescription.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        vertical=new VBox();
        vertical.setAlignment(Pos.TOP_CENTER);
        vertical.setSpacing(40);
        vertical.setLayoutX(300);
        vertical.setLayoutY(50);
        vertical.setPrefWidth(600);
        vertical.getChildren().add(enableDescription);
        vertical.getChildren().add(description);
        vertical.getChildren().add(playSound);

        enableDescription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!enableDescription.isSelected()) {
                    vertical.getChildren().remove(description);
                    vertical.setAlignment(Pos.TOP_CENTER);
                }
                else if (!vertical.getChildren().contains(description)) {
                    vertical.getChildren().remove(playSound);
                    vertical.getChildren().add(description);
                    vertical.getChildren().add(playSound);
                    vertical.setAlignment(Pos.TOP_CENTER);
                }
            }
        });

        AnchorPane pane=new AnchorPane();
        pane.setPrefSize(1000, 700);
        pane.getChildren().add(tabs);
        pane.getChildren().add(vertical);

        return new Scene(pane);
    }
    /**
     * Initializes scene to work in metronome mode
     * @return generated scene
     */
    private Scene initMetronomeScene() throws InterruptedException,FileNotFoundException
    {
        Label transposeModeLabel,metronomeModeLabel,tuningForkModeLabel;
        Tab transposeMode,metronomeMode,tuningForkMode;
        TabPane tabs;
        Label chooseSound,seeSpeed;
        VBox vertical;
        Slider speed;
        ComboBox<String> sounds;
        Button play;

        transposeModeLabel=new Label("Transpose");
        transposeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        metronomeModeLabel=new Label("Metronome");
        metronomeModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        tuningForkModeLabel=new Label("Tuning-fork");
        tuningForkModeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        transposeMode=new Tab();
        transposeMode.setId("Transpose");
        transposeMode.setGraphic(transposeModeLabel);
        transposeMode.setClosable(false);

        metronomeMode=new Tab();
        metronomeMode.setId("Metronome");
        metronomeMode.setGraphic(metronomeModeLabel);
        metronomeMode.setClosable(false);

        tuningForkMode=new Tab();
        tuningForkMode.setId("Tuning-fork");
        tuningForkMode.setGraphic(tuningForkModeLabel);
        tuningForkMode.setClosable(false);

        tabs=new TabPane();
        tabs.setSide(Side.LEFT);
        tabs.setPrefSize(200, 700);
        tabs.setTabMinHeight(180);
        tabs.setTabMinWidth(50);
        tabs.getTabs().add(transposeMode);
        tabs.getTabs().add(metronomeMode);
        tabs.getTabs().add(tuningForkMode);

        SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
        selectionModel.select(metronomeMode);

        seeSpeed=new Label();
        seeSpeed.setText("Current tick/min : " + 1);
        seeSpeed.setPrefSize(200, 50);
        seeSpeed.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        seeSpeed.setAlignment(Pos.CENTER);

        speed = new Slider(1, 120, 1);
        speed.setBlockIncrement(1);
        speed.setOrientation(Orientation.HORIZONTAL);
        speed.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                seeSpeed.setText("Current tick/min : " + Math.round(new_val.intValue()));
            }
        });


        chooseSound=new Label("Choose sound of the metronome:");
        chooseSound.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        sounds=new ComboBox<>();
        sounds.setPrefSize(250, 40);

        File soundsDirectory=new File(MUSIC_PATH);
        ObservableList<String> list=FXCollections.observableArrayList();

        FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0)
                {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');

                    // get extension
                    String str = name.substring(lastIndex);

                    // match path name extension
                    if(str.equals(".wav"))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
        File[] musicFiles=soundsDirectory.listFiles(fileNameFilter);
        for(File file : musicFiles)
        {
            list.add(file.getName());
        }
        sounds.setItems(list);
        sounds.setValue(list.get(0));

        play = new Button("Play");
        play.setPrefSize(180, 40);
        play.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (play.getText().equals("Play")) {
                    metronome = new Metronome();
                    metronome.play(speed.getValue(), "Resources/Metronome/" + sounds.getValue());
                    sounds.setDisable(true);
                    play.setText("Stop");
                    speed.setDisable(true);
                } else {
                    metronome.stop();
                    play.setText("Play");
                    speed.setDisable(false);
                    sounds.setDisable(false);
                }
            }
        });

        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (newValue.getId().equals("Tuning-fork")) {
                            setActiveTuningForkScene();
                            selectionModel.select(metronomeMode);
                            if (metronome != null) metronome.stop();
                            speed.setDisable(false);
                            sounds.setDisable(false);
                            play.setText("Play");
                        }
                        if (newValue.getId().equals("Transpose")) {
                            setActiveTrasponerScene();
                            selectionModel.select(metronomeMode);
                            if (metronome != null) metronome.stop();
                            speed.setDisable(false);
                            sounds.setDisable(false);
                            play.setText("Play");
                        }
                    }
                }
        );

        vertical = new VBox();
        vertical.setAlignment(Pos.TOP_CENTER);
        vertical.setSpacing(40);
        vertical.setLayoutX(450);
        vertical.setLayoutY(70);
        vertical.getChildren().add(seeSpeed);
        vertical.getChildren().add(speed);
        vertical.getChildren().add(chooseSound);
        vertical.getChildren().add(sounds);
        vertical.getChildren().add(play);

        AnchorPane pane=new AnchorPane();
        pane.setPrefSize(1000,700);

        pane.getChildren().add(tabs);
        pane.getChildren().add(vertical);

        return new Scene(pane);
    }
    /**
     * Parses entered names
     * @param names names of the entered chords
     * @return vector with parsed names
     */
    public Vector<Chord> parseEnteredNames(String names) {
        Vector<Chord> chords = new Vector<>();
        int flag=0;
        if(names.length()==0) return chords;
        String[] array= names.split("\\s+");
        String tt="";
        for (String anArray : array) {
            if (anArray.length() > 1) {
                if (anArray.charAt(1) == '#') {
                    tt += anArray.charAt(0);
                    tt += anArray.charAt(1);
                }
                else tt += anArray.charAt(0);
            }
            else tt += anArray.charAt(0);

            for(Chord tempChord : chords) {
                if(anArray.equals(tempChord.getName())) {
                    flag++;
                    break;
                }
            }
           if(flag==0) chords.add(new Chord(anArray, tt));
            flag=0;
            tt = "";
        }
        return chords;
    }

    /**
     * Draws chord on canvas
     * @param chordVector vector of chords that we need to draw
     * @return canvas with the images
     */
    private Canvas drawChord(Vector<Chord> chordVector)
    {
        int min;
        int offsetX=20;
        int offsetY=80;
        int fretSize=36;
        int stringOffset=20;
        double circleRadius=16;
        int fretsSize=fretSize*5;
        int grifSize=stringOffset*7;
        int chordHSize=grifSize+stringOffset*4;

        int count=chordVector.size();
        for(int f=0;f<chordVector.size();f++)
        {
            if(!chordVector.get(f).getValid())
                count--;
        }
        Canvas canvas=new Canvas(count*chordHSize,280);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(Chord temp : chordVector ) {
            if (temp.getValid()) {
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(4);
                gc.strokeRect(offsetX, offsetY, grifSize, fretsSize);//draw rectangle for the chord

                gc.setLineWidth(2);
                for (int i = 1; i < 7; i++)
                    gc.strokeLine(offsetX + stringOffset * i, offsetY, offsetX + stringOffset * i, offsetY + fretsSize);//draw strings

                for (int i = 1; i < 5; i++)
                    gc.strokeLine(offsetX, offsetY + fretSize * i, offsetX + grifSize, offsetY + fretSize * i);//draw frets

                Integer[] tempBuf = temp.getApplicature();//save applicature of the chord
                min = 13;
                for (Integer j : tempBuf) {
                    if (j != -1 && j!=0 && j < min)
                        min = j;
                }
                gc.setFill(Color.RED);
                for (int i = 0; i < 6; i++)//draw red circles
                    if (tempBuf[i] != -1 && tempBuf[i] != 0) {
                        gc.strokeOval(offsetX + stringOffset * (6 - i) - (circleRadius / 2), offsetY - (fretSize / 2) + fretSize * (tempBuf[i] - min+1) - circleRadius / 2, circleRadius, circleRadius);
                        gc.fillOval(offsetX + stringOffset * (6 - i) - (circleRadius / 2), offsetY - (fretSize / 2) + fretSize * (tempBuf[i] - min+1) - circleRadius / 2, circleRadius, circleRadius);
                    }
                gc.setLineWidth(2);

                gc.setFont(new Font("Arial", 12));
                for(Integer j=0;j<tempBuf.length;j++)//draw applicature
                {
                    if(tempBuf[j]==-1)
                    gc.strokeText("x",offsetX+stringOffset*(6-j),offsetY-10);
                    else gc.strokeText(tempBuf[j].toString(),offsetX+stringOffset*(6-j),offsetY-10);
                }

                for(int j=0;j<5;j++)//draw frets
                {
                    gc.strokeText(Integer.toString(min+j),offsetX-10,offsetY+fretSize/2+fretSize*j);
                }

                offsetX += grifSize + 2*stringOffset;
            }
        }
        return canvas;
    }
    /**
     * Draws default chord on canvas
     * @return canvas with chords
     */
    public Canvas drawDefault()
    {   int offsetX=20;
        int offsetY=80;
        int stringOffset=20;
        int grifSize=stringOffset*7;
        Canvas temp = drawChord(chordControl.getDefaultChords());
        GraphicsContext gc = temp.getGraphicsContext2D();
        for (Chord chord : chordControl.getDefaultChords()) {
            gc.setFont(new Font("Arial", 24));
            gc.setTextAlign(TextAlignment.CENTER);//draw name of the chord
            gc.strokeText(chord.getName(), offsetX + (grifSize / 2), offsetY - 40);
            offsetX += grifSize + 2*stringOffset;
        }
        return temp;
    }
    /**
     * Draws default chord on canvas
     * @return canvas with chords
     */
    public Canvas drawTransponed()
    {
        String tempString;
        int offsetX=20;
        int offsetY=80;
        int stringOffset=20;
        int grifSize=stringOffset*7;
        Canvas temp = drawChord(chordControl.getTransponedChords());
        GraphicsContext gc = temp.getGraphicsContext2D();
        gc.setFont(new Font("Arial", 24));
        gc.setTextAlign(TextAlignment.CENTER);//draw name of the chord
        for (int i=0;i<chordControl.getTransponedChords().size(); i++) {
            if(chordControl.getTone()>=0)
            tempString=chordControl.getTransponedChords().get(i).getName()+"("+chordControl.getDefaultChords().get(i).getName()+" +"+chordControl.getTone()+")";
            else tempString=chordControl.getTransponedChords().get(i).getName()+"("+chordControl.getDefaultChords().get(i).getName()+" "+chordControl.getTone()+")";
            gc.strokeText(tempString, offsetX + (grifSize / 2), offsetY - 40);
            offsetX += grifSize + 2*stringOffset;
        }
        return temp;
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            trasponerScene=this.initTransponerScene();
            tuningForkScene=this.initTuningForkScene();
            metronomeScene=this.initMetronomeScene();

            mainStage=primaryStage;
            mainStage.setTitle("ChordsHelper");//заголовок окна

            mainStage.setWidth(1000);
            mainStage.setHeight(700);
            mainStage.setResizable(false);

            mainStage.getIcons().add(new Image(new File("Resources/Images/icon.png").toURI().toString()));
            mainStage.setScene(trasponerScene);
            mainStage.show();

        }

        catch (Exception e) {
            Alert al=new Alert(Alert.AlertType.ERROR);
            al.setContentText(e.getMessage());
            al.showAndWait();
            return;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
