package ch.math.spatial.sanitizer;

import ch.math.spatial.shapes.Rectangle;
import ch.math.spatial.shapes.SpatialShape;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class InputSanitizerTest {

    @InjectMocks
    private SpatialShapeInputSanitizer inputSanitizer;

    private java.util.List<SpatialShape> createXEqualShapes(Integer x) {
        java.util.List<SpatialShape> list = new ArrayList<>();
        ch.math.spatial.shapes.SpatialShape SpatialShape = new Rectangle(100, 100, 250, 80);
        IntStream.range(0, x).forEach(index -> list.add(SpatialShape));
        return list;
    }

    @Test
    public void InputSanitizerTest_ItShouldNotProcessMoreThan10SpatialShapes() {
        List<SpatialShape> list = this.createXEqualShapes(11);
        List<SpatialShape> validShapes = inputSanitizer.sanitize(list);
        Assert.assertEquals(10, validShapes.size());
    }

    @Test
    public void InputSanitizerTest_whenItProcessValidInoutTheOutputShouldNotBeTouched() {
        List<SpatialShape> list = this.createXEqualShapes(10);
        List<SpatialShape> validShapes = inputSanitizer.sanitize(list);
        Assert.assertTrue(list.equals(validShapes));
    }

}
