package ch.math.spatial.validator;

import ch.math.spatial.shapes.SpatialShape;
import ch.math.spatial.shapes.SpatialShape;
import org.springframework.stereotype.Component;

@Component("InputValidator")
public class ShapeValidator implements Validator<SpatialShape> {

    @Override
    public void validate(SpatialShape elem) throws InvalidStructureException {
        aLengthIsAlwaysAPositiveInteger(elem);
    }

    //todo: violating open close principle
    // the validation rules should become strategies to be injected during construction
    private void aLengthIsAlwaysAPositiveInteger(SpatialShape elem) throws InvalidStructureException {
        boolean isValid = elem.getBounds().height > 0 && elem.getBounds().width > 0;
        if (!isValid) {
            throw new InvalidStructureException();
        }
    }
}
