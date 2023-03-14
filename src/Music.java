import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    static Map<String, MediaPlayer> music = new HashMap<>();
    static Map<String, AudioClip> effect = new HashMap<>();
    public Music(){
        try{
            AudioClip eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Click.wav").toExternalForm());
            effect.put("Click", eff);
            eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Pistol.mp3").toURI().toString());
            effect.put("Gun", eff);
            eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Empty_Mag.mp3").toURI().toString());
            effect.put("Empty_Mag", eff);
            eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Hit_Enemy.mp3").toURI().toString());
            effect.put("Hit_Enemy", eff);
            eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Gun_Sup.wav").toExternalForm());
            effect.put("Gun_Sup", eff);
            // eff = new AudioClip(getClass().getResource("bgm/Bgm_War Series/Walking.mp3").toURI().toString());
            // effect.put("Walking", eff);


            MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Home.wav").toExternalForm()));
            music.put("Home", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Menu.wav").toExternalForm()));
            music.put("Menu", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Pause.wav").toExternalForm()));
            music.put("Pause", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Man_Survival.wav").toExternalForm()));
            music.put("Man_Survival", player);
            player.setVolume(0.3);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Man_Timer.wav").toExternalForm()));
            music.put("Man_Timer", player);
            player.setVolume(0.3);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Success.wav").toExternalForm()));
            music.put("Success", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Fail.wav").toExternalForm()));
            music.put("Fail", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Alert.wav").toExternalForm()));
            music.put("Alert", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/God.wav").toExternalForm()));
            music.put("God", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Walking.mp3").toURI().toString()));
            music.put("Walking", player);
            player = new MediaPlayer(new Media(getClass().getResource("bgm/Bgm_War Series/Loading.wav").toExternalForm()));
            music.put("Loading", player);
            // player.setVolume(1);

        }catch(URISyntaxException uriEx){
            System.err.println(uriEx);
        }
    }
}
