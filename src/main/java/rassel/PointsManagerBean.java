package rassel;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import rassel.entity.Point;

import java.util.ArrayList;
import java.util.List;

public class PointsManagerBean {
    private Session session;
    private Point point = new Point();
    private List<Point> points = new ArrayList<Point>();

    public void addPoint() {
        point.setInside(calculateHit(point.getX(), point.getY(), point.getR()));
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("database-datasource-orbis");
        EntityManager entityManager = factory.createEntityManager();

        System.out.println("Пытаюсь добавить точку " + point.toString());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(point);
            entityManager.getTransaction().commit();
            points.add(point);
            point = new Point();
            System.out.println("Точка " + point.toString() + " добавлена");
        } catch (Exception e) {
            System.out.println("Точка не добавлена");
            e.printStackTrace();
        } finally {
            System.out.println("Список точек: " + getPointsFromTable().toString());
            entityManager.close();
            factory.close();
        }
    }

    public List<Point> getPointsFromTable() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("database-datasource-orbis");
        EntityManager entityManager = factory.createEntityManager();
        session = entityManager.unwrap(Session.class);

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Point> cq = cb.createQuery(Point.class);
        Root<Point> rootEntry = cq.from(Point.class);
        CriteriaQuery<Point> all = cq.select(rootEntry);

        TypedQuery<Point> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

    public int calculateHit(float x, float y, float r) {
        if ((x <= 0 && x >= -r / 2 && y <= r && y >= 0) ||
                (x <= 0 && y <= 0 && x * x + y * y <= r * r / 4) ||
                (x >= 0 && y <= 0 && y >= 2 * x - r)) {
            return 1;
        }
        return 0;
    }
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}