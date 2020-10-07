package ch.math.spatial.shapes.operation.quadtree;

public class Bound<T> {
    private double x;
    private double y;
    private double w;
    private double h;
    private Bound<T> opt_parent;
    private T content;
    private NodeType nodetype = NodeType.EMPTY;
    private Bound<T> nw;
    private Bound<T> ne;
    private Bound<T> sw;
    private Bound<T> se;

    /**
     * Constructs a new quad tree node.
     *
     * @param {double} x X-coordiate of node.
     * @param {double} y Y-coordinate of node.
     * @param {double} w Width of node.
     * @param {double} h Height of node.
     * @param {Node}   opt_parent Optional parent node.
     * @constructor
     */
    public Bound(double x, double y, double w, double h, Bound<T> opt_parent, T content) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.content = content;
        this.opt_parent = opt_parent;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Bound<T> getParent() {
        return opt_parent;
    }

    public void setParent(Bound<T> opt_parent) {
        this.opt_parent = opt_parent;
    }

    public void setContent(T content) {
        this.content = this.content;
    }

    public T getContent() {
        return this.content;
    }

    public void setNodeType(NodeType nodetype) {
        this.nodetype = nodetype;
    }

    public NodeType getNodeType() {
        return this.nodetype;
    }


    public void setNw(Bound<T> nw) {
        this.nw = nw;
    }

    public void setNe(Bound<T> ne) {
        this.ne = ne;
    }

    public void setSw(Bound<T> sw) {
        this.sw = sw;
    }

    public void setSe(Bound<T> se) {
        this.se = se;
    }

    public Bound<T> getNe() {
        return ne;
    }

    public Bound<T> getNw() {
        return nw;
    }

    public Bound<T> getSw() {
        return sw;
    }

    public Bound<T> getSe() {
        return se;
    }
}
