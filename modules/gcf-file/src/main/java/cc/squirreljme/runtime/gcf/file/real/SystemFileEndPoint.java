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
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A connection to a real system filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileEndPoint
	extends FileEndPoint
{
	public SystemFileEndPoint(@NotNull UriGenericPart __part, int __mode,
		@Nullable UriGenericPart __dotDot)
		throws NullPointerException
	{
		super(__part, __mode, __dotDot);
		
		throw Debugging.todo();
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
	
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
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
	public void close()
		throws IOException
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
