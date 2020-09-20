package ch.math.spatial;

import ch.math.spatial.deserializer.DeserializerImpl;
import ch.math.spatial.deserializer.handler.DeserializationProblemHandlerImpl;
import ch.math.spatial.deserializer.handler.field.RectangleFieldHandlerStrategy;
import ch.math.spatial.sanitizer.RectangleInputSanitizer;
import ch.math.spatial.shapes.operation.IntersectionCalculatorService;
import ch.math.spatial.shapes.Rectangle;
import ch.math.spatial.validator.RectangleValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.List;

@SpringBootApplication
public class IntersectionApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(IntersectionApplication.class, args);
    }

    @Override
    public void run(String[] args){
        IntersectionCommand<Rectangle> intersectionCommand = new IntersectionCommand<>(
                new DeserializerImpl<>(
                        new DeserializationProblemHandlerImpl(
                                new RectangleFieldHandlerStrategy[]{ new RectangleFieldHandlerStrategy() }
                        ),
                        new TypeReference<List<Rectangle>>() {},
                        new NamedType(Rectangle.class, "rect")
                ),
                new RectangleInputSanitizer(),
                new RectangleValidator(),
                new IntersectionCalculatorService()
        );
        System.exit(new CommandLine(intersectionCommand).execute(args));
    }
}
