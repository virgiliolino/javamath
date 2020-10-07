package ch.math.spatial;

import ch.math.spatial.deserializer.DeserializerImpl;
import ch.math.spatial.deserializer.InputStreamFactory;
import ch.math.spatial.deserializer.handler.DeserializationProblemHandlerImpl;
import ch.math.spatial.deserializer.handler.field.RectangleFieldHandlerStrategy;
import ch.math.spatial.sanitizer.SpatialShapeInputSanitizer;
import ch.math.spatial.shapes.Ellipse;
import ch.math.spatial.shapes.SpatialShape;
import ch.math.spatial.shapes.operation.IntersectionCalculatorService;
import ch.math.spatial.validator.ShapeValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import picocli.CommandLine;

import java.io.*;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class IntersectionCommandTest {
    private final int CORRECT_COMMAND_EXECUTION = 0;

    private IntersectionCommand<SpatialShape> intersectionCommand;
    private CommandLine it;
    private StringWriter itsOutput;
    private StringWriter itsErrors;

    @Mock
    private InputStreamFactory inputStreamFactory;

    @Before
    public void setUp() throws FileNotFoundException {
        String defaultInput = "[\n" +
                "    {\"type\": \"rectangle\",\"x\": 100,\"y\": 100,\"width\": 250,\"height\": 80},\n" +
                "    {\"type\": \"rectangle\",\"x\": 120,\"y\": 200,\"width\": 250,\"height\": 150},\n" +
                "    {\"type\": \"rectangle\",\"x\": 140,\"y\": 160,\"width\": 250,\"height\": 100},\n" +
                "    {\"type\": \"rectangle\",\"x\": 160,\"y\": 140,\"width\": 350,\"height\": 190}\n" +
                "]";
        String defaultInputWithEllipse = "[\n" +
                "    {\"type\": \"ellipse\",\"x\": 100,\"y\": 100,\"width\": 250,\"height\": 80},\n" +
                "    {\"type\": \"rectangle\",\"x\": 120,\"y\": 200,\"width\": 250,\"height\": 150},\n" +
                "    {\"type\": \"rectangle\",\"x\": 140,\"y\": 160,\"width\": 250,\"height\": 100},\n" +
                "    {\"type\": \"rectangle\",\"x\": 160,\"y\": 140,\"width\": 350,\"height\": 190}\n" +
                "]";

        when(inputStreamFactory.fromFile("default.json")).thenReturn(new ByteArrayInputStream(defaultInput.getBytes()));
        when(inputStreamFactory.fromFile("withEllipse.json")).thenReturn(new ByteArrayInputStream(defaultInputWithEllipse.getBytes()));

        intersectionCommand = new IntersectionCommand<>(
                inputStreamFactory,
                new DeserializerImpl<>(
                        new DeserializationProblemHandlerImpl(
                                new RectangleFieldHandlerStrategy[]{ new RectangleFieldHandlerStrategy() }
                        ),
                        new TypeReference<List<SpatialShape>>() {},
                        new NamedType[] {
                                new NamedType(ch.math.spatial.shapes.Rectangle.class, "rectangle"),
                                new NamedType(Ellipse.class, "ellipse")
                        }
                ),
                new SpatialShapeInputSanitizer(),
                new ShapeValidator(),
                new IntersectionCalculatorService()
        );

        itsOutput = new StringWriter();
        itsErrors = new StringWriter();
        it = new CommandLine(intersectionCommand);
        it.setOut(new PrintWriter(itsOutput));
        it.setErr(new PrintWriter(itsErrors));
    }

    @Test
    public void itShouldRecognizeEveryValidShape() {
        String expectedOutput = "Input:\n" +
                "    1:  Ellipse at  (100,100), delta_x=(250), delta_y=(80).\n" +
                "    2:  Rectangle at  (120,200), delta_x=(250), delta_y=(150).\n" +
                "    3:  Rectangle at  (140,160), delta_x=(250), delta_y=(100).\n" +
                "    4:  Rectangle at  (160,140), delta_x=(350), delta_y=(190).\n" +
                "\n" +
                "Intersections:\n" +
                "    1:  Intersection between shape 3 and 1 at  (140,160), delta_x=(210), delta_y=(20).\n" +
                "    2:  Intersection between shape 3 and 2 at  (140,200), delta_x=(230), delta_y=(60).\n" +
                "    3:  Intersection between shape 4 and 1 at  (160,140), delta_x=(190), delta_y=(40).\n" +
                "    4:  Intersection between shape 4 and 2 at  (160,200), delta_x=(210), delta_y=(130).\n" +
                "    5:  Intersection between shape 4 and 3 at  (160,160), delta_x=(230), delta_y=(100).\n" +
                "    6:  Intersection between shape 4 and 3 and 1 at  (160,160), delta_x=(190), delta_y=(20).\n" +
                "    7:  Intersection between shape 4 and 3 and 2 at  (160,200), delta_x=(210), delta_y=(60).";
        int exitCode = it.execute("withEllipse.json");
        Assert.assertEquals(CORRECT_COMMAND_EXECUTION, exitCode);
        Assert.assertEquals(itsOutput.toString().trim(), expectedOutput.trim());
    }

    @Test
    public void itShouldRecognizeShapesInDefaultInput() {
        String expectedOutput = "Input:\n" +
                "    1:  Rectangle at  (100,100), delta_x=(250), delta_y=(80).\n" +
                "    2:  Rectangle at  (120,200), delta_x=(250), delta_y=(150).\n" +
                "    3:  Rectangle at  (140,160), delta_x=(250), delta_y=(100).\n" +
                "    4:  Rectangle at  (160,140), delta_x=(350), delta_y=(190).\n" +
                "\n" +
                "Intersections:\n" +
                "    1:  Intersection between shape 3 and 1 at  (140,160), delta_x=(210), delta_y=(20).\n" +
                "    2:  Intersection between shape 3 and 2 at  (140,200), delta_x=(230), delta_y=(60).\n" +
                "    3:  Intersection between shape 4 and 1 at  (160,140), delta_x=(190), delta_y=(40).\n" +
                "    4:  Intersection between shape 4 and 2 at  (160,200), delta_x=(210), delta_y=(130).\n" +
                "    5:  Intersection between shape 4 and 3 at  (160,160), delta_x=(230), delta_y=(100).\n" +
                "    6:  Intersection between shape 4 and 3 and 1 at  (160,160), delta_x=(190), delta_y=(20).\n" +
                "    7:  Intersection between shape 4 and 3 and 2 at  (160,200), delta_x=(210), delta_y=(60).";
        int exitCode = it.execute("default.json");
        Assert.assertEquals(CORRECT_COMMAND_EXECUTION, exitCode);
        Assert.assertEquals(itsOutput.toString().trim(), expectedOutput.trim());
    }

    @Test
    public void IntersectionCommandTest_itShouldExpectOneParameterOrGiveFeedback() {
        int exitCode = it.execute();
        String expectedMessage = "Missing required parameter: '<filename>'";
        Assert.assertNotEquals(CORRECT_COMMAND_EXECUTION, exitCode);
        Assert.assertTrue(itsErrors.toString().contains(expectedMessage));
    }
}
