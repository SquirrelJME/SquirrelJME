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
 * Note that some methods are missing.
 *
 * To convert a path to absolute, resolve from the root the other path to
 * make absolute (since these classes only handle paths and know not what the
 * current directory is).
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
	 * @param __o The path to compare against.
	 * @throws InvalidNativePathException If the other path is of another
	 * path type (this is POSIX and the other is DOS).
	 * @return An integer comparison.
	 */
	@Override
	public abstract int compareTo(NativePath __o);
	
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
	
	/**
	 * Returns the root component of this path.
	 *
	 * @return The root component of this path or {@code null} if there is
	 * none.
	 * @since 2016/08/21
	 */
	public abstract NativePath getRoot();
	
	/**
	 * Returns {@code true} if this path has a root component which specifies
	 * that it is an absolute path.
	 *
	 * @return {@code true} if the path is absolute.
	 * @since 2016/08/21
	 */
	public abstract boolean isAbsolute();
	
	/**
	 * This resolves the other path against this one.
	 *
	 * If the other path is absolute then the other path is returned.
	 * If the other is an empty path then this path is returned.
	 * Otherwise, this path is treated as if it were a directory and the
	 * other path is appended to the end of this path.
	 *
	 * @param __o The path to resolve this one against.
	 * @throws InvalidNativePathException If the other path is not valid or
	 * the resulting path would not be valid.
	 * @since 2016/08/21
	 */
	public abstract NativePath resolve(NativePath __o)
		throws InvalidNativePathException;
}

