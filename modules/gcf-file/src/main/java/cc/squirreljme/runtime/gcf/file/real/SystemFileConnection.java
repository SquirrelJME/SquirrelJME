// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.real;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.file.AbstractFileConnection;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A connection to a real system filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileConnection
	extends AbstractFileConnection
{
	/**
	 * Initializes the file system connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public SystemFileConnection(int __mode)
	{
		super(__mode);
	}
	
	@Override
	protected BasicFileAttributes attachedAttributes()
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
	
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		throw Debugging.todo();
	}
}
