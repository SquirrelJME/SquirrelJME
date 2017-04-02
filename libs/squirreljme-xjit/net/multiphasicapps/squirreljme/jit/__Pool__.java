// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.AbstractList;
import java.util.List;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents the constant pool of a class which has been read, this is
 * used during translation of the class and also when it is output.
 *
 * @since 2016/06/29
 */
final class __Pool__
	extends AbstractList<__PoolEntry__>
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
	
	/** Internal entries. */
	private final __PoolEntry__[] _entries;
	
	/** The next active index to use. */
	volatile int _nextadx;
	
	/**
	 * Decodes the constant pool of an input class file.
	 *
	 * @param __dis The input stream to read for class files.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws JITException If the constant pool is malformed.
	 * @since 2016/06/29
	 */
	__Pool__(DataInputStream __dis)
		throws IOException, NullPointerException, JITException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ0d The input class has an empty constant
		// pool.}
		int count = __dis.readUnsignedShort();
		if (count <= 0)
			throw new JITException("AQ0d");
		
		// Setup entries
		__PoolEntry__[] entries = new __PoolEntry__[count];
		this._entries = entries;
		
		// Always initialize the first (null entry)
		entries[0] = new __PoolEntry__(this, (byte)0, 0, new int[0]);
		
		// Decode all entry data
		for (int i = 1; i < count; i++)
		{
			// Read the tag
			int tag = __dis.readUnsignedByte();
			Object data;
			
			// {@squirreljme.error AQ0e Java ME does not support dynamic
			// invocation (such as method handles or lambda expressions).}
			if (tag == TAG_METHODHANDLE || tag == TAG_METHODTYPE ||
				tag == TAG_INVOKEDYNAMIC)
				throw new JITException("AQ0e");
			
			// UTF-8 String
			if (tag == TAG_UTF8)
			{
				// Read the string data
				try
				{
					data = __dis.readUTF();
				}
				
				// {@squirreljme.error AQ0f The modified UTF-8 data string in
				// the constant pool is malformed.}
				catch (UTFDataFormatException e)
				{
					throw new JITException("AQ0f", e);
				}
			}
			
			// Field and Method references, Name and type
			else if (tag == TAG_FIELDREF || tag == TAG_METHODREF ||
				tag == TAG_INTERFACEMETHODREF || tag == TAG_NAMEANDTYPE)
				data = new int[]
					{
						__dis.readUnsignedShort(),
						__dis.readUnsignedShort()
					};
			
			// Class reference, string
			else if (tag == TAG_CLASS || tag == TAG_STRING)
				data = new int[]
					{
						__dis.readUnsignedShort()
					};
			
			// Integer
			else if (tag == TAG_INTEGER)
				data = Integer.valueOf(__dis.readInt());
			
			// Long
			else if (tag == TAG_LONG)
				data = Long.valueOf(__dis.readLong());
			
			// Float
			else if (tag == TAG_FLOAT)
				data = Float.valueOf(__dis.readFloat());
			
			// Double
			else if (tag == TAG_DOUBLE)
				data = Double.valueOf(__dis.readDouble());
			
			// {@squirreljme.error AQ0g Unknown constant pool tag. (The tag of
			// the constant pool entry)}
			else
				throw new JITException(String.format("AQ0g %d", tag));
		
			// Create entry
			__PoolEntry__ dup;
			entries[i] =
				(dup = new __PoolEntry__(this, (byte)tag, i, data));
			
			// Double up?
			if (tag == TAG_LONG || tag == TAG_DOUBLE)
				entries[++i] = dup;
		}
	}
	
	/**
	 * Returns the number of active entries.
	 *
	 * @return The active entry count.
	 * @since 2016/08/17
	 */
	public int activeCount()
	{
		return this._nextadx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/17
	 */
	@Override
	public __PoolEntry__ get(int __dx)
	{
		return this._entries[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/02
	 */
	@Override
	public int size()
	{
		return this._entries.length;
	}
}

