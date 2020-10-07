package ch.math.spatial.sanitizer;

import java.util.List;
import java.util.stream.Collectors;

import ch.math.spatial.shapes.SpatialShape;
import org.springframework.stereotype.Component;

@Component("InputSanitizer")
public class SpatialShapeInputSanitizer implements Sanitizer<ch.math.spatial.shapes.SpatialShape> {

    private List<ch.math.spatial.shapes.SpatialShape> itShouldContainNotMoreThan10Rectangles(List<ch.math.spatial.shapes.SpatialShape> elem) {
        return elem.stream()
            .filter(shape -> shape instanceof SpatialShape)
            .limit(10).collect(Collectors.toList());
    }

    @Override
    public List<ch.math.spatial.shapes.SpatialShape> sanitize(List<ch.math.spatial.shapes.SpatialShape> elem) {
        return itShouldContainNotMoreThan10Rectangles(elem);
    }
}
