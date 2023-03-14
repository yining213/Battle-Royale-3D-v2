import java.lang.Comparable;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Collections;
import javafx.geometry.Point3D;
import java.util.Arrays;



class Cell implements Comparable<Cell>{
    // Row and Column index of its parent
    // Note that 0 <= i <= ROW-1 & 0 <= j <= COL-1
    int row, col;
    int parent_row, parent_col;
    // f = g + h
    double f, g, h;
    World world;
    int ROW;
    int COL;
    Cell(int row, int col, World world){
        f = Double.MAX_VALUE;
        g = Double.MAX_VALUE;
        h = Double.MAX_VALUE;
        ROW =  world.ROW;
        COL =  world.COL;
        parent_row= -1;
        parent_col = -1;
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(Object me) {
        if(me == this)
            return true;
        if(!(me instanceof Cell))
            return false;
        Cell c = (Cell)me;
        return this.f == c.f;
    }
    @Override
    public int compareTo(Cell n) {
        return Double.compare(this.f, n.f);
    }
    double calculateHValue(Cell dest)
    {
        // // Return using the distance formula
        // return ((double)Math.sqrt(
        //     (row - dest.x) * (row - dest.x)
        //     + (col - dest.y) * (col - dest.y)));
        double smaller, bigger;
        if(Math.abs(row - dest.row) < Math.abs(col - dest.col)){
            smaller = Math.abs(row - dest.row);
            bigger = Math.abs(col - dest.col);
        }else{
            smaller = Math.abs(col - dest.col);
            bigger = Math.abs(row - dest.row);
        }
        return Math.sqrt(2)*smaller+(bigger-smaller);
    }
    class Pair {
        int row, col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    
    
}