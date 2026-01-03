// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import javax.microedition.io.Connection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a file end point that scans through a file to detect magic numbers
 * and provides access to those regions as files. This is so that certain
 * types of files such as DoJa Scratchpads can be accessed via the media
 * player despite not a file structure.
 * 
 * Note that while this works, it is not the most efficient means of accessing
 * the contents of arbitrary blocks of data.
 *
 * @since 2026/01/02
 */
@SquirrelJMEVendorApi
public class LinearScanEndPoint
	extends FileEndPoint
{
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-linear-scan%3A%2F%2F%3F!";
	
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-linear-scan://?!";
	
	/** The connection to wrap. */
	@SquirrelJMEVendorApi
	protected final Connection wrapped;
	
	/** The scanned contents and magic numbers. */
	private volatile String[] _contents;
	
	/**
	 * Initializes the endpoint.
	 *
	 * @param __part The URI part of the endpoint.
	 * @param __mode The mode the endpoint is opened in.
	 * @param __wrapped The wrapped connection.
	 * @param __dotDot The optional parent directory to return to.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/02
	 */
	@SquirrelJMEVendorApi
	public LinearScanEndPoint(@NotNull UriGenericPart __part, int __mode,
		Connection __wrapped, @Nullable UriGenericPart __dotDot)
		throws NullPointerException
	{
		super(__part, __mode, __dotDot);
		
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __wrapped;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		if (this.isDirectory())
			return PseudoAttributes.DIRECTORY;
		return PseudoAttributes.FILE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// Not attached to a filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		// Not attached to a filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the wrapped connection
		this.wrapped.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		if (__into == null)
			throw new NullPointerException("NARG");
		
		// Only root is valid
		UriGenericPart part = this.part;
		if (!"/".equals(part.getPath()))
			throw new IOException("FILE");
		
		// Have the contents already been determined?
		String[] contents;
		synchronized (this)
		{
			contents = this._contents;
		}
		
		// Only add dot-dot if it is known
		UriGenericPart dotDot = this.dotDot;
		if (dotDot != null)
			__into.put("..", dotDot);
		
		// Are there contents cached?
		if (contents != null)
		{
			// Load in contents
			for (String item : contents)
				__into.put(item, part.withPath(item));
			
			// No more processing is needed
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
