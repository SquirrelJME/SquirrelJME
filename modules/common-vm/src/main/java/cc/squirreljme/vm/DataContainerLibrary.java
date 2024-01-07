// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.jvm.launch.ApplicationParser;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Represents a plain data container for detected types which are not JARs or
 * SQCs.
 *
 * @since 2021/06/13
 */
public class DataContainerLibrary
	implements RawVMClassLibrary
{
	/** Data resource name. */
	public static final String RESOURCE_NAME =
		ApplicationParser.DATA_RESOURCE;
	
	/** The path to the ROM. */
	protected final Path path;
	
	/**
	 * Initializes the ROM library.
	 * 
	 * @param __path The ROM path.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/06/13
	 */
	public DataContainerLibrary(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * Opens a stream to the contained data.
	 * 
	 * @return The stream to the data.
	 * @throws IOException If it could not be opened.
	 * @since 2021/09/04
	 */
	public final InputStream asStream()
		throws IOException
	{
		return this.resourceAsStream(DataContainerLibrary.RESOURCE_NAME);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String[] listResources()
	{
		// There is only ever a single resource
		return new String[]{DataContainerLibrary.RESOURCE_NAME};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String name()
	{
		return this.path.getFileName().toString();
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
	 * @since 2023/12/30
	 */
	@Override
	public void rawData(int __jarOffset, byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
		NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Check that the size is correct.
		int bufLen = __b.length;
		int libLen = this.rawSize();
		if (__jarOffset < 0 || (__jarOffset + __l) < 0 ||
			(__jarOffset + __l) > libLen || __o < 0 || __l < 0 ||
			(__o + __l) < 0 || (__o + __l) > bufLen)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Seek through and find the data
		try (InputStream in = Files.newInputStream(this.path,
			StandardOpenOption.READ))
		{
			// Seek first, stop if EOF is hit
			for (int at = 0; at < __jarOffset; at++)
				if (in.read() < 0)
					throw new IllegalStateException("FEOF");
			
			// Do a standard read here
			if (in.read(__b, __o, __l) != __l)
				throw new IllegalStateException("SHRT");
		}
		catch (IOException __e)
		{
			throw new IllegalStateException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/30
	 */
	@Override
	public int rawSize()
		throws IllegalStateException
	{
		try
		{
			return (int)Math.min(Integer.MAX_VALUE, Files.size(this.path));
		}
		catch (IOException __e)
		{
			throw new IllegalStateException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		// Not our data?
		if (!DataContainerLibrary.RESOURCE_NAME.equals(__rc))
			return null;
		
		return Files.newInputStream(this.path, StandardOpenOption.READ);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public final String toString()
	{
		return this.path.toString();
	}
}
