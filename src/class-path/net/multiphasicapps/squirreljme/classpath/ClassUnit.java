// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.descriptors.ClassLoaderNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.manifest.JavaManifest;
import net.multiphasicapps.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIClass;

/**
 * This is a class unit which is a collection of classes and resources such as
 * those contained within a single JAR.
 *
 * @since 2016/05/25
 */
public abstract class ClassUnit
	implements Comparable<ClassUnit>
{
	/** The cached manifest. */
	private volatile Reference<JavaManifest> _manifest;
	
	/**
	 * Compares the key of the class unit to the given key.
	 *
	 * @param __k The key to compare against.
	 * @return The result of comparison.
	 * @throws NullPointerException If the key is null.
	 * @since 2016/05/29
	 */
	public abstract int compareTo(String __k)
		throws NullPointerException;
	
	/**
	 * Locates a class using the given class name in this unit and possibly
	 * returns it if this unit has such a class available.
	 *
	 * @param __cns The class name symbol to locate a class for, or
	 * {@code null} if it is not available in this unit.
	 * @return The class associated with the given class name.
	 * @throws CIException If the class could not be loaded.
	 * @since 2016/05/27
	 */
	public abstract CIClass locateClass(ClassNameSymbol __cns)
		throws CIException;
	
	/**
	 * Locates a resource using the given absolute name.
	 *
	 * @param __res The absolute name of the resource to find, this must not
	 * start with a forward slash.
	 * @return The input stream which is associated with the given resource or
	 * {@code null} if it was not found.
	 * @since 2016/05/28
	 */
	public abstract InputStream locateResource(String __res);
	
	/**
	 * Returns the name of the JAR file which is used for the given class
	 * unit.
	 *
	 * @return The class unit key.
	 * @since 2016/05/26
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Compares the key of this class unit to the key of the other.
	 *
	 * @param __cu The class unit to obtain a key from and then compare
	 * against.
	 * @return The result of comparison.
	 * @throws NullPointerException On null arugments.
	 * @since 2016/05/29 
	 */
	@Override
	public final int compareTo(ClassUnit __cu)
		throws NullPointerException
	{
		// Check
		if (__cu == null)
			throw new NullPointerException("NARG");
		
		return compareTo(__cu.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Compare with string
		if (__o instanceof String)
			return 0 == compareTo((String)__o);
		
		// Otherwise only this object is used.
		return __o == this;
	}
	
	/**
	 * Returns the name of the main class.
	 *
	 * @return The main class name.
	 * @since 2016/05/31
	 */
	public final ClassNameSymbol mainClass()
	{
		// Could fail
		try
		{
			// Load the manifest
			JavaManifest man = manifest();
			if (man == null)
				return null;
			
			// Get main attributes
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Get the main class
			String mval = attr.get("Main-Class");
			if (mval == null)
				return null;
			
			// Obtain it
			return ClassLoaderNameSymbol.of(mval).asClassName();
		}
		
		// Failed to read the manifest, treat as no main
		catch (IOException e)
		{
			return null;
		}
	}
	
	/**
	 * Returns the manifest for this class unit.
	 *
	 * @return The manifest for the class unit or {@code null} if not found.
	 * @throws IOException If the manifest was found, however it could not be
	 * read properly.
	 * @since 2016/05/31
	 */
	public final JavaManifest manifest()
		throws IOException
	{
		// Get ref
		Reference<JavaManifest> ref = _manifest;
		JavaManifest rv;
		
		// Needs loading?
		if (ref == null || null == (rv = ref.get()))
			try (InputStream is = locateResource("META-INF/MANIFEST.MF"))
			{
				// Not found
				if (is == null)
					return null;
				
				// Load it
				rv = new JavaManifest(is);
				
				// Cache it
				_manifest = new WeakReference<>(rv);
			}
		
		// Return it
		return rv;
	}
	
	/**
	 * This represents the type of class unit that this provides.
	 *
	 * @since 2016/05/26
	 */
	public static enum Type
	{
		/** Unknown. */
		UNKNOWN,
		
		/** A standard Java console application. */
		CONSOLE,
		
		/** A LIBlet. */
		LIBRARY,
		
		/** A MIDlet. */
		APPLICATION,
		
		/** Multiple types. */
		MULTIPLE,
		
		/** End. */
		;
	}
}

