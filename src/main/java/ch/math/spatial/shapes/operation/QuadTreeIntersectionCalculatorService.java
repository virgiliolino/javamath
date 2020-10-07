package ch.math.spatial.shapes.operation;

import ch.math.spatial.shapes.SpatialShape;
import ch.math.spatial.shapes.operation.quadtree.Bound;
import ch.math.spatial.shapes.operation.quadtree.QuadTree;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class QuadTreeIntersectionCalculatorService implements IntersectionCalculator<SpatialShape> {

    @Override
    public List<Intersection<SpatialShape>> getIntersections(List<SpatialShape> shapes) {
        List<Intersection<SpatialShape>> intersections = new ArrayList<>();
        //QuadTree<SpatialShape> quadTree = new QuadTree<SpatialShape>(new Rectangle2D.Float(0, 0, 10, 10), 0);
        //populate the quadTree
        QuadTree<Integer> quadTree = new QuadTree<>(0, 0, 1000, 1000, 0);
        int index = 0;
        for (ch.math.spatial.shapes.SpatialShape shape : shapes) {
            index++;
            Rectangle bounds = shape.getBounds();
            quadTree.set(bounds.x, bounds.y, bounds.width, bounds.height, index);
        }
        for (ch.math.spatial.shapes.SpatialShape shape : shapes) {
            List<Integer> shapeKeys = new ArrayList<>();
            Rectangle commonArea = new ch.math.spatial.shapes.Rectangle(
                    shape.getBounds().x, shape.getBounds().y, shape.getBounds().width ,
            shape.getBounds().height);
            Bound<Integer>[] intersectedAreas = quadTree.searchIntersect(commonArea.getX(), commonArea.getY(), commonArea.getX() + commonArea.getWidth(), commonArea.getY() + commonArea.getHeight());
            for(Bound<Integer> intersectedArea : intersectedAreas) {
                shapeKeys.add(intersectedArea.getContent());
                commonArea = commonArea.intersection(
                        new Rectangle(
                                (int)intersectedArea.getX(), (int)intersectedArea.getY(),
                                (int)intersectedArea.getX()+(int)intersectedArea.getW(),
                                (int)intersectedArea.getY()+(int)intersectedArea.getH()
                        )
                );
                intersections.add(new Intersection<SpatialShape>(shapeKeys,
                        new ch.math.spatial.shapes.Rectangle(commonArea.x, commonArea.y,
                                commonArea.x + commonArea.width, commonArea.y + commonArea.height)
                ));
            }
        }
        return intersections;
    }
}
