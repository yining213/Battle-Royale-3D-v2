import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server extends Application{
    ServerSocket ss;
    Vector<Socket> socketVect = new Vector<>();
    int clientNo = 0;
    TextArea result = new TextArea("Result:\n");
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new ScrollPane(result), 500, 500);
        stage.setScene(scene);
        stage.setTitle("MultiServer");
        stage.show();
        
        new Thread(()->{
            try{
                ss = new ServerSocket(8000);
                result.appendText("Server started at " + new Date() + "\n");
                System.out.println("construct server");
                
                while(true){
                    Socket sck = ss.accept();
                    clientNo++;
                    // socketVect.add(sck);
                    Platform.runLater(()->result.appendText("\n" + "starting thread for client " + clientNo + "\n"));
                    InetAddress addr = sck.getInetAddress();
                    result.appendText("Client " + clientNo + "'s hostname: " + addr.getHostName() + "\n");
                    result.appendText("Client " + clientNo + " address: " + addr + "\n");
                    new Thread(new HandleClient(sck)).start();
                }
            }
            catch(IOException err){
                System.err.println(err);
            }
            // try{
            //     System.out.println("clese1");
            //     ss.close();
            // }catch(IOException err){
            //     System.err.println(err);
            // }
        }).start();
        System.out.println("arrive");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                Platform.exit();
                stage.close();
                try{
                    ss.close();
                }catch(IOException err){
                    System.err.println(err);
                }
            };
        });
    }
    class HandleClient implements Runnable{
        private Socket sk;
        public HandleClient(Socket s){ sk = s;}
        
        @Override
        public void run() {
            try{
                // ObjectInputStream input = new ObjectInputStream(sk.getInputStream());
                DataInputStream input = new DataInputStream(sk.getInputStream());
                DataOutputStream output = new DataOutputStream(sk.getOutputStream());
                
                while(true){
                    // double radius = input.readDouble();
                    // double area = radius*radius*Math.PI;
                    // output.writeDouble(area);
                    // result.appendText("Area: " +  area + "\n");
                    ObjectInputStream in = new ObjectInputStream(sk.getInputStream());
                    try{
                        System.out.println("Get obj before");
                        Object obj = in.readObject();
                        System.out.println("Get obj");
                        if(obj instanceof FirstScene){
                            System.out.println("read obj");
                            FirstScene scene = (FirstScene)obj;
                            Platform.runLater(()->{
                                result.appendText("Received firstScene: " +  scene.menu.mapNo + "\n");
                            });
                        }
                        else{
                            System.out.println("waiting");
                            Platform.runLater(()->{
                                result.appendText(obj + "\n");
                            });
                        }
                        
                    }catch(ClassNotFoundException cex){
                        System.err.println(cex);
                    }
                    
                }
            }
            catch(IOException err){
                result.appendText("Closing...\n");
                System.err.println(err);
            }
            // try{
            //     result.appendText("Closing\n\n");
            //     input.close();
            //     output.close();
            //     socketVect.remove(sk);
            //     result.appendText("socketNo: " + socketVect.size());
            // }catch(IOException err){
            //     System.err.println(err);
            // }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
