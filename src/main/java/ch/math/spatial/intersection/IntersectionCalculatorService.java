package ch.math.spatial.intersection;

import org.springframework.stereotype.Service;
import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class IntersectionCalculatorService implements IntersectionCalculator {

    public List<Intersection> getIntersections(List<Shape> shapes) {
        List<Intersection> intersections = new ArrayList<>();
        int indexA = 0;
        for (Shape shapeA : shapes) {
            indexA++;
            intersections = this.appendNestedIntersections(
                shapes, intersections,
                new Intersection(
                    Collections.singletonList(indexA), shapeA
                )
            );
        }
        return intersections;
    }

   private List<Intersection> appendNestedIntersections(
        List<Shape> shapes,
        List<Intersection> intersections,
        Intersection lastIntersection
   ) {
       int index = 1;
       for (Shape shape : shapes) {
           boolean alreadyEvaluated = lastIntersection.getShapeKeys().contains(index);
           if (alreadyEvaluated) {
               continue;
           }
           if (shape.intersects(lastIntersection.getCommonArea().getBounds())) {
               List<Integer> newKeys = new ArrayList<>(lastIntersection.getShapeKeys());
               newKeys.add(index);
               Intersection newIntersection = new Intersection(
                   newKeys,
                   shape.getBounds().intersection(lastIntersection.getCommonArea().getBounds())
               );
               intersections.add(newIntersection);
               intersections = this.appendNestedIntersections(shapes, intersections, newIntersection);
           }
           index++;
       }
       return intersections;
   }
}
