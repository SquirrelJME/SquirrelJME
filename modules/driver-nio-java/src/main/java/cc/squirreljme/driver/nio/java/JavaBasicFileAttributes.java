// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.java;

import cc.squirreljme.driver.nio.java.shelf.JavaFileAttributesBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Accesses Java file attributes.
 *
 * @since 2023/08/21
 */
public class JavaBasicFileAttributes
	implements BasicFileAttributes
{
	/** The attributes to base on. */
	private final JavaFileAttributesBracket _attrib;
	
	/**
	 * Initializes the attribute wrapper.
	 *
	 * @param __attrib The Java linked attributes.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/21
	 */
	public JavaBasicFileAttributes(JavaFileAttributesBracket __attrib)
		throws NullPointerException
	{
		if (__attrib == null)
			throw new NullPointerException("NARG");
		
		this._attrib = __attrib;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public FileTime creationTime()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean isDirectory()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean isOther()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean isRegularFile()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean isSymbolicLink()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public FileTime lastAccessTime()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public FileTime lastModifiedTime()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public long size()
	{
		throw Debugging.todo();
	}
}
