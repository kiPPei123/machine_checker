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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Version object that implement comparison as specified by "semantic
 * versioning" at https://semver.org/
 *
 * @author Patrik Larsson
 */
public class Version implements Comparable<Version> {
	private final String originalString;
	private final List<String> components;
	private final List<String> suffixes;
	private final String buildMetadata;

	/**
	 * Creates a new version object from the given string.
	 *
	 * @param aString version string
	 * @throws NullPointerException if the argument is <code>null</code>
	 */
	public Version(final String aString) {
		originalString = Objects.requireNonNull(aString);
		final String[] metaDataSplit = originalString.split("\\+", 2);
		buildMetadata = metaDataSplit.length > 1 ? metaDataSplit[1] : null;
		final String withoutMetadata = metaDataSplit[0];
		final String[] suffixSplit = withoutMetadata.split("-", 2);
		components = Arrays.stream(suffixSplit[0].split("\\.")).toList();
		final String suffix = suffixSplit.length > 1 ? suffixSplit[1] : null;
		suffixes = suffix == null ? List.of() : Arrays.stream(suffix.split("\\.")).toList();
	}

	@Override
	public String toString() {
		return originalString;
	}

	public List<String> getComponents() {
		// Safe to return the list directly since it should be unmodifiable.
		return components;
	}

	public List<String> getSuffixes() {
		// Safe to return the list directly since it should be unmodifiable.
		return suffixes;
	}

	public String getBuildMetadata() {
		return buildMetadata;
	}

	@Override
	public int compareTo(final Version aOther) {
		final int componentsCompare = compareStringLists(getComponents(), aOther.getComponents(), true);
		if (componentsCompare != 0)
			return componentsCompare;
		return compareStringLists(getSuffixes(), aOther.getSuffixes(), false);
	}

	private static int compareStringLists(final List<String> aListA, final List<String> aListB,
			final boolean aPadWithZeroes) {
		if (aListA.isEmpty() && aListB.isEmpty())
			return 0;
		if (!aListA.isEmpty() && aListB.isEmpty())
			return -1;
		if (aListA.isEmpty() && !aListB.isEmpty())
			return 1;

		for (int i = 0;; i++) {
			if (i >= aListA.size() && i >= aListB.size())
				return 0;
			if (!aPadWithZeroes && i >= aListA.size())
				return -1;
			if (!aPadWithZeroes && i >= aListB.size())
				return 1;

			final String thisString = i < aListA.size() ? aListA.get(i) : "0";
			final String otherString = i < aListB.size() ? aListB.get(i) : "0";
			final boolean thisIsNumeric = Pattern.matches("\\d+", thisString);
			final boolean otherIsNumeric = Pattern.matches("\\d+", otherString);

			if (thisIsNumeric && !otherIsNumeric)
				return -1;
			if (!thisIsNumeric && otherIsNumeric)
				return 1;
			if (thisIsNumeric && otherIsNumeric) {
				final int numericCompare = Integer.parseInt(thisString) - Integer.parseInt(otherString);
				if (numericCompare != 0)
					return numericCompare;
				continue;
			}
			// Neither is numeric.
			final int lexicographicCompare = thisString.compareTo(otherString);
			if (lexicographicCompare != 0)
				return lexicographicCompare;
		}
	}

	public boolean lessThanOrEqualTo(final Version aOther) {
		return compareTo(aOther) <= 0;
	}

	public boolean lessThanOrEqualTo(final String aOther) {
		return lessThanOrEqualTo(new Version(aOther));
	}

	public boolean greaterOrEqualTo(final Version aOther) {
		return compareTo(aOther) >= 0;
	}

	public boolean greaterOrEqualTo(final String aOther) {
		return greaterOrEqualTo(new Version(aOther));
	}

	/**
	 * Designed for checking a maximum allowed version of tools.
	 * <p>
	 * The given parameter is truncated to the same length as this version (same
	 * number of dot-separated numbers) before comparison. This means one can
	 * compare version "17.5" with other versions and return true for 17, 17.5 and
	 * 17.5.999 but false for 17.6.
	 *
	 * @param other version to compare to
	 * @return <code>true</code> if this version is greater than or equal to the
	 *         given other version truncated to the same length as this version.
	 */
	public boolean greaterOrEqualToIgnoringLessSignificantNumbers(final Version aOther) {
		if (aOther.components.size() <= components.size())
			return greaterOrEqualTo(aOther);
		return compareStringLists(components, aOther.components.subList(0, components.size()), true) >= 0;
	}
}
