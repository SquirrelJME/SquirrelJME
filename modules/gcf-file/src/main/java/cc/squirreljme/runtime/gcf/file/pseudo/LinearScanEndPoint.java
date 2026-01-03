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
	
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
