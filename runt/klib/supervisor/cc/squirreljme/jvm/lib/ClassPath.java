// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

/**
 * This represents a class path which contains multiple class libraries.
 *
 * @since 2019/09/22
 */
public final class ClassPath
{
	/** The shift for index access. */
	private static final int _INDEX_SHIFT =
		24;
	
	/** The mask for the index. */
	private static final int _INDEX_MASK =
		0x00FFFFFF;
	
	/** Mask for JAR index. */
	private static final int _JAR_MASK =
		0x7F000000;
	
	/** The classpath. */
	public final ClassLibrary[] classpath;
	
	/**
	 * Initializes the classpath.
	 *
	 * @param __cp The class path to use.
	 * @throws IllegalArgumentException If the number of class path libraries
	 * exceeds the absolute limit.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/22
	 */
	public ClassPath(ClassLibrary... __cp)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error SV0a Too many entries on the class path.}
		int n = __cp.length;
		if (n > (_JAR_MASK >>> _INDEX_SHIFT))
			throw new IllegalArgumentException("SV0a");
		
		// Defensive copy and check
		ClassLibrary[] classpath = new ClassLibrary[n];
		for (int i = 0; i < n; i++)
		{
			// Make sure this is not null!
			ClassLibrary cl = __cp[i];
			if (cl == null)
				throw new NullPointerException("NARG");
			
			classpath[i] = cl;
		}
		
		// Use this
		this.classpath = classpath;
	}
	
	/**
	 * Searches for the given class name resource for the given class
	 *
	 * @param __name The name of the class.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceClassFind(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return this.resourceFind(__name + ".class");
	}
	
	/**
	 * Returns the data pointer for the given resource.
	 *
	 * @param __dx The index to get the data pointer for.
	 * @return The data pointer of the resource.
	 * @throws IndexOutOfBoundsException If the index is not found in any
	 * library.
	 * @since 2019/07/11
	 */
	public final BinaryBlob resourceData(int __dx)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV08 Out of range resource.}
		int cpdx = __dx >>> _INDEX_SHIFT;
		ClassLibrary[] classpath = this.classpath;
		if (cpdx < 0 || cpdx >= classpath.length)
			throw new IndexOutOfBoundsException("SV08");
		
		// Get resource pointer from this
		return classpath[cpdx].resourceData(__dx & _INDEX_MASK);
	}
	
	/**
	 * Searches for the given resource in this client task.
	 *
	 * @param __name The name of the resource.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFind(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Scan the classpath
		ClassLibrary[] classpath = this.classpath;
		for (int i = 0, n = classpath.length; i < n; i++)
		{
			// Locate resource
			int rv = classpath[i].indexOf(__name);
			
			// Was found?
			if (rv >= 0)
				return (i << _INDEX_SHIFT) | rv;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Searches for the given resource in this client task in the given
	 * specified classpath library.
	 *
	 * @param __name The name of the resource.
	 * @param __in The class path library to look in.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFindIn(String __name, int __in)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Out of range values are always not found
		ClassLibrary[] classpath = this.classpath;
		if (__in < 0 || __in >= classpath.length)
			return -1;
		
		// Locate resource
		int rv = classpath[__in].indexOf(__name);
		
		// If it was found shift in
		if (rv >= 0)
			return (__in << _INDEX_SHIFT) | rv;
		
		// Otherwise does not exist
		return -1;
	}
	
	/**
	 * Searches for the given resource in this client task in the given
	 * specified classpath library, if it is not found in that library then
	 * all libraries on the classpath are searched.
	 *
	 * @param __name The name of the resource.
	 * @param __in The class path library to look in.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFindInOtherwise(String __name, int __in)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Search in this specific library first
		int rv = this.resourceFindIn(__name, __in);
		if (rv >= 0)
			return rv;
		
		// Then locate it in any class library
		return this.resourceFind(__name);
	}
	
	/** 
	 * Returns the JAR index of the given resource index.
	 *
	 * @param __rcid The resource index.
	 * @return The JAR index of the given resource.
	 * @since 2019/10/27
	 */
	public static final int resourceIndexToJarIndex(int __rcid)
	{
		return (__rcid & _JAR_MASK) >>> _INDEX_SHIFT;
	}
}

