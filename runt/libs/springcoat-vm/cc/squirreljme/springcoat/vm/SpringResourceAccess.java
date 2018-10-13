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
	/** The class loader for resources. */
	protected final SpringClassLoader classloader;
	
	/** Opened resources. */
	private final Map<Integer, InputStream> _streams =
		new HashMap<>();
	
	/** The next resource ID to use. */
	private volatile int _next;
	
	/**
	 * Initializes the resource access.
	 *
	 * @param __cl The class loader.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public SpringResourceAccess(SpringClassLoader __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classloader = __cl;
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
		
		// Locate the library first, if it is not found then we need not bother
		SpringClassLoader classloader = this.classloader;
		SpringClassLibrary lib = classloader.findLibrary(__jar);
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
}

