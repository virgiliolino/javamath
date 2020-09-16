package ch.math.spatial.intersection;

import java.awt.*;
import java.util.List;

/**
 * Value Object representing an intersection between to shapes.
 */
final public class Intersection {
    private final List<Integer> shapeKeys;
    private final Shape commonArea;

    /**
     *
     * @param shapeKeys Keys of the Shapes that share a portion of space
     * @param commonArea Area built by the intersaction of the shapes
     */
    Intersection(List<Integer> shapeKeys, Shape commonArea) {
        this.shapeKeys = shapeKeys;
        this.commonArea = commonArea;
    }

    public List<Integer> getShapeKeys() {
        return this.shapeKeys;
    }

    public Shape getCommonArea() {
        return this.commonArea;
    }
}
