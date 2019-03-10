// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.mini.MinimizedClassFile;
import net.multiphasicapps.classfile.mini.Minimizer;

/**
 * This is a class which caches classes within class libraries, it is intended
 * to .
 *
 * @since 2019/03/09
 */
public final class CachingClassLibrary
{
	/** The library to wrap around. */
	protected final VMClassLibrary library;
	
	/** Loaded class files that have been cached. */
	protected final Map<String, MinimizedClassFile> _cache =
		new HashMap<>();
	
	/**
	 * Initializes the cached class library.
	 *
	 * @param __l The library to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/09
	 */
	public CachingClassLibrary(VMClassLibrary __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.library = __l;
	}
	
	/**
	 * Checks the cache if the given file as been loaded as a class and returns
	 * it, otherwise it is cached.
	 *
	 * @param __bn The binary name to cache as a class.
	 * @return The cached class file or {@code null} if it does not exist.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the class could not load properly.
	 * @since 2019/03/09
	 */
	public final MinimizedClassFile cacheClass(String __bn)
		throws NullPointerException, VMException
	{
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Check the cache
		Map<String, MinimizedClassFile> cache = this._cache;
		synchronized (this)
		{
			// Pre-cached to either exist or not exist, use that given status
			// to prevent multiple lookups of unknown classes
			MinimizedClassFile rv = cache.get(__bn);
			if (cache.containsKey(__bn))
				return rv;
			
			// Just going to look in our own library
			VMClassLibrary library = this.library;
			
			try
			{
				// Load already cached minimized class?
				if (rv == null)
					try (InputStream in = library.resourceAsStream(
						__bn + ".sjz"))
					{
						// Load it?
						if (in != null)
							rv = MinimizedClassFile.decode(in);
					}
				
				// Minimize plain class file?
				if (rv == null)
					try (InputStream in = library.resourceAsStream(
						__bn + ".class"))
					{
						if (in != null)
							rv = Minimizer.minimize(ClassFile.decode(in));
					}
			}
			catch (InvalidClassFormatException e)
			{
				// {@squirreljme.error AE05 The class is not formatted
				// correctly. (The class name)}
				throw new VMException("AE05 " + __bn, e);
			}
			catch (IOException e)
			{
				// {@squirreljme.error AE04 Read error trying to read
				// the class file. (The class)}
				throw new VMException("AE04 " + __bn, e);
			}
			
			// Cache it, even it null it will be cached as null
			cache.put(__bn, rv);
			return rv;
		}
	}
}

