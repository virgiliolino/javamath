package ch.math.spatial.shapes;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return " Intersection between shape " +
            getIndexes(getShapeKeys()) + " at " +
            String.format(
                " (%d,%d), delta_x=(%d), delta_y=(%d).", commonArea.getBounds().x, commonArea.getBounds().y,
                commonArea.getBounds().width, commonArea.getBounds().height
            );
    }

    private String getIndexes(List<Integer> indexes) {
        return indexes.stream().map(String::valueOf).collect(Collectors.joining(" and "));
    }

}
