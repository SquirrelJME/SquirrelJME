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
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * Listing of all possible filesystem volumes, including pseudo volumes.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class AllVolumesEndPoint
	extends FileEndPoint
{
	/**
	 * Initializes the all volumes connection.
	 *
	 * @param __part The path part.
	 * @param __mode The mode this is connected in.
	 * @throws ConnectionNotFoundException If the part is not valid.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public AllVolumesEndPoint(UriPart __part, int __mode)
		throws ConnectionNotFoundException
	{
		super(__part, __mode);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected BasicFileAttributes attachedAttributes()
		throws SecurityException
	{
		throw Debugging.todo();
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
	
	@Override
	protected void changingFullPart(UriPart __part)
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	protected String[] directoryListParts(boolean __includeHidden)
		throws IOException, SecurityException
	{
		List<String> rv = new ArrayList<>();
		
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
			
			// Add it, refer to just by ID or its base name
			if (baseName == null || baseName.isEmpty())
				rv.add(String.format("x-squirreljme-library://%d/",
					id));
			else
				rv.add(String.format("x-squirreljme-library://%s/",
					UriPart.encode(baseName)));
		}
		
		return rv.toArray(new String[rv.size()]);
	}
}
