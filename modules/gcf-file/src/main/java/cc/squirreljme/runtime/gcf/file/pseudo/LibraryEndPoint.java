// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.attrib.AbstractFileAttributes;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.cldc.full.attrib.StaticFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import javax.microedition.io.ConnectionNotFoundException;
import org.jetbrains.annotations.NotNull;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * A connection to a specific library to browse its contents as if it were
 * a filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class LibraryEndPoint
	extends FileEndPoint
{
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
	
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-library://?!";
	
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-library%3A%2F%2F%3F!";
	
	/** The Jar being accessed. */
	@SquirrelJMEVendorApi
	protected final JarPackageBracket jar;
	
	/**
	 * Initializes the library connection.
	 *
	 * @param __jar The Jar to access.
	 * @param __part The initial part.
	 * @param __mode The mode this is opened in.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public LibraryEndPoint(@NotNull JarPackageBracket __jar,
		UriGenericPart __part, int __mode)
		throws NullPointerException
	{
		super(__part, __mode);
		
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		// This could be a directory or a file
		if (this.part.getPath().endsWith("/"))
			return LibraryEndPoint.ATTRIBUTES_DIRECTORY;
		return LibraryEndPoint.ATTRIBUTES_FILE;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public void close()
		throws IOException
	{
		// This is purely a pseudo filesystem, additionally any streams opened
		// to a resource are done via JarPackageShelf and not this
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		if (__into == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
