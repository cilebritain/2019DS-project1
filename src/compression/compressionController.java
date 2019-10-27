package compression;
import algorithm.huffman;
import algorithm.dir_compress;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class compressionController implements Initializable {
    public JFXTextArea filepath;
    public JFXTextArea topath;
    public StackPane stackPane;
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

    public void choosezipfile(ActionEvent event) {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogTitle("选取你要压缩的文件\\文件夹");
        int return_Value = chooser.showOpenDialog(null);
        File dir = null;
        if(return_Value == JFileChooser.APPROVE_OPTION){
            dir = chooser.getSelectedFile();
        }
        if(dir!=null)filepath.setText(dir.getAbsolutePath());
    }

    public void choosetofile(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择你要压缩到的路径");
        File dir = null;
        dir = chooser.showDialog(anchor.getScene().getWindow());
        if(dir!=null)topath.setText(dir.getAbsolutePath());
    }

    public void compress(ActionEvent event) {
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
            File test = new File(input);
            if(test.isDirectory()){
                String[] p = input.split("\\\\");
                String name = p[p.length-1];
                String zipname = output + name + ".zip";
                dir_compress.work(input, zipname);
            }else {
                String[] p = input.split("\\\\");
                String name = p[p.length-1];
                String zipname = output + name + ".zip";
                huffman.work(input, zipname);
            }
        }
    }
}
