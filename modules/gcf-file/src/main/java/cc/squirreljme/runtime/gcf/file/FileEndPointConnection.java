// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IteratorToEnumeration;
import cc.squirreljme.runtime.gcf.AbstractStreamConnection;
import cc.squirreljme.runtime.gcf.file.real.SystemFileEndPointFactory;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Deque;
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
	/** The standard file: factory. */
	private static final FileEndPointFactory _FILE =
		new SystemFileEndPointFactory();
	
	/** Service loader for file endpoint handlers. */
	private static final ServiceLoader<FileEndPointFactory> _SERVICE =
		ServiceLoader.load(FileEndPointFactory.class);
	
	/** The directory listing file to part cache. */
	private final Map<String, UriGenericPart> _listing =
		new LinkedHashMap<>();
	
	/** The directory stack, for improved dot-dot handling. */
	private final Deque<UriGenericPart> _stack =
		new ArrayDeque<>();
	
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
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	public final String getPath()
	{
		synchronized (this)
		{
			// If not connected, the path makes no sense
			FileEndPoint current = this.__current();
			if (current == null)
				return null;
			
			// Otherwise it is based on the URI itself
			return current.part.getPath();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public final String getURL()
	{
		synchronized (this)
		{
			FileEndPoint current = this.__current();
			if (current == null)
				return "file:";
			return "file:" + current.part;
		}
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
		FileEndPoint current = this.__current();
		if (current == null)
			return false;
		
		// Is this considered a directory?
		return current.isDirectory();
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
				throw new IOException("FILE");
			
			// Need to load the directory list?
			Map<String, UriGenericPart> listing = this.__listing(false);
			
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
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	public final InputStream openInputStream()
		throws IllegalModeException, IOException
	{
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Cannot open a stream for a directory, unless it is the root
			// filesystem which allows for block level access
			if (this.isDirectory() && !"/".equals(this.getPath()))
				throw new IOException("ADIR");
			
			// Forward to the endpoint
			FileEndPoint current = this.__current();
			if (current == null)
				throw new IOException("DISC");
			return current.openInputStream();
		}
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public final void setFileConnection(String __fileName)
		throws ConnectionClosedException, IllegalArgumentException,
			IOException, NullPointerException, SecurityException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Need to load the directory list?
			UriGenericPart part = null;
			Map<String, UriGenericPart> listing = this.__listing(false);
			
			// Going to the parent directory?
			// Try to load the parent from the stack first
			Deque<UriGenericPart> stack = this._stack;
			if ("..".equals(__fileName))
			{
				// Top of the stack is the current item, so drop it
				stack.pollLast();
				
				// This would be the parent directory
				part = stack.pollLast();
			}
			
			// Load from the listing
			if (part == null)
				part = listing.get(__fileName);
			
			/* {@squirreljme.error EC34 The specified file does not
			exist in the current directory. (The file; The base URL)} */
			if (part == null)
				throw new IllegalArgumentException(
					__error__("EC34 %s", __fileName, this.getURL()));
			
			// Only .. is valid on files, otherwise there would be no way
			// to escape them
			if (!this.isDirectory() && !"..".equals(__fileName))
				throw new IOException("FILE");
			
			// Use the top of the stack as the return point
			UriGenericPart dotDot = stack.peekLast();
			
			// Change to it since it is valid
			this.__changeEndPoint(part, dotDot);
		}
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
	 * @param __dotDot The dot-dot path to use, may be {@code null}.
	 * @return {@code this}.
	 * @throws IOException If the part could not be changed.
	 * @throws SecurityException If the operation was not permitted.
	 * @since 2025/12/28
	 */
	@SuppressWarnings("resource")
	@KeepWhenCompacting
	FileEndPointConnection __changeEndPoint(UriGenericPart __part,
		UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException, SecurityException
	{
		synchronized (this)
		{
			// Does the old endpoint need to be closed?
			FileEndPoint current = this._current;
			if (current != null)
				this.__currentClose();
			
			// No new endpoint is set?
			if (__part == null)
				return this;
			
			// Setting a new endpoint?
			FileEndPoint endPoint = null;
			int mode = this.mode;
			
			// Determine the factory to use
			FileEndPointFactory factory = null;
			
			// If there is no host, we know it is this system factory
			UriAuthority auth = __part.getAuthority();
			if (auth == null || auth.host() == null ||
				auth.host().isEmpty())
				factory = FileEndPointConnection._FILE;
			
			// Otherwise, find one via the service loader
			else
				for (FileEndPointFactory it : FileEndPointConnection._SERVICE)
					if (it.handleAuthority(auth))
					{
						factory = it;
						break;
					}
			
			// Nothing found?
			/* {@squirreljme.error EC30 No endpoint was found that
			can handle the given authority. (The URI; The authority)} */
			if (factory == null)
				throw new ConnectionNotFoundException(
					__error__("EC30 %s", __part, auth));
			
			// Connect to the endpoint, use the old dot-dot to return to the
			// previous point, overriding whatever was here
			endPoint = factory.connect(__part, mode, __dotDot);
			if (endPoint == null)
				throw Debugging.oops();
			
			// Use this one
			this._current = endPoint;
			
			// No exceptions were thrown so now we are at a valid point, so
			// push to the stack
			this._stack.offerLast(__part);
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
	
	/**
	 * Fills the directory listing if needed and then returns it.
	 *
	 * @param __drop Should the cache be dropped?
	 * @return The resultant listing.
	 * @throws IOException On read errors.
	 * @since 2025/12/30
	 */
	private Map<String, UriGenericPart> __listing(boolean __drop)
		throws IOException
	{
		Map<String, UriGenericPart> listing = this._listing;
		synchronized (this)
		{
			this.checkClosed();
			this.checkRead();
			
			// Drop the cache?
			if (__drop)
				listing.clear();
			
			// Needs to be filled?
			FileEndPoint current = this.__current();
			if (current != null && listing.isEmpty())
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
		}
		
		return listing;
	}
}
