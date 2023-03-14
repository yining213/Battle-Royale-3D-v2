import javafx.scene.shape.Shape3D;

abstract public class Weapon {
    private int status;
    private int CDTIme;
    private int damageP;
    private Shape3D shape;

    void CoolDown(){
        System.out.println("Cool down");
    }
    abstract void attack();
    
}
