package rassel.entity;

import javax.persistence.*;

@Entity
@Table(name = "POINTS")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id")
    private int pointID;
    private double x;
    private double y;
    @Column(name = "radius")
    private double r;

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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
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
