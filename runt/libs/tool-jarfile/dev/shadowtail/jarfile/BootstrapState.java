// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.Constants;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.Minimizer;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This contains the state of the entire bootstrap, its memory and all of the
 * classes which may be loaded accordingly.
 *
 * @since 2019/09/14
 */
public final class BootstrapState
{
	/** The size of the static field area. */
	public static final int STATIC_FIELD_SIZE =
		8192;
	
	/** The initializer to use. */
	protected final Initializer initializer =
		new Initializer();
	
	/** Class information which has been loaded. */
	private final Map<ClassName, LoadedClassInfo> _classinfos =
		new LinkedHashMap<>();
	
	/** Reference to self. */
	private final Reference<BootstrapState> _selfref =
		new WeakReference<>(this);
	
	/** Static field pointer area. */
	private int _sfieldarea;
	
	/** Static field area next pointer. */
	private int _sfieldnext;
	
	/**
	 * Allocates static field space.
	 *
	 * @return The pointer to the static field area that was allocated.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2019/09/14
	 */
	public final int allocateStaticFieldSpace(int __sz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BC0e Cannot allocate zero or negative static
		// field space.}
		if (__sz <= 0)
			throw new IllegalArgumentException("BC0e");
		
		// Allocate area for static fields, if not done yet
		int sfieldarea = this.staticFieldAreaAddress();
		
		// Determine the space that is needed
		int sfieldnext = this._sfieldnext;
		int snext = sfieldnext + __sz;
		
		// {@squirreljme.error BC03 Ran out of static field space.}
		if (snext >= BootstrapState.STATIC_FIELD_SIZE)
			throw new RuntimeException("BC03");
			
		// Set next pointer area
		this._sfieldnext = snext;
		
		// Pointer is here
		return sfieldnext;
	}
	
	/**
	 * Allocates an array of integers and stores values.
	 *
	 * @param __m The modifier of the values to use.
	 * @param __v The values in the array.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int buildIntArray(Modifier __m, int... __v)
		throws NullPointerException
	{
		return this.buildIntArray(new ClassName("[I"), __m, __v);
	}
	
	/**
	 * Allocates an array of integers and stores values.
	 *
	 * @param __t The type to use.
	 * @param __m The modifier of the values to use.
	 * @param __v The values in the array.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int buildIntArray(String __t, Modifier __m, int... __v)
		throws NullPointerException
	{
		if (__t == null || __v == null)
			throw new NullPointerException("NARG");
		
		return this.buildIntArray(new ClassName(__t), __m, __v);
	}
	
	/**
	 * Allocates an array of integers and stores values.
	 *
	 * @param __t The type to use.
	 * @param __m The modifier of the values to use.
	 * @param __v The values in the array.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int buildIntArray(ClassName __t, Modifier __m, int... __v)
		throws NullPointerException
	{
		if (__t == null || __v == null)
			throw new NullPointerException("NARG");
		
		return this.finalizeIntArray(this.reserveIntArray(__v.length),
			__t, __m, __v);
	}
	
	/**
	 * Returns an info pointer for the given class names.
	 *
	 * @param __cls The class names to get info pointers for.
	 * @return The pointer to the class info pointer list.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final int classNamesInfoPointer(ClassNames __cls)
		throws NullPointerException
	{
		if (__cls == null)
			throw new NullPointerException("NARG");
		
		// Needed for allocations and writes
		Initializer initializer = this.initializer;
		
		// Fill in values for pointers
		int n = __cls.size();
		int[] values = new int[n];
		for (int i = 0; i < n; i++)
			values[i] = this.findClass(__cls.get(i)).infoPointer();
		
		// Return array of these pointers
		return this.buildIntArray("[I", Modifier.RAM_OFFSET, values);
	}
	
	/**
	 * Finalizes the integer array by writing values over it.
	 *
	 * @param __p The array pointer.
	 * @param __m The modifier of the values to use.
	 * @param __v The values in the array.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int finalizeIntArray(int __p, Modifier __m, int... __v)
		throws NullPointerException
	{
		return this.finalizeIntArray(__p, new ClassName("[I"), __m, __v);
	}
	
	/**
	 * Finalizes the integer array by writing values over it.
	 *
	 * @param __p The array pointer.
	 * @param __t The type to use.
	 * @param __m The modifier of the values to use.
	 * @param __v The values in the array.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int finalizeIntArray(int __p, ClassName __t, Modifier __m,
		int... __v)
		throws NullPointerException
	{
		if (__t == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Needed for allocations and writes
		Initializer initializer = this.initializer;
		
		// Force a default modifier
		if (__m == null)
			__m = Modifier.NONE;
		
		// The number of elements to store
		int n = __v.length;
		
		// Write details of object
		initializer.memWriteInt(Modifier.RAM_OFFSET,
			__p + Constants.OBJECT_CLASS_OFFSET,
			this.findClass(__t).infoPointer());
		initializer.memWriteInt(
			__p + Constants.OBJECT_COUNT_OFFSET,
			999999);
		initializer.memWriteInt(
			__p + Constants.ARRAY_LENGTH_OFFSET,
			n);
		
		// Write values in the array
		for (int i = 0, wp = __p + Constants.ARRAY_BASE_SIZE;
			i < n; i++, wp += 4)
			initializer.memWriteInt(__m, wp, __v[i]);
		
		return __p;
	}
	
	/**
	 * Finds the class which uses the given name.
	 *
	 * @param __cl The class name to find.
	 * @return The loaded class information.
	 * @throws InvalidClassFormatException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo findClass(String __cl)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this.findClass(new ClassName(__cl));
	}
	
	/**
	 * Finds the class which uses the given name.
	 *
	 * @param __cl The class name to find.
	 * @return The loaded class information.
	 * @throws InvalidClassFormatException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo findClass(ClassName __cl)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Locate pre-loaded class
		Map<ClassName, LoadedClassInfo> classinfos = this._classinfos;
		LoadedClassInfo rv = classinfos.get(__cl);
		if (rv == null)
		{
			// Load special primitive and array types magically!
			if (__cl.isPrimitive() || __cl.isArray())
				classinfos.put(__cl, (rv = new LoadedClassInfo(
					Minimizer.minimizeAndDecode(
						ClassFile.special(__cl.field())), 0, this._selfref)));
			
			// {@squirreljme.error BC0c Could not find the specified class.
			// (The class name)}
			else
				throw new InvalidClassFormatException("BC0c " + __cl);
		}
		
		return rv;
	}
	
	/**
	 * Loads the class file information.
	 *
	 * @param __b The class file data.
	 * @param __pos The position of the class file in ROM.
	 * @return The loaded class information
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo loadClassFile(byte[] __b, int __pos)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Decode it
		MinimizedClassFile cf = MinimizedClassFile.decode(__b);
		
		// Store loaded for later boot usage
		LoadedClassInfo rv;
		if (this._classinfos.put(cf.thisName(),
			(rv = new LoadedClassInfo(cf, __pos, this._selfref))) != null)
		{
			// {@squirreljme.error BC0b Class file has already been loaded.
			// (The class name)}
			throw new IllegalStateException("BC0b " + cf.thisName());
		}
		
		return rv;
	}
	
	/**
	 * Reserves an integer array that can fit the specified number of entries.
	 *
	 * @param __n The number of entries to reserve.
	 * @return The pointer to the integer array.
	 * @since 2019/09/16
	 */
	public final int reserveIntArray(int __n)
		throws NullPointerException
	{
		return this.initializer.allocate(
			Constants.ARRAY_BASE_SIZE + (4 * __n));
	}
	
	/**
	 * Returns the address of the static field area.
	 *
	 * @return The static field address area.
	 * @since 2019/09/14
	 */
	public final int staticFieldAreaAddress()
	{
		// Allocate area for static fields, if not done yet
		int sfieldarea = this._sfieldarea;
		if (sfieldarea <= 0)
			this._sfieldarea = (sfieldarea = this.initializer.allocate(
				BootstrapState.STATIC_FIELD_SIZE));
		
		return sfieldarea;
	}
}

