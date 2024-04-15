// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean endsWith(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean endsWith(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getFileName()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final int getNameCount()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getParent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getRoot()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean isAbsolute()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Iterator<Path> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path normalize()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path relativize(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolve(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolve(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolveSibling(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path resolveSibling(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean startsWith(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean startsWith(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path subpath(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path toAbsolutePath()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path toRealPath(LinkOption... __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
}

