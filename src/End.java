import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class End {
    @FXML Label try_lb, back_to_lb;
    @FXML Text result_lb;
    Stage stage;
    Menu menu;
    FirstScene f;
    boolean success;
    public void lbPressed(MouseEvent e){
        if(success) Camera3D.music_src.music.get("Success").stop();
        else Camera3D.music_src.music.get("Fail").stop();
        if(e.getSource() == try_lb){
            FirstScene scene = new FirstScene(stage, menu.getWorld());
            if(Home.musicOn){
                if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").play();
                else Camera3D.music_src.music.get("Man_Survival").play();
            }
        }
        else if(e.getSource() == back_to_lb){
            f.backToMenu();
            if(Home.musicOn) Camera3D.music_src.music.get("Menu").play();
        }
    }
    public void setResult(boolean success){
        this.success = success;
        if(success) result_lb.setText("Successfully\nEscaped");
        else result_lb.setText("Fail to\nEscaped");
    }
    public void setStage(Stage stage){
        this.stage = stage;
        
    }
    public void setMenu(Menu menu){
        this.menu = menu;
    }
    public void setFirstScene(FirstScene f){
        this.f = f;
    }
}
