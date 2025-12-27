// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.util.Enumeration;
import javax.microedition.io.file.FileConnection;

/**
 * Base implementation for file connections.
 *
 * @since 2025/12/26
 */
@SquirrelJMEVendorApi
public abstract class AbstractFileConnection
	implements FileConnection
{
	/**
	 * Returns the attached filesystem.
	 *
	 * @return The attached filesystem.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract FileSystem attachedFileSystem();
	
	@Override
	public final long availableSize()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean canRead()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean canWrite()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void create()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void delete()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final long directorySize(boolean __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean exists()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final long fileSize()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final String getName()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final String getPath()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final String getURL()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean isDirectory()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean isHidden()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final boolean isOpen()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final long lastModified()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final Enumeration list()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final Enumeration list(String __a, boolean __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void mkdir()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final DataInputStream openDataInputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final DataOutputStream openDataOutputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final InputStream openInputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final OutputStream openOutputStream(long __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final OutputStream openOutputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void rename(String __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void setFileConnection(String __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void setHidden(boolean __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void setReadable(boolean __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void setWritable(boolean __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final long totalSize()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void truncate(long __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public final long usedSize()
	{
		throw Debugging.todo();
	}
}
