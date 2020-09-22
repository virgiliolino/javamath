package ch.math.spatial.shapes.operation;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Value Object representing an intersection between to shapes.
 */
final public class Intersection<T extends Shape> {
    private final List<Integer> shapeKeys;
    private final T commonArea;

    /**
     *
     * @param shapeKeys Keys of the Shapes that share a portion of space
     * @param commonArea Area built by the intersaction of the shapes
     */
    public Intersection(List<Integer> shapeKeys, T commonArea) {
        this.shapeKeys = shapeKeys;
        this.commonArea = commonArea;
    }

    public List<Integer> getShapeKeys() {
        return this.shapeKeys;
    }

    public T getCommonArea() {
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
