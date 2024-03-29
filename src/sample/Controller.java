package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import compression.compression;
import decompression.decompression;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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

    @FXML
    StackPane stackPane;

    @FXML
    public void about(ActionEvent event){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        Text title = new Text("About:");
        title.setFont(Font.font("consolas",24));
        dialogLayout.setHeading(title);
        Text text = new Text("Developer: Yuchun Dai\n" +
                "Developing Time: 2019.10\n"+
                "Programming Language: JavaFx\n");
        text.setFont(Font.font("consolas", 20));
        dialogLayout.setBody(text);
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("OK");
        button.setFont(Font.font("consolas", 14));
        button.setPrefSize(60,45);
        button.setOnAction(event1 -> dialog.close());
        dialogLayout.setActions(button);
        dialog.show();
    }

    @FXML
    JFXButton com_button;

    @FXML
    JFXButton decom_button;

    @FXML
    public void to_decompression(ActionEvent event){
        Stage stage = (Stage)anchor.getScene().getWindow();
        decompression compression = new decompression();
        Scene scene = compression.work();
        stage.setScene(scene);
    }

    @FXML
    public void to_compression(ActionEvent event){
        Stage stage = (Stage)anchor.getScene().getWindow();
        compression compression = new compression();
        Scene scene = compression.work();
        stage.setScene(scene);
    }
}
