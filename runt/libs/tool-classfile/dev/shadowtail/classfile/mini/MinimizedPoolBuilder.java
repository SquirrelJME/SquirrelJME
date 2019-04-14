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

import dev.shadowtail.classfile.nncc.AccessedField;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.collections.IntegerList;

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
	 * Initializes the base pool.
	 *
	 * @since 2019/04/14
	 */
	{
		// Add null entry to mean nothing
		this._pool.put(0, null);
	}
	
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
		{
			AccessedField af = (AccessedField)__v;
			return this.__add(__v,
				af.time().ordinal(), af.type().ordinal(),
				this.add(af.field()));
		}
		
		// Class name
		else if (__v instanceof ClassName)
		{
			// Write representation of array type and its component type to
			// make sure they are always added
			ClassName cn = (ClassName)__v;
			if (cn.isArray())
				return this.__add(__v,
					this.add(__v.toString()), this.add(cn.componentType()));
			
			// Not an array
			else
				return this.__add(__v,
					this.add(__v.toString()), 0);
		}
		
		// Record handle for the method
		else if (__v instanceof InvokedMethod)
		{
			InvokedMethod iv = (InvokedMethod)__v;
			
			return this.__add(__v,
				iv.type().ordinal(), this.add(iv.handle()));
		}
		
		// Field descriptor
		else if (__v instanceof FieldDescriptor)
			return this.__add(__v,
				this.add(__v.toString()), this.add(
					((FieldDescriptor)__v).className()));
		
		// Field/Method name
		else if (__v instanceof FieldName ||
			__v instanceof MethodName)
			return this.__add(__v,
				this.add(__v.toString()));
		
		// Field reference
		else if (__v instanceof FieldReference)
		{
			FieldReference v = (FieldReference)__v;
			return this.__add(__v,
				this.add(v.className()),
				this.add(v.memberName()),
				this.add(v.memberType()));
		}
		
		// Method descriptor, add parts of the descriptor naturally
		else if (__v instanceof MethodDescriptor)
		{
			MethodDescriptor md = (MethodDescriptor)__v;
			
			// Setup with initial string
			List<Integer> sub = new ArrayList<>();
			sub.add(this.add(__v.toString()));
			
			// Put in argument count
			FieldDescriptor[] args = md.arguments();
			sub.add(args.length);
			
			// Add return value
			FieldDescriptor rv = md.returnValue();
			sub.add((rv == null ? 0 : this.add(rv)));
			
			// Fill in arguments
			FieldDescriptor[] margs = md.arguments();
			for (FieldDescriptor marg : margs)
				sub.add(this.add(marg));
			
			// Convert to integer
			int n = sub.size();
			int[] isubs = new int[n];
			for (int i = 0; i < n; i++)
				isubs[i] = sub.get(i);
			
			// Put in descriptor with all the pieces
			return this.__add(__v,
				isubs);
		}
		
		// Method handle
		else if (__v instanceof MethodHandle)
		{
			MethodHandle v = (MethodHandle)__v;
			return this.__add(__v,
				this.add(v.outerClass()),
				this.add(v.name()),
				this.add(v.descriptor()));
		}
		
		// String
		else if (__v instanceof String)
			return this.__add(__v,
				__v.hashCode(),
				((String)__v).length());
		
		// Untranslated
		else
			return this.__add(__v);
	}
	
	/**
	 * Returns the pool size.
	 *
	 * @return The pool size.
	 * @since 2019/04/14
	 */
	public final int size()
	{
		return this._pool.size();
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
		todo.DEBUG.note("Pool add %s %s (%d parts: %s)",
			__v.getClass().getName(), __v, __parts.length,
			new IntegerList(__parts));
		
		// Otherwise it gets added at the end
		int rv = pool.size();
		pool.put(__v, rv);
		this._parts.add((__parts == null ? new int[0] : __parts.clone()));
		return rv;
	}
}

