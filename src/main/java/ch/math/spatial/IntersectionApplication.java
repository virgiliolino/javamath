package ch.math.spatial;

import ch.math.spatial.deserializer.DeserializerImpl;
import ch.math.spatial.deserializer.InputStreamFactory;
import ch.math.spatial.deserializer.handler.DeserializationProblemHandlerImpl;
import ch.math.spatial.deserializer.handler.field.RectangleFieldHandlerStrategy;
import ch.math.spatial.sanitizer.SpatialShapeInputSanitizer;
import ch.math.spatial.shapes.Ellipse;
import ch.math.spatial.shapes.SpatialShape;
import ch.math.spatial.shapes.operation.IntersectionCalculatorService;
import ch.math.spatial.shapes.Rectangle;
import ch.math.spatial.validator.ShapeValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@SpringBootApplication
public class IntersectionApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(IntersectionApplication.class, args);
    }

    @Override
    public void run(String[] args){
        IntersectionCommand<SpatialShape> intersectionCommand = new IntersectionCommand<>(
                new InputStreamFactory(),
                new DeserializerImpl<>(
                        new DeserializationProblemHandlerImpl(
                                new RectangleFieldHandlerStrategy[]{ new RectangleFieldHandlerStrategy() }
                        ),
                        new TypeReference<List<SpatialShape>>() {},
                        new NamedType[] {
                                new NamedType(Rectangle.class, "rectangle"),
                                new NamedType(Ellipse.class, "ellipse")
                        }
                ),
                new SpatialShapeInputSanitizer(),
                new ShapeValidator(),
                new IntersectionCalculatorService()
        );
        StringWriter commandOutput = new StringWriter();
        CommandLine command = new CommandLine(intersectionCommand);
        command.setOut(new PrintWriter(commandOutput));
        int exitCode = command.execute(args);
        System.out.println(commandOutput.toString());
        System.exit(exitCode);
    }
}
