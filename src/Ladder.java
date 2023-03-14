import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Translate;

public class Ladder extends Group {
    protected Group body;
    World world;
    Scene scene;
    PointLight pointlight;
    Translate pivot;
    
    public Ladder(SubScene subScene, World world) {
        super();
        scene = subScene.getScene();
        this.world = world;

        URL url = getClass().getResource("Ladder/tinker.obj");
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);
        int count = 0;
        for (MeshView view : importer.getImport()) {
            this.getChildren().add(view);
            count++;
        }
        body = this;
        // body = loadModel(getClass().getResource("TreasureBox/tinker.obj"));

        pointlight = new PointLight(Color.YELLOW);
        pointlight.setMaxRange(30);
        body.getChildren().add(pointlight);
        pivot = new Translate();
        pivot.setY(-30);
        body.getTransforms().addAll(
                pivot);
        // pointlight.getTransforms().addAll(
        //         pivot);
        setIniPos();

        

        Group g = (Group) subScene.getRoot();
        g.getChildren().addAll(this);

    }

    public void setIniPos() {
        int row, col;
        do {
            row = (int) (Math.random() * world.ROW);
            col = (int) (Math.random() * world.COL);
        } while (!world.isUnBlocked(row, col));
        double x = world.bound.getMinX() + col * world.len;
        double z = world.bound.getMinZ() + row * world.len;
        pivot.setX(x);
        pivot.setZ(z);
    }

    public Group loadModel(URL url) {
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            this.getChildren().add(view);
        }
        importer.close();
        return (Group) this;
    }

    public Bounds getBound() {
        return body.getBoundsInParent();
    }
}
