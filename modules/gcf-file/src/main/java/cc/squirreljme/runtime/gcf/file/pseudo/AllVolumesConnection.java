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
import cc.squirreljme.runtime.gcf.file.AbstractFileConnection;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Listing of all possible filesystem volumes, including pseudo volumes.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class AllVolumesConnection
	extends AbstractFileConnection
{
	/**
	 * Initializes the all volumes connection.
	 *
	 * @param __mode The mode this is connected in.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public AllVolumesConnection(int __mode)
	{
		super(__mode);
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
	protected String[] directoryList(boolean __includeHidden)
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
