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
	protected final Map<String, ClassFile> _cache =
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
	 * @param __fn The file name to cache as a class.
	 * @return The cached class file or {@code null} if it does not exist.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the class could not load properly.
	 * @since 2019/03/09
	 */
	public final ClassFile cacheClassFile(String __fn)
		throws NullPointerException, VMException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Check the cache
		Map<String, ClassFile> cache = this._cache;
		synchronized (this)
		{
			// Pre-cached to either exist or not exist, use that given status
			// to prevent multiple lookups of unknown classes
			ClassFile rv = cache.get(__fn);
			if (cache.containsKey(__fn))
				return rv;
			
			// Try loading the class
			try (InputStream in = this.library.resourceAsStream(__fn))
			{
				// Not found
				if (in == null)
				{
					cache.put(__fn, null);
					return null;
				}
				
				// Load it
				rv = ClassFile.decode(in);
			}
			catch (InvalidClassFormatException e)
			{
				// {@squirreljme.error AE05 The class is not formatted
				// correctly. (The class name)}
				throw new VMException("AE05 " + __fn, e);
			}
			catch (IOException e)
			{
				// {@squirreljme.error AE04 Read error trying to read
				// the class file. (The class)}
				throw new VMException("AE04 " + __fn, e);
			}
			
			// Cache it
			cache.put(__fn, rv);
			return rv;
		}
	}
}

