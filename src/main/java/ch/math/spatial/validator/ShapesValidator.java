package ch.math.spatial.validator;

import java.awt.*;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("InputValidator")
public class ShapesValidator implements Validator<List<Shape>> {

    @Override
    public void validate(List<Shape> elem) throws InvalidLengthException {
        aLengthIsAlwaysAPositiveInteger(elem);
    }

    //todo: violating open close principle
    // the validation rules should become strategies to be injected during construction
    private void aLengthIsAlwaysAPositiveInteger(List<Shape> elem) throws InvalidLengthException {
        boolean isValid = elem.stream()
            .allMatch(shape ->
                    shape.getBounds().height > 0 && shape.getBounds().width > 0);
        if (!isValid) {
            throw new InvalidLengthException();
        }
    }
}
