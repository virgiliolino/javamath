package ch.math.spatial.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ShapeValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private ShapesValidator shapesValidator;

    @Test
    public void shapesValidatorTest_ShapeHeightMustBePositive() throws InvalidLengthException {
        thrown.expect(InvalidLengthException.class);

        Shape rectangle = new Rectangle(100, 100, -250, 80);
        java.util.List<Shape> list = Arrays.asList(rectangle);
        shapesValidator.validate(list);
    }

    @Test
    public void shapesValidatorTest_ShapeWidthMustBePositive() throws InvalidLengthException {
        thrown.expect(InvalidLengthException.class);

        Shape rectangle = new Rectangle(100, 100, -250, 80);
        java.util.List<Shape> list = Arrays.asList(rectangle);
        shapesValidator.validate(list);
    }


}

