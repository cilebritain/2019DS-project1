package compression;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class compressionController implements Initializable {
    public JFXTextArea filepath;
    public JFXTextArea topath;
    @FXML
    AnchorPane anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), anchor);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1);
        fadeTransition.setDuration(Duration.seconds(0.3));
        fadeTransition.play();
    }

    public void message(javafx.event.ActionEvent event) {
        Stage stage = (Stage)anchor.getScene().getWindow();
        Main main = new Main();
        Scene scene = main.work();
        stage.setScene(scene);
    }

    private File dir;

    public void choosezipfile(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择你要压缩的文件/文件夹");
        dir = chooser.showDialog(anchor.getScene().getWindow());
        if(dir!=null)filepath.setText(dir.getAbsolutePath());
    }

    public void choosetofile(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择你要压缩到的路径");
        dir = chooser.showDialog(anchor.getScene().getWindow());
        if(dir!=null)topath.setText(dir.getAbsolutePath());
    }
}
