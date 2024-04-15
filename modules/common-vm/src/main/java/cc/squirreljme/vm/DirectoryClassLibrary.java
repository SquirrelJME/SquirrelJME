// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * This is a class library which is used directly on a directory on the disk.
 *
 * @since 2020/04/19
 */
public class DirectoryClassLibrary
	implements VMClassLibrary
{
	/** The path of the library. */
	protected final Path path;
	
	/**
	 * Initializes the class library.
	 *
	 * @param __path The path to the library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/19
	 */
	public DirectoryClassLibrary(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String[] listResources()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String name()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public Path path()
	{
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
