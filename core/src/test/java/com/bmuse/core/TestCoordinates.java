package com.bmuse.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.bmuse.model.Coordinates;

public class TestCoordinates {
	Coordinates origin;
	Coordinates point2_15;
	Coordinates point15_2;
	Coordinates point15_15;
	Coordinates point2_2;
	Coordinates point2_2_duplicate;

	@Before
	public void setUp() throws Exception {
		origin = new Coordinates(0, 0);
		point15_15 = new Coordinates(15, 15);
		point2_15 = new Coordinates(2, 15);
		point2_2 = new Coordinates(2, 2);
		point2_2_duplicate = new Coordinates(2, 2);
		point15_2 = new Coordinates(15, 2);
		
	}

	@Test
	public void testHashCode() {
		//should return true if equals return true;
		assertEquals(point2_2.hashCode(), point2_2_duplicate.hashCode());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(point15_15.equals(null));
		assertFalse(origin.equals(Integer.valueOf(0)));
	}

	@Test
	public void testEqualsCoordinates() {
		assertTrue(point2_2.equals(point2_2_duplicate));
		assertFalse(point2_2.equals(point2_15));
		assertFalse(point2_2.equals(point15_2));
		assertFalse(point15_2.equals(point2_15));
		assertFalse(point15_15.equals(origin));
	}

	@Test
	public void testCompareTo() {
		assertTrue(origin.compareTo(point15_15) < 0);
		assertTrue(point15_2.compareTo(point2_15) > 0);
		assertTrue(point2_2.compareTo(point2_2_duplicate) == 0);
	}

}
