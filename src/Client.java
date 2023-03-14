import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Client extends Application{
    private TextField tf;
    private TextArea ta;
    // private DataInputStream input = null;
    // private DataOutputStream output = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    @Override
    public void start(Stage stage) throws Exception {
        FlowPane fp = new FlowPane();
        tf = new TextField("");
        ta = new TextArea();
        fp.getChildren().addAll(tf, ta);
        
        Scene scene = new Scene(fp, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Client");
        stage.show();

        tf.setOnAction(e->{
            try{
                double radius = Double.parseDouble(tf.getText().trim());
                output.writeDouble(radius);
                output.flush();
                double area = input.readDouble();
                ta.appendText("Radius is " + radius + "\n");
                ta.appendText("Area received: " + area + "\n");
            }catch(IOException err){
                
                System.err.println(err);
            }
        });
        try{
            Socket skt = new Socket("localhost", 8000);
            input = new DataInputStream(skt.getInputStream());
            output = new DataOutputStream(skt.getOutputStream());
        }catch(IOException err){
            System.err.println(err);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
