// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * This class decodes the constant pool and provides generic access to the
 * contents of it.
 *
 * @since 2017/06/08
 */
public final class Pool
{
	/** The UTF constant tag. */
	public static final int TAG_UTF8 =
		1;
	
	/** Integer constant. */
	public static final int TAG_INTEGER =
		3;
	
	/** Float constant. */
	public static final int TAG_FLOAT =
		4;
	
	/** Long constant. */
	public static final int TAG_LONG =
		5;
	
	/** Double constant. */
	public static final int TAG_DOUBLE =
		6;
	
	/** Reference to another class. */
	public static final int TAG_CLASS =
		7;
	
	/** String constant. */
	public static final int TAG_STRING =
		8;
	
	/** Field reference. */
	public static final int TAG_FIELDREF =
		9;
	
	/** Method reference. */
	public static final int TAG_METHODREF =
		10;
	
	/** Interface method reference. */
	public static final int TAG_INTERFACEMETHODREF =
		11;
	
	/** Name and type. */
	public static final int TAG_NAMEANDTYPE =
		12;
	
	/** Method handle (illegal). */
	public static final int TAG_METHODHANDLE =
		15;
	
	/** Method type (illegal). */
	public static final int TAG_METHODTYPE =
		16;
	
	/** Invoke dynamic call site (illegal). */
	public static final int TAG_INVOKEDYNAMIC =
		18;
	
	/** The top of a long/double. */
	public static final int TAG_WIDETOP =
		-1;
	
	/** Entries within the constant pool. */
	private final Object[] _entries;
	
	/**
	 * Parses and initializes the constant pool structures.
	 *
	 * @param __e The entries which make up the pool, this is used directly.
	 * @since 2017/06/08
	 */
	Pool(Object... __e)
	{
		this._entries = (__e == null ? new Object[0] : __e);
	}
	
	/**
	 * Obtains the entry at the specified index.
	 *
	 * @param <C> The type of class to get.
	 * @param __cl The type of class to get.
	 * @param __i The index of the entry to get.
	 * @return The entry at the specified position as the given class or
	 * {@code null} if it does not exist.
	 * @throws InvalidClassFormatException If the class type does not match or
	 * the pool index is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public <C> C get(Class<C> __cl, int __i)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Short circuit, the zero entry is always nothing
		if (__i == 0)
			return null;
		
		// {@squirreljme.error JC1c The specified index is not within the bounds
		// of the constant pool. (The index of the entry)}
		Object[] entries = this._entries;
		if (__i < 0 || __i >= entries.length)
			throw new InvalidClassFormatException(
				String.format("JC1c %d", __i));
		
		// {@squirreljme.error JC1d The specified entry's class is not of the
		// expected class. (The index of the entry; The class the entry is; The
		// expected class)}
		Object val = entries[__i];
		if (val != null && !__cl.isInstance(val))
			throw new InvalidClassFormatException(
				String.format("JC1d %d %s %s", __i, val.getClass(), __cl));
		
		return __cl.cast(val);
	}
	
	/**
	 * This is similar to {@link #get(Class, int)} except that it is not valid
	 * if the entry is the {@code null} entry (the first one).
	 *
	 * @param <C> The type of class to get.
	 * @param __cl The type of class to get.
	 * @param __i The index of the entry to get.
	 * @return The entry at the specified position as the given class.
	 * @throws InvalidClassFormatException If the class type does not match,
	 * the pool index is out of range, or the entry is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/14
	 */
	public <C> C require(Class<C> __cl, int __i)
		throws InvalidClassFormatException, NullPointerException
	{
		// {@squirreljme.error JC1e Expected the specified constant pool entry
		// to not be the null entry. (The index; The expected class)}
		C rv = this.<C>get(__cl, __i);
		if (rv == null)
			throw new InvalidClassFormatException(
				String.format("JC1e %d %s", __i, __cl));
		return rv;
	}
	
	/**
	 * Decodes the constant pool.
	 *
	 * @param __in The input stream.
	 * @return The read constant pool.
	 * @throws InvalidClassFormatException If the constant pool is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public static Pool decode(DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read the raw constant pool contents first
		int count = __in.readUnsignedShort();
		int[] tags = new int[count];
		Object[] rawdata = new Object[count];
		for (int i = 1; i < count; i++)
		{
			// Read tag
			int tag = __in.readUnsignedByte();
			tags[i] = tag;
			
			// Parse tag data
			Object data;
			switch (tag)
			{
					// UTF-8 String
					// The string is wrapped in a wrapper class so that String
					// constants are actual String references. It is illegal for
					// UTF-8 constants to be directly used by the byte code so
					// this prevents their usage from occuring by causing a
					// class cast exception
				case TAG_UTF8:
					try
					{
						data = new UTFConstantEntry(__in.readUTF());
					}
					
					// {@squirreljme.error JC1f Modified UTF-8 data is not in
					// the correct format.}
					catch (UTFDataFormatException e)
					{
						throw new InvalidClassFormatException("JC1f", e);
					}
					break;
					
					// Reference to two entries
				case TAG_FIELDREF:
				case TAG_METHODREF:
				case TAG_INTERFACEMETHODREF:
				case TAG_NAMEANDTYPE:
					data = new int[]{__in.readUnsignedShort(),
						__in.readUnsignedShort()};
					break;
					
					// References to single entry
				case TAG_CLASS:
				case TAG_STRING:
					data = new int[]{__in.readUnsignedShort()};
					break;
					
					// Integer
				case TAG_INTEGER:
					data = new ConstantValueInteger(
						Integer.valueOf(__in.readInt()));
					break;
					
					// Long
				case TAG_LONG:
					data = new ConstantValueLong(
						Long.valueOf(__in.readLong()));
					break;
					
					// Float
				case TAG_FLOAT:
					data = new ConstantValueFloat(
						Float.valueOf(__in.readFloat()));
					break;
					
					// Double
				case TAG_DOUBLE:
					data = new ConstantValueDouble(
						Double.valueOf(__in.readDouble()));
					break;
					
					// {@squirreljme.error JC1g Java ME does not support dynamic
					// invocation (such as method handles or lambda
					// expressions).}
				case TAG_METHODHANDLE:
				case TAG_METHODTYPE:
				case TAG_INVOKEDYNAMIC:
					throw new InvalidClassFormatException("JC1g");
				
					// {@squirreljme.error JC1h Unknown tag type in the constant
					// pool. (The constant pool tag)}
				default:
					throw new InvalidClassFormatException(
						String.format("JC1h %d", tag));
			}
			rawdata[i] = data;
			
			// Skip long/double?
			if (tag == TAG_LONG || tag == TAG_DOUBLE)
			{
				rawdata[++i] = new WideConstantTopEntry();
				tags[i] = TAG_WIDETOP;
			}
		}
		
		// Go through tags again and initialize their raw data into type-safe
		// classes 
		Object[] entries = new Object[count];
		try
		{
			__initializeEntries(entries, tags, rawdata);
		}
		
		// {@squirreljme.error JC1i The constant pool is not valid.}
		catch (ClassCastException|IndexOutOfBoundsException|
			NullPointerException e)
		{
			throw new InvalidClassFormatException("JC1i", e);
		}
		
		// Setup
		return new Pool(entries);
	}
	
	/**
	 * This initializes the entries in the constant pool.
	 *
	 * @param __entries Output pool entries.
	 * @param __tags Constant pool tags for the entries.
	 * @param __rawdata The raw pool data.
	 * @throws InvalidClassFormatException If the entries are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	private static void __initializeEntries(Object[] __entries, int[] __tags,
		Object[] __rawdata)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__entries == null || __tags == null || __rawdata == null)
			throw new NullPointerException("NARG");
		
		// Instead of having a nested loop which goes through every entry
		// multiple times, the sequence entries should be parsed in is very
		// known with a direct line of dependencies. Entries with lower
		// sequences are only depended on. This saves an extra loop and makes
		// the code much cleaner for the most part.
		int count = __entries.length;
		int[] order = new int[count];
		for (int i = 0; i < count; i++)
		{
			int tag = __tags[i],
				sequence;
			
			// Determine the sequence based on the tag
			switch (tag)
			{
				case 0:
				case TAG_UTF8:
				case TAG_INTEGER:
				case TAG_FLOAT:
				case TAG_LONG:
				case TAG_DOUBLE:
				case TAG_WIDETOP:
					sequence = 0;
					break;
				
				case TAG_CLASS:
				case TAG_STRING:
					sequence = 1;
					break;
				
				case TAG_NAMEANDTYPE:
					sequence = 2;
					break;
				
				case TAG_FIELDREF:
				case TAG_METHODREF:
				case TAG_INTERFACEMETHODREF:
					sequence = 3;
					break;
					
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Add to order
			order[i] = (sequence << 16) | i;
		}
		Arrays.sort(order);
		
		// Go through and process all of the entries now that their major
		// sequence order is known.
		for (int j = 0; j < count; j++)
		{
			int i = order[j] & 0xFFFF,
				tag = __tags[i];
			
			// Process tags
			Object in = __rawdata[i],
				out;
			switch (tag)
			{
					// These are copied directly
				case 0:
				case TAG_UTF8:
				case TAG_INTEGER:
				case TAG_FLOAT:
				case TAG_LONG:
				case TAG_DOUBLE:
				case TAG_WIDETOP:
					out = in;
					break;
					
					// Name of a class
				case TAG_CLASS:
					out = new ClassName(((UTFConstantEntry)
						__entries[((int[])in)[0]]).toString());
					break;
					
					// String constant
				case TAG_STRING:
					out = new ConstantValueString(
						((UTFConstantEntry)__entries[((int[])in)[0]]).
							toString());
					break;
					
					// Name and type information
				case TAG_NAMEANDTYPE:
					{
						int[] ina = (int[])in;
						out = new NameAndType(
							((UTFConstantEntry)__entries[ina[0]]).toString(),
							((UTFConstantEntry)__entries[ina[1]]).toString());
					}
					break;
					
					// Field and method references
				case TAG_FIELDREF:
				case TAG_METHODREF:
				case TAG_INTERFACEMETHODREF:
					{
						int[] ina = (int[])in;
						ClassName cn = (ClassName)__entries[ina[0]];
						NameAndType nat = (NameAndType)__entries[ina[1]];
						
						if (tag == TAG_FIELDREF)
							out = new FieldReference(cn,
								new FieldName(nat.name()),
								new FieldDescriptor(nat.type()));
						else
							out = new MethodReference(cn,
								new MethodName(nat.name()),
								new MethodDescriptor(nat.type()),
								tag == TAG_INTERFACEMETHODREF);
					}
					break;
				
					// Unhandled, should not happen
				default:
					throw new RuntimeException(String.format("OOPS %d", tag));
			}
			
			__entries[i] = out;
		}
	}
}

