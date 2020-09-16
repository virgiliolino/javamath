package ch.math.spatial.view;

import ch.math.spatial.intersection.Intersection;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

//@todo: concrete implementation, and it relies by default on system.out
@Service
public class OutputHandler {
    public void error(String error) {
        System.out.println(error);
        System.exit(1);
    }
    public void displayInput(List<Shape> shapes) {
        System.out.println("Input:");
        int index = 1;
        for (Shape shape : shapes) {
            System.out.println(
                "    " + index + ": " + shape.getClass().getSimpleName() + displayShape(shape)
            );
            index++;
        }
    }

    public void displayOutput(List<Intersection> intersections) {
        System.out.println("\nIntersections:");
        int index = 1;
        for (Intersection intersection : intersections) {
            System.out.println(
                "    " + index + ": Intersection between shape " +
                getIndexes(intersection.getShapeKeys()) + " at" +
                displayShape(intersection.getCommonArea())
            );
            index++;
        }
    }

    private String displayShape(Shape shape) {
        Rectangle bounds = shape.getBounds();
        return String.format(
            " (%d,%d), delta_x=(%d), delta_y=(%d).", bounds.x, bounds.y,
            bounds.width, bounds.height
        );
    }

    private String getIndexes(List<Integer> indexes) {
        return indexes.stream().map(String::valueOf).collect(Collectors.joining(" and "));
    }
}
