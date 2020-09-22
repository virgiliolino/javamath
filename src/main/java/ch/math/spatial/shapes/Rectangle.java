package ch.math.spatial.shapes;

public class Rectangle extends java.awt.Rectangle {
    public Rectangle() {
        super();
    }

    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return " Rectangle at " + displayShape();
    }

    private String displayShape() {
        return String.format(
                " (%d,%d), delta_x=(%d), delta_y=(%d).", getBounds().x, getBounds().y,
                getBounds().width, getBounds().height
        );
    }
}
