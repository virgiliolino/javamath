package ch.math.spatial.shapes;

import java.awt.geom.Ellipse2D;

public class Ellipse extends Ellipse2D.Double
implements SpatialShape {
    public Ellipse() {
        super();
    }

    public Ellipse(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return " Ellipse at " + displayShape();
    }

    private String displayShape() {
        return String.format(
                " (%d,%d), delta_x=(%d), delta_y=(%d).", getBounds().x, getBounds().y,
                getBounds().width, getBounds().height
        );
    }
}
