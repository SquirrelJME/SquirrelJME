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

import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.xlate.DataType;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldFlags;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents a field which has been minimized.
 *
 * @since 2019/03/11
 */
public final class MinimizedField
{
	/** The flags for this field. */
	public final int flags;
	
	/** Offset to the field data. */
	public final int offset;
	
	/** The size of this field. */
	public final int size;
	
	/** The field name. */
	public final FieldName name;
	
	/** The field type. */
	public final FieldDescriptor type;
	
	/** The field value. */
	public final Object value;
	
	/** The data type. */
	public final DataType datatype;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** Field flags. */
	private Reference<FieldFlags> _flags;
	
	/** Name and type. */
	private Reference<FieldNameAndType> _nat;
	
	/**
	 * Initializes the minimized field.
	 *
	 * @param __f The field flags.
	 * @param __o The offset.
	 * @param __s The size.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @param __v The value of this field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/11
	 */
	public MinimizedField(int __f, int __o, int __s, FieldName __n,
		FieldDescriptor __t, Object __v)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.flags = __f;
		this.offset = __o;
		this.size = __s;
		this.name = __n;
		this.type = __t;
		this.value = __v;
		this.datatype = DataType.of(__t);
	}
	
	/**
	 * Returns the flags for this field.
	 *
	 * @return The flags for the field.
	 * @since 2019/04/18
	 */
	public final FieldFlags flags()
	{
		Reference<FieldFlags> ref = this._flags;
		FieldFlags rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._flags = new WeakReference<>((rv =
				new FieldFlags(this.flags)));
		
		return rv;
	}
	
	/**
	 * Returns the name and type for this field.
	 *
	 * @return The name and type for this field.
	 * @since 2019/09/21
	 */
	public final FieldNameAndType nameAndType()
	{
		Reference<FieldNameAndType> ref = this._nat;
		FieldNameAndType rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._nat = new WeakReference<>(
				(rv = new FieldNameAndType(this.name, this.type)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/11
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Field %s:%s @ %d (%d bytes), flags=%x, value=%s", this.name,
				this.type, this.offset, this.size, this.flags, this.value)));
		
		return rv;
	}
	
	/**
	 * Decodes the field data.
	 *
	 * @param __n The number of fields.
	 * @param __p The constant pool.
	 * @param __b Input data.
	 * @param __o Offset into array.
	 * @param __l Length of data rea.
	 * @return The decoded fields.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws InvalidClassFormatException If the fields could not be decoded.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public static final MinimizedField[] decodeFields(int __n,
		DualClassRuntimePool __p, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, InvalidClassFormatException,
			NullPointerException
	{
		if (__b == null || __p == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Setup return value
		MinimizedField[] rv = new MinimizedField[__n];
		
		// Read field data
		try (DataInputStream dis = new DataInputStream(
			new ByteArrayInputStream(__b, __o, __l)))
		{
			for (int i = 0; i < __n; i++)
			{
				rv[i] = new MinimizedField(
					dis.readInt(),
					dis.readUnsignedShort(),
					dis.readUnsignedShort(),
					new FieldName(__p.getByIndex(false,
						dis.readUnsignedShort()).<String>value(String.class)),
					__p.getByIndex(false, dis.readUnsignedShort()).
						<ClassName>value(ClassName.class).field(),
					__p.getByIndex(false, dis.readUnsignedShort()).value);
				
				// Data type and padding, the data type here is determined by
				// the field so we do not need to use this pre-calculated
				// value
				dis.readUnsignedByte();
				dis.readUnsignedByte();
			}
		}
		
		// {@squirreljme.error JC05 Could not read field data.}
		catch (ClassCastException|IOException|IndexOutOfBoundsException e)
		{
			throw new InvalidClassFormatException("JC05");
		}
		
		return rv;
	}
}

