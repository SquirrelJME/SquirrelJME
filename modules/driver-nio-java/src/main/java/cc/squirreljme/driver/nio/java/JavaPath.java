// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.java;

import cc.squirreljme.driver.nio.java.shelf.JavaNioShelf;
import cc.squirreljme.driver.nio.java.shelf.JavaPathBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.AbstractPath;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Directly bridged host virtual machine's version of {@link Path}.
 *
 * @since 2023/08/20
 */
public class JavaPath
	extends AbstractPath<JavaFileSystem>
{
	/** The internal path. */
	final JavaPathBracket _path;
	
	/**
	 * Initializes the internal path.
	 *
	 * @param __fs The Java filesystem creating this.
	 * @param __path The external path.
	 * @since 2023/08/20
	 */
	JavaPath(JavaFileSystem __fs, JavaPathBracket __path)
		throws NullPointerException
	{
		super(__fs);
		
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this._path = __path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean endsWith(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean endsWith(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	protected Path getInternalName(int __dx)
		throws IllegalArgumentException
	{
		return new JavaPath(this.fileSystem,
			JavaNioShelf.pathName(this._path, __dx));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	protected int getInternalNameCount()
	{
		return JavaNioShelf.pathNameCount(this._path);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	protected Path getInternalRoot()
	{
		// Obtain the root
		JavaPathBracket result = JavaNioShelf.pathRoot(this._path);
		
		// Do we return something?
		if (result == null)
			return null;
		return new JavaPath(this.fileSystem, result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	protected String internalString()
	{
		return JavaNioShelf.pathString(this._path);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Iterator<Path> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path normalize()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path relativize(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path resolveSibling(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path resolveSibling(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean startsWith(Path __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean startsWith(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path subpath(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path toAbsolutePath()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path toRealPath(LinkOption... __a)
		throws IOException
	{
		throw Debugging.todo();
	}
}
