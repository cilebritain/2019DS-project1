package compression;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class compression {
    public Scene work(){
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("compression.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return (new Scene(root, 600, 400));
    }
}
