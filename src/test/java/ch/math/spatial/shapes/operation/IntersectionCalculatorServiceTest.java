package ch.math.spatial.shapes.operation;
import ch.math.spatial.shapes.Rectangle;
import ch.math.spatial.shapes.SpatialShape;
import ch.math.spatial.shapes.operation.Intersection;
import ch.math.spatial.shapes.operation.IntersectionCalculatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class IntersectionCalculatorServiceTest {

    @InjectMocks
    private IntersectionCalculatorService intersectionCalculatorService;

    /**
     * Multiple input SpatialShapes with the same coordinates and sizes are nevertheless
     * distinct. All should be included when determining intersections.
     */
    private List<SpatialShape> createXEqualShapes(Integer x) {
        List<SpatialShape> list = new ArrayList<>();
        SpatialShape SpatialShape = new Rectangle(100, 100, 250, 80);
        IntStream.range(0, x).forEach(index -> list.add(SpatialShape));
        return list;
    }

    /**
     * Multiple input SpatialShapes with the same coordinates and sizes are nevertheless
     * distinct. All should be included when determining intersections.
     */
    @Test
    public void IntersectionCalculatorServiceTest_ItShouldProcessMultipleSpatialShapesWithSameCoordinates() {
        int n = 3;
        List<SpatialShape> list = this.createXEqualShapes(n);
        List<Intersection<SpatialShape>> intersections = intersectionCalculatorService.getIntersections(list);
        int numberOfIntersections = 4;
        Assert.assertEquals(intersections.size(), numberOfIntersections);
    }

    /**
     * An intersection without an area should not be considered an intersection, i.e. an
     * intersection with weight and/or height equal to zero.
     */
    @Test
    public void IntersectionCalculatorServiceTest_whenAnIntersectionIsEmpty_thenSkip() {
        List<SpatialShape> list = new ArrayList<>();

        list.add(new Rectangle(100, 100, 250, 80));
        list.add(new Rectangle(100, 50, 250, 50));
        List<Intersection<SpatialShape>> intersections = this.intersectionCalculatorService.getIntersections(list);
        Assert.assertEquals(0, intersections.size());
    }

    @Test
    public void IntersectionCalculatorServiceTest_whenEmptyList_thenReturnEmpty() {
        List<SpatialShape> emptyList = new ArrayList<>();
        assertEquals(
                "it should be empty",
                intersectionCalculatorService.getIntersections(emptyList),
                emptyList);
    }

    @Test
    public void IntersectionCalculatorServiceTest_itShouldBeCommutative() {
        List<SpatialShape> list = new ArrayList<>();

        list.add(new Rectangle(100, 100, 250, 80));
        list.add(new Rectangle(120, 200, 250, 150));
        list.add(new Rectangle(140, 160, 250, 100));
        list.add(new Rectangle(160, 140, 350, 190));

        List<Intersection<SpatialShape>> actualReturn = intersectionCalculatorService.getIntersections(list);
        List<Intersection> expectedReturn = Arrays.asList(
            new Intersection(Arrays.asList(1, 3), new Rectangle(140, 160, 210, 20)),
            new Intersection(Arrays.asList(1, 4), new Rectangle(160, 140, 190, 40)),
            new Intersection(Arrays.asList(2, 3), new Rectangle(140, 200, 230, 60)),
            new Intersection(Arrays.asList(2, 3), new Rectangle(160, 200, 210,130)),
            new Intersection(Arrays.asList(3, 4), new Rectangle(160, 160, 230, 100)),
            new Intersection(Arrays.asList(3, 4, 1), new Rectangle(160, 160, 190, 20)),
            new Intersection(Arrays.asList(4, 4, 1), new Rectangle(160, 200, 210, 60))
        );

        boolean expectedResultEqualActualResult = actualReturn.stream().allMatch(actualIntersection ->
                expectedReturn.stream().anyMatch(expectedIntersection ->
                    expectedIntersection.getCommonArea().equals(actualIntersection.getCommonArea())
                )
        );
        Assert.assertTrue(expectedResultEqualActualResult);

        Assert.assertEquals("it should find 7 intersections", 7, actualReturn.size());
        Intersection firstIntersection = actualReturn.get(0);
        List<Integer> shapesInvolved = firstIntersection.getShapeKeys();
        List<Integer> commutedShapesInvolved = Arrays.asList(shapesInvolved.get(1), shapesInvolved.get(0));

        boolean commutativePropertyIsEnsured = actualReturn.stream().noneMatch(intersection ->
                intersection.getShapeKeys().equals(commutedShapesInvolved));
        Assert.assertTrue(commutativePropertyIsEnsured);
    }

    @Test
    public void IntersectionCalculatorServiceTest_itShouldFindEveryIntersection() {
        List<SpatialShape> list = new ArrayList<>();

        IntStream.range(100, 110).forEach(x ->
                list.add(new Rectangle(x, 100, 250, 80))
        );

        List<Intersection<SpatialShape>> actualReturn = intersectionCalculatorService.getIntersections(list);

        Assert.assertTrue(actualReturn.get(actualReturn.size() -1).getShapeKeys().size() > 6);
    }
}