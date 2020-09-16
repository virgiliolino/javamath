package ch.math.spatial;

import ch.math.spatial.deserializer.Deserializer;
import ch.math.spatial.intersection.Intersection;
import ch.math.spatial.intersection.IntersectionCalculator;
import ch.math.spatial.sanitizer.Sanitizer;
import ch.math.spatial.validator.InvalidLengthException;
import ch.math.spatial.validator.Validator;
import ch.math.spatial.view.OutputHandler;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.*;
import java.util.List;

@SpringBootApplication
public class IntersectionApplication implements CommandLineRunner {
    private final Deserializer<List<Shape>> deserializer;
    private final Sanitizer<List<Shape>> sanitizer;
    private Validator<List<Shape>> shapesValidator;
    private final IntersectionCalculator intersectionCalculator;
    private final OutputHandler outputHandler;

    public static void main(String[] args) {
        SpringApplication.run(IntersectionApplication.class, args);
    }

    IntersectionApplication(
            Deserializer<List<Shape>> deserializer,
            Sanitizer<List<Shape>> inputSanitizer,
            Validator<List<Shape>> shapesValidator,
            IntersectionCalculator intersectionCalculator,
            OutputHandler outputHandler
    ) {
        this.deserializer = deserializer;
        this.sanitizer = inputSanitizer;
        this.shapesValidator = shapesValidator;
        this.intersectionCalculator = intersectionCalculator;
        this.outputHandler = outputHandler;
    }

    @Override
    public void run(String[] args) throws IOException {
        if (args.length != 1) {
            outputHandler.error("Usage: command <filename.json>");
        }

        String fileName = args[0];
        try {
            InputStream input = new FileInputStream(fileName);
            List<Shape> shapes = this.deserializer.deserialize(input);
            this.shapesValidator.validate(shapes);

            List<Shape> validShapes = this.sanitizer.sanitize(shapes);

            this.outputHandler.displayInput(validShapes);
            List<Intersection> intersections = this.intersectionCalculator.getIntersections(validShapes);
            this.outputHandler.displayOutput(intersections);

        } catch (java.io.FileNotFoundException fileNotFoundException) {
            outputHandler.error(
                "Indicated filename was not found." +
                "Please ensure its correctly written and in the specified path"
            );
        } catch (JsonParseException jsonParseException) {
            this.outputHandler.error("The provided file is not correct");
        } catch (InvalidLengthException e) {
            this.outputHandler.error(e.getMessage());
        }
    }

}
