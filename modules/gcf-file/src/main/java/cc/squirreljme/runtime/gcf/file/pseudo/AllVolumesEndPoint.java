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
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.microedition.io.ConnectionNotFoundException;
import org.jetbrains.annotations.NotNull;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Listing of all possible filesystem volumes, including pseudo volumes.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class AllVolumesEndPoint
	extends FileEndPoint
{
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-all-volumes%3A%2F%2F%3F!";
	
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-all-volumes://?!";
	
	/** Attributes for the root directory. */
	@SquirrelJMEVendorApi
	public static final StaticFileAttributes ATTRIBUTES =
		new StaticFileAttributes(AbstractFileAttributes.IS_DIRECTORY |
			AbstractFileAttributes.IS_DOS_READ_ONLY |
			AbstractFileAttributes.IS_POSIX_USER_READ |
			AbstractFileAttributes.IS_POSIX_USER_EXECUTE |
			AbstractFileAttributes.IS_POSIX_GROUP_READ |
			AbstractFileAttributes.IS_POSIX_GROUP_EXECUTE |
			AbstractFileAttributes.IS_POSIX_OTHER_READ |
			AbstractFileAttributes.IS_POSIX_OTHER_EXECUTE, 0);
	
	/**
	 * Initializes the endpoint.
	 *
	 * @param __part The URI part.
	 * @param __mode The open mode.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	protected AllVolumesEndPoint(@NotNull UriGenericPart __part, int __mode)
		throws ConnectionNotFoundException, NullPointerException
	{
		super(__part, __mode);
		
		// Must always be the root component
		/* {@squirreljme.error GF0a All volume connection is only valid
		when there is only the root path specified.} */
		if (!"/".equals(__part.getPath()))
			throw new ConnectionNotFoundException(
				__error__("GF0a %s", __part));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/27
	 */
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		return AllVolumesEndPoint.ATTRIBUTES;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// No filestore is attached
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
		// This is purely a pseudo endpoint and has no resources open
		// on the disk in any way
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	protected String[] directoryListParts(boolean __includeHidden)
		throws IOException, SecurityException
	{
		List<String> rv = new ArrayList<>();
		
		
		return rv.toArray(new String[rv.size()]);
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
		
		// The parent directory for the all volumes root just points to here
		__into.put("..", this.part);
		
		// List all libraries
		for (JarPackageBracket library : JarPackageShelf.libraries())
		{
			// The ID is always used
			int id = JarPackageShelf.libraryId(library);
			
			// Is there a name for this? We just want the base name
			String baseName = JarPackageShelf.libraryPath(library);
			if (baseName != null && !baseName.isEmpty())
			{
				// Forward slash strip
				int ls = baseName.lastIndexOf('/');
				if (ls >= 0)
					baseName = baseName.substring(ls + 1);
				
				// Backslash strip
				ls = baseName.lastIndexOf('\\');
				if (ls >= 0)
					baseName = baseName.substring(ls + 1);
			}
			
			// Determine the base filename that is used
			String fileName;
			if (baseName == null || baseName.isEmpty())
				fileName = id + "/";
			else
				fileName = UriPart.encode(baseName) + "/";
			
			// Determine full URI connection to this item
			__into.put(fileName, new UriGenericPart(
				"//" + LibraryEndPoint.HOST + fileName));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		throw new IOException("ADIR");
	}
}
