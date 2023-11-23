// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@Api
public interface Path
	extends Comparable<Path>, Iterable<Path>
{
	/**
	 * Compares this path against another lexicographically, it must take into
	 * consideration if the file system is case-sensitive or not and how
	 * characters should compare to each other.
	 * 
	 * Two paths that are part of different filesystem providers may not be
	 * compared.
	 * 
	 * @param __b The path to compare against.
	 * @return The comparison between the two.
	 * @throws ClassCastException If the filesystem providers are different.
	 * @since 2023/08/21
	 */
	@Override
	@Api
	int compareTo(Path __b)
		throws ClassCastException;
	
	@Api
	boolean endsWith(Path __a);
	
	@Api
	boolean endsWith(String __a);
	
	@Api
	@Override
	boolean equals(Object __a);
	
	/**
	 * Returns the name of the file this represents, which will be the last
	 * name component or {@code null} if this is an empty path.
	 *
	 * @return The name of the file
	 * @since 2023/08/21
	 */
	@Api
	Path getFileName();
	
	/**
	 * Returns the file system which is bound to this path.
	 *
	 * @return The filesystem path.
	 * @since 2023/08/21
	 */
	@Api
	FileSystem getFileSystem();
	
	@Api
	Path getName(int __dx)
		throws IllegalArgumentException;
	
	@Api
	int getNameCount();
	
	/**
	 * Returns the parent of this path.
	 *
	 * @return The parent path.
	 * @since 2023/08/21
	 */
	@Api
	Path getParent();
	
	/**
	 * Returns the root path if there is one, otherwise {@code null}.
	 *
	 * @return The root of the given path.
	 * @since 2023/08/20
	 */
	@Api
	Path getRoot();
	
	@Api
	boolean isAbsolute();
	
	@Api
	Path normalize();
	
	@Api
	Path relativize(Path __a);
	
	/**
	 * If {@code __target} is absolute then that path is returned, if
	 * {@code __target} is a blank path then this returns {@code this},
	 * otherwise the current path will be treated as a directory and the
	 * path will be a child of that path.
	 *
	 * @param __target The target path.
	 * @return The resultant path.
	 * @throws InvalidPathException If the resultant path would not be valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/21
	 */
	@Api
	Path resolve(Path __target)
		throws InvalidPathException, NullPointerException;
	
	/**
	 * If {@code __target} is absolute then that path is returned, if
	 * {@code __target} is a blank path then this returns {@code this},
	 * otherwise the current path will be treated as a directory and the
	 * path will be a child of that path.
	 *
	 * @param __target The target path.
	 * @return The resultant path.
	 * @throws InvalidPathException If the resultant path would not be valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/21
	 */
	@Api
	Path resolve(String __target)
		throws InvalidPathException, NullPointerException;
	
	@Api
	Path resolveSibling(Path __a);
	
	@Api
	Path resolveSibling(String __a);
	
	@Api
	boolean startsWith(Path __a);
	
	@Api
	boolean startsWith(String __a);
	
	@Api
	Path subpath(int __a, int __b);
	
	@Api
	Path toAbsolutePath();
	
	@Api
	Path toRealPath(LinkOption... __a)
		throws IOException;
}

