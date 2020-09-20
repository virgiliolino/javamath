package ch.math.spatial.validator;

import ch.math.spatial.shapes.Rectangle;
import org.springframework.stereotype.Component;

@Component("InputValidator")
public class RectangleValidator implements Validator<Rectangle> {

    @Override
    public void validate(Rectangle elem) throws InvalidStructureException {
        aLengthIsAlwaysAPositiveInteger(elem);
    }

    //todo: violating open close principle
    // the validation rules should become strategies to be injected during construction
    private void aLengthIsAlwaysAPositiveInteger(Rectangle elem) throws InvalidStructureException {
        boolean isValid = elem.height > 0 && elem.width > 0;
        if (!isValid) {
            throw new InvalidStructureException();
        }
    }
}
