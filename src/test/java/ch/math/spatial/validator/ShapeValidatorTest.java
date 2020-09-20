package ch.math.spatial.validator;

import ch.math.spatial.shapes.Rectangle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ShapeValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private RectangleValidator shapesValidator;

    @Test
    public void shapesValidatorTest_ShapeHeightMustBePositive() throws InvalidStructureException {
        thrown.expect(InvalidStructureException.class);

        ch.math.spatial.shapes.Rectangle rectangle = new Rectangle(100, 100, -250, 80);
        java.util.List<Rectangle> list = Arrays.asList(rectangle);
        for(Rectangle shape: list) {
            this.shapesValidator.validate(shape);
        }
    }

    @Test
    public void shapesValidatorTest_ShapeWidthMustBePositive() throws InvalidStructureException {
        thrown.expect(InvalidStructureException.class);

        Rectangle rectangle = new Rectangle(100, 100, -250, 80);
        java.util.List<Rectangle> list = Arrays.asList(rectangle);
        for(Rectangle shape: list) {
            this.shapesValidator.validate(shape);
        }
    }

    @Test
    public void shapesValidatorTest_ValidShapesDontThrowException() throws InvalidStructureException {
        Rectangle rectangle = new Rectangle(100, 100, 250, 80);
        java.util.List<Rectangle> list = Arrays.asList(rectangle);
        for(Rectangle shape: list) {
            this.shapesValidator.validate(shape);
        }
        Assert.assertEquals(1, list.size());
    }

}

