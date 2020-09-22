package ch.math.spatial.sanitizer;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("InputSanitizer")
public class RectangleInputSanitizer implements Sanitizer<ch.math.spatial.shapes.Rectangle> {

    private List<ch.math.spatial.shapes.Rectangle> itShouldContainNotMoreThan10Rectangles(List<ch.math.spatial.shapes.Rectangle> elem) {
        return elem.stream()
            .filter(shape -> shape instanceof Rectangle)
            .limit(10).collect(Collectors.toList());
    }

    @Override
    public List<ch.math.spatial.shapes.Rectangle> sanitize(List<ch.math.spatial.shapes.Rectangle> elem) {
        return itShouldContainNotMoreThan10Rectangles(elem);
    }
}
