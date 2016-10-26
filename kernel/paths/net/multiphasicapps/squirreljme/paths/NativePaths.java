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
 * This class is the native equivalent to the {@link java.nio.file.Paths}
 * class in that it is able to translate {@code String}s to
 * {@link NativePath}s.
 *
 * @since 2016/07/30
 */
public interface NativePaths
{
	/**
	 * Returns the separator which is used to separate file names from
	 * directories.
	 *
	 * @return The file name separator.
	 * @since 2016/07/30
	 */
	public abstract String fileSeparator();
	
	/**
	 * Converts the given set of strings into a native path.
	 *
	 * Note that all input strings are treated as if they were a single unit
	 * joined by the file separator. For example on POSIX systems
	 * {@code get("foo/bar", "baz")} is equivalent to
	 * {@code get("foo/bar/baz")}.
	 *
	 * @param __f The first string.
	 * @param __m The remaining strings.
	 * @return A native representation of the given path.
	 * @throws InvalidNativePathException If the path is not valid.
	 * @throws NullPointerException If the first path was not specified or
	 * if more paths were specified and the array contains a null element.
	 * @since 2016/07/30
	 */
	public abstract NativePath get(String __f, String... __r)
		throws InvalidNativePathException, NullPointerException;
	
	/**
	 * This is the separator which is used when a {@code PATH}-like variable
	 * is specified (such as that used in the class path argument).
	 *
	 * @return The path separator that the native path system uses.
	 * @since 2016/07/30
	 */
	public abstract String pathSeparator();
}

