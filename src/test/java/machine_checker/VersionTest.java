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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	void testCompareToWithOnlyNumbers() {
		assertCompareEqual("17", "17");
		assertCompareEqual("17.0", "17");
		assertCompareEqual("17.0.0", "17");
		assertCompareEqual("17.0.0", "17.0");
		assertCompareEqual("17.0.0", "17.0.0");

		assertLessThan("17.1.2.2", "17.1.2.3");
		assertLessThan("17.1.2", "17.1.2.3");
		assertLessThan("17.1", "17.1.2.3");
		assertLessThan("17", "17.1.2.3");
		assertLessThan("16", "17.1.2.3");
		assertLessThan("17.1.2.3", "18");
		assertLessThan("17.0", "17.1.2.3");
		assertLessThan("17.0.9.9", "17.1.2.3");
		assertLessThan("17.1.1", "17.1.2.3");
		assertLessThan("17.1.1.9", "17.1.2.3");
		assertLessThan("17.1.2.2", "17.1.2.3");
	}

	@Test
	void testCompareToWithPreRelease() {
		assertCompareEqual("17.1.2-hello", "17.1.2-hello");
		assertCompareEqual("17.1.2-hello.yo", "17.1.2-hello.yo");
		assertCompareEqual("17.1.2-hello.3", "17.1.2-hello.3");
		assertCompareEqual("17.1.2-hello.3", "17.1.2-hello.3");
		assertLessThan("17.1.2-hello.3", "17.1.2-jello.3");
		assertLessThan("17.1.2-hello.3", "17.1.2-hollo.3");
		assertLessThan("17.1.2-hello.3", "17.1.2-hello.3.0");
		assertLessThan("17.1.2-hello.3", "17.1.2-hello.04");
		assertLessThan("17.1.2-hello.3", "17.1.2-hello.3.fooo");
	}

	@Test
	void testCompareToWithBuildMetadata() {
		assertCompareEqual("17.1.2-hello.3+whatever", "17.1.2-hello.3+whatever");
		assertCompareEqual("17.1.2-hello.3+whatever.1", "17.1.2-hello.3+whatever.2");
		assertCompareEqual("17.1.2-hello.3+whatever", "17.1.2-hello.3+somethingelse");
		assertCompareEqual("17+whatever", "17+somethingelse");
	}

	@Test
	void testGreaterOrEqualToIgnoringLessSignificantNumbers() {
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("16")));
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17")));
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1")));
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.2")));
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.2.3")));
		assertTrue(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.2.3.999")));
		assertFalse(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.2.4")));
		assertFalse(
				new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.2.4.999")));
		assertFalse(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.1.3")));
		assertFalse(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("17.2")));
		assertFalse(new Version("17.1.2.3").greaterOrEqualToIgnoringLessSignificantNumbers(new Version("18")));
	}
}
