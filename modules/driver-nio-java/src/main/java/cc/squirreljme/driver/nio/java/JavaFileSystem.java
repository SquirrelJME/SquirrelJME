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
import cc.squirreljme.driver.nio.java.shelf.JavaNioShelf;
import cc.squirreljme.driver.nio.java.shelf.JavaPathBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.AbstractFileSystem;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileStore;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
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
		// Does nothing, cannot be closed
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	protected int compare(Path __a, Path __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/23
	 */
	@Override
	public void createDirectory(Path __path, FileAttribute<?>... __attribs)
		throws IOException, NullPointerException, UnsupportedOperationException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
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
	protected Path getPath(String __path)
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
			bracket = JavaNioShelf.fsPath(__path, failSegment);
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
		return new JavaPath(this, bracket);
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
		return JavaNioShelf.fsSeparator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public boolean isOpen()
	{
		// Filesystem is always considered open
		return true;
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
		
		/* {@squirreljme.error ZY07 The attribute class is not supported.} */
		if (__attributeType != BasicFileAttributes.class &&
			__attributeType != JavaFileAttributes.class)
			throw new UnsupportedOperationException("ZY07");
		
		// Do not follow symlinks?
		boolean noFollow = false;
		if (__linkOptions != null)
			for (LinkOption linkOption : __linkOptions)
				if (linkOption == LinkOption.NOFOLLOW_LINKS)
				{
					noFollow = true;
					break;
				}
		
		// Read in attribute
		JavaFileAttributesBracket attrib;
		try
		{
			// Read in attributes
			attrib = JavaNioShelf.fsReadAttributes(
				((JavaPath)__path)._path, noFollow);
			
			/* {@squirreljme.error ZY08 The specified file does not exist.
			(The file) */
			if (attrib == null)
				throw new NoSuchFileException("ZY08 " + __path);
		}
		catch (MLECallError __e)
		{
			/* {@squirreljme.error ZY09 Could not read file attributes. */
			throw new IOException("ZY09", __e);
		}
		
		// Wrap attributes
		return __attributeType.cast(new JavaBasicFileAttributes(attrib));
	}
}
