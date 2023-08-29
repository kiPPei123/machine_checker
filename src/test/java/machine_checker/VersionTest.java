/*
 * This file is part of "Machine Checker".
 * Copyright (c) 2023 Patrik Larsson.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package machine_checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class VersionTest {

	@Test
	void testToString() {
		assertEquals("17", new Version("17").toString());
		assertEquals("017", new Version("017").toString());
		assertEquals("I'll be back", new Version("I'll be back").toString());
	}

	private int compare(final String aVersionA, final String aVersionB) {
		return new Version(aVersionA).compareTo(new Version(aVersionB));
	}

	private void assertCompareEqual(final String aVersionA, final String aVersionB) {
		assertEquals(0, compare(aVersionA, aVersionB));
		assertEquals(0, compare(aVersionB, aVersionA));
	}

	private void assertLessThan(final String aVersionA, final String aVersionB) {
		assertTrue(compare(aVersionA, aVersionB) < 0);
		assertTrue(compare(aVersionB, aVersionA) > 0);
	}

	@Test
	void testCompareTo() {
		assertCompareEqual("17", "17");
		assertCompareEqual("17.0", "17");
		assertCompareEqual("17.0.0", "17");
		assertCompareEqual("17.0.0", "17.0");
		assertCompareEqual("17.0.0", "17.0.0");
		assertCompareEqual("17.1.2-hello", "17.1.2-hello");
		assertCompareEqual("17.1.2-hello.yo", "17.1.2-hello.yo");
		assertCompareEqual("17.1.2-hello.3", "17.1.2-hello.3");
		assertCompareEqual("17.1.2-hello.3+whatever", "17.1.2-hello.3+somethingelse");
		assertCompareEqual("17+hello", "17.0");
		assertLessThan("17-hello", "17");
		assertLessThan("17-hello", "17.0");
		assertLessThan("17.1-hello", "17.1");
		assertLessThan("17.1-0001", "17.1-1000");
		assertLessThan("17.1-a", "17.1-b");
		assertLessThan("17.1-aa", "17.1-b");
		assertLessThan("17.1-b", "17.1-ba");
		fail("Not yet implemented");
	}

	@Test
	void testGreaterOrEqualToIgnoringLessSignificantNumbers() {
		fail("Not yet implemented");
	}

}
