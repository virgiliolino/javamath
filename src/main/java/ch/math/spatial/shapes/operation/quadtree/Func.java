package ch.math.spatial.shapes.operation.quadtree;

public interface Func<T> {
    public void call(QuadTree<T> quadTree, Bound<T> node);
}
