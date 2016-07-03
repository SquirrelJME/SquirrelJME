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
		
		// {@squirreljme.error DV05 The input class has an empty constant
		// pool.}
		int count = __dis.readUnsignedShort();
		if (count <= 0)
			throw new JITException("DV05");
		
		// The set of pool tags
		byte[] tags = new byte[count];
		
		// Setup data
		Object[] data = new Object[count];
		
		// Decode all entry data
		for (int i = 1; i < count; i++)
		{
			// Read the tag
			int tag = __dis.readUnsignedByte();
			
			// {@squirreljme.error DV06 Java ME does not support dynamic
			// invocation (such as method handles or lambda expressions).}
			if (tag == TAG_METHODHANDLE || tag == TAG_METHODTYPE ||
				tag == TAG_INVOKEDYNAMIC)
				throw new JITException("DV06");
			
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
				
				// {@squirreljme.error DV08 The modified UTF-8 data string in
				// the constant pool is malformed.}
				catch (UTFDataFormatException e)
				{
					throw new JITException("DV08", e);
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
			
			// {@squirreljme.error DV07 Unknown constant pool tag. (The tag of
			// the constant pool entry)}
			else
				throw new JITException(String.format("DV07 %d", tag));
			
			// Double up?
			if (tag == TAG_LONG || tag == TAG_DOUBLE)
				i++;
		}
		
		// Set
		this._tags = tags;
		this._data = data;
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

