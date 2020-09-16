package ch.math.spatial.sanitizer;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("InputSanitizer")
public class InputSanitizer implements Sanitizer<List<Shape>> {

    @Override
    public List<Shape> sanitize(List<Shape> elem) {
        return itShouldContainNotMoreThan10Rectangles(elem);
    }

    //todo: violating open close principle
    // the sanitazion rules should become strategies to be injected during construction
    private List<Shape> itShouldContainNotMoreThan10Rectangles(List<Shape> elem) {
        return elem.stream()
            .filter(shape -> shape instanceof Rectangle)
            .limit(10).collect(Collectors.toList());
    }
}
