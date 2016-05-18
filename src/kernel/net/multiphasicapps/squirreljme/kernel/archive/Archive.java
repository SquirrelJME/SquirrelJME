// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.classinterface.NCIClass;

/**
 * This is the base class for providers which provide archive views of JAR
 * files, native executables, and potentially other details.
 *
 * The base class keeps an archive of {@link NCIClass} for classes.
 *
 * @since 2016/05/18
 */
public abstract class Archive
{
	/**
	 * This is a flagging object which is used to indicate that a class does
	 * not exist in this archive.
	 */
	private static final __MissingClass__ _MISSING_CLASS =
		new __MissingClass__();
	
	/** Cache of loaded classes. */
	private final Map<ClassNameSymbol, Reference<NCIClass>> _cache =
		new WeakHashMap<>();
	
	/**
	 * If the class is not cached then this is called so that the archive
	 * may load and initialize the desired class interface which is used by
	 * the implementation of the kernel. The returned value may either be the
	 * standard class file representation or it could be a native binary.
	 *
	 * @param __n The name of the class to load.
	 * @return The newly initialized class interface or {@code null} if the
	 * class does not exist in this archive.
	 * @since 2016/05/18
	 */
	protected abstract NCIClass internalLocateClass(ClassNameSymbol __n);
	
	/**
	 * Locates a resource of the given name, the name is absolute.
	 *
	 * @param __n The absolute name of the resource to locate.
	 * @return The resource or {@code null} if it does not exist.
	 * @since 2016/05/18
	 */
	protected abstract InputStream locateResource(String __n);
	
	/**
	 * Checks the cache to see if a given class was loaded, otherwise it
	 * will attempt to locate it.
	 *
	 * @param __n The class name to lookup.
	 * @return The cached class interface, or {@code null} if the class does
	 * not exist in this archive.
	 * @throws IllegalArgumentException If the specified class is an array
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public final NCIClass locateClass(ClassNameSymbol __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0b Cannot locate a class which is a primitive
		// or an array type. (The class to locate)}
		if (__n.isPrimitive() || __n.isArray())
			throw new IllegalArgumentException(String.format("AY0b %s", __n));
		
		// Lock
		Map<ClassNameSymbol, Reference<NCIClass>> cache = this._cache;
		synchronized (cache)
		{
			throw new Error("TODO");
		}
	}
}

