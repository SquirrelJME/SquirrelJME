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
import cc.squirreljme.runtime.cldc.full.attrib.AbstractFileAttributes;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.cldc.full.attrib.StaticFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Endpoint which is capable of reading ZIP files.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public class ZipEndPoint
	extends FileEndPoint
{
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-zip://?!";
	
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-zip%3A%2F%2F%3F!";
	
	/** Attributes for any directory. */
	@SquirrelJMEVendorApi
	public static final StaticFileAttributes ATTRIBUTES_DIRECTORY =
		new StaticFileAttributes(
			AbstractFileAttributes.IS_DIRECTORY |
			AbstractFileAttributes.IS_DOS_READ_ONLY |
			AbstractFileAttributes.IS_POSIX_USER_READ |
			AbstractFileAttributes.IS_POSIX_USER_EXECUTE |
			AbstractFileAttributes.IS_POSIX_GROUP_READ |
			AbstractFileAttributes.IS_POSIX_GROUP_EXECUTE |
			AbstractFileAttributes.IS_POSIX_OTHER_READ |
			AbstractFileAttributes.IS_POSIX_OTHER_EXECUTE, 0);
	
	/** Attributes for any file. */
	@SquirrelJMEVendorApi
	public static final StaticFileAttributes ATTRIBUTES_FILE =
		new StaticFileAttributes(
			AbstractFileAttributes.IS_DOS_READ_ONLY |
			AbstractFileAttributes.IS_POSIX_USER_READ |
			AbstractFileAttributes.IS_POSIX_GROUP_READ |
			AbstractFileAttributes.IS_POSIX_OTHER_READ, 0);
	
	/**
	 * Initializes the ZIP connection.
	 *
	 * @param __part The initial part.
	 * @param __mode The mode this is opened in.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected ZipEndPoint(@NotNull UriGenericPart __part, int __mode)
		throws NullPointerException
	{
		super(__part, __mode);
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected final ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		// This could be a directory or a file
		if (this.part.getPath().endsWith("/"))
			return ZipEndPoint.ATTRIBUTES_DIRECTORY;
		return ZipEndPoint.ATTRIBUTES_FILE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// No filesystem is ever attached
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		// No filesystem is attached
		return null;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
