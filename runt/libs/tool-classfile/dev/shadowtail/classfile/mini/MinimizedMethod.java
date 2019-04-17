// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;

/**
 * This represents a method which has been mimized.
 *
 * @since 2019/03/14
 */
public final class MinimizedMethod
{
	/** Flags that are used for the method. */
	public final int flags;
	
	/** The index of this method in the instance table. */
	public final int index;
	
	/** The name of the method. */
	public final MethodName name;
	
	/** The type of the method. */
	public final MethodDescriptor type;
	
	/** Translated method code. */
	final byte[] _code;
	
	/** Line number table. */
	final byte[] _lines;
	
	/**
	 * Initializes the minimized method.
	 *
	 * @param __f The method flags.
	 * @param __o Index in the method table for instances.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @param __tc Transcoded instructions.
	 * @param __ln Line number table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/14
	 */
	public MinimizedMethod(int __f, int __o,
		MethodName __n, MethodDescriptor __t, byte[] __tc, byte[] __ln)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.flags = __f;
		this.index = __o;
		this.name = __n;
		this.type = __t;
		this._code = (__tc == null ? new byte[0] : __tc.clone());
		this._lines = (__ln == null ? new byte[0] : __ln.clone());
	}
	
	/**
	 * Decodes the method data.
	 *
	 * @param __n The number of methods.
	 * @param __pool The constant pool.
	 * @param __b Input data.
	 * @param __o Offset into array.
	 * @param __l Length of data rea.
	 * @return The decoded methods.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws InvalidClassFormatException If the methods could not be decoded.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public static final MinimizedMethod[] decodeMethods(int __n,
		MinimizedPool __pool, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, InvalidClassFormatException,
			NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
}

