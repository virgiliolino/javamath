package ch.math.spatial.shapes.operation;

import java.awt.*;
import java.util.List;

/**
 * Service that generate the interactions between shapes.
 */
public interface IntersectionCalculator<T extends Shape> {
    List<Intersection<T>> getIntersections(List<T> shapes);
}