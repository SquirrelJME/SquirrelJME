// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import java.util.List;
import java.util.ArrayList;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;

/**
 * This is used as a builder for both class and run-time pools.
 *
 * @since 2019/07/17
 */
public final class DualClassRuntimePoolBuilder
{
	/** The class pool. */
	protected final BasicPoolBuilder classpool =
		new BasicPoolBuilder();
	
	/** The run-time pool. */
	protected final BasicPoolBuilder runpool =
		new BasicPoolBuilder();
	
	/**
	 * Adds the specified pool entry.
	 *
	 * @param __rt Place into the run-time pool?
	 * @param __v The value to store.
	 * @return The resulting pool entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public final BasicPoolEntry add(boolean __rt, Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		if (__rt)
			return this.addRuntime(__v);
		return this.addStatic(__v);
	}
	
	/**
	 * Adds an entry to the run-time pool.
	 *
	 * @param __v The value to add.
	 * @return The entry which was created.
	 * @throws IllegalArgumentException If the value cannot be stored into
	 * the specified pool.
	 * @since 2019/09/01
	 */
	public final BasicPoolEntry addRuntime(Object __v)
		throws IllegalArgumentException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// The pool to be added to and the underlying static pool
		BasicPoolBuilder runpool = this.runpool,
			classpool = this.classpool;
		
		// Already within the pool?
		BasicPoolEntry rv = runpool.getByValue(__v);
		if (rv != null)
			return rv;
		
		// Depends on the type to be stored
		MinimizedPoolEntryType type;
		switch ((type = MinimizedPoolEntryType.ofClass(__v.getClass())))
		{
				// Null is always the first entry
			case NULL:
				return runpool.getByValue(null);
			
				// A field which has been accessed
			case ACCESSED_FIELD:
				AccessedField af = (AccessedField)__v;
				FieldReference fr = af.field();
				return runpool.add(__v,
					af.time().ordinal(),
					af.type().ordinal(),
					this.addStatic(fr.className()).index,
					this.addStatic(fr.memberName().toString()).index,
					this.addStatic(fr.memberType().className()).index);
			
				// Class information pointer
			case CLASS_INFO_POINTER:
				return runpool.add(__v,
					this.addStatic(((ClassInfoPointer)__v).name).index);
						
				// The constant pool of another (or current) class
			case CLASS_POOL:
				return runpool.add(__v,
					this.addStatic(((ClassPool)__v).name).index);
				
				// A method which has been invoked
			case INVOKED_METHOD:
				InvokedMethod iv = (InvokedMethod)__v;
				MethodHandle mh = iv.handle();
				
				return runpool.add(__v,
					iv.type().ordinal(),
					this.addStatic(mh.outerClass()).index,
					this.addStatic(mh.name().toString()).index,
					this.addStatic(mh.descriptor()).index);
				
				// The index of a method
			case METHOD_INDEX:
				MethodIndex v = (MethodIndex)__v;
				return runpool.add(__v,
					0x7FFF,
					this.addStatic(v.inclass).index,
					this.addStatic(v.name.toString()).index,
					this.addStatic(v.type).index);
				
				// A string that is noted for its value (debugging)
			case NOTED_STRING:
				return runpool.add(__v,
					this.addStatic(__v.toString()).index);
				
				// A string that is used
			case USED_STRING:
				return runpool.add(__v,
					this.addStatic(__v.toString()).index);
			
				// Unknown
			default:
				// {@squirreljme.error JC4f Invalid type in runtime pool.
				// (The type)}
				if (!type.isRuntime())
					throw new IllegalArgumentException("JC4f " + type);
				throw new todo.OOPS(type.name());
		}
	}
	
	/**
	 * Adds an entry to the static pool.
	 *
	 * @param __v The value to add.
	 * @return The entry which was created.
	 * @throws IllegalArgumentException If the value cannot be stored into
	 * the specified pool.
	 * @since 2019/09/01
	 */
	public final BasicPoolEntry addStatic(Object __v)
		throws IllegalArgumentException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// The pool to be added to
		BasicPoolBuilder classpool = this.classpool;
		
		// Already within the pool?
		BasicPoolEntry rv = classpool.getByValue(__v);
		if (rv != null)
			return rv;
		
		// Depends on the type to be stored
		MinimizedPoolEntryType type;
		switch ((type = MinimizedPoolEntryType.ofClass(__v.getClass())))
		{
			case INTEGER:
				int bi = (Integer)__v;
				return classpool.add(__v,
					(bi >> 16) & 0xFFFF,
					(bi) & 0xFFFF);
				
			case FLOAT:
				int bf = Float.floatToRawIntBits((Float)__v);
				return classpool.add(__v,
					(bf >> 16) & 0xFFFF,
					(bf) & 0xFFFF);
			
			case LONG:
				long l = (Long)__v;
				return classpool.add(__v,
					((int)(l >>> 48)) & 0xFFFF,
					((int)(l >>> 32)) & 0xFFFF,
					((int)(l >>> 16)) & 0xFFFF,
					((int)l) & 0xFFFF);
			
			case DOUBLE:
				long d = Double.doubleToRawLongBits((Double)__v);
				return classpool.add(__v,
					((int)(d >>> 48)) & 0xFFFF,
					((int)(d >>> 32)) & 0xFFFF,
					((int)(d >>> 16)) & 0xFFFF,
					((int)d) & 0xFFFF);
				
				// Name of class (used), the component type is recorded if
				// this is detected to be an array
			case CLASS_NAME:
				ClassName cn = (ClassName)__v;
				return classpool.add(__v,
					this.addStatic(cn.toString()).index,
					(!cn.isArray() ? 0 :
						this.addStatic(cn.componentType()).index));
				
				// List of class names (interfaces)
			case CLASS_NAMES:
				// Adjust the value to map correctly
				ClassNames names = (ClassNames)__v;
				
				// Fill into indexes
				int nn = names.size();
				int[] indexes = new int[nn];
				for (int i = 0; i < nn; i++)
					indexes[i] = this.addStatic(names.get(i)).index;
				
				// Add it now
				return classpool.add(names, indexes);
			
				// Descriptor of a method
			case METHOD_DESCRIPTOR:
				MethodDescriptor md = (MethodDescriptor)__v;
				
				// Need arguments to process them
				FieldDescriptor mrv = md.returnValue();
				FieldDescriptor[] args = md.arguments();
				
				// Argument set
				int[] isubs = new int[3 + args.length];
				
				// String, argument count, and return value
				isubs[0] = this.addStatic(__v.toString()).index;
				isubs[1] = args.length;
				isubs[2] = (mrv == null ? 0 :
					this.addStatic(mrv.className()).index);
				
				// Fill in arguments
				for (int q = 0, n = args.length; q < n; q++)
					isubs[3 + q] = this.addStatic(args[q].className()).index;
				
				// Put in descriptor with all the pieces
				return classpool.add(__v, isubs);
			
			case STRING:
				return classpool.add(__v,
					__v.hashCode() & 0xFFFF,
					((String)__v).length());
			
				// Unknown
			default:
				// {@squirreljme.error JC4e Invalid type in static pool.
				// (The type)}
				if (!type.isStatic())
					throw new IllegalArgumentException("JC4e " + type);
				throw new todo.OOPS(type.name());
		}
	}
	
	/**
	 * Builds this dual pool.
	 *
	 * @return The resulting dual pool.
	 * @since 2019/09/07
	 */
	public final DualClassRuntimePool build()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the static class pool.
	 *
	 * @return The pool.
	 * @since 2019/07/17
	 */
	public final BasicPoolBuilder classPool()
	{
		return this.classpool;
	}
	
	/**
	 * Returns the runtime class pool.
	 *
	 * @return The pool.
	 * @since 2019/07/17
	 */
	public final BasicPoolBuilder runtimePool()
	{
		return this.runpool;
	}
}

