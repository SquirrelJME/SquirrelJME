// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * This represents a null path.
 *
 * @since 2019/12/22
 */
public class NullPath
	implements Path
{
	/** The only valid path permitted. */
	public static final String ONLY_PATH_STRING =
		"null:$";
	
	/** The instance of this class. */
	public static final Path INSTANCE =
		new NullPath();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final int compareTo(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean endsWith(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean endsWith(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getFileName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final FileSystem getFileSystem()
	{
		return NullFileSystem.INSTANCE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getName(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final int getNameCount()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getParent()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getRoot()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean isAbsolute()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Iterator<Path> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path normalize()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path relativize(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolve(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolve(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolveSibling(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolveSibling(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean startsWith(Path __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean startsWith(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path subpath(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path toAbsolutePath()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path toRealPath(LinkOption... __a)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

