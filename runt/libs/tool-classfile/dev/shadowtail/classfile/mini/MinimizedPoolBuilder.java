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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.collections.IntegerList;
import net.multiphasicapps.collections.UnmodifiableList;

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
		this._pool.put(null, 0);
		this._parts.add(new int[0]);
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
		
		// Class names
		else if (__v instanceof ClassNames)
		{
			// Adjust the value to map correctly
			ClassNames names = (ClassNames)__v;
			
			// Fill into indexes
			int n = names.size();
			int[] indexes = new int[n];
			for (int i = 0; i < n; i++)
				indexes[i] = this.add(names.get(i));
			
			// Add it now
			return this.__add(names, indexes);
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
		
		// Primitives
		else if (__v instanceof Integer ||
			__v instanceof Long ||
			__v instanceof Float ||
			__v instanceof Double)
			return this.__add(__v);
		
		// {@squirreljme.error JC3p Cannot add the specified entry to the
		// constant pool. (The class type)}
		else
			throw new IllegalArgumentException("JC3p " + __v.getClass());
	}
	
	/**
	 * Adds the entry to the pool and returns the passed value.
	 *
	 * @param __v The value to add.
	 * @return {@code __v}.
	 * @since 2019/04/14
	 */
	public final <V> V addSelf(V __v)
	{
		this.add(__v);
		return __v;
	}
	
	/**
	 * Gets the index of where the entry is stored in the pool.
	 *
	 * @param __v The entry to get.
	 * @return The index of the entry.
	 * @throws IllegalStateException If the entry is not in the pool.
	 * @since 2019/04/14
	 */
	public final int get(Object __v)
		throws IllegalStateException
	{
		Integer rv = this._pool.get(__v);
		if (rv == null)
			throw new IllegalStateException("JC3q " + __v);
		return rv;
	}
	
	/**
	 * Returns the byte representation of the data here.
	 *
	 * @return The byte data representation.
	 * @since 2019/04/14
	 */
	public final byte[] getBytes()
	{
		Map<Object, Integer> pool = this._pool;
		List<int[]> parts = this._parts;
		
		// Write data
		try
		{
			// Table header information
			ByteArrayOutputStream tbytes = new ByteArrayOutputStream();
			DataOutputStream tdos = new DataOutputStream(tbytes);
			
			// Actual table data
			ByteArrayOutputStream dbytes = new ByteArrayOutputStream();
			DataOutputStream ddos = new DataOutputStream(dbytes);
			
			// Write the number of entries in the pool
			int poolcount = pool.size();
			tdos.writeInt(poolcount);
			
			// Guess where all the data will be written in the pool
			int reloff = 4 + (poolcount * 4) + 4;
			
			// Write all the values in the pool, the value in the map is
			// ignored because that just stores the index identifier
			int pdx = 0;
			for (Object value : pool.keySet())
			{
				// Get type and part information
				MinimizedPoolEntryType et = (pdx == 0 ?
					MinimizedPoolEntryType.NULL :
					MinimizedPoolEntryType.ofClass(value.getClass()));
				int[] part = parts.get(pdx++);
				
				// Have two pool entry formats, a wide one and a narrow one
				// This is to reduce the size of the constant pool in classes
				// where small values can be used instead
				// This adds extra types for parsing because otherwise having
				// variable ints and such will be very confusing and
				// complicated!
				int faketype = et.ordinal();
				switch (et)
				{
						// These are always fixed types and as such are never
						// wide
					case STRING:
					case INTEGER:
					case LONG:
					case FLOAT:
					case DOUBLE:
						break;
					
						// If any reference part is outside of this basic
						// range then use wide values here
					default:
						// If there are lots of parts then this needs to take
						// up a larger area
						if (part.length > 127)
							faketype |= 0x80;
						
						// Scan parts
						for (int i : part)
							if (i < 0 || i > 127)
							{
								faketype |= 0x80;
								break;
							}
						break;
				}
				
				// Is wide being used?
				boolean iswide = ((faketype & 0x80) != 0);
				
				// Write position and the entry type, use 24-bits for the
				// entry offset just to use the space since maybe the pool
				// will get pretty big? It is Java ME though so hopefully
				// the pool never exceeds 65K.
				int dxo = reloff + ddos.size();
				tdos.writeByte(faketype);
				tdos.writeByte(dxo >>> 16);
				tdos.writeShort(dxo & 0xFFFF);
				
				// Depends on the type used
				switch (et)
				{
					// Just write a zero for null, just in case!
					case NULL:
						ddos.writeInt(0);
						break;
					
						// String are special because they have actual
						// string data stored
					case STRING:
						{
							// Record hashCode and the String size as simple
							// fields to read. Note that even though there is
							// the UTF length, the length of the actual string
							// could be useful. But only keep the lowest part
							// as that will be "good enough"
							ddos.writeShort((short)part[0]);
							ddos.writeShort(Minimizer.__checkUShort(part[1]));
							
							// Write string UTF data
							ddos.writeUTF((String)value);
						}
						break;
						
						// Integer
					case INTEGER:
						ddos.writeInt((Integer)value);
						break;
						
						// Long
					case LONG:
						ddos.writeLong((Long)value);
						break;
						
						// Float
					case FLOAT:
						ddos.writeInt(
							Float.floatToRawIntBits((Float)value));
						break;
						
						// Double
					case DOUBLE:
						ddos.writeLong(
							Double.doubleToRawLongBits((Double)value));
						break;
						
						// Everything else just consists of parts which are
						// either values to other indexes or an ordinal
					case ACCESSED_FIELD:
					case CLASS_NAME:
					case CLASS_NAMES:
					case INVOKED_METHOD:
					case FIELD_DESCRIPTOR:
					case FIELD_NAME:
					case FIELD_REFERENCE:
					case METHOD_DESCRIPTOR:
					case METHOD_HANDLE:
					case METHOD_NAME:
						{
							// Write number of parts
							int npart = part.length;
							if (iswide)
								ddos.writeShort(
									Minimizer.__checkUShort(npart));
							else
								ddos.writeByte(npart);
							
							// Write all the parts
							for (int i = 0; i < npart; i++)
								if (iswide)
									ddos.writeShort(Minimizer.__checkUShort(
										part[i]));
								else
									ddos.writeByte(part[i]);
						}
						break;
						
						// Should not occur
					default:
						throw new todo.OOPS(et.name());
				}
				
				// Round positions
				Minimizer.__dosRound(ddos);
			}
			
			// Write end of table marker and the table end area thing
			int dxo = reloff + ddos.size();
			tdos.writeByte(0xFF);
			tdos.writeByte(dxo >>> 16);
			tdos.writeShort(dxo & 0xFFFF);
			
			// Merge the data bytes into the table then use the completed
			// table
			tbytes.write(dbytes.toByteArray());
			return tbytes.toByteArray();
		}
		
		// Should not occur
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
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

