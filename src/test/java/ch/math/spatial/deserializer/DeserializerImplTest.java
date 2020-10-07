package ch.math.spatial.deserializer;
import ch.math.spatial.deserializer.handler.DeserializationProblemHandlerImpl;
import ch.math.spatial.deserializer.handler.field.RectangleFieldHandlerStrategy;
import ch.math.spatial.shapes.Ellipse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DeserializerImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DeserializerImpl deserializerImpl;

    @Before
    public void setUp(){
        this.deserializerImpl = new DeserializerImpl<>(
                new DeserializationProblemHandlerImpl(
                        new RectangleFieldHandlerStrategy[]{ new RectangleFieldHandlerStrategy() }
                ),
                new TypeReference<List<ch.math.spatial.shapes.SpatialShape>>() {},
                new NamedType[] {
                        new NamedType(ch.math.spatial.shapes.Rectangle.class, "rectangle"),
                        new NamedType(Ellipse.class, "ellipse")
                }
        );
    }

    @Test
    public void deserializerImplTest_whenLoadingValidStructuresItShouldReturnValidObjects() throws IOException {
        String inputString = "[\n" +
                "    {\n" +
                "      \"type\": \"rectangle\",\n" +
                "      \"x\": 100,\n" +
                "      \"y\": 100,\n" +
                "      \"width\": 250,\n" +
                "      \"height\": 80\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"rectangle\",\n" +
                "      \"x\": 120,\n" +
                "      \"y\": 200,\n" +
                "      \"width\": 250,\n" +
                "      \"height\": 150\n" +
                "    }\n" +
                "]";

        byte[] data = inputString.getBytes();
        InputStream input = new ByteArrayInputStream(data);
        List<Rectangle> output = deserializerImpl.deserialize(input);
        Assert.assertEquals(2, output.size());
        Assert.assertEquals(output.get(0), new Rectangle(100, 100, 250, 80));
        Assert.assertEquals(output.get(1), new Rectangle(120, 200, 250, 150));
    }

    @Test
    public void deserializerImplTest_itShouldRecognizeEveryValidShape() throws IOException {
        String inputString = "[\n" +
                "    {\n" +
                "      \"type\": \"ellipse\",\n" +
                "      \"x\": 100,\n" +
                "      \"y\": 100,\n" +
                "      \"width\": 250,\n" +
                "      \"height\": 80\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"rectangle\",\n" +
                "      \"x\": 120,\n" +
                "      \"y\": 200,\n" +
                "      \"width\": 250,\n" +
                "      \"height\": 150\n" +
                "    }\n" +
                "]";

        byte[] data = inputString.getBytes();
        InputStream input = new ByteArrayInputStream(data);
        List<Rectangle> output = deserializerImpl.deserialize(input);
        Assert.assertEquals(2, output.size());
        Assert.assertEquals(new Ellipse(100, 100, 250, 80), output.get(0));
        Assert.assertEquals(new Rectangle(120, 200, 250, 150), output.get(1));
    }

    @Test
    public void deserializerImplTest_whenLoadingEmptyStructureWeShouldDefaultToAShape() throws IOException {
        String inputString = "[\n" +
            "    {\n" +
            "      \"type\": \"rectangle\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"rectangle\",\n" +
            "      \"x\": 120,\n" +
            "      \"y\": 200,\n" +
            "      \"width\": 250,\n" +
            "      \"height\": 150\n" +
            "    }\n" +
            "]";

        byte[] data = inputString.getBytes();
        InputStream input = new ByteArrayInputStream(data);
        List<Rectangle> output = deserializerImpl.deserialize(input);
        Assert.assertEquals(2, output.size());
    }

    @Test
    public void deserializerImplTest_whenEvaluatingUnrecognizedShapesWeShouldStopDeserialization() throws IOException {
        thrown.expect(com.fasterxml.jackson.databind.exc.InvalidTypeIdException.class);
        String inputString = "[\n" +
                "    {\n" +
                "      \"type\": \"Circle\",\n" +
                "      \"x1\": 120,\n" +
                "      \"x2\": 200,\n" +
                "      \"y1\": 250,\n" +
                "      \"y2\": 150\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"rectangle\",\n" +
                "      \"x\": 120,\n" +
                "      \"y\": 200,\n" +
                "      \"width\": 250,\n" +
                "      \"height\": 150\n" +
                "    }\n" +
                "]";

        byte[] data = inputString.getBytes();
        InputStream input = new ByteArrayInputStream(data);
        deserializerImpl.deserialize(input);
    }

}