import java.net.URL;
import java.util.Vector;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.shape.MeshView;

public class Object3D {
    public enum objectEnum{
        GHOST,
        BAT
    }
    static Vector<MeshView[]> enemy = new Vector<>();
    public Object3D(){
        URL u1 = getClass().getResource("Little_Ghost/tinker.obj");
        URL u2 = getClass().getResource("bat/tinker.obj");
        loadModel(enemy, u1);
        loadModel(enemy, u2);
    }
    public void loadModel(Vector<MeshView[]> e, URL url) {
        //Group modelRoot = new Group();
        try{
            ObjModelImporter importer = new ObjModelImporter();
            importer.read(url);
            System.out.println(url);
            MeshView[] meshview = importer.getImport();
            System.out.println(meshview);
            importer.close();
            e.add(meshview);
        }catch(Exception err){
            System.out.println(err);
        }
        // System.out.println("load model end");
    }
    static public MeshView[] getModel(objectEnum m){
        MeshView[] cur = enemy.get(m.ordinal());
        return cur;
    }
}
