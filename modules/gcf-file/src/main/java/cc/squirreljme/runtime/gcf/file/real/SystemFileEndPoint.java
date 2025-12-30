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
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.file.FileEndPointConnection;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * A connection to a real system filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileEndPoint
	extends FileEndPoint
{
	/**
	 * Initializes the file system connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @throws ConnectionNotFoundException If the part is not valid.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public SystemFileEndPoint(UriPart __part, int __mode)
		throws ConnectionNotFoundException
	{
		super(__part, __mode);
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
	
	@Override
	protected void changingFullPart(UriPart __part)
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Override
	protected String[] directoryListParts(boolean __includeHidden)
		throws IOException, SecurityException
	{
		throw Debugging.todo();
	}
}
