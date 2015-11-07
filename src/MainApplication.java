/**
 * Created by ƒмитрий on 29.10.2015.
 */

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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.sql.SQLException;
import java.util.IllegalFormatException;
import java.util.Vector;


public class MainApplication extends Application {
    private Stage mainStage;
    private Scene trasponerScene;
    private Scene tuningForkScene;
    private Scene metronomeScene;
    private TuningFork tuningFork;
    private Metronome metronome;
    private String MUSIC_PATH="Resources/Metronome/";
    private Database db;
    private ChordControl chordControl;

    public Stage getStage()//возвращаем ссылку на главную область
    {return mainStage;
    }

    public void setActiveTrasponerScene()//переходим на сцену транспонировани€ аккордов
    {mainStage.setScene(trasponerScene);
    }

    public void setActiveTuningForkScene()//переходим на сцену камертона
    {mainStage.setScene(tuningForkScene);
    }

    public void setActiveMetronomeScene()//переходим на сцену метронома
    {mainStage.setScene(metronomeScene);
    }

    private Scene initTransponerScene()
    {   Button increaseTone,decreaseTone,search;
        TextField typeChords;
        VBox vertical,chordsVbox;
        Label defaultChords,transposedChords,enterChords;
        Label transposeModeLabel,metronomeModeLabel,tuningForkModeLabel;
        Tab transposeMode,metronomeMode,tuningForkMode;
        TabPane tabs;
        Canvas transponedChordsView,defaultChordsView;

        try {
            db=new Database("Resources/Database/Chords.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        chordControl=new ChordControl();

        typeChords=new TextField();
        typeChords.setAlignment(Pos.TOP_LEFT);
        typeChords.setPrefSize(250, 300);
        typeChords.setLayoutX(200);
        typeChords.setLayoutY(90);
        typeChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        search=new Button("Search chords");
        search.setPrefSize(180, 40);
        search.setLayoutX(240);
        search.setLayoutY(420);
        search.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    chordControl.setDefaultChords(parseEnteredNames(typeChords.getText()));
                    db.getChords(chordControl.getDefaultChords());
                    int c=0;
                    c++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });



        increaseTone=new Button("Tone +1");
        increaseTone.setPrefSize(180, 40);
        increaseTone.setLayoutX(240);
        increaseTone.setLayoutY(500);
        increaseTone.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));


        decreaseTone=new Button("Tone -1");
        decreaseTone.setPrefSize(180, 40);
        decreaseTone.setLayoutX(240);
        decreaseTone.setLayoutY(560);
        decreaseTone.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        defaultChords=new Label("Default chords:");
        defaultChords.setPrefSize(200, 50);
        defaultChords.setLayoutX(600);
        defaultChords.setLayoutY(50);
        defaultChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 18));

        defaultChordsView=new Canvas(500,300);

        transposedChords=new Label("Transposed chords:");
        transposedChords.setPrefSize(200, 50);
        transposedChords.setLayoutX(600);
        transposedChords.setLayoutY(400);
        transposedChords.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 18));

        transponedChordsView=new Canvas(500,300);

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
        vertical.setLayoutY(40);
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
        chordsVbox.setLayoutX(500);
        chordsVbox.setLayoutY(400);
        chordsVbox.getChildren().add(defaultChords);
        chordsVbox.getChildren().add(transposedChords);
        chordsVbox.getChildren().add(defaultChordsView);
        chordsVbox.getChildren().add(transponedChordsView);

        AnchorPane pane=new AnchorPane();
        pane.setPrefSize(1000, 700);

        pane.getChildren().add(tabs);
        pane.getChildren().add(vertical);
        pane.getChildren().add(chordsVbox);

        return new Scene(pane);
    }

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
        description.setText("Instruction for usage :\n"+
        "1) Hold the first string at the fifth fret and make it tune with the tuning fork sound\n"+
        "2) Hold the second string at the fifth fret and make it tune with the open first.\n"+
        "3) Hold the third string at the fourth fret and make it tune with the open second.\n"+
        "4) Hold the fourth string at the fifth fret and make it tune with the open third.\n"+
        "5) Hold the fifth string at the fifth fret and make it tune with the open fourth.\n"+
        "6) Hold the sixth string at the fifth fret and make it tune with the open fifth.");
        description.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        enableDescription=new CheckBox("Show/Don't show description");
        enableDescription.setPrefSize(300, 50);
        enableDescription.setSelected(true);
        enableDescription.setAlignment(Pos.CENTER);

        vertical=new VBox();
        vertical.setAlignment(Pos.TOP_CENTER);
        vertical.setSpacing(40);
        vertical.setLayoutX(300);
        vertical.setLayoutY(50);
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
                else
                    if(!vertical.getChildren().contains(description)) {
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

    private Scene initMetronomeScene() throws NumberFormatException, InterruptedException,IllegalFormatException
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

        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (newValue.getId().equals("Tuning-fork")) {
                            setActiveTuningForkScene();
                            selectionModel.select(metronomeMode);
                            if (metronome!=null) metronome.stop();
                        }
                        if (newValue.getId().equals("Transpose")) {
                            setActiveTrasponerScene();
                            selectionModel.select(metronomeMode);
                            if (metronome!=null) metronome.stop();
                        }

                    }
                }
        );

        seeSpeed=new Label();
        seeSpeed.setText("Current tick/min : " + 1);
        seeSpeed.setPrefSize(200, 50);
        seeSpeed.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 16));

        speed = new Slider(1, 300, 1);
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
        File[] musicFiles=soundsDirectory.listFiles();
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
                    play.setText("Stop");
                    speed.setDisable(true);
                } else {
                    metronome.stop();
                    play.setText("Play");
                    speed.setDisable(false);
                }
            }
        });


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

    public Vector<Chord> parseEnteredNames(String names) {
        Vector<Chord> chords = new Vector<>();
        String[] array= names.split("\\s+");
        String tt=new String();
        for(int i=0;i<array.length;i++)
        { if(array[i].length()>1) {
                if (array[i].charAt(1) == '#') {
                    tt += array[i].charAt(0);
                    tt += array[i].charAt(1);
                }
                else
                    tt += array[i].charAt(0);
                }
          else tt += array[i].charAt(0);
          chords.add(new Chord(array[i],tt));
          tt="";
        }
        return chords;
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

            mainStage.setScene(trasponerScene);
            mainStage.show();

        }
        catch(Exception e)
        {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
