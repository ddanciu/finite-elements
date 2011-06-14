package ro.ddanciu.test.finite.elements.api.readers;

import static java.lang.String.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.readers.ElementsReader;

public class ElementsReaderTest {
	
	private InputStream stream;
	private ElementsReader reader;
	
	@Test(expected=IllegalArgumentException.class)
	public void empty() {
		stream = new ByteArrayInputStream("".getBytes());
		reader = new ElementsReader(stream);
		
		reader.readPoliLine();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void onePoint() {
		stream = new ByteArrayInputStream("5, 6".getBytes());
		reader = new ElementsReader(stream);
		
		reader.readPoliLine();
	}
	
	@Test
	public void triangle() {
		stream = new ByteArrayInputStream("0.00, 0.0\n#Comment\n1.50, 1.66\n\n2.33, 2.56".getBytes());
		reader = new ElementsReader(stream);
		
		PoliLine triangle = reader.readPoliLine();
		PoliLine expected = new PoliLine(
				new Point(0, 0), 
				new Point(new BigDecimal("1.5"), new BigDecimal("1.66")), 
				new Point(new BigDecimal("2.33"), new BigDecimal("2.56")));
		
		Assert.assertEquals("Triangle read failed!", expected, triangle);
	}
	
	@Test
	public void bs() {
		stream = new ByteArrayInputStream("0.00, 0.0\n#Comment\n1.50, bs\n2.33, 2.56".getBytes());
		reader = new ElementsReader(stream);
		
		try {
			PoliLine pl = reader.readPoliLine();
			Assert.fail(format("Exception should have been thrown! Received: %s", pl));
		} catch (Exception e) {
			Assert.assertTrue(format("Got wrong message! Was: %s.", e.getMessage()), e.getMessage().endsWith(">bs<"));
		}
	}
	
	@Test
	public void poliLineOnOneLine() {
		stream = new ByteArrayInputStream("#Comment\n0.00 0.0, 1.50 2.33, 2.56 0\n0.00 1.50, 0.0 1.66, 2.33 2.56\n".getBytes());
		reader = new ElementsReader(stream);
		
		List<PoliLine> pls = reader.readPoliLines();
		
		PoliLine expected0 = new PoliLine(
				new Point(0, 0), 
				new Point(new BigDecimal("1.5"), new BigDecimal("2.33")), 
				new Point(new BigDecimal("2.56"), BigDecimal.ZERO));	
		
		PoliLine expected1 = new PoliLine(
				new Point(BigDecimal.ZERO, new BigDecimal("1.5")), 
				new Point(BigDecimal.ZERO, new BigDecimal("1.66")), 
				new Point(new BigDecimal("2.33"), new BigDecimal("2.56")));

		Assert.assertEquals(format("Wrong polilines <> 2: %s", pls), 2, pls.size());
		Assert.assertEquals("Triangle read failed!", expected0, pls.get(0));
		Assert.assertEquals("Triangle read failed!", expected1, pls.get(1));
	}
	
	
	@After
	public void close() throws IOException {
		reader.close();
	}
	
	

}
