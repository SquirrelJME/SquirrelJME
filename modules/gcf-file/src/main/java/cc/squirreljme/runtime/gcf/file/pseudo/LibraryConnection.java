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
import javax.microedition.io.ConnectionNotFoundException;
import org.jetbrains.annotations.NotNull;

/**
 * A connection to a specific library to browse its contents as if it were
 * a filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class LibraryConnection
	extends AbstractFileConnection
{
	/**
	 * Initializes the library connection.
	 *
	 * @param __part The initial part.
	 * @param __mode The mode this is opened in.
	 * @throws ConnectionNotFoundException If the part is not valid.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public LibraryConnection(String __part, int __mode)
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
	protected void changingFullPart(@NotNull String __part)
		throws IOException, NullPointerException, SecurityException
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
