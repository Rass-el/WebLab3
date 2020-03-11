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
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("database");
    private Session session;
    private Point point = new Point();
    private List<Point> points = new ArrayList<Point>();

    public void addPoint() {
        point.setInside(calculateHit(point.getX(), point.getY(), point.getR()));
        EntityManager entityManager = factory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(point);
            entityManager.getTransaction().commit();
            points.add(point);
            point = new Point();
        } catch (Exception e) {
            System.err.print("\nТочка не добавлена\n");
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<Point> getPointsFromTable() {
        EntityManager entityManager = factory.createEntityManager();
        try {
            session = entityManager.unwrap(Session.class);
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Point> cq = cb.createQuery(Point.class);
            Root<Point> rootEntry = cq.from(Point.class);
            CriteriaQuery<Point> all = cq.select(rootEntry);
            TypedQuery<Point> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            System.err.print("\nНе удалось получить данные из таблицы\n");
            e.printStackTrace();
        } finally {
            entityManager.close();
            session.close();
        }
        return null;
    }

    private int calculateHit(double x, double y, double r) {
        if (    (x <= 0 && x >= -r / 2 && y <= r && y >= 0)      ||
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