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
	protected final Object constval;
	
	/** Annotated values. */
	private final AnnotationElement[] _annotations;
	
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
	 * @throws NullPointerException On null arguments, except for {@code __cv}.
	 * @since 2017/10/02
	 */
	Field(FieldFlags __f, FieldName __n, FieldDescriptor __t, Object __cv,
		AnnotationElement[] __avs)
		throws NullPointerException
	{
		if (__f == null || __n == null || __t == null || __avs == null)
			throw new NullPointerException("NARG");
		
		this.flags = __f;
		this.name = __n;
		this.type = __t;
		this.constval = __cv;
		this._annotations = __avs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final List<AnnotationElement> annotatedElements()
	{
		return UnmodifiableArrayList.<AnnotationElement>of(
			this._annotations);
	}
	
	/**
	 * Returns the constant value of the field.
	 *
	 * @return The field constant value.
	 * @since 2018/05/14
	 */
	public final Object constantValue()
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
			
			// Annotated values
			Set<AnnotationElement> avs = new LinkedHashSet<>();
			
			// Handle attributes
			int na = __in.readUnsignedShort();
			String[] attr = new String[1];
			int[] alen = new int[1];
			Object constval = null;
			for (int j = 0; j < na; j++)
				try (DataInputStream ai = ClassFile.__nextAttribute(__in,
					__pool, attr, alen))
				{
					// Parse annotations?
					if (ClassFile.__maybeParseAnnotation(__pool, attr[0], avs,
						ai))
						continue;
					
					// Only care about the constant value attribute
					if (!"ConstantValue".equals(attr[0]))
						continue;
					
					// {@squirreljme.error JC0q There may only be one constant
					// value for a field.}
					if (constval != null)
						throw new InvalidClassFormatException("JC0q");
					
					// {@squirreljme.error JC0r Expected the constant value
					// of a field to be a constant value type, not one that
					// is another type. (The value that was input)}.
					Object read = __pool.<Object>require(Object.class,
						ai.readUnsignedShort());
					if (!(read instanceof Integer || read instanceof Long ||
						read instanceof Float || read instanceof Double ||
						read instanceof String) || read == null)
						throw new InvalidClassFormatException(String.format(
							"JC0r %s", read));
					
					// Set
					constval = read;
				}
			
			// Create field
			rv[i] = new Field(flags, name, type, constval,
				avs.<AnnotationElement>toArray(new AnnotationElement[avs.size()]));
		}
		
		// All done!
		return rv;
	}
}

