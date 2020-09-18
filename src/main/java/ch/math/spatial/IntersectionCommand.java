package ch.math.spatial;

import ch.math.spatial.deserializer.Deserializer;
import ch.math.spatial.sanitizer.Sanitizer;
import ch.math.spatial.shapes.IntersectionCalculator;
import ch.math.spatial.validator.Validator;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import ch.math.spatial.shapes.Intersection;
import ch.math.spatial.validator.InvalidLengthException;

@Component
@Command(name = "intersections", mixinStandardHelpOptions = true, version = "version 0.1",
        description = "Print the intersections between Rectangles.")
class IntersectionCommand implements Callable<Integer> {
    @Parameters(index = "0", description = "The json")
    private File filename;

    private final Deserializer<List<Shape>> deserializer;
    private final Sanitizer<List<Shape>> sanitizer;
    private final Validator<List<Shape>> shapesValidator;
    private final IntersectionCalculator intersectionCalculator;
    private static final String OUTPUT_FORMAT = "    %d: %s \n";

    IntersectionCommand(
            Deserializer<java.util.List<Shape>> deserializer,
            Sanitizer<java.util.List<Shape>> inputSanitizer,
            Validator<List<Shape>> shapesValidator,
            IntersectionCalculator intersectionCalculator
    ) {
        this.deserializer = deserializer;
        this.sanitizer = inputSanitizer;
        this.shapesValidator = shapesValidator;
        this.intersectionCalculator = intersectionCalculator;
    }

    @Override
    public Integer call() throws Exception {
        try {
            InputStream input = new FileInputStream(this.filename);
            List<Shape> shapes = this.deserializer.deserialize(input);
            this.shapesValidator.validate(shapes);
            System.out.println("Input:");
            int index = 1;
            for (Shape shape : shapes) {
                System.out.printf(OUTPUT_FORMAT, index, shape);
                index++;
            }
            List<Shape> validShapes = this.sanitizer.sanitize(shapes);
            System.out.println("\nIntersections:");
            index = 1;
            List<Intersection> intersections = this.intersectionCalculator.getIntersections(validShapes);
            for (Intersection intersection : intersections) {
                System.out.printf(OUTPUT_FORMAT, index, intersection);
                index++;
            }
            return 0;
        } catch (java.io.FileNotFoundException fileNotFoundException) {
            System.out.println(
                    "Indicated filename was not found." +
                            "Please ensure its correctly written and in the specified path"
            );
        } catch (JsonParseException jsonParseException) {
            System.out.println("The provided file is not correct");
        } catch (InvalidLengthException e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }
}