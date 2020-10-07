package ch.math.spatial;

import ch.math.spatial.deserializer.Deserializer;
import ch.math.spatial.deserializer.InputStreamFactory;
import ch.math.spatial.prettier.ListPrettierOperation;
import ch.math.spatial.sanitizer.Sanitizer;
import ch.math.spatial.shapes.operation.IntersectionCalculator;
import ch.math.spatial.validator.Validator;
import com.fasterxml.jackson.core.JsonParseException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import ch.math.spatial.shapes.operation.Intersection;

@Command(name = "intersections", mixinStandardHelpOptions = true, version = "version 0.1",
        description = "Finds and displays to CLI the intersections between Rectangles.")
class IntersectionCommand<T extends Shape> implements Callable<Integer> {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    
    @Parameters(index = "0", description = "The json")
    private String filename;

    private InputStreamFactory inputStreamFactory;
    private final Deserializer<T> deserializer;
    private final Sanitizer<T> sanitizer;
    private final Validator<T> shapesValidator;
    private final IntersectionCalculator<T> intersectionCalculator;
    private static final String OUTPUT_FORMAT = "    %d: %s";

    IntersectionCommand(
            InputStreamFactory inputStreamFactory,
            Deserializer<T> deserializer,
            Sanitizer<T> inputSanitizer,
            Validator<T> inputValidator,
            IntersectionCalculator<T> intersectionCalculator
    ) {
        this.inputStreamFactory = inputStreamFactory;
        this.deserializer = deserializer;
        this.sanitizer = inputSanitizer;
        this.shapesValidator = inputValidator;
        this.intersectionCalculator = intersectionCalculator;
    }

    @Override
    public Integer call() throws Exception {
        try {
            InputStream input = inputStreamFactory.fromFile(filename);
            List<T> shapes = this.deserializer.deserialize(input);
            List<T> validShapes = this.sanitizer.sanitize(shapes);
            for(T shape: validShapes) {
                this.shapesValidator.validate(shape);
            }

            spec.commandLine().getOut().println("Input:");
            ListPrettierOperation<T> inputPrettier = new ListPrettierOperation<>(
                OUTPUT_FORMAT, validShapes
            );
            inputPrettier.call().forEach(spec.commandLine().getOut()::println);

            spec.commandLine().getOut().println("\nIntersections:");
            List<Intersection<T>> intersections = this.intersectionCalculator.getIntersections(validShapes);
            ListPrettierOperation<Intersection<T>> outputPrettier = new ListPrettierOperation<>(
                OUTPUT_FORMAT, intersections
            );
            outputPrettier.call().forEach( spec.commandLine().getOut()::println);
            return 0;

        } catch (java.io.FileNotFoundException fileNotFoundException) {
             spec.commandLine().getOut().println(
                    "Indicated filename was not found." +
                            "Please ensure its correctly written and in the specified path"
            );
        } catch (JsonParseException jsonParseException) {
             spec.commandLine().getOut().println("The provided file is not correct");
        }

        return 1;
    }
}