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
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
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
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-library%3A%2F%2F%3F!";
	
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-library://?!";
	
	/** The Jar being accessed. */
	@SquirrelJMEVendorApi
	protected final JarPackageBracket jar;
	
	/**
	 * Initializes the library connection.
	 *
	 * @param __part The initial part.
	 * @param __mode The mode this is opened in.
	 * @throws ConnectionNotFoundException If the part is not valid.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public LibraryEndPoint(UriGenericPart __part, int __mode)
		throws ConnectionNotFoundException
	{
		super(__part, __mode);
		
		/* {@squirreljme.error GF04 Library connection has no host.
		(The URI)} */
		UriAuthority auth = __part.getAuthority();
		String desireName = (auth == null ? null : auth.host());
		if (auth == null || desireName == null || desireName.isEmpty())
			throw new ConnectionNotFoundException(
				__error__("GF04 %s", __part));
		
		// Match by ID?
		int desireId = -1;
		try
		{
			desireId = Integer.parseInt(desireName, 10);
		}
		catch (NumberFormatException ignored)
		{
		}
		
		// Go through libraries to determine which one to actually use
		JarPackageBracket jar = null;
		JarPackageBracket[] libs = JarPackageShelf.libraries();
		for (int n = libs.length, i = 0; i < n; i++)
		{
			// Jar happens to be blank? Skip
			JarPackageBracket lib = libs[i];
			if (lib == null)
				continue;
			
			// Matches this ID?
			if (i == desireId)
			{
				jar = lib;
				break;
			}
			
			// If the Jar has no known path, skip
			String path = JarPackageShelf.libraryPath(lib);
			if (path == null)
				continue;
			
			// Find the last slash for the basename, if applicable
			int ls = Math.max(path.lastIndexOf('/'),
				path.lastIndexOf('\\'));
			if (ls >= 0)
				path = path.substring(ls + 1);
			
			// Is this the Jar?
			if (path.equals(desireName))
			{
				jar = lib;
				break;
			}
		}
		
		/* {@squirreljme.error GF05 Could not find the specified Jar.
		(The URI)} */
		if (jar == null)
			throw new ConnectionNotFoundException(
				__error__("GF05 %s", __part));
		
		this.jar = jar;
	}
	
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		throw Debugging.todo();
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
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	protected String[] directoryListParts(boolean __includeHidden)
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
