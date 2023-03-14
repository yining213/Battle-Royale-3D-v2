import javafx.scene.Group;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.net.URL;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.collections.ObservableFloatArray;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Vector;
import javafx.scene.paint.Paint;
import javafx.geometry.Bounds;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class World extends Group {
    public MeshView[] meshViews;
    public MeshView[] meshViews2;
    TriangleMesh triMesh = new TriangleMesh();
    ObservableFaceArray faces;
    ObservableFloatArray points;
    Group walls;
    Bounds bound;
    int map[][];
    Vector<Treasure> treasures = new Vector<>();
    Ladder ladder;
    Timeline ladderGenerate;
    Timeline treasureGenerate;
    BooleanProperty ladderShow = new SimpleBooleanProperty();
    // static public MeshView[] meshViews;
    // static public MeshView[] meshViews2;
    // static TriangleMesh triMesh = new TriangleMesh();
    // static ObservableFaceArray faces;
    // static ObservableFloatArray points;
    // static Group walls;
    // static Bounds bound;
    // static int map[][];

    public World(String s) {
        super();
        ObjModelImporter importer = new ObjModelImporter();
        try {
            // File file = new File("src/Classic(3D).obj");
            URL file = getClass().getResource(s);
            // File file = new File("src/box/tinker.obj");
            importer.read(file);
        } catch (ImportException e) {
            e.printStackTrace();
            return;
        }
        meshViews = importer.getImport();
        this.getChildren().addAll(meshViews);
        // System.out.println(meshViews.length);
        try {
            // FileInputStream inputStream = new FileInputStream("images/wall.jpeg");
            // Image image = new Image(inputStream);
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseMap(new Image(new FileInputStream(new File("images/wall.jpeg"))));
            // material.setDiffuseColor(Color.CORAL);
            for (int i = 1; i < meshViews.length - 1; i++) {
                meshViews[i].setDrawMode(DrawMode.FILL);
                meshViews[i].setMaterial(material);
                // System.out.println("set material " + i);
            }
            PhongMaterial sky_material = new PhongMaterial();
            sky_material.setDiffuseMap(new Image(new FileInputStream("images/Sky1.jpg")));
            meshViews[meshViews.length - 1].setDrawMode(DrawMode.FILL);
            meshViews[meshViews.length - 1].setMaterial(sky_material);

            PhongMaterial floor_material = new PhongMaterial();
            // floor_material.setDiffuseMap(new Image(new FileInputStream(new File("images/floor.jpg"))));
            floor_material.setDiffuseColor(Color.WHITE);
            meshViews[0].setDrawMode(DrawMode.FILL);
            meshViews[0].setMaterial(floor_material);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            // System.out.println("can't find image");
        }
        // material.setDiffuseColor(Color.LIGHTCORAL);

        // loadWalls();
        // this.getChildren().add(walls);
        importer.close();

        bound = this.getBoundsInParent();
        makeMap();
        ladderShow.set(false);
        // //URL url = getClass().getResource("Copy_of_Maze/tinker.obj");
        // URL url = getClass().getResource("brickwall_Maze/tinker.obj");
        // //URL url = getClass().getResource("test.obj");
        // ObjModelImporter importer = new ObjModelImporter();
        // importer.read(url);
        // int count = 0;
        // for (MeshView view : importer.getImport()) {
        // this.getChildren().add(view);
        // count++;
        // }
        // System.out.println(count);


    }
    public void generateLadder(SubScene subscene){
        World world = this;
        ladderGenerate = new Timeline(
                new KeyFrame(Duration.seconds(10),
                    new EventHandler<ActionEvent>() {
                        @Override 
                        public void handle(ActionEvent event){
                            if(ladderShow.get() == false) ladder = new Ladder(subscene, world);
                            ladderShow.set(true);
                            // System.out.println("ladder show");
                            ladder.setIniPos();
                        }
                    }));
        ladderGenerate.setCycleCount(Timeline.INDEFINITE);
        ladderGenerate.play();

    }
    public void generateTreasure(SubScene subScene){
        treasures.add(new Treasure(subScene, this));
        Group w = this;
        // randomly generate treasure every 40 secs
        treasureGenerate = new Timeline(
            new KeyFrame(Duration.millis(40000),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e){
                        System.out.println("generate treasure");
                        treasures.add(new Treasure(subScene, (World)w));
                    }
                }    
            ));
        treasureGenerate.setCycleCount(Timeline.INDEFINITE);
        treasureGenerate.play();
    }
    public Boolean hitWall(Bounds b, Scene scene) {
        // System.out.println("check hit wall");
        for (int i = 0; i < meshViews.length - 1; i++) {

            if (b.intersects(meshViews[i].getBoundsInParent())) {
                // System.out.println("hit wall");
                return true;
            }
        }
        return false;
    }
    // public Boolean hitWall2(Group g) {
    //     // System.out.println("check hit wall");
    //     int row = (int)((g.localToParent(0,0,0).getZ()-bound.getMinZ())/len);
    //     int col = (int)((g.localToParent(0,0,0).getX()-bound.getMinX())/len);
    //     for(int i=-1;i<=1;i++){
    //         for(int j=-1;j<=1;j++){
    //             if(isValid(row+i, col+j))
    //                 if(map[row+i][col+j] == 0){
    //                     return true;
    //                 }
    //         }
    //     }
        
    //     return false;
    // }
    public Boolean hitWall3(double x, double z) {
        // System.out.println("check hit wall");
        int row = (int)((z-bound.getMinZ())/len);
        int col = (int)((x-bound.getMinX())/len);

        for(int i=0;i<=0;i++){
            for(int j=0;j<=0;j++){
                if(isValid(row+i, col+j))
                    if(map[row+i][col+j] == 0){
                        return true;
                    }
            }
        }
        return false;
    }
    public int getPosInMap_Col(double x){
        return (int)((x-bound.getMinX())/len);
    }
    public int getPosInMap_Row(double z){
        return (int)((z-bound.getMinZ())/len);
    }

    public void enableSky(Boolean b) {
        meshViews[meshViews.length - 1].setVisible(b);
    }

    int ROW;
    int COL;
    int len = 2;
    public void makeMap() {
        double WIDTH = bound.getWidth();
        double HEIGHT = bound.getDepth();
        ROW = (int) (HEIGHT / len) + 1;
        COL = (int) (WIDTH / len) + 1;
        // Point3D map[][] = new Point3D[ROW][COL];
        map = new int[ROW][COL];
        double minX = bound.getMinX();
        double minZ = bound.getMinZ();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // map[i][j] = new Point3D(minX+j*len, 10, minZ+i*len);
                Point3D p = new Point3D(minX + j * len, -10, minZ + i * len);
                // System.out.println(p);
                for (int t = 0; t < meshViews.length - 1; t++) {
                    if (meshViews[t].getBoundsInParent().contains(p)) {
                        // System.out.println("hit wall");
                        for(int m=-3;m<=3;m++){
                            for(int n=-3;n<=3;n++)
                                if(isValid(i+m, j+n))
                                    map[i+m][j+n] = 0;
                        }
                        
                        break;
                    } else
                        map[i][j] = 1;
                }
            }
        }
        // System.out.println(ROW + " " + COL);

        // for (int i = 0; i < ROW; i++) {
        //     for (int j = 0; j < COL; j++) {
        //         System.out.print(map[i][j] + " ");
        //     }
        //     System.out.println();
        // }
    }

    class Pair {
        int row, col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    boolean isValid(int row, int col) {
        // Returns true if row number and column number
        // is in range
        return (row >= 0)
                && (row < ROW)
                && (col >= 0)
                && (col < COL);
    }

    boolean isUnBlocked(int row, int col) {
        // Returns true if the cell is not blocked else false
        if (map[row][col] == 1)
            return (true);
        else
            return (false);
    }

    boolean isDestination(int row, int col, int dest_row, int dest_col) {
        if (row == dest_row && col == dest_col)
            return (true);
        else
            return (false);
    }
    Cell cellDetails[][];
    Cell AStarSearch(Point3D srcP, Point3D destP){
        int src_row = (int)((srcP.getZ()-bound.getMinZ())/len);
        int src_col = (int)((srcP.getX()-bound.getMinX())/len);

        int dest_row = (int)((destP.getZ()-bound.getMinZ())/len);
        int dest_col =  (int)((destP.getX()-bound.getMinX())/len);
           
        // for (int i = 0; i < ROW; i++) {
        //     for (int j = 0; j < COL; j++) {
        //         if(i == dest_row && j == dest_col){
        //             System.out.print("d" + " ");
                    
        //         }else {
        //             if(i == src_row && j == src_col)
        //                 System.out.print("s" + " ");
        //             else 
        //                 System.out.print(map[i][j] + " ");
        //         }
        //     }
        //     System.out.println();
        // }
        if(isValid(src_row, src_col) == false || isValid(dest_row, dest_col) == false){
            // System.out.println("source or destination is invalid");
            return null;
        }
        if(isUnBlocked(src_row, src_col) == false || isUnBlocked(dest_row, dest_col) == false){
            // System.out.println("Source or the destination is blocked\n");
            return null;
        }

        boolean closedList[][] = new boolean[ROW][COL];
        for(int i=0;i<ROW;i++)
            for(int j=0;j<COL;j++)
                closedList[i][j] = false;

        cellDetails = new Cell[ROW][COL];
        for(int i=0;i<ROW;i++)
            for(int j=0;j<COL;j++)
                cellDetails[i][j] = new Cell(i,j, this);
        Cell start = cellDetails[src_row][src_col];
        Cell target = cellDetails[dest_row][dest_col];
        start.g = 0;
        start.h = start.calculateHValue(target);
        start.f = start.g + start.calculateHValue(target);
        PriorityQueue<Cell> openList = new PriorityQueue<Cell>();
        openList.add(start);

        while(!openList.isEmpty()){
            Cell c = openList.peek();
            // System.out.println("pop "+c.row+" "+c.col);
            if(c == target){
                System.out.println("find target ");
                return c;
            }

            closedList[c.row][c.col] = true;
            
            // successor of cell c
            // N.W   N   N.E
            //    \  |   /
            //     \ |  /
            // W----Cell----E
            //      / | \
            //     /  |  \
            // S.W    S   S.E
            for(int row=c.row-1;row<=c.row+1;row++){
                for(int col=c.col-1;col<=c.col+1;col++){
                    if(row == c.row && col==c.col)
                        continue;
                    if(isValid(row, col)){
                        if(isDestination(row, col, dest_row, dest_col)){
                            cellDetails[row][col].parent_row = c.row;
                            cellDetails[row][col].parent_col = c.col;
                            // System.out.println("The destination cell is found"); 
                            return cellDetails[row][col];
                        }
                        else if (closedList[row][col] == false && isUnBlocked(row, col) == true){
                            //System.out.println("check successor"); 
                            double gNew = c.g+Math.sqrt((row-c.row)*(row-c.row)+(col-c.col)*(col-c.col));
                            double hNew = cellDetails[row][col].calculateHValue(target);
                            double fNew = gNew+hNew;
                            
                            // System.out.println("gNew:"+gNew); 
                            // System.out.println("hNew:"+hNew); 
                            // System.out.println("fNew:"+fNew); 
                            
                            
                            if(cellDetails[row][col].f > fNew){
                                cellDetails[row][col].f = fNew;
                                cellDetails[row][col].g = gNew;
                                cellDetails[row][col].h = hNew;
                                cellDetails[row][col].parent_row = c.row;
                                cellDetails[row][col].parent_col = c.col;
                                openList.add(cellDetails[row][col]);
                            }
                        }
                    }
                }
            }
            openList.remove(c);           
        }
        return null;
    }
    void printPath(Cell target){
        class Pair {
            int row, col;
    
            Pair(int row, int col) {
                this.row = row;
                this.col = col;
            }
        }
        Cell n = target;
    
        if(n==null)
            return;

        List<Pair> pathList = new ArrayList<>();
        
        while(n.parent_row != -1 && n.parent_col != -1){
            pathList.add(new Pair(n.parent_row, n.parent_col));
            n = cellDetails[n.parent_row][n.parent_col];
        }
        // pathList.add(new Pair(n.row, n.col));
        Collections.reverse(pathList);
        
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                boolean found = false;
                for(Pair p:pathList){
                    if(p.row == i && p.col == j){
                        // System.out.print("p" + " ");
                        found = true;
                        break;
                    }
                }
                if(!found){
                    // System.out.print(map[i][j] + " ");
                }
            }
            // System.out.println();
        }

        // for(Pair p : pathList){
        //     System.out.println(p.col + " " + p.row);
        // }
    }
    public Bounds getSkyBounds(){
        return meshViews[meshViews.length - 1].getBoundsInParent();
    }
    public void stopAll(){
        if(treasureGenerate!=null) treasureGenerate.stop();
        if(ladderGenerate!=null) ladderGenerate.stop();
    }
    public void pauseAll(){
        if(treasureGenerate!=null) treasureGenerate.pause();
        if(ladderGenerate!=null) ladderGenerate.pause();
    }
    public void resumeAll(){
        if(treasureGenerate!=null) treasureGenerate.play();
        if(ladderGenerate!=null) ladderGenerate.play();
    }
}
