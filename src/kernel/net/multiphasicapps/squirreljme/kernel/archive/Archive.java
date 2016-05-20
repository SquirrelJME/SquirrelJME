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
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.manifest.JavaManifest;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIException;

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
	/** Alternative lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * This is a flagging object which is used to indicate that a class does
	 * not exist in this archive.
	 */
	private static final __MissingClass__ _MISSING_CLASS =
		new __MissingClass__();
	
	/**
	 * This is a flagging object which indicates that a given class failed
	 * to load properly.
	 */
	private static final __MissingClass__ _ILLEGAL_CLASS =
		new __MissingClass__();
	
	/** Cache of loaded classes. */
	private final Map<ClassNameSymbol, Reference<NCIClass>> _cache =
		new WeakHashMap<>();
	
	/** The manifest cache. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** No manifest file? */
	private volatile boolean _nomanifest;
	
	/**
	 * If the class is not cached then this is called so that the archive
	 * may load and initialize the desired class interface which is used by
	 * the implementation of the kernel. The returned value may either be the
	 * standard class file representation or it could be a native binary.
	 *
	 * @param __n The name of the class to load.
	 * @return The newly initialized class interface or {@code null} if the
	 * class does not exist in this archive.
	 * @throws IOException On read errors.
	 * @since 2016/05/18
	 */
	protected abstract NCIClass internalLocateClass(ClassNameSymbol __n)
		throws IOException;
	
	/**
	 * Locates a resource of the given name, the name is absolute.
	 *
	 * @param __n The absolute name of the resource to locate.
	 * @return The resource or {@code null} if it does not exist.
	 * @throws IOException On read errors.
	 * @since 2016/05/18
	 */
	protected abstract InputStream internalLocateResource(String __n)
		throws IOException;
	
	/**
	 * Checks the cache to see if a given class was loaded, otherwise it
	 * will attempt to locate it.
	 *
	 * @param __n The class name to lookup.
	 * @return The cached class interface, or {@code null} if the class does
	 * not exist in this archive.
	 * @throws IllegalArgumentException If the specified class is an array.
	 * @throws IOException On read errors.
	 * @throws NCIException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public final NCIClass locateClass(ClassNameSymbol __n)
		throws IllegalArgumentException, IOException, NCIException,
			NullPointerException
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
			// Get reference
			Reference<NCIClass> ref = cache.get(__n);
			NCIClass rv;
			
			// Needs caching?
			Throwable t = null;
			if (ref == null || null == (rv = ref.get()))
			{
				// The class could be badly formatted
				try
				{
					// Attempt loading class
					rv = internalLocateClass(__n);
					
					// If not found, set as null
					if (rv == null)
						rv = _MISSING_CLASS;
				}
				
				// Make the class illegal
				catch (NCIException e)
				{
					// Remember the cause
					t = e;
					
					// Mark as illegal
					rv = _ILLEGAL_CLASS;
				}
				
				// Cache
				cache.put(__n, new WeakReference<>(rv));
			}
			
			// {@squirreljme.error AY0c The class is not valid.
			// (The name of the class)}
			if (rv == _ILLEGAL_CLASS)
				throw new IOException(String.format("AY0c %s", __n), t);
			
			// Missing?
			else if (rv == _MISSING_CLASS)
				return null;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Locates a resource of the given name, the name is absolute.
	 *
	 * @param __n The absolute name of the resource to locate.
	 * @return The resource or {@code null} if it does not exist.
	 * @since 2016/05/19
	 */
	public final InputStream locateResource(String __s)
		throws IOException
	{
		return internalLocateResource(__s);
	}
	
	/**
	 * Returns the manifest that is contained within this JAR or {@code null}
	 * if it does not have one.
	 *
	 * @return The manifest file or {@code null} if it does not contain one.
	 * @throws IOException If the manifest is badly formatted.
	 * @since 2016/05/20
	 */
	public final JavaManifest manifest()
		throws IOException
	{
		// No manifest?
		if (this._nomanifest)
			return null;
		
		// Lock
		synchronized (this.lock)
		{
			// Get
			Reference<JavaManifest> ref = this._manifest;
			JavaManifest rv;
			
			// Needs loading?
			if (ref == null || null == (rv = ref.get()))
				try (InputStream is = locateResource("META-INF/MANIFEST.MF"))
				{
					// Missing?
					if (is == null)
					{
						this._nomanifest = true;
						return null;
					}
					
					// Load it
					this._manifest = new WeakReference<>(
						(rv = new JavaManifest(is)));
				}
			
			// Return it
			return rv;
		}
	}
}

