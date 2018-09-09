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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableArrayList;

/**
 * This represents a field in a class which is used to store values either as
 * static instances or as instance variables outside of methods.
 *
 * @since 2017/09/30
 */
public final class Field
	extends Member
	implements Annotated
{
	/** The flags for the field. */
	protected final FieldFlags flags;
	
	/** The name of the field. */
	protected final FieldName name;
	
	/** The descriptor of the field. */
	protected final FieldDescriptor type;
	
	/** The constant value, if there is none then this is {@code null}. */
	protected final ConstantValue constval;
	
	/** Annotated values. */
	protected final AnnotationTable annotations;
	
	/** Name and type reference. */
	private volatile Reference<FieldNameAndType> _nameandtype;
	
	/**
	 * Initializes the field.
	 *
	 * @param __f The flags for the field.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @param __cv The constant value of the field, may be {@code null}.
	 * @param __avs Annotated values.
	 * @throws InvalidClassFormatException If the class format is not valid.
	 * @throws NullPointerException On null arguments, except for {@code __cv}.
	 * @since 2017/10/02
	 */
	Field(FieldFlags __f, FieldName __n, FieldDescriptor __t,
		ConstantValue __cv, AnnotationTable __avs)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__f == null || __n == null || __t == null || __avs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC21 The constant value is not compatible with
		// the given field type. (The value; The value type; The field type)}
		if (__cv != null && !__cv.type().isCompatibleWith(__t))
			throw new InvalidClassFormatException(String.format(
				"JC21 %s %s %s", __cv, __cv.type(), __t));
		
		this.flags = __f;
		this.name = __n;
		this.type = __t;
		this.constval = __cv;
		this.annotations = __avs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final AnnotationTable annotationTable()
	{
		return this.annotations;
	}
	
	/**
	 * Returns the constant value of the field.
	 *
	 * @return The field constant value.
	 * @since 2018/05/14
	 */
	public final ConstantValue constantValue()
	{
		return this.constval;
	}
	
	/**
	 * Returns the flags for the field.
	 *
	 * @return The field flags.
	 * @since 2017/10/12
	 */
	public final FieldFlags flags()
	{
		return this.flags;
	}
	
	/**
	 * Is this an instance field?
	 *
	 * @return If this is an instance field or not.
	 * @since 2108/09/08
	 */
	public final boolean isInstance()
	{
		return !this.flags.isStatic();
	}
	
	/**
	 * Is this field static?
	 *
	 * @return If this field is static.
	 * @since 2018/09/09
	 */
	public final boolean isStatic()
	{
		return this.flags.isStatic();
	}
	
	/**
	 * Returns the name of the field.
	 *
	 * @return The field name.
	 * @since 2018/05/14
	 */
	public final FieldName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the name and type of the method.
	 *
	 * @return The method name and type.
	 * @since 2017/10/12
	 */
	public final FieldNameAndType nameAndType()
	{
		Reference<FieldNameAndType> ref = this._nameandtype;
		FieldNameAndType rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._nameandtype = new WeakReference<>(
				rv = new FieldNameAndType(this.name, this.type));
		
		return rv;
	}
	
	/**
	 * Returns the field type.
	 *
	 * @return The field type.
	 * @since 2018/05/14
	 */
	public final FieldDescriptor type()
	{
		return this.type;
	}
	
	/**
	 * Decodes all fields from the input class data.
	 *
	 * @param __ver The version of the class.
	 * @param __tn The name of the owning class.
	 * @param __cf The flags for the owning class.
	 * @param __pool The constant pool for the class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/30
	 */
	public static Field[] decode(ClassVersion __ver, ClassName __tn,
		ClassFlags __cf, Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__ver == null || __tn == null || __cf == null || __pool == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		int nf = __in.readUnsignedShort();
		Field[] rv = new Field[nf];
		Set<NameAndType> dup = new HashSet<>();
		
		// Parse fields
		for (int i = 0; i < nf; i++)
		{
			FieldFlags flags = new FieldFlags(__cf, __in.readUnsignedShort());
			FieldName name = new FieldName(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			FieldDescriptor type = new FieldDescriptor(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			
			// {@squirreljme.error JC0p A duplicate method exists within the
			// class. (The method name; The method descriptor)}
			if (!dup.add(new NameAndType(name.toString(), type.toString())))
				throw new InvalidClassFormatException(String.format(
					"JC0p %s %s", name, type));
			
			// Handle attributes
			AttributeTable attrs = AttributeTable.parse(__pool, __in);
			
			// Parse annotations
			AnnotationTable annotations = AnnotationTable.parse(__pool, attrs);
			
			// Parse constant value if there is one
			ConstantValue constval = null;
			Attribute cvalattr = attrs.get("ConstantValue");
			if (cvalattr != null)
				try (DataInputStream ai = cvalattr.open())
				{
					constval = __pool.<ConstantValue>require(
						ConstantValue.class, ai.readUnsignedShort());
				}
			
			// Create field
			rv[i] = new Field(flags, name, type, constval, annotations);
		}
		
		// All done!
		return rv;
	}
}

