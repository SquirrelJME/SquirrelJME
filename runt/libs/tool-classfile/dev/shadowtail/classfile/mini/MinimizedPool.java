// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.nncc.AccessedField;
import dev.shadowtail.classfile.nncc.FieldAccessTime;
import dev.shadowtail.classfile.nncc.FieldAccessType;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.xlate.InvokeType;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;

/**
 * This represents the minimized constant pool.
 *
 * @since 2019/04/16
 */
public final class MinimizedPool
{
	/** Entry types. */
	private final byte[] _types;
	
	/** Parts. */
	private final int[][] _parts;
	
	/** Values. */
	final Object[] _values;
	
	/**
	 * Initializes the minimized pool.
	 *
	 * @param __ts Types.
	 * @param __ps Parts.
	 * @param __vs Values.
	 * @throws IllegalArgumentException If the arrays are of a different size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public MinimizedPool(byte[] __ts, int[][] __ps, Object[] __vs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__ts == null || __ps == null || __vs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC3y  Input arrays are of a different length.
		// (The expected length)}
		int n = __ts.length;
		if (__ps.length != n || __vs.length != n)
			throw new IllegalArgumentException("JC3y " + n);
		
		// Defensive copy each
		__ts = __ts.clone();
		__ps = __ps.clone();
		__vs = __vs.clone();
		
		// Clone all elements in the parts
		for (int i = 0; i < n; i++)
		{
			int[] p = __ps[i];
			
			if (p == null)
				throw new NullPointerException("NARG");
			
			__ps[i] = p.clone();
		}
		
		// Check values for null, skip entry 0
		for (int i = 1; i < n; i++)
			if (__vs[i] == null)
				throw new NullPointerException("NARG");
		
		// Set
		this._types = __ts;
		this._parts = __ps;
		this._values = __vs;
	}
	
	/**
	 * Decodes the minimized constant pool.
	 *
	 * @param __n Number of entries in the pool.
	 * @param __is The bytes to decode from.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @return The resulting minimized pool.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static final MinimizedPool decode(int __n, byte[] __is, int __o,
		int __l)
		throws IndexOutOfBoundsException, InvalidClassFormatException,
			NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __is.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Types and offsets
		byte[] types = new byte[__n];
		int[] offsets = new int[__n];
		
		// Debug
		todo.DEBUG.note("Decode %d (%d bytes @ %d)", __n, __l, __o);
		
		// Read the type and offset table
		try (DataInputStream dis = new DataInputStream(
			new ByteArrayInputStream(__is, __o, __l)))
		{
			// Read type table
			dis.readFully(types);
			
			// {@squirreljme.error JC44 Pool uneven padding byte was not
			// 255. (The value it was)}
			if ((__n & 1) != 0)
			{
				int val;
				if ((val = dis.readUnsignedByte()) != 0xFF)
					throw new InvalidClassFormatException("JC44 " + val);
			}
			
			// Read offsets into the structure
			for (int i = 0; i < __n; i++)
				offsets[i] = dis.readUnsignedShort();
			
			// Debug
			for (int i = 0; i < __n; i++)
				todo.DEBUG.note("%3d: %02x (@%d)", i,
					(types[i] & 0xFF), offsets[i]);
		}
		
		// {@squirreljme.error JC3z Invalid read of pool data.}
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JC3z", e);
		}
		
		// Output pool entry types, values, and parts
		int[][] parts = new int[__n][];
		Object[] values = new Object[__n];
		
		// Zero int for empty parts
		int[] nopart = new int[0];
		
		// Re-build individual pool entries
		Object[] entries = new Object[__n];
		for (int i = 0; i < __n; i++)
		{
			// Get type and pointer
			int rtype = types[i],
				ptr = offsets[i];
			
			// Is this wide?
			boolean iswide;
			if ((iswide = ((rtype & 0x80) != 0)))
			{
				rtype &= 0x7F;
				
				// Re-adjust type array since we use this for the type list
				types[i] = (byte)rtype;
			}
			
			// Read info
			int[] part = null;
			Object v = null;
			
			// It is easier to handle this as a stream of bytes
			try (DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(__is, __o + ptr, __l - ptr)))
			{
				// Depends on the type
				MinimizedPoolEntryType type = MinimizedPoolEntryType.of(rtype);
				switch (type)
				{
						// Null is nothing, so do not bother
					case NULL:
						// {@squirreljme.error JC42 NULL pool entry is only
						// permitted as the first type, the class file is
						// corrupt.}
						if (i != 0)
							throw new InvalidClassFormatException("JC42");
						break;
						
						// Strings
					case STRING:
						part = new int[]{
								dis.readUnsignedShort(),
								dis.readUnsignedShort(),
							};
						v = dis.readUTF();
						break;
						
						// Integer
					case INTEGER:
						v = dis.readInt();
						break;
						
						// Long
					case LONG:
						v = dis.readLong();
						break;
						
						// Float
					case FLOAT:
						v = Float.intBitsToFloat(dis.readInt());
						break;
						
						// Double
					case DOUBLE:
						v = Double.longBitsToDouble(dis.readLong());
						break;
						
						// Types which consist of parts
					case CLASS_NAME:
					case CLASS_NAMES:
					case ACCESSED_FIELD:
					case FIELD_DESCRIPTOR:
					case INVOKED_METHOD:
					case METHOD_DESCRIPTOR:
						// Wide parts
						if (iswide)
						{
							// Read length
							int np = dis.readUnsignedShort();
							
							// Read values
							part = new int[np];
							for (int j = 0; j < np; j++)
								part[j] = dis.readUnsignedShort();
						}
						
						// Narrow parts
						else
						{
							// Read length
							int np = dis.readUnsignedByte();
							
							// Read values
							part = new int[np];
							for (int j = 0; j < np; j++)
								part[j] = dis.readUnsignedByte();
						}
						
						// Now that the parts were read, we can build the
						// actual value
						switch (type)
						{
								// Class name
							case CLASS_NAME:
								v = new ClassName((String)values[part[0]]);
								break;
								
								// Names of classes
							case CLASS_NAMES:
								{
									// Read class count
									int ncn = part[0];
									
									// Read class names
									ClassName[] names = new ClassName[ncn];
									for (int j = 0, k = 1; j < ncn; j++, k++)
										names[j] = (ClassName)values[part[k]];
									
									// Build names
									v = new ClassNames(names);
								}
								break;
								
								// Field which was accessed
							case ACCESSED_FIELD:
								v = new AccessedField(
									FieldAccessTime.of(part[0]),
									FieldAccessType.of(part[1]),
									new FieldReference(
										(ClassName)values[part[2]],
										new FieldName((String)values[part[3]]),
										((ClassName)values[part[4]]).field()));
								break;
								
								// Field descriptor
							case FIELD_DESCRIPTOR:
								v = ((ClassName)values[part[0]]).field();
								break;
								
								// Invoked method
							case INVOKED_METHOD:
								v = new InvokedMethod(
									InvokeType.of(part[0]),
									new MethodHandle(
										(ClassName)values[part[1]],
										new MethodName(
											(String)values[part[2]]),
										(MethodDescriptor)values[part[3]]));
								break;
							
								// Method descriptor
							case METHOD_DESCRIPTOR:
								v = new MethodDescriptor(
									(String)values[part[0]]);
								break;
								
							default:
								throw new todo.OOPS(type.name());
						}
						break;
					
					default:
						throw new todo.OOPS(type.name());
				}
			}
			
			// {@squirreljme.error JC3x Invalid read of pool data.}
			catch (IllegalArgumentException|IOException e)
			{
				throw new InvalidClassFormatException("JC3x", e);
			}
			
			// Debug
			todo.DEBUG.note("Read %s", v);
			
			// Set data
			parts[i] = (part == null ? nopart : part);
			values[i] = v;
		}
		
		// Build
		return new MinimizedPool(types, parts, values);
	}
}

