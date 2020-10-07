package ch.math.spatial.shapes.operation;

import ch.math.spatial.shapes.Rectangle;
import ch.math.spatial.shapes.SpatialShape;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

@Service
public class IntersectionCalculatorService implements IntersectionCalculator<SpatialShape> {

    @Override
    public List<Intersection<ch.math.spatial.shapes.SpatialShape>> getIntersections(List<ch.math.spatial.shapes.SpatialShape> shapes) {
        List<Intersection<ch.math.spatial.shapes.SpatialShape>> intersections = new ArrayList<>();
        int index = 0;
        for (ch.math.spatial.shapes.SpatialShape shape : shapes) {
            index++;
            intersections = this.appendNestedIntersections(
                shapes, intersections,
                new Intersection<>(
                    Collections.singletonList(index), shape
                )
            );
        }
        return intersections;
    }

   private List<Intersection<ch.math.spatial.shapes.SpatialShape>> appendNestedIntersections(
        List<ch.math.spatial.shapes.SpatialShape> shapes,
        List<Intersection<ch.math.spatial.shapes.SpatialShape>> intersections,
        Intersection<ch.math.spatial.shapes.SpatialShape> lastIntersection
   ) {
       int index = 1;
       for (ch.math.spatial.shapes.SpatialShape shape : shapes) {
           boolean alreadyEvaluated = lastIntersection.getShapeKeys().contains(index);
           if (alreadyEvaluated) {
               continue;
           }
           if (shape.intersects(lastIntersection.getCommonArea().getBounds())) {
               List<Integer> newKeys = new ArrayList<>(lastIntersection.getShapeKeys());
               newKeys.add(index);
               java.awt.Rectangle bounds = shape.getBounds().intersection(lastIntersection.getCommonArea().getBounds());
               Intersection<ch.math.spatial.shapes.SpatialShape> newIntersection = new Intersection<>(
                       newKeys,
                       new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height)
               );
               intersections.add(newIntersection);
               intersections = this.appendNestedIntersections(shapes, intersections, newIntersection);
           }
           index++;
       }
       return intersections;
   }
}
