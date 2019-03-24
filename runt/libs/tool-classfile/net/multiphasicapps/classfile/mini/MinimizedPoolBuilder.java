// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.register.AccessedField;
import net.multiphasicapps.classfile.register.InvokedMethod;

/**
 * This class is used to build the constant pool for a minimized class.
 *
 * @since 2019/03/11
 */
public final class MinimizedPoolBuilder
{
	/** Constant pool. */
	private final Map<Object, Integer> _pool =
		new LinkedHashMap<>();
	
	/** Parts list. */
	private final List<int[]> _parts =
		new ArrayList<>();
	
	/**
	 * Adds the constant pool entry and returns the index to it.
	 *
	 * @param __v The entry to add.
	 * @return The index the entry is at.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	public final int add(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Field access
		if (__v instanceof AccessedField)
			throw new todo.TODO();
		
		// Class name
		else if (__v instanceof ClassName)
			return this.__add(__v, this.add(__v.toString()));
		
		// Record handle for the method
		else if (__v instanceof InvokedMethod)
			return this.__add(__v, this.add(((InvokedMethod)__v).handle()));
		
		// Method descriptor
		else if (__v instanceof MethodDescriptor)
			return this.__add(__v, this.add(__v.toString()));
		
		// Method handle
		else if (__v instanceof MethodHandle)
		{
			MethodHandle v = (MethodHandle)__v;
			return this.__add(__v,
				this.add(v.outerClass()),
				this.add(v.name()),
				this.add(v.descriptor()));
		}
		
		// Method name
		else if (__v instanceof MethodName)
			return this.__add(__v, this.add(__v.toString()));
		
		// Untranslated
		else
			return this.__add(__v);
	}
	
	/**
	 * Internal add logic.
	 *
	 * @param __v The entry to add.
	 * @param __parts Parts which make up the entry.
	 * @return The index the entry is at.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final int __add(Object __v, int... __parts)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		Map<Object, Integer> pool = this._pool;
		
		// Does this entry pre-exist in the pool already?
		Integer pre = pool.get(__v);
		if (pre != null)
			return pre;
		
		// Debug
		todo.DEBUG.note("Pool add %s %s (%d parts)", __v.getClass().getName(),
			__v, __parts.length);
		
		// Otherwise it gets added at the end
		int rv = pool.size();
		pool.put(__v, rv);
		this._parts.add((__parts == null ? new int[0] : __parts.clone()));
		return rv;
	}
}

