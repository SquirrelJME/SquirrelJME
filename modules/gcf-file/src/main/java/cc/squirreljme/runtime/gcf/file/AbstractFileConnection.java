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
import cc.squirreljme.runtime.cldc.util.IteratorToEnumeration;
import cc.squirreljme.runtime.gcf.AbstractStreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.ConnectionClosedException;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.IllegalModeException;
import net.multiphasicapps.collections.UnmodifiableArrayList;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation for file connections.
 *
 * @since 2025/12/26
 */
@SquirrelJMEVendorApi
public abstract class AbstractFileConnection
	extends AbstractStreamConnection
	implements FileConnection
{
	/**
	 * Initializes the base connection.
	 *
	 * @param __part The URI part.
	 * @param __mode The mode this is opened in.
	 * @throws ConnectionNotFoundException If the part is not valid.
	 * @throws IllegalArgumentException If the connection mode is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected AbstractFileConnection(
		@NotNull String __part,
		@MagicConstant(valuesFromClass = Connector.class) int __mode)
		throws ConnectionNotFoundException, IllegalArgumentException,
			NullPointerException
	{
		super(__mode);
		
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// Set the connection to this path
		try
		{
			this.changeFullPart(__part);
		}
		catch (IOException __e)
		{
			throw new ConnectionNotFoundException(__e.getMessage());
		}
	}
	
	/**
	 * Returns the attributes which are currently attached.
	 *
	 * @return The attached attributes.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract BasicFileAttributes attachedAttributes()
		throws SecurityException;
	
	/**
	 * Returns the attached file store.
	 *
	 * @return The attached file store or {@code null} if not available.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract FileStore attachedFileStore()
		throws SecurityException;
	
	/**
	 * Returns the attached filesystem.
	 *
	 * @return The attached filesystem or {@code null} if not available.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract FileSystem attachedFileSystem()
		throws SecurityException;
	
	/**
	 * This is called before the full part is being changed.
	 *
	 * @param __part The new part.
	 * @throws IOException If the part could not be changed.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the operation was not permitted.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	protected abstract void changingFullPart(@NotNull String __part)
		throws IOException, NullPointerException, SecurityException;
	
	/**
	 * Returns the list of directory contents, all returned values are
	 * considered to be URI parts to be passed
	 * to {@link #changeFullPart(String)}.
	 *
	 * @param __includeHidden Should file that are hidden be included?
	 * @return The directory content listing, if the resultant string is a
	 * URI it should be treated as such.
	 * @throws IOException If the directory could not be listed or other
	 * read errors.
	 * @throws SecurityException If this operation is not permitted. 
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	protected abstract String[] directoryListParts(boolean __includeHidden)
		throws IOException, SecurityException;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final long availableSize()
		throws ConnectionClosedException, IllegalModeException,
			SecurityException
	{
		FileStore fs = this.__fileStore();
		if (fs == null)
			return -1;
		
		// This could potentially fail
		try
		{
			return fs.getUnallocatedSpace();
		}
		catch (IOException ignored)
		{
			return -1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected final void becomingClosed()
		throws IOException
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
	
	/**
	 * Sets the full file part.
	 *
	 * @param __part The part to change to.
	 * @throws IOException If it could not be changed.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the operation is not permitted.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	protected void changeFullPart(@NotNull String __part)
		throws IOException, NullPointerException, SecurityException
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final boolean isDirectory()
		throws ConnectionClosedException, IllegalModeException,
			SecurityException
	{
		BasicFileAttributes attrib = this.__fileAttributes();
		if (attrib == null)
			return false;
		
		return attrib.isDirectory();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final Enumeration list()
		throws ConnectionClosedException, IllegalModeException, IOException,
			SecurityException
	{
		return this.list("*", false);
	}
	
	@Override
	public final Enumeration list(@NotNull String __filter,
		boolean __includeHidden)
		throws ConnectionClosedException, IllegalModeException, IOException,
			SecurityException
	{
		String[] contents;
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Not a directory?
			if (!this.isDirectory())
				throw new IOException("NOPE"); 
			
			// Get list of contents
			contents = this.directoryListParts(__includeHidden);
			if (contents == null)
				contents = new String[0]; 
		}
		
		// Filter and wrap accordingly (because Enumeration is terrible)
		return new IteratorToEnumeration<String>(
			new __BasicGlobFilter__(__filter,
				UnmodifiableArrayList.of(contents).iterator()));
	}
	
	@Override
	public final void mkdir()
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
	
	/**
	 * Get and check file attributes.
	 *
	 * @return The file attributes.
	 * @since 2025/12/27
	 */
	private BasicFileAttributes __fileAttributes()
	{
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Are there attributes?
			return this.attachedAttributes();
		}
	}
	
	/**
	 * Get and check file store.
	 *
	 * @return The file store.
	 * @since 2025/12/27
	 */
	private FileStore __fileStore()
	{
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Is there a filestore?
			return this.attachedFileStore();
		}
	}
}
