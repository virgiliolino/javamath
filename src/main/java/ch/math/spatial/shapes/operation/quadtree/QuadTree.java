package ch.math.spatial.shapes.operation.quadtree;

import ch.math.spatial.shapes.Rectangle;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Datastructure: A point Quad Tree for representing 2D data. Each
 * region has the same ratio as the bounds for the tree.
 * <p/>
 * The implementation currently requires pre-determined bounds for data as it
 * can not rebalance itself to that degree.
 */
public class QuadTree<T> {


    private Bound<T> root_;
    private int count_ = 0;

    /**
     * Constructs a new quad tree.
     *
     * @param {double} minX Minimum x-value that can be held in tree.
     * @param {double} minY Minimum y-value that can be held in tree.
     * @param {double} maxX Maximum x-value that can be held in tree.
     * @param {double} maxY Maximum y-value that can be held in tree.
     */
    public QuadTree(double minX, double minY, double maxX, double maxY, T content) {
        this.root_ = new Bound<T>(minX, minY, maxX - minX, maxY - minY, null, content);
    }

    /**
     * Returns a reference to the tree's root node.  Callers shouldn't modify nodes,
     * directly.  This is a convenience for visualization and debugging purposes.
     *
     * @return {Node} The root node.
     */
    public Bound<T> getRootNode() {
        return this.root_;
    }

    /**
     * Sets the value of an (x, y) point within the quad-tree.
     *
     * @param {double} x The x-coordinate.
     * @param {double} y The y-coordinate.
     * @param {T} value The value associated with the point.
     */
    public void set(double x, double y, double w, double h, T value) {

        Bound<T> root = this.root_;
        if (x < root.getX() || y < root.getY() || x > root.getX() + root.getW() || y > root.getY() + root.getH()) {
            throw new QuadTreeException("Out of bounds : (" + x + ", " + y + ")");
        }
        if (this.insert(root, new Bound<T>(x, y, w, h, root, value))) {
            this.count_++;
        }
    }

    /**
     * Gets the value of the point at (x, y) or null if the point is empty.
     *
     * @param {double} x The x-coordinate.
     * @param {double} y The y-coordinate.
     * @param {T} opt_default The default value to return if the node doesn't
     *                 exist.
     * @return {*} The value of the node, the default value if the node
     *         doesn't exist, or undefined if the node doesn't exist and no default
     *         has been provided.
     */
    public T get(double x, double y, T opt_default) {
        Bound<T> node = this.find(this.root_, x, y);
        return node != null ? node.getContent() : opt_default;
    }

    /**
     * Removes a point from (x, y) if it exists.
     *
     * @param {double} x The x-coordinate.
     * @param {double} y The y-coordinate.
     * @return {T} The value of the node that was removed, or null if the
     *         node doesn't exist.
     */
    public T remove(double x, double y) {
        Bound<T> node = this.find(this.root_, x, y);
        if (node != null) {
            T value = node.getContent();
            node.setContent(null);
            node.setNodeType(NodeType.EMPTY);
            this.balance(node);
            this.count_--;
            return value;
        } else {
            return null;
        }
    }

    /**
     * Returns true if the point at (x, y) exists in the tree.
     *
     * @param {double} x The x-coordinate.
     * @param {double} y The y-coordinate.
     * @return {boolean} Whether the tree contains a point at (x, y).
     */
    public boolean contains(double x, double y) {
        return this.get(x, y, null) != null;
    }

    /**
     * @return {boolean} Whether the tree is empty.
     */
    public boolean isEmpty() {
        return this.root_.getNodeType() == NodeType.EMPTY;
    }

    /**
     * @return {number} The number of items in the tree.
     */
    public int getCount() {
        return this.count_;
    }

    /**
     * Removes all items from the tree.
     */
    public void clear() {
        this.root_.setNw(null);
        this.root_.setNe(null);
        this.root_.setSw(null);
        this.root_.setSe(null);
        this.root_.setNodeType(NodeType.EMPTY);
        this.root_.setContent(null);
        this.count_ = 0;
    }

    /**
     * Returns an array containing the coordinates of each point stored in the tree.
     * @return {Array.<Point>} Array of coordinates.
     */
    public Bound<T>[] getKeys() {
        final List<Bound<T>> arr = new ArrayList<Bound<T>>();
        this.traverse(this.root_, new Func<T>() {
            @Override
            public void call(QuadTree<T> quadTree, Bound<T> node) {
                arr.add(node);
            }
        });
        return arr.toArray((Bound<T>[]) new Bound[arr.size()]);
    }

    /**
     * Returns a list containing all values stored within the tree.
     * @return {List<T>} The values stored within the tree.
     */
    public List<T> getValues() {
        final List<T> arr = new ArrayList<T>();
        this.traverse(this.root_, new Func<T>() {
            @Override
            public void call(QuadTree<T> quadTree, Bound<T> node) {
                arr.add(node.getContent());
            }
        });

        return arr;
    }

    public Bound<T>[] searchIntersect(final double xmin, final double ymin, final double xmax, final double ymax) {
        final List<Bound<T>> arr = new ArrayList<Bound<T>>();
        this.navigate(this.root_, new Func<T>() {
            @Override
            public void call(QuadTree<T> quadTree, Bound<T> node) {
                Bound<T> pt = node;
                Rectangle2D.Double r1 = new Rectangle2D.Double(xmin, ymin, xmax, ymax);
                Rectangle2D.Double r2 = new Rectangle2D.Double(node.getX(), node.getY(), node.getX()+node.getW(), node.getY()+node.getH());
//                if (pt.getX() < xmin || pt.getX() > xmax || pt.getY() < ymin || pt.getY() > ymax) {
                    // Definitely not within the polygon!
//                } else {
                  if (r1.intersects(r2)) {
                    arr.add(node);
                  }

            }
        }, xmin, ymin, xmax, ymax);
        return arr.toArray((Bound<T>[]) new Bound[arr.size()]);
    }

    public Bound<T>[] searchWithin(final double xmin, final double ymin, final double xmax, final double ymax) {
        final List<Bound<T>> arr = new ArrayList<Bound<T>>();
        this.navigate(this.root_, new Func<T>() {
            @Override
            public void call(QuadTree<T> quadTree, Bound<T> node) {
                Bound<T> pt = node;
                if (pt.getX() > xmin && pt.getX() < xmax && pt.getY() > ymin && pt.getY() < ymax) {
                    arr.add(node);
                }
            }
        }, xmin, ymin, xmax, ymax);
        return arr.toArray((Bound<T>[]) new Bound[arr.size()]);
    }

    public void navigate(Bound<T> node, Func<T> func, double xmin, double ymin, double xmax, double ymax) {
        switch (node.getNodeType()) {
            case LEAF:
                func.call(this, node);
                break;

            case POINTER:
                if (intersects(xmin, ymax, xmax, ymin, node.getNe()))
                    this.navigate(node.getNe(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getSe()))
                    this.navigate(node.getSe(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getSw()))
                    this.navigate(node.getSw(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getNw()))
                    this.navigate(node.getNw(), func, xmin, ymin, xmax, ymax);
                break;
        }
    }

    private boolean intersects(double left, double bottom, double right, double top, Bound<T> node) {
        return !(node.getX() > right ||
                (node.getX() + node.getW()) < left ||
                node.getY() > bottom ||
                (node.getY() + node.getH()) < top);
    }
    /**
     * Clones the quad-tree and returns the new instance.
     * @return {QuadTree} A clone of the tree.
     */
    public QuadTree<T> clone() {
        double x1 = this.root_.getX();
        double y1 = this.root_.getY();
        double x2 = x1 + this.root_.getW();
        double y2 = y1 + this.root_.getH();
        final QuadTree<T> clone = new QuadTree<T>(x1, y1, x2, y2, this.root_.getContent());
        // This is inefficient as the clone needs to recalculate the structure of the
        // tree, even though we know it already.  But this is easier and can be
        // optimized when/if needed.
        this.traverse(this.root_, new Func<T>() {
            @Override
            public void call(QuadTree<T> quadTree, Bound<T> node) {
                clone.set(node.getX(), node.getY(), node.getW(), node.getH(), node.getContent());
            }
        });


        return clone;
    }

    /**
     * Traverses the tree depth-first, with quadrants being traversed in clockwise
     * order (NE, SE, SW, NW).  The provided function will be called for each
     * leaf node that is encountered.
     * @param {QuadTree.Node} node The current node.
     * @param {function(QuadTree.Node)} fn The function to call
     *     for each leaf node. This function takes the node as an argument, and its
     *     return value is irrelevant.
     * @private
     */
    public void traverse(Bound<T> node, Func<T> func) {
        switch (node.getNodeType()) {
            case LEAF:
                func.call(this, node);
                break;

            case POINTER:
                this.traverse(node.getNe(), func);
                this.traverse(node.getSe(), func);
                this.traverse(node.getSw(), func);
                this.traverse(node.getNw(), func);
                break;
        }
    }

    /**
     * Finds a leaf node with the same (x, y) coordinates as the target point, or
     * null if no point exists.
     * @param {QuadTree.Node} node The node to search in.
     * @param {number} x The x-coordinate of the point to search for.
     * @param {number} y The y-coordinate of the point to search for.
     * @return {QuadTree.Node} The leaf node that matches the target,
     *     or null if it doesn't exist.
     * @private
     */
    public Bound<T> find(Bound<T> node, double x, double y) {
        Bound<T> resposne = null;
        switch (node.getNodeType()) {
            case EMPTY:
                break;

            case LEAF:
                resposne = node.getX() == x && node.getY() == y ? node : null;
                break;

            case POINTER:
                resposne = this.find(this.getQuadrantForPoint(node, x, y), x, y);
                break;

            default:
                throw new QuadTreeException("Invalid nodeType");
        }
        return resposne;
    }

    /**
     * Inserts a point into the tree, updating the tree's structure if necessary.
     * @param {.QuadTree.Node} parent The parent to insert the point
     *     into.
     * @param {QuadTree.Point} point The point to insert.
     * @return {boolean} True if a new node was added to the tree; False if a node
     *     already existed with the correpsonding coordinates and had its value
     *     reset.
     * @private
     */
    private boolean insert(Bound<T> parent, Bound<T> point) {
        Boolean result = false;
        switch (parent.getNodeType()) {
            case EMPTY:
                this.setPointForNode(parent, point);
                result = true;
                break;
            case LEAF:
                if (parent.getX() == point.getX() && parent.getY() == point.getY()) {
                    this.setPointForNode(parent, point);
                    result = false;
                } else {
                    this.split(parent);
                    result = this.insert(parent, point);
                }
                break;
            case POINTER:
                result = this.insert(
                        this.getQuadrantForPoint(parent, point.getX(), point.getY()), point);
                break;

            default:
                throw new QuadTreeException("Invalid nodeType in parent");
        }
        return result;
    }

    /**
     * Converts a leaf node to a pointer node and reinserts the node's point into
     * the correct child.
     * @param {QuadTree.Node} node The node to split.
     * @private
     */
    private void split(Bound<T> node) {
        Bound<T> oldPoint = node;
        node.setContent(null);

        node.setNodeType(NodeType.POINTER);

        double x = node.getX();
        double y = node.getY();
        double hw = node.getW() / 2;
        double hh = node.getH() / 2;

        node.setNw(new Bound<T>(x, y, hw, hh, node, node.getContent()));
        node.setNe(new Bound<T>(x + hw, y, hw, hh, node, node.getContent()));
        node.setSw(new Bound<T>(x, y + hh, hw, hh, node, node.getContent()));
        node.setSe(new Bound<T>(x + hw, y + hh, hw, hh, node, node.getContent()));

        this.insert(node, oldPoint);
    }

    /**
     * Attempts to balance a node. A node will need balancing if all its children
     * are empty or it contains just one leaf.
     * @param {QuadTree.Node} node The node to balance.
     * @private
     */
    private void balance(Bound<T> node) {
        switch (node.getNodeType()) {
            case EMPTY:
            case LEAF:
                if (node.getParent() != null) {
                    this.balance(node.getParent());
                }
                break;

            case POINTER: {
                Bound<T> nw = node.getNw();
                Bound<T> ne = node.getNe();
                Bound<T> sw = node.getSw();
                Bound<T> se = node.getSe();
                Bound<T> firstLeaf = null;

                // Look for the first non-empty child, if there is more than one then we
                // break as this node can't be balanced.
                if (nw.getNodeType() != NodeType.EMPTY) {
                    firstLeaf = nw;
                }
                if (ne.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = ne;
                }
                if (sw.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = sw;
                }
                if (se.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = se;
                }

                if (firstLeaf == null) {
                    // All child nodes are empty: so make this node empty.
                    node.setNodeType(NodeType.EMPTY);
                    node.setNw(null);
                    node.setNe(null);
                    node.setSw(null);
                    node.setSe(null);

                } else if (firstLeaf.getNodeType() == NodeType.POINTER) {
                    // Only child was a pointer, therefore we can't rebalance.
                    break;

                } else {
                    // Only child was a leaf: so update node's point and make it a leaf.
                    node.setNodeType(NodeType.LEAF);
                    node.setNw(null);
                    node.setNe(null);
                    node.setSw(null);
                    node.setSe(null);
                    node.setContent(firstLeaf.getContent());
                }

                // Try and balance the parent as well.
                if (node.getParent() != null) {
                    this.balance(node.getParent());
                }
            }
            break;
        }
    }

    /**
     * Returns the child quadrant within a node that contains the given (x, y)
     * coordinate.
     * @param {QuadTree.Node} parent The node.
     * @param {number} x The x-coordinate to look for.
     * @param {number} y The y-coordinate to look for.
     * @return {QuadTree.Node} The child quadrant that contains the
     *     point.
     * @private
     */
    private Bound<T> getQuadrantForPoint(Bound<T> parent, double x, double y) {
        double mx = parent.getX() + parent.getW() / 2;
        double my = parent.getY() + parent.getH() / 2;
        if (x < mx) {
            return y < my ? parent.getNw() : parent.getSw();
        } else {
            return y < my ? parent.getNe() : parent.getSe();
        }
    }

    /**
     * Sets the point for a node, as long as the node is a leaf or empty.
     * @param {QuadTree.Node} node The node to set the point for.
     * @param {QuadTree.Point} point The point to set.
     * @private
     */
    private void setPointForNode(Bound<T> node, Bound<T> point) {
        if (node.getNodeType() == NodeType.POINTER) {
            throw new QuadTreeException("Can not set point for node of type POINTER");
        }
        node.setNodeType(NodeType.LEAF);
        node.setContent(point.getContent());
    }
}
