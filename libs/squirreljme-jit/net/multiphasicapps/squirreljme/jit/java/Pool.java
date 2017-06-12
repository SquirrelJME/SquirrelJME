// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class decodes the constant pool and provides generic access to the
 * contents of it.
 *
 * @since 2017/06/08
 */
public class Pool
{
	/** The UTF constant tag. */
	private static final int _TAG_UTF8 =
		1;
	
	/** Integer constant. */
	private static final int _TAG_INTEGER =
		3;
	
	/** Float constant. */
	private static final int _TAG_FLOAT =
		4;
	
	/** Long constant. */
	private static final int _TAG_LONG =
		5;
	
	/** Double constant. */
	private static final int _TAG_DOUBLE =
		6;
	
	/** Reference to another class. */
	private static final int _TAG_CLASS =
		7;
	
	/** String constant. */
	private static final int _TAG_STRING =
		8;
	
	/** Field reference. */
	private static final int _TAG_FIELDREF =
		9;
	
	/** Method reference. */
	private static final int _TAG_METHODREF =
		10;
	
	/** Interface method reference. */
	private static final int _TAG_INTERFACEMETHODREF =
		11;
	
	/** Name and type. */
	private static final int _TAG_NAMEANDTYPE =
		12;
	
	/** Method handle (illegal). */
	private static final int _TAG_METHODHANDLE =
		15;
	
	/** Method type (illegal). */
	private static final int _TAG_METHODTYPE =
		16;
	
	/** Invoke dynamic call site (illegal). */
	private static final int _TAG_INVOKEDYNAMIC =
		18;
	
	/** The top of a long/double. */
	private static final int _TAG_WIDETOP =
		-1;
	
	/** Entries within the constant pool. */
	private final Object[] _entries;
	
	/**
	 * Parses and initializes the constant pool structures.
	 *
	 * @param __in The input class containing the constant pool to be read.
	 * @throws IOException On read errors.
	 * @throws JITException If the constant pool is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public Pool(DataInputStream __in)
		throws IOException, JITException, NullPointerException
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
				case _TAG_UTF8:
					try
					{
						data = new UTFConstantEntry(__in.readUTF());
					}
					
					// {@squirreljme.error JI0b Modified UTF-8 data is not in
					// the correct format.}
					catch (UTFDataFormatException e)
					{
						throw new JITException("JI0b", e);
					}
					break;
					
					// Reference to two entries
				case _TAG_FIELDREF:
				case _TAG_METHODREF:
				case _TAG_INTERFACEMETHODREF:
				case _TAG_NAMEANDTYPE:
					data = new int[]{__in.readUnsignedShort(),
						__in.readUnsignedShort()};
					break;
					
					// References to single entry
				case _TAG_CLASS:
				case _TAG_STRING:
					data = new int[]{__in.readUnsignedShort()};
					break;
					
					// Integer
				case _TAG_INTEGER:
					data = Integer.valueOf(__in.readInt());
					break;
					
					// Long
				case _TAG_LONG:
					data = Long.valueOf(__in.readLong());
					break;
					
					// Float
				case _TAG_FLOAT:
					data = Float.valueOf(__in.readFloat());
					break;
					
					// Double
				case _TAG_DOUBLE:
					data = Double.valueOf(__in.readDouble());
					break;
					
					// {@squirreljme.error JI09 Java ME does not support dynamic
					// invocation (such as method handles or lambda
					// expressions).}
				case _TAG_METHODHANDLE:
				case _TAG_METHODTYPE:
				case _TAG_INVOKEDYNAMIC:
					throw new JITException("JI09");
				
					// {@squirreljme.error JI0a Unknown tag type in the constant
					// pool. (The constant pool tag)}
				default:
					throw new JITException(String.format("JI0a %d", tag));
			}
			rawdata[i] = data;
			
			// Skip long/double?
			if (tag == _TAG_LONG || tag == _TAG_DOUBLE)
			{
				rawdata[++i] = new WideConstantTopEntry();
				tags[i] = _TAG_WIDETOP;
			}
		}
		
		// Go through tags again and initialize their raw data into type-safe
		// classes 
		Object[] entries = new Object[count];
		try
		{
			__initializeEntries(entries, tags, rawdata);
		}
		
		// {@squirreljme.error JI0e The constant pool is not valid.}
		catch (ClassCastException|IndexOutOfBoundsException|
			NullPointerException e)
		{
			throw new JITException("JI0e", e);
		}
		
		this._entries = entries;
	}
	
	/**
	 * Obtains the entry at the specified index.
	 *
	 * @param <C> The type of class to get.
	 * @param __cl The type of class to get.
	 * @param __i The index of the entry to get.
	 * @return The entry at the specified position as the given class.
	 * @throws JITException If the class type does not match or the pool index
	 * is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public <C> C get(Class<C> __cl, int __i)
		throws JITException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI0c The specified index is not within the bounds
		// of the constant pool. (The index of the entry)}
		Object[] entries = this._entries;
		if (__i < 0 || __i >= entries.length)
			throw new JITException(String.format("JI0c %d", __i));
		
		// {@squirreljme.error JI0d The specified entry's class is not of the
		// expected class. (The index of the entry; The class the entry is; The
		// expected class)}
		Object val = entries[__i];
		if (val != null && !__cl.isInstance(val))
			throw new JITException(String.format("JI0d %d %s %s", __i,
				val.getClass(), __cl));
		
		return __cl.cast(val);
	}
	
	/**
	 * This initializes the entries in the constant pool.
	 *
	 * @param __entries Output pool entries.
	 * @param __tags Constant pool tags for the entries.
	 * @param __rawdata The raw pool data.
	 * @throws JITException If the entries are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	private final void __initializeEntries(Object[] __entries, int[] __tags,
		Object[] __rawdata)
		throws JITException, NullPointerException
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
				case _TAG_UTF8:
				case _TAG_INTEGER:
				case _TAG_FLOAT:
				case _TAG_LONG:
				case _TAG_DOUBLE:
				case _TAG_WIDETOP:
					sequence = 0;
					break;
				
				case _TAG_CLASS:
				case _TAG_STRING:
					sequence = 1;
					break;
				
				case _TAG_NAMEANDTYPE:
					sequence = 2;
					break;
				
				case _TAG_FIELDREF:
				case _TAG_METHODREF:
				case _TAG_INTERFACEMETHODREF:
					sequence = 3;
					break;
					
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Add to order
			order[i] = (sequence << 16) | i;
		}
		Arrays.sort(order);
		
		throw new todo.TODO();
	}
}

