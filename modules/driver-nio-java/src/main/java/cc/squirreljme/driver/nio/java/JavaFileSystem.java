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
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.AbstractFileSystem;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileStore;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

/**
 * Provides support for access to Java based filesystems.
 *
 * @since 2023/08/20
 */
public class JavaFileSystem
	extends AbstractFileSystem
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Iterable<FileStore> getFileStores()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Path getPath(String __path)
		throws InvalidPathException, NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// The failing segment, if applicable
		int[] failSegment = new int[]{-1};
		
		// Get the native path
		JavaPathBracket bracket;
		try
		{
			bracket = JavaNioShelf.getPath(__path, failSegment);
		}
		catch (MLECallError __e)
		{
			/* {@squirreljme.error NU01 Invalid path requested.} */
			InvalidPathException t = new InvalidPathException(__path, "NU01",
				failSegment[0]);
			
			t.initCause(__e);
			
			throw t;
		}
		
		// Wrap it
		return new JavaPath(bracket);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Iterable<Path> getRootDirectories()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	protected String getSeparatorInternal()
	{
		return JavaNioShelf.getSeparator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean isOpen()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean isReadOnly()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public FileChannel open(Path __path, Set<? extends OpenOption> __options,
		FileAttribute<?>... __attribs)
		throws IllegalArgumentException, IOException, SecurityException,
		UnsupportedOperationException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path __path,
		Class<A> __attributeType, LinkOption... __linkOptions)
		throws IOException, SecurityException, UnsupportedOperationException
	{
		if (__path == null || __attributeType == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Set<String> supportedFileAttributeViews()
	{
		throw Debugging.todo();
	}
}
