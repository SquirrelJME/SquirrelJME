// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents the constant pool of a class which has been read, this is
 * only temporarily used during the translation of the class.
 *
 * @since 2016/06/29
 */
class __ClassPool__
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
	
	/** Constant pool tags used. */
	private final byte[] _tags;
	
	/** Constant pool initialized data. */
	private final Object[] _data;
	
	/**
	 * Decodes the constant pool of an input class file.
	 *
	 * @param __dis The input stream to read for class files.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws JITException If the constant pool is malformed.
	 * @since 2016/06/29
	 */
	__ClassPool__(DataInputStream __dis)
		throws IOException, NullPointerException, JITException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ED15 The input class has an empty constant
		// pool.}
		int count = __dis.readUnsignedShort();
		if (count <= 0)
			throw new JITException("ED15");
		
		// The set of pool tags
		byte[] tags = new byte[count];
		
		// Setup data
		Object[] data = new Object[count];
		
		// Decode all entry data
		for (int i = 1; i < count; i++)
		{
			// Read the tag
			int tag = __dis.readUnsignedByte();
			
			// {@squirreljme.error ED16 Java ME does not support dynamic
			// invocation (such as method handles or lambda expressions).}
			if (tag == TAG_METHODHANDLE || tag == TAG_METHODTYPE ||
				tag == TAG_INVOKEDYNAMIC)
				throw new JITException("ED16");
			
			// Store tag
			tags[i] = (byte)tag;
			
			// UTF-8 String
			if (tag == TAG_UTF8)
			{
				// Read the string data
				try
				{
					data[i] = __dis.readUTF();
				}
				
				// {@squirreljme.error ED18 The modified UTF-8 data string in
				// the constant pool is malformed.}
				catch (UTFDataFormatException e)
				{
					throw new JITException("ED18", e);
				}
			}
			
			// Field and Method references, Name and type
			else if (tag == TAG_FIELDREF || tag == TAG_METHODREF ||
				tag == TAG_INTERFACEMETHODREF || tag == TAG_NAMEANDTYPE)
				data[i] = new int[]
					{
						__dis.readUnsignedShort(),
						__dis.readUnsignedShort()
					};
			
			// Class reference, string
			else if (tag == TAG_CLASS || tag == TAG_STRING)
				data[i] = new int[]
					{
						__dis.readUnsignedShort()
					};
			
			// Integer
			else if (tag == TAG_INTEGER)
				data[i] = Integer.valueOf(__dis.readInt());
			
			// Long
			else if (tag == TAG_LONG)
				data[i] = Long.valueOf(__dis.readLong());
			
			// Float
			else if (tag == TAG_FLOAT)
				data[i] = Float.valueOf(__dis.readFloat());
			
			// Double
			else if (tag == TAG_DOUBLE)
				data[i] = Double.valueOf(__dis.readDouble());
			
			// {@squirreljme.error ED17 Unknown constant pool tag. (The tag of
			// the constant pool entry)}
			else
				throw new JITException(String.format("ED17 %d", tag));
			
			// Double up?
			if (tag == TAG_LONG || tag == TAG_DOUBLE)
				i++;
		}
		
		// Set
		this._tags = tags;
		this._data = data;
	}
	
	/**
	 * Obtains the index at the specified position as the given type.
	 *
	 * @param <R> The type of value to get.
	 * @param __dx The index of the entry.
	 * @param __cl The expected class type.
	 * @return The value at the given location.
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R get(int __dx, Class<R> __cl)
		throws JITException, NullPointerException
	{
		// Get
		R rv = this.<R>optional(__dx, __cl);
		
		// {@squirreljme.error ED0b No constant pool entry was defined at
		// this position. (The index; The expected type)}
		if (rv == null)
			throw new JITException(String.format("ED0b %d %s", __dx, __cl));
		
		// Ok
		return rv;
	}
	
	/**
	 * Obtains the index at the specified position as the given type, if the
	 * index is zero then {@code null} is returned.
	 *
	 * @param <R> The type of value to get.
	 * @param __dx The index of the entry, zero will return {@code null}.
	 * @param __cl The expected class type.
	 * @return The value at the given location or {@code null} if zero was
	 * requested.
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R optional(int __dx, Class<R> __cl)
		throws JITException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Zero is always null
		if (__dx == 0)
			return null;
		
		// {@squirreljme.error ED0c The requested index is not within the
		// constant pool bounds. (The index)}
		Object[] data = this._data;
		int n = data.length;
		if (__dx < 0 || __dx >= n)
			throw new JITException(String.format("ED0c %d", __dx));
		
		// Get raw data
		Object raw = data[__dx];
		
		// {@squirreljme.error ED0d The requested entry does not contain a
		// value because it is the top of a long or double constant value.
		// (The index)}
		if (raw == null)
			throw new JITException(String.format("ED0d %d", __dx));
		
		// If an integer array, requires conversion
		if (raw instanceof int[])
		{
			// Get input fields
			int[] fields = (int[])raw;
			raw = null;
			
			// Depends on the tag
			byte tag = this._tags[__dx];
			switch (tag)
			{
					// Strings
				case TAG_STRING:
					raw = this.<String>get(fields[0], String.class);
					break;
					
					// {@squirreljme.error ED0f Could not obtain the constant
					// pool entry information because its tag data relation is
					// not known. (The index; The tag type)}
				default:
					throw new JITException(String.format("ED0f %d %d", __dx,
						tag));
			}
			
			// {@squirreljme.error ED0g The field data was never translated
			// to known useable data. (The index)}
			if (raw == null)
				throw new NullPointerException(String.format("ED0g %d", __dx));
			
			// Reset
			data[__dx] = raw;
		}
		
		// {@squirreljme.error ED0e The value at the given index was not of
		// the expected class type. (The index; The expected type; The type
		// that it was)}
		if (!__cl.isInstance(raw))
			throw new JITException(String.format("ED0e %d %s", __dx, __cl,
				raw.getClass()));
		
		// Cast
		return __cl.cast(raw);
	}
	
	/**
	 * Returns the size of the constant pool.
	 *
	 * @return The constant pool size.
	 * @since 2016/07/02
	 */
	public int size()
	{
		return this._tags.length;
	}
}

