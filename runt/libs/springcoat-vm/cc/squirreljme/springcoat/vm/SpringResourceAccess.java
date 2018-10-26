// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages access to resources within the virtual machine.
 *
 * @since 2018/10/07
 */
public final class SpringResourceAccess
{
	/** The manager for suites. */
	protected final SpringSuiteManager suites;
	
	/** Opened resources. */
	private final Map<Integer, InputStream> _streams =
		new HashMap<>();
	
	/** The next resource ID to use. */
	private volatile int _next;
	
	/**
	 * Initializes the resource access.
	 *
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public SpringResourceAccess(SpringSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
	}
	
	/**
	 * Closes the open resource.
	 *
	 * @param __fd The file descriptor.
	 * @return Zero on success or a negative value on failure.
	 * @since 2018/10/13
	 */
	public int close(int __fd)
	{
		// Locate the stream to close
		InputStream in;
		Map<Integer, InputStream> streams = this._streams;
		synchronized (streams)
		{
			in = streams.get(__fd);
			
			// Remove it
			if (in != null)
				streams.remove(__fd);
		}
		
		// No stream was found, so fail
		if (in == null)
			return -2;
		
		// Close it
		try
		{
			in.close();
			return 0;
		}
		catch (IOException e)
		{
			return -3;
		}
	}
	
	/**
	 * Opens the given resource in the given JAR.
	 *
	 * @param __jar The source JAR.
	 * @param __rc The resource to load.
	 * @return The resource file descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public int open(String __jar, String __rc)
		throws NullPointerException
	{
		if (__jar == null || __rc == null)
			throw new NullPointerException("NARG");
		
		// Load the library to access its resources
		SpringClassLibrary lib = this.suites.loadLibrary(__jar);
		if (lib == null)
			return -2;
		
		// Open input stream, if it even exists
		InputStream rv;
		try
		{
			rv = lib.resourceAsStream(__rc);
			
			// Debug
			todo.DEBUG.note("rAS(%s, %s) = %b", __jar, __rc, rv != null);
			
			if (rv == null)
				return -1;
		}
		catch (IOException e)
		{
			return -3;
		}
		
		// It does exist so it needs to be registered
		int id;
		Map<Integer, InputStream> streams = this._streams;
		synchronized (streams)
		{
			streams.put((id = ++this._next), rv);
		}
		
		// Only the ID is used
		return id;
	}
	
	/**
	 * Reads from the 
	 *
	 * @param __fd The file descriptor to read from.
	 * @param __b The output bytes.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The number of bytes read or a negative status code.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public int read(int __fd, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Locate the stream to read from
		InputStream in;
		Map<Integer, InputStream> streams = this._streams;
		synchronized (streams)
		{
			in = streams.get(__fd);
		}
		
		// No stream was found, so fail
		if (in == null)
			return -2;
		
		// Do the read
		try
		{
			int rv = in.read(__b, __o, __l);
			
			// Normalize the return value because negative values mean special
			// values, while we just want EOF
			if (rv < 0)
				return -1;
			return rv;
		}
		
		// Failed so just pass that some exception happened
		catch (IOException e)
		{
			return -3;
		}
	}
}

