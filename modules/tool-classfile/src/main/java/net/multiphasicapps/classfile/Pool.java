// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Arrays;

/**
 * This class decodes the constant pool and provides generic access to the
 * contents of it.
 *
 * @since 2017/06/08
 */
public final class Pool
	implements Contexual
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
	
	/** Combined raw data. */
	private final Object[] _combined;
	
	/** Entries within the constant pool. */
	private final Object[] _entries;
	
	/** The constant pool tags. */
	private final int[] _tags;
	
	/** Raw bytes for the constant pool. */
	private final byte[] _rawBytes;
	
	/**
	 * Parses and initializes the constant pool structures.
	 *
	 * @param __rawBytes Raw constant pool bytes.
	 * @param __combined Combined data.
	 * @param __tags The pool tags.
	 * @param __e The entries which make up the pool, this is used directly.
	 * @since 2017/06/08
	 */
	Pool(byte[] __rawBytes, Object[] __combined, int[] __tags, Object... __e)
	{
		this._rawBytes = __rawBytes;
		this._combined = __combined;
		this._tags = __tags;
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
		
		/* {@squirreljme.error JC3o The specified index is not within the bounds
		of the constant pool. (The index of the entry)} */
		Object[] entries = this._entries;
		if (__i < 0 || __i >= entries.length)
			throw new InvalidClassFormatException(
				String.format("JC3o %d", __i), this);
		
		/* {@squirreljme.error JC3p The specified entry's class is not of the
		expected class. (The index of the entry; The class the entry is; The
		expected class)} */
		Object val = entries[__i];
		if (val != null && !__cl.isInstance(val))
			throw new InvalidClassFormatException(
				String.format("JC3p %d %s %s", __i, val.getClass(), __cl), this);
		
		return __cl.cast(val);
	}
	
	/**
	 * Returns the raw constant pool bytes.
	 *
	 * @return The raw bytes for the constant pool.
	 * @since 2024/01/20
	 */
	public byte[] rawData()
	{
		return this._rawBytes;
	}
	
	/**
	 * Returns the raw data for the given entry.
	 *
	 * @param __i The index.
	 * @return The raw data.
	 * @since 2024/06/09
	 */
	public Object rawData(int __i)
	{
		Object[] combined = this._combined;
		if (__i < 0 || __i >= combined.length)
			throw new InvalidClassFormatException(
				String.format("JC3o %d", __i), this);
		
		return combined[__i];
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
		/* {@squirreljme.error JC3q Expected the specified constant pool entry
		to not be the null entry. (The index; The expected class)} */
		C rv = this.<C>get(__cl, __i);
		if (rv == null)
			throw new InvalidClassFormatException(
				String.format("JC3q %d %s", __i, __cl), this);
		return rv;
	}
	
	/**
	 * Returns the size of the constant pool.
	 *
	 * @return The constant pool size.
	 * @since 2023/08/09
	 */
	public int size()
	{
		return this._entries.length;
	}
	
	/**
	 * Returns the constant pool tags.
	 *
	 * @return The tags used for the constant pool.
	 * @since 2023/08/09
	 */
	public int[] tags()
	{
		return this._tags.clone();
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
		return Pool.decode(__in, -1);
	}
	
	/**
	 * Decodes the constant pool.
	 *
	 * @param __in The input stream.
	 * @param __count The pool count, may be negative if it should be read.
	 * @return The read constant pool.
	 * @throws InvalidClassFormatException If the constant pool is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public static Pool decode(DataInputStream __in, int __count)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Raw byte output
		ByteArrayOutputStream rawBytes = new ByteArrayOutputStream();
		DataOutputStream raw = new DataOutputStream(rawBytes);
		
		// Does the count need to be read?
		int count;
		if (__count < 0)
			count = __in.readUnsignedShort();
		else
			count = __count;
		
		// Raw parsed data
		Object[] combinedData = new Object[count];
		
		// Read the raw constant pool contents first
		int[] tags = new int[count];
		Object[] rawdata = new Object[count];
		for (int i = 1; i < count; i++)
		{
			// Read tag
			int tag = __in.readUnsignedByte();
			tags[i] = tag;
			
			// Send to raw
			raw.writeByte(tag);
			
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
				case Pool.TAG_UTF8:
					try
					{
						data = new UTFConstantEntry(__in.readUTF());
						
						raw.writeUTF(data.toString());
					}
					
					/* {@squirreljme.error JC3r Modified UTF-8 data is not in
					the correct format.} */
					catch (UTFDataFormatException e)
					{
						throw new InvalidClassFormatException("JC3r", e);
					}
					break;
					
					// Reference to two entries
				case Pool.TAG_FIELDREF:
				case Pool.TAG_METHODREF:
				case Pool.TAG_INTERFACEMETHODREF:
				case Pool.TAG_NAMEANDTYPE:
					data = new int[]{__in.readUnsignedShort(),
						__in.readUnsignedShort()};
					
					raw.writeShort(((int[])data)[0]);
					raw.writeShort(((int[])data)[1]);
					break;
					
					// References to single entry
				case Pool.TAG_CLASS:
				case Pool.TAG_STRING:
					data = new int[]{__in.readUnsignedShort()};
					
					raw.writeShort(((int[])data)[0]);
					break;
					
					// Integer
				case Pool.TAG_INTEGER:
					data = new ConstantValueInteger(
						Integer.valueOf(__in.readInt()));
					
					raw.writeInt(((ConstantValueInteger)data).intValue());
					break;
					
					// Long
				case Pool.TAG_LONG:
					data = new ConstantValueLong(
						Long.valueOf(__in.readLong()));
					
					raw.writeLong(((ConstantValueLong)data).longValue());
					break;
					
					// Float
				case Pool.TAG_FLOAT:
					data = new ConstantValueFloat(
						Float.valueOf(__in.readFloat()));
					
					raw.writeFloat(((ConstantValueFloat)data).floatValue());
					break;
					
					// Double
				case Pool.TAG_DOUBLE:
					data = new ConstantValueDouble(
						Double.valueOf(__in.readDouble()));
					
					raw.writeDouble(((ConstantValueDouble)data).doubleValue());
					break;
					
					// Invoke dynamic method handle
				case Pool.TAG_METHODHANDLE:
					__in.readByte();
					__in.readShort();
					
					data = new UnsupportedInvokeDynamic();
					break;
					
					// Invoke dynamic method type
				case Pool.TAG_METHODTYPE:
					__in.readShort();
					
					data = new UnsupportedInvokeDynamic();
					break;
					
					// Invoke dynamic
				case Pool.TAG_INVOKEDYNAMIC:
					__in.readShort();
					__in.readShort();
					
					data = new UnsupportedInvokeDynamic();
					break;
				
					/* {@squirreljme.error JC3t Unknown tag type in the
					constant pool. (The constant pool tag)} */
				default:
					throw new InvalidClassFormatException(
						String.format("JC3t %d", tag));
			}
			rawdata[i] = data;
			
			// Combine
			combinedData[i] = data;
			
			// Skip long/double?
			if (tag == Pool.TAG_LONG || tag == Pool.TAG_DOUBLE)
			{
				rawdata[++i] = new WideConstantTopEntry();
				tags[i] = Pool.TAG_WIDETOP;
			}
		}
		
		// Go through tags again and initialize their raw data into type-safe
		// classes 
		Object[] entries = new Object[count];
		try
		{
			Pool.__initializeEntries(entries, tags, rawdata);
		}
		
		/* {@squirreljme.error JC3u The constant pool is not valid.} */
		catch (ClassCastException|IndexOutOfBoundsException|
			NullPointerException e)
		{
			throw new InvalidClassFormatException("JC3u", e);
		}
		
		// Setup
		return new Pool(rawBytes.toByteArray(), combinedData, tags, entries);
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
			int tag = __tags[i];
			int sequence;
			
			// Determine the sequence based on the tag
			switch (tag)
			{
				case 0:
				case Pool.TAG_UTF8:
				case Pool.TAG_INTEGER:
				case Pool.TAG_FLOAT:
				case Pool.TAG_LONG:
				case Pool.TAG_DOUBLE:
				case Pool.TAG_WIDETOP:
					sequence = 0;
					break;
				
				case Pool.TAG_CLASS:
				case Pool.TAG_STRING:
					sequence = 1;
					break;
				
				case Pool.TAG_NAMEANDTYPE:
					sequence = 2;
					break;
				
				case Pool.TAG_FIELDREF:
				case Pool.TAG_METHODREF:
				case Pool.TAG_INTERFACEMETHODREF:
					sequence = 3;
					break;
					
					// Invoke dynamics which are ignored
				case Pool.TAG_METHODHANDLE:
				case Pool.TAG_METHODTYPE:
				case Pool.TAG_INVOKEDYNAMIC:
					sequence = 3;
					break;
					
				default:
					throw Debugging.oops();
			}
			
			// Add to order
			order[i] = (sequence << 16) | i;
		}
		Arrays.sort(order);
		
		// Go through and process all of the entries now that their major
		// sequence order is known.
		for (int j = 0; j < count; j++)
		{
			int i = order[j] & 0xFFFF;
			int tag = __tags[i];
			
			// Process tags
			Object in = __rawdata[i];
			Object out;
			switch (tag)
			{
					// These are copied directly
				case 0:
				case Pool.TAG_UTF8:
				case Pool.TAG_INTEGER:
				case Pool.TAG_FLOAT:
				case Pool.TAG_LONG:
				case Pool.TAG_DOUBLE:
				case Pool.TAG_WIDETOP:
					out = in;
					break;
					
					// Name of a class
				case Pool.TAG_CLASS:
					out = new ClassName(__entries[((int[])in)[0]].toString());
					break;
					
					// String constant
				case Pool.TAG_STRING:
					out = new ConstantValueString(
						__entries[((int[])in)[0]].
							toString());
					break;
					
					// Name and type information
				case Pool.TAG_NAMEANDTYPE:
					{
						int[] ina = (int[])in;
						out = new NameAndType(
							__entries[ina[0]].toString(),
							__entries[ina[1]].toString());
					}
					break;
					
					// Field and method references
				case Pool.TAG_FIELDREF:
				case Pool.TAG_METHODREF:
				case Pool.TAG_INTERFACEMETHODREF:
					{
						int[] ina = (int[])in;
						ClassName cn = (ClassName)__entries[ina[0]];
						NameAndType nat = (NameAndType)__entries[ina[1]];
						
						if (tag == Pool.TAG_FIELDREF)
							out = new FieldReference(cn,
								new FieldName(nat.name()),
								new FieldDescriptor(nat.type()));
						else
							out = new MethodReference(cn,
								new MethodName(nat.name()),
								new MethodDescriptor(nat.type()),
								tag == Pool.TAG_INTERFACEMETHODREF);
					}
					break;
					
					// Invoke dynamics which are ignored
				case Pool.TAG_METHODHANDLE:
				case Pool.TAG_METHODTYPE:
				case Pool.TAG_INVOKEDYNAMIC:
					out = in;
					break;
				
					// Unhandled, should not happen
				default:
					throw new RuntimeException(String.format("OOPS %d", tag));
			}
			
			__entries[i] = out;
		}
	}
}

