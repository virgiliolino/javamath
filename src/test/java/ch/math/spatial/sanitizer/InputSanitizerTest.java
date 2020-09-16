package ch.math.spatial.sanitizer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class InputSanitizerTest {

    @InjectMocks
    private InputSanitizer inputSanitizer;

    private java.util.List<Shape> createXEqualShapes(Integer x) {
        java.util.List<Shape> list = new ArrayList<>();
        Shape rectangle = new Rectangle(100, 100, 250, 80);
        IntStream.range(0, x).forEach(index -> list.add(rectangle));
        return list;
    }

    @Test
    public void InputSanitizerTest_ItShouldNotProcessMoreThan10Rectangles() {
        List<Shape> list = this.createXEqualShapes(11);
        List<Shape> validShapes = inputSanitizer.sanitize(list);
        Assert.assertEquals(10, validShapes.size());
    }

}
