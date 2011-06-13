package ro.ddanciu.test.finite.elements.api.utils;

import static java.lang.String.format;
import static junit.framework.Assert.assertEquals;
import static ro.ddanciu.finite.elements.api.utils.TriangulationUtils.gatherExterior;
import static ro.ddanciu.finite.elements.api.utils.TriangulationUtils.gatherInterior;
import static ro.ddanciu.finite.elements.api.utils.TriangulationUtils.mapping;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;
import ro.ddanciu.finite.elements.api.Vector;
import ro.ddanciu.finite.elements.api.utils.TriangulationUtils;

public class TriangulationUtilsTest {
	
	private Set<Triangle> triangulation;
	
	@Before
	public void setup() {
		triangulation = new HashSet<Triangle>();

		triangulation.add(new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6)));
		triangulation.add(new Triangle(new Point(1, 0), new Point(2, 6), new Point(3, 3)));
		triangulation.add(new Triangle(new Point(1, 0), new Point(3, 3), new Point(4, 1)));
		triangulation.add(new Triangle(new Point(4, 1), new Point(3, 3), new Point(2, 6)));
		triangulation.add(new Triangle(new Point(4, 1), new Point(2, 6), new Point(6, 2)));
		triangulation.add(new Triangle(new Point(6, 2), new Point(2, 6), new Point(5, 5)));
	}
	
	@Test
	public void mappingTriangles() {
		Map<Vector, List<Vector>> mapping = 
				mapping(Collections.singleton(new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6))));
		
		Map<Vector, List<Vector>> expected = new HashMap<Vector, List<Vector>>();
		Vector v1 = new Vector(new Point(1, 0), new Point(0, 4));
		Vector v2 = new Vector(new Point(0, 4), new Point(2, 6));
		Vector v3 = new Vector(new Point(2, 6), new Point(1, 0));

		expected.put(v1, Arrays.asList(v2, v3));
		expected.put(v2, Arrays.asList(v3, v1));
		expected.put(v3, Arrays.asList(v1, v2));
		
		Assert.assertEquals("Mapping failed!", expected, mapping);
	}
	
	@Test
	public void exteriorOfTriangle() {
	
		Triangle t1 = new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6));
		Triangle t2 = new Triangle(new Point(1, 0), new Point(2, 6), new Point(3, 3));

		Set<Triangle> triangles = new HashSet<Triangle>();
		triangles.add(t1);
		triangles.add(t2);
		Map<Vector, List<Vector>> mapping = mapping(triangles);
		
		
		Set<Vector> minimum = new HashSet<Vector>();
		for (Segment s : t1.segments()) {
			minimum.add(new Vector(s, true));
		}
		
		Collection<Triangle> result = gatherExterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected = Collections.singleton(t2);
		
		assertEquals("Exterior doesn't work!", expected, actual);
		
	}
	
	
	@Test
	public void exterior() {
		
		Map<Vector, List<Vector>> mapping = mapping(triangulation);
		
		Set<Vector> minimum = new HashSet<Vector>();
		minimum.add(new Vector( new Point(2, 6), new Point(5, 5)));
		minimum.add(new Vector( new Point(5, 5), new Point(6, 2)));
		minimum.add(new Vector( new Point(6, 2), new Point(4, 1)));
		minimum.add(new Vector( new Point(4, 1), new Point(3, 3)));
		minimum.add(new Vector( new Point(3, 3), new Point(2, 6)));
		
		Collection<Triangle> result = gatherExterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6)));
		expected.add(new Triangle(new Point(1, 0), new Point(2, 6), new Point(3, 3)));
		expected.add(new Triangle(new Point(1, 0), new Point(3, 3), new Point(4, 1)));
		
		assertEquals("Exterior doesn't work!", expected, actual);
		
	}

	@Test
	public void interiorOfATriangle() {
	
		Triangle t1 = new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6));

		Set<Triangle> triangles = new HashSet<Triangle>();
		triangles.add(t1);
		Map<Vector, List<Vector>> mapping = mapping(triangles);
		
		
		Set<Vector> minimum = new HashSet<Vector>();
		for (Segment s : t1.segments()) {
			minimum.add(new Vector(s, true));
		}
		
		Collection<Triangle> result = gatherInterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected = Collections.singleton(t1);
		
		assertEquals("Interior doesn't work!", expected, actual);
		
	}
	

	@Test
	public void interiorOfTwoTriangles() {
	
		Triangle t1 = new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6));
		Triangle t2 = new Triangle(new Point(1, 0), new Point(2, 6), new Point(3, 3));
		
		Set<Triangle> triangles = new HashSet<Triangle>();
		triangles.add(t1);
		triangles.add(t2);
		Map<Vector, List<Vector>> mapping = mapping(triangles);
		
		
		Set<Vector> minimum = new HashSet<Vector>();
		minimum.add(new Vector(new Point(1, 0), new Point(0, 4)));
		minimum.add(new Vector(new Point(0, 4), new Point(2, 6)));
		minimum.add(new Vector(new Point(2, 6), new Point(3, 3)));
		minimum.add(new Vector(new Point(3, 3), new Point(1, 0)));
		
		Collection<Triangle> result = gatherInterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected =  new HashSet<Triangle>();
		expected.add(t1);
		expected.add(t2);
		
		assertEquals("Interior doesn't work!", expected, actual);
		
	}

	@Test
	public void interiorOfTwoTrianglesWithExterior() {
	
		Triangle t1 = new Triangle(new Point(1, 0), new Point(0, 4), new Point(2, 6));
		Triangle t2 = new Triangle(new Point(1, 0), new Point(2, 6), new Point(3, 3));
		Triangle t3 = new Triangle(new Point(1, 0), new Point(3, 3), new Point(4, 1));

		Set<Triangle> triangles = new HashSet<Triangle>();
		triangles.add(t1);
		triangles.add(t2);
		triangles.add(t3);
		Map<Vector, List<Vector>> mapping = mapping(triangles);
		
		
		Set<Vector> minimum = new HashSet<Vector>();
		minimum.add(new Vector(new Point(1, 0), new Point(0, 4)));
		minimum.add(new Vector(new Point(0, 4), new Point(2, 6)));
		minimum.add(new Vector(new Point(2, 6), new Point(3, 3)));
		minimum.add(new Vector(new Point(3, 3), new Point(1, 0)));
		
		Collection<Triangle> result = gatherInterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected =  new HashSet<Triangle>();
		expected.add(t1);
		expected.add(t2);
		
		assertEquals("Interior doesn't work!", expected, actual);
		
	}
	
	@Test
	public void interior() {
		
		Map<Vector, List<Vector>> mapping = mapping(triangulation);
		
		Set<Vector> minimum = new HashSet<Vector>();
		minimum.add(new Vector( new Point(2, 6), new Point(5, 5)));
		minimum.add(new Vector( new Point(5, 5), new Point(6, 2)));
		minimum.add(new Vector( new Point(6, 2), new Point(4, 1)));
		minimum.add(new Vector( new Point(4, 1), new Point(3, 3)));
		minimum.add(new Vector( new Point(3, 3), new Point(2, 6)));
		
		Collection<Triangle> result = gatherInterior(mapping, minimum);
		Set<Triangle> actual = new HashSet<Triangle>(result);
		
		Set<Triangle> expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(4, 1), new Point(3, 3), new Point(2, 6)));
		expected.add(new Triangle(new Point(4, 1), new Point(2, 6), new Point(6, 2)));
		expected.add(new Triangle(new Point(6, 2), new Point(2, 6), new Point(5, 5)));
		
		assertEquals("Interior doesn't work!", expected, actual);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void splitOtherTriangle() {

		Triangle t = new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540")));
		
		Set<Triangle> container = new HashSet<Triangle>();
		container.add(new Triangle(new Point(4, 1), new Point(3, 3), new Point(2, 6)));
		container.add(new Triangle(new Point(4, 1), new Point(2, 6), new Point(6, 2)));
		container.add(new Triangle(new Point(6, 2), new Point(2, 6), new Point(5, 5)));
		
		Point c0 = t.incenter();
		TriangulationUtils.divideByPoint(t, c0, container);
		
	}
	
	@Test
	public void split() {

		Triangle other = new Triangle(new Point(4, 1), new Point(3, 3), new Point(2, 6));

		Triangle victim = new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540")));
		
		Set<Triangle> container = new HashSet<Triangle>();
		container.add(victim);
		container.add(other);
		
		Point c0 = victim.incenter();
		

		Triangle child1 = new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				c0);
		
		Triangle child2 = new Triangle(
				c0, 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540")));
		
		Triangle child3 = new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				c0,
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540")));
		
		Set<Triangle> expected = new HashSet<Triangle>();
		expected.add(other);
		expected.add(child1);
		expected.add(child2);
		expected.add(child3);

		TriangulationUtils.divideByPoint(victim, c0, container);
		
		
		assertEquals("Interior doesn't work!", expected, container);
	}
	
	@Test
	public void minimumCommonPoliLine() {
		
		Triangle t1 = new Triangle(
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000"))); 
		Triangle t2 = new Triangle(
					new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		Triangle t3 = new Triangle(
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000")), 
					new Point(new BigDecimal("3.0000"), new BigDecimal("6.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))); 
		Triangle t4 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		Triangle t5 = new Triangle(
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		
		
		Triangle t6 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))); 
		Triangle t7 = new Triangle(
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000")), 
					new Point(new BigDecimal("3.0000"), new BigDecimal("6.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))); 
		Triangle t8 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000"))); 

		Set<Triangle> mom = new HashSet<Triangle>(Arrays.asList(t1, t2, t3, t4, t5));
		Set<Triangle> dad = new HashSet<Triangle>(Arrays.asList(t6, t7, t8));
		
		Map<Vector, List<Vector>> momsMapping = TriangulationUtils.mapping(mom);
		Map<Vector, List<Vector>> dadsMapping = TriangulationUtils.mapping(dad);
		
		Vector startup = new Vector(
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")));
		
		Set<Vector> actual = TriangulationUtils.minimumCommonPoliLine(momsMapping, dadsMapping, startup);
		Set<Vector> expected = new HashSet<Vector>(
				Arrays.asList(
						new Vector(new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000"))), 
						new Vector(new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))), 
						new Vector(new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000"))) 
				));
		
		Assert.assertEquals("minimumCommonPoliLine failed", expected, actual);
		
	}
	
	

	@Test
	public void interiorAllHart() {
		
		Triangle t1 = new Triangle(
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000"))); 
		Triangle t2 = new Triangle(
					new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		Triangle t3 = new Triangle(
					new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000")), 
					new Point(new BigDecimal("3.0000"), new BigDecimal("6.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))); 
		Triangle t4 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), 
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		Triangle t5 = new Triangle(
					new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), 
					new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208"))); 
		
		Set<Triangle> mom = new HashSet<Triangle>(Arrays.asList(t1, t2, t3, t4, t5));
		Map<Vector, List<Vector>> momsMapping = TriangulationUtils.mapping(mom);
		
		Set<Vector> minimum = new HashSet<Vector>(
				Arrays.asList(
						new Vector(new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000")), new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000"))), 
						new Vector(new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")), new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000"))), 
						new Vector(new Point(new BigDecimal("1.0000"), new BigDecimal("5.0000")), new Point(new BigDecimal("3.0000"), new BigDecimal("6.0000"))), 
						new Vector(new Point(new BigDecimal("3.0000"), new BigDecimal("6.0000")), new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000"))), 
						new Vector(new Point(new BigDecimal("4.0000"), new BigDecimal("4.0000")), new Point(new BigDecimal("2.0000"), new BigDecimal("0.0000"))) 
				));
		
		Collection<Triangle> interior = TriangulationUtils.gatherInterior(momsMapping, minimum);
		
		Assert.assertEquals("gatherInterior failed", mom, new HashSet<Triangle>(interior));
	}
	
	@Test
	public void testVectorEquals() {
		Vector v1 = new Vector(
				new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")),
				new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208")));
		Vector v2 = new Vector(
				new Point(new BigDecimal("0.0000"), new BigDecimal("2.0000")),
				new Point(new BigDecimal("1.7208"), new BigDecimal("1.7208")));

		Assert.assertEquals("Vector equals failed", v1, v2);
		Assert.assertEquals("Vector equals failed", v1.invert(), v2.invert());
		
		Assert.assertFalse("Vector equals failed", v1.equals(v2.invert()));
		Assert.assertFalse("Vector equals failed", v1.invert().equals(v2));
		
		
	}
	
	@Test
	public void testDivideByMedians() {
		Triangle t1 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("1.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("0.0000"))); 
		Triangle t2 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("-1.0000"))); 
		Triangle t3 = new Triangle(
				new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
				new Point(new BigDecimal("0.0000"), new BigDecimal("1.0000")), 
				new Point(new BigDecimal("-1.0000"), new BigDecimal("0.0000"))); 		
		
		Triangle t4 = new Triangle(
						new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
						new Point(new BigDecimal("-1.0000"), new BigDecimal("0.0000")), 
						new Point(new BigDecimal("-1.0000"), new BigDecimal("-1.0000"))); 
		
		Set<Triangle> container = new HashSet<Triangle>(Arrays.asList(t1, t2, t3, t4));
		
		TriangulationUtils.divideByMedians(t1, container);
		
		Triangle te1 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.5000"))); 
		Triangle te2 = new Triangle(
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.5000"))); 
		Triangle te3 = new Triangle(
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.5000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.5000"))); 
		Triangle te4 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.5000")), 
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.5000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("1.0000"))); 
		
		Triangle te5 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.5000")), 
					new Point(new BigDecimal("-1.0000"), new BigDecimal("0.0000"))); 
		Triangle te6 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.5000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("1.0000")), 
					new Point(new BigDecimal("-1.0000"), new BigDecimal("0.0000"))); 
		Triangle te7 = new Triangle(
					new Point(new BigDecimal("0.0000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("-1.0000")), 
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.0000"))); 
		Triangle te8 = new Triangle(
					new Point(new BigDecimal("0.5000"), new BigDecimal("0.0000")), 
					new Point(new BigDecimal("0.0000"), new BigDecimal("-1.0000")), 
					new Point(new BigDecimal("1.0000"), new BigDecimal("0.0000"))); 


		Assert.assertTrue(format("division by medians fails! %s", te1), container.remove(te1));
		Assert.assertTrue(format("division by medians fails! %s", te2), container.remove(te2));
		Assert.assertTrue(format("division by medians fails! %s", te3), container.remove(te3));
		Assert.assertTrue(format("division by medians fails! %s", te4), container.remove(te4));
		Assert.assertTrue(format("division by medians fails! %s", te5), container.remove(te5));
		Assert.assertTrue(format("division by medians fails! %s", te6), container.remove(te6));
		Assert.assertTrue(format("division by medians fails! %s", te7), container.remove(te7));
		Assert.assertTrue(format("division by medians fails! %s", te8), container.remove(te8));
		Assert.assertTrue(format("division by medians fails! %s", t4), container.remove(t4));

		Assert.assertTrue("division by medians fails!", container.isEmpty());
		
	}
}
