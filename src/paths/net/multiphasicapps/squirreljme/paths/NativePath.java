// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.paths;

/**
 * This is similar to {@link java.nio.file.Path} in that it is used to
 * represent how the native path system on a given system operates. Note that
 * these paths have no interaction with the host filesystem and as such it
 * is unknown if the specified paths are even valid.
 *
 * @since 2016/07/30
 */
public interface NativePath
	extends Comparable<NativePath>
{
	/**
	 * This compares two paths lexographically to determine the order in where
	 * it is used to sort files.
	 *
	 * @param __np The path to compare against.
	 * @throws InvalidNativePathException If the other path is of another
	 * path type (this is POSIX and the other is DOS).
	 * @return An integer comparison.
	 */
	@Override
	public abstract int compareTo(NativePath __np);
	
	/**
	 * Checks if two paths are equal to each other. Native paths of different
	 * kinds are not equal to each other even if their string representation
	 * is the same.
	 *
	 * @return {@code true} if the paths are equal.
	 * @since 2016/07/30
	 */
	@Override
	public abstract boolean equals(Object __o);
}

