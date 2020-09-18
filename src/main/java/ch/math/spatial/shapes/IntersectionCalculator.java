package ch.math.spatial.shapes;

import java.awt.*;
import java.util.List;

/**
 * Service that generate the interactions between shapes.
 */
public interface IntersectionCalculator {
    List<Intersection> getIntersections(List<Shape> shapes);
}