package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;
import ro.ddanciu.finite.elements.api.Vector;

public class TriangulationUtils {
	
	public static Collection<Triangle> gatherInterior(Map<Vector, List<Vector>> mapping, Set<Vector> minimum) {
		List<Triangle> interior = new ArrayList<Triangle>();
		Stack<Vector> stack = new Stack<Vector>();
		stack.addAll(minimum);
		while (! stack.isEmpty()) {
			Vector item = stack.pop();
			List<Vector> roommates = mapping.get(item);
			for (Vector roommate: roommates) {
				int pos = stack.indexOf(roommate);
				if (pos >= 0) {
					stack.remove(pos);
				} else {
					stack.add(roommate.invert());
				}
			}
			Iterator<Vector> it = roommates.iterator();
			interior.add(new Triangle(item.getSegment(), it.next().getSegment(), it.next().getSegment()));
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
		Set<Vector> minimum = new HashSet<Vector>();
		
		Stack<Vector> stack = new Stack<Vector>();

		stack.add(startup);
		stack.add(startup.invert());
		
		while (!stack.isEmpty()) {
			Vector x = stack.pop();
			if (dadsMapping.containsKey(x)) {
				 minimum.add(x);
			} else {
				Vector switched = x.invert();
				List<Vector> list = momsMapping.get(switched);
				if (list == null) {
					return null;
				}
				for (Vector v : list) {
					stack.add(v);
				}
			}
		}
		return minimum;
	}
	
	public static void divideByCentrum(Triangle which, Set<Triangle> container) {
		Point circumcenter = TriangleUtils.circumcenter(which);
		divideByPoint(which, circumcenter, container);
	}

	public static void divideByPoint(Triangle which,
			Point circumcenter, Set<Triangle> container) {
		
		if (! container.remove(which)) {
			throw new IllegalArgumentException(format("%s doesn't contain %s", container, which));
		}
		
		for (Segment s : which.segments()) {
			container.add(new Triangle(s.getP1(), s.getP2(), circumcenter));
		}
		
	}

}
