package rassel.entity;

import javax.persistence.*;

@Entity
@Table(name = "POINTS")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id")
    private int pointID;
    private float x;
    private float y;
    @Column(name = "radius")
    private float r;

    @Column(name = "is_inside")
    private int isInside;


    public Point(float x, float y, float r, int isInside) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInside = isInside;
    }

    public Point() {
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public int getInside() {
        return isInside;
    }

    public void setInside(int inside) {
        isInside = inside;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + " | r = " + r + ", id = " + pointID + ")";
    }
}
