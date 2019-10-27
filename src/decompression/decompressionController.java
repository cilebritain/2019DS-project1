package decompression;
import algorithm.decompress;
import algorithm.dir_decompress;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class decompressionController implements Initializable {
    public JFXTextArea topath;
    public JFXTextArea filepath;
    public StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), anchor);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1);
        fadeTransition.setDuration(Duration.seconds(0.3));
        fadeTransition.play();
    }
    @FXML
    AnchorPane anchor;

    public void message(ActionEvent event) {
        Stage stage = (Stage)anchor.getScene().getWindow();
        Main main = new Main();
        Scene scene = main.work();
        stage.setScene(scene);
    }

    private File dir;

    public void unzipfile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择你要解压的文件");
        dir = chooser.showOpenDialog(anchor.getScene().getWindow());
        if(dir!=null)filepath.setText(dir.getAbsolutePath());
    }

    public void unzippath(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择你要解压到的路径");
        dir = chooser.showDialog(anchor.getScene().getWindow());
        if(dir!=null)topath.setText(dir.getAbsolutePath());
    }

    public void decompress(ActionEvent event) {
        String input = filepath.getText();
        String output = topath.getText();
        if(input==null || input.equals("") || output==null || output.equals("")){
            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            Text text = new Text("Please select proper file and directory!");
            text.setFont(Font.font("consolas", 20));
            dialogLayout.setBody(text);
            JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
            JFXButton button = new JFXButton("OK");
            button.setFont(Font.font("consolas", 14));
            button.setPrefSize(60,45);
            button.setOnAction(event1 -> dialog.close());
            dialogLayout.setActions(button);
            dialog.show();
        }else {
            output += "\\";
            String[] p = input.split("\\\\");
            String name = p[p.length-1];
            if(name.indexOf(".") == name.lastIndexOf(".")){
                dir_decompress.work(input, output);
            }else {
                String yname = name.substring(0, name.lastIndexOf("."));
                decompress.work(input, output+yname);
            }
        }
    }
}
