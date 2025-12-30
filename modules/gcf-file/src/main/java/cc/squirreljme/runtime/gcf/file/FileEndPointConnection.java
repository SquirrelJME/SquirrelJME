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
import cc.squirreljme.runtime.gcf.file.real.SystemFileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.ConnectionClosedException;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.IllegalModeException;
import net.multiphasicapps.collections.UnmodifiableArrayList;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Base implementation for file connections.
 *
 * @since 2025/12/26
 */
@SquirrelJMEVendorApi
public final class FileEndPointConnection
	extends AbstractStreamConnection
	implements FileConnection
{
	/** Service loader for file endpoint handlers. */
	private static final ServiceLoader<FileEndPointFactory> _SERVICE =
		ServiceLoader.load(FileEndPointFactory.class);
	
	/** The directory listing file to part cache. */
	private final Map<String, UriGenericPart> _listing =
		new LinkedHashMap<>();
	
	/** The currently connected endpoint. */
	private volatile FileEndPoint _current;
	
	/**
	 * Initializes the base connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @throws IllegalArgumentException If the connection mode is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public FileEndPointConnection(
		@MagicConstant(flagsFromClass = Connector.class) int __mode)
		throws IllegalArgumentException, NullPointerException
	{
		super(__mode);
	}
	
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
		synchronized (this)
		{
			// Does the old endpoint need to be closed?
			FileEndPoint current = this._current;
			if (current != null)
				this.__currentClose();
		}
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
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
			
			// Not connected to any endpoint?
			FileEndPoint current = this.__current();
			if (current == null)
				throw new IOException("DISC");
			
			// Not a directory?
			if (!this.isDirectory())
				throw new IOException("NOPE");
			
			// Need to load the directory list?
			Map<String, UriGenericPart> listing = this._listing;
			if (listing.isEmpty())
				try
				{
					current.listDirectory(listing);
				}
				catch (IOException|RuntimeException __e)
				{
					// Invalidate the directory listing
					listing.clear();
					
					// Retoss
					throw __e;
				}
			
			// Only keep the keys from the listing
			contents = listing.keySet().toArray(new String[listing.size()]);
		}
		
		// Filter and wrap accordingly (because Enumeration is terrible)
		return new IteratorToEnumeration<>(
			new BasicGlobFilter(__filter,
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
	 * This is called to change the endpoint.
	 *
	 * @param __part The new part, if {@code null} then none is set.
	 * @return {@code this}.
	 * @throws IOException If the part could not be changed.
	 * @throws SecurityException If the operation was not permitted.
	 * @since 2025/12/28
	 */
	@SuppressWarnings("resource")
	@SquirrelJMEVendorApi
	Connection __changeEndPoint(UriGenericPart __part)
		throws ConnectionNotFoundException, IOException, SecurityException
	{
		synchronized (this)
		{
			// Does the old endpoint need to be closed?
			FileEndPoint current = this._current;
			if (current != null)
				this.__currentClose();
			
			// Setting a new endpoint?
			if (__part != null)
			{
				FileEndPoint endPoint = null;
				int mode = this.mode;
				
				// If there is no host, we know it is this system
				UriAuthority auth = __part.getAuthority();
				if (auth == null || auth.host() == null ||
					auth.host().isEmpty())
					endPoint = new SystemFileEndPoint(__part, mode);
				
				// Find the matching service, if non-default
				if (endPoint == null)
					for (FileEndPointFactory it :
						FileEndPointConnection._SERVICE)
						if (it.handleAuthority(auth))
						{
							endPoint = it.connect(__part, mode);
							break;
						}
				
				// Nothing found?
				/* {@squirreljme.error EC30 No endpoint was found that
				can handle the given authority. (The URI; The authority)} */
				if (endPoint == null)
					throw new ConnectionNotFoundException(
						__error__("EC30 %s", __part, auth));
				
				// Use this one
				this._current = endPoint;
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the currently attached endpoint.
	 *
	 * @return The current endpoint.
	 * @since 2025/12/30
	 */
	FileEndPoint __current()
	{
		synchronized (this)
		{
			return this._current;
		}
	}
	
	/**
	 * Closes the current endpoint.
	 *
	 * @throws IOException If it could not be closed.
	 * @since 2025/12/30
	 */
	private void __currentClose()
		throws IOException
	{
		synchronized (this)
		{
			FileEndPoint current = this._current;
			if (current != null)
			{
				// Clear
				this._current = null;
				
				// Invalidate the directory cache
				this._listing.clear();
				
				// Close the underlying endpoint
				current.close();
			}
		}
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
			FileEndPoint endPoint = this.__current();
			if (endPoint == null)
				return null;
			return endPoint.attachedAttributes();
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
			FileEndPoint endPoint = this.__current();
			if (endPoint == null)
				return null;
			return endPoint.attachedFileStore();
		}
	}
}
