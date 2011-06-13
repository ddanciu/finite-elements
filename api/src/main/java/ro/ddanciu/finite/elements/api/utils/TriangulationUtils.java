package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;
import ro.ddanciu.finite.elements.api.Vector;

public class TriangulationUtils {
	
	public static Collection<Triangle> gatherInterior(Map<Vector, List<Vector>> mapping, Set<Vector> minimum) {
		List<Triangle> interior = new ArrayList<Triangle>();

		Set<Vector> done = new HashSet<Vector>();
		Stack<Vector> stack = new Stack<Vector>();
		stack.addAll(minimum);
		while (! stack.isEmpty()) {
			Vector item = stack.pop();
			done.add(item);
			List<Vector> roommates = mapping.get(item);
			if (roommates != null) {
				for (Vector roommate: roommates) {
					int pos = stack.indexOf(roommate);
					if (pos >= 0) {
						stack.remove(pos);
					} else {
						Vector roomateInverted = roommate.invert();
						if (! done.contains(roomateInverted)) {
							stack.add(roomateInverted);
						}
					}
				}
				Iterator<Vector> it = roommates.iterator();
				interior.add(new Triangle(item.getSegment(), it.next().getSegment(), it.next().getSegment()));
			}
		}
		return interior;
	}

	public static Collection<Triangle> gatherExterior(Map<Vector, List<Vector>> mapping, Set<Vector> minimum) {
		List<Triangle> exterior = new ArrayList<Triangle>();
		Stack<Vector> stack = new Stack<Vector>();
		
		for (Vector v : minimum) {
			stack.add(v.invert());
		}
		
		while (! stack.isEmpty()) {
			Vector item = stack.pop();
			List<Vector> roommates = mapping.get(item);
			if (roommates != null) {
				for (Vector roommate: roommates) {
					int pos;
					if ((pos = stack.indexOf(roommate))  >= 0 || (pos = stack.indexOf(roommate.invert()))  >= 0) {
						stack.remove(pos);
					} else {
						stack.add(roommate.invert());
					}
				}
				Iterator<Vector> it = roommates.iterator();
				exterior.add(new Triangle(item.getSegment(), it.next().getSegment(), it.next().getSegment()));
			}
		}
		return exterior;
	}
	
	
	public static Map<Vector, List<Vector>> mapping(Set<Triangle> triangles) {
		Map<Vector, List<Vector>> map = new HashMap<Vector, List<Vector>>();
		for (Triangle t : triangles) {
			Vector v1 = new Vector(t.getP1(), t.getP2());
			Vector v2 = new Vector(t.getP2(), t.getP3());
			Vector v3 = new Vector(t.getP3(), t.getP1());
			map.put(v1, Arrays.asList(v2, v3));
			map.put(v2, Arrays.asList(v3, v1));
			map.put(v3, Arrays.asList(v1, v2));
		}
		return map;
	}
	

	public static Set<Vector> minimumCommonPoliLine(
			Map<Vector, List<Vector>> momsMapping,
			Map<Vector, List<Vector>> dadsMapping, Vector startup) {
		Set<Vector> minimum = new LinkedHashSet<Vector>();
		
		Set<Vector> done = new HashSet<Vector>();
		
		Stack<Vector> stack = new Stack<Vector>();

		stack.add(startup);
		stack.add(startup.invert());
		
		while (!stack.isEmpty()) {
			Vector x = stack.pop();
			done.add(x);
			if (dadsMapping.containsKey(x)) {
				 minimum.add(x);
			} else {
				Vector switched = x.invert();
				List<Vector> list = momsMapping.get(switched);
				if (list == null) {
					return null;
				}
				for (Vector v : list) {
					if (done.contains(v)) {
						continue;
					}
					stack.add(v);
				}
			}
		}
		return minimum;
	}

	
	public static void divideByPoint(Triangle which, Point point, Set<Triangle> container) {
		
		if (! container.remove(which)) {
			throw new IllegalArgumentException(format("%s doesn't contain %s", container, which));
		}
		
		for (Segment s : which.segments()) {
			container.add(new Triangle(s.getP1(), s.getP2(), point));
		}
		
	}
	
	public static void divideByMedians(Triangle which, Set<Triangle> container) {
		
		if (! container.remove(which)) {
			throw new IllegalArgumentException(format("%s doesn't contain %s", container, which));
		}
		Point[] midpoints = new Point[3];
		midpoints[0] = which.getE1().getMiddlePoint();
		midpoints[1] = which.getE2().getMiddlePoint();
		midpoints[2] = which.getE3().getMiddlePoint();

		container.add(new Triangle(midpoints[0], midpoints[1], midpoints[2]));
		
		container.add(new Triangle(midpoints[1], midpoints[0], which.getP3()));
		container.add(new Triangle(midpoints[2], midpoints[1], which.getP1()));
		container.add(new Triangle(midpoints[0], midpoints[2], which.getP2()));
		
		Map<Triangle, Segment> neighbours = new HashMap<Triangle, Segment>();
		for (Segment s : which.segments()) {
			for (Triangle other : container) {
				if (other.containsSegment(s)) {
					neighbours.put(other, s);
				}
			}
		}
		
		for (Entry<Triangle, Segment> entry : neighbours.entrySet()) {
			Triangle other = entry.getKey();
			Segment s = entry.getValue();
			
			container.remove(other);
			
			Point otherPoint = opositePoint(other, s);
			
			container.add(new Triangle(s.getP2(), s.getMiddlePoint(), otherPoint));
			container.add(new Triangle(s.getMiddlePoint(), s.getP1(), otherPoint));
				
		}
		
	}

	public static Point opositePoint(Triangle other, Segment l) {
		HashSet<Point> xxx = new HashSet<Point>(other);
		xxx.removeAll(Arrays.asList(l.getP1(), l.getP2()));
		return xxx.iterator().next();
	}

}
