// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

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
		
		// {@squirreljme.error ZZqa Too many entries on the class path.}
		int n = __cp.length;
		if (n > (ClassPath._JAR_MASK >>> ClassPath._INDEX_SHIFT))
			throw new IllegalArgumentException("ZZqa");
		
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
	 * Returns the class library of the given index.
	 *
	 * @param __rcdx The class library to get the index of.
	 * @return The class library of the given index.
	 * @throws IndexOutOfBoundsException If the resource index is outside
	 * of bounds.
	 * @since 2019/11/17
	 */
	public final ClassLibrary classLibrary(int __rcdx)
		throws IndexOutOfBoundsException
	{
		return this.classpath[ClassPath.resourceIndexToJarIndex(__rcdx)];
	}
	
	/**
	 * Returns a class file parser for the given index.
	 *
	 * @param __rcdx The index to load.
	 * @return The class file parser.
	 * @throws IndexOutOfBoundsException If the resource is not valid.
	 * @since 2019/11/17
	 */
	public final ClassFileParser classParser(int __rcdx)
		throws IndexOutOfBoundsException
	{
		// We need to take the pools from the class library, if it has any
		ClassLibrary clib = this.classLibrary(__rcdx);
		
		return new ClassFileParser(this.resourceData(__rcdx),
			clib.splitPool(false), clib.splitPool(true));
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
		// {@squirreljme.error ZZq8 Out of range resource.}
		int cpdx = __dx >>> ClassPath._INDEX_SHIFT;
		ClassLibrary[] classpath = this.classpath;
		if (cpdx < 0 || cpdx >= classpath.length)
			throw new IndexOutOfBoundsException("ZZq8");
		
		// Get resource pointer from this
		return classpath[cpdx].resourceData(__dx & ClassPath._INDEX_MASK);
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
				return (i << ClassPath._INDEX_SHIFT) | rv;
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
			return (__in << ClassPath._INDEX_SHIFT) | rv;
		
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
		return (__rcid & ClassPath._JAR_MASK) >>> ClassPath._INDEX_SHIFT;
	}
}

