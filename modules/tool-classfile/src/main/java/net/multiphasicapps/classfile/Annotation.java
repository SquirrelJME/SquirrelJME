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
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This represents an annotation which has values assigned to keys.
 *
 * @since 2018/06/16
 */
public final class Annotation
	implements AnnotationValue, Contexual
{
	/** The type name of the annotation. */
	protected final ClassName typename;
	
	/** The values for the annotation. */
	private final Map<MethodName, AnnotationValue> _values =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the annotation.
	 *
	 * @param __tn The type name to use.
	 * @param __vs The value mappings.
	 * @throws InvalidClassFormatException If an annotation key is duplicated.
	 * @throws NullPointerException On null arguments or if the value mappings
	 * contain a null value.
	 * @since 2018/09/01
	 */
	private Annotation(ClassName __tn, Map<MethodName, AnnotationValue> __vs)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__tn == null || __vs == null)
			throw new NullPointerException("NARG");
		
		this.typename = __tn;
		
		// Copy key and values over
		Map<MethodName, AnnotationValue> values = this._values;
		for (Map.Entry<MethodName, AnnotationValue> e : __vs.entrySet())
		{
			MethodName k = e.getKey();
			AnnotationValue v = e.getValue();
			
			if (k == null || v == null)
				throw new NullPointerException();
			
			/* {@squirreljme.error JC1t Duplicate key within annotation. (The
			key)} */
			if (null != values.put(k, v))
				throw new InvalidClassFormatException(String.format(
					"JC1t %s", k), this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the value for the given method name.
	 *
	 * @param __n The key to get the value for.
	 * @return The value of the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/06/16
	 */
	public final AnnotationValue get(MethodName __n)
		throws NullPointerException
	{
		// Will never be found
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the key and value map.
	 *
	 * @return The key and value map.
	 * @since 2018/10/28
	 */
	public final Map<MethodName, AnnotationValue> keyValueMap()
	{
		return UnmodifiableMap.<MethodName, AnnotationValue>of(this._values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type of this annotation.
	 *
	 * @return The annotation type.
	 * @since 2018/06/16
	 */
	public final ClassName type()
	{
		return this.typename;
	}
	
	/**
	 * Decodes a single annotation from the input data stream and returns the
	 * parsed annotation.
	 *
	 * @param __pool The constant pool.
	 * @param __in The input stream to read from.
	 * @return The parsed annotation.
	 * @throws InvalidClassFormatException If the annotation is invalid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/06/16
	 */
	public static Annotation parse(Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read the type name, which is a class
		String rawtypename;
		ClassName typename = new FieldDescriptor((rawtypename = __pool.
			<UTFConstantEntry>get(UTFConstantEntry.class,
			__in.readUnsignedShort()).toString())).className();
		
		/* {@squirreljme.error JC1u Annotation type is not correct. (The type)} */
		if (typename == null)
			throw new InvalidClassFormatException(String.format("JC1u %s",
				rawtypename));
		
		// Read element table
		Map<MethodName, AnnotationValue> values = new LinkedHashMap<>();
		int n = __in.readUnsignedShort();
		for (int i = 0; i < n; i++)
			values.put(new MethodName(__pool.<UTFConstantEntry>get(
				UTFConstantEntry.class, __in.readUnsignedShort()).toString()),
				Annotation.parseValue(__pool, __in));
		
		return new Annotation(typename, values);
	}
	
	/**
	 * Parses a single annotation value.
	 *
	 * @param __pool The pool to read from.
	 * @param __in The input stream.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the value is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/01
	 */
	public static AnnotationValue parseValue(Pool __pool,
		DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read tag, which represents the type of things
		int tag = __in.readUnsignedByte();
		switch (tag)
		{
			case 'B':
			case 'C':
			case 'I':
			case 'S':
			case 'Z':
			case 'D':
			case 'F':
			case 'J':
				return new AnnotationValuePrimitive(
					__pool.<ConstantValueNumber>get(ConstantValueNumber.class,
					__in.readUnsignedShort()).number());
			
			case 's':
				return new AnnotationValueString(
					__pool.<UTFConstantEntry>get(UTFConstantEntry.class,
					__in.readUnsignedShort()).toString());
			
			case 'e':
				return new AnnotationValueEnum(
					new FieldDescriptor(__pool.<UTFConstantEntry>get(
						UTFConstantEntry.class, __in.readUnsignedShort()).
						toString()),
					new FieldName(__pool.<UTFConstantEntry>get(
						UTFConstantEntry.class, __in.readUnsignedShort()).
						toString()));
			
			case 'c':
				return new AnnotationValueClass(new FieldDescriptor(
					__pool.<UTFConstantEntry>get(UTFConstantEntry.class,
					__in.readUnsignedShort()).toString()));
			
			case '@':
				return Annotation.parse(__pool, __in);
			
			case '[':
				{
					// Read length
					int len = __in.readUnsignedShort();
					
					// Read in all values
					AnnotationValue[] values = new AnnotationValue[len];
					for (int i = 0; i < len; i++)
						values[i] = Annotation.parseValue(__pool, __in);
					
					// Build
					return new AnnotationValueArray(values);
				}
			
				/* {@squirreljme.error JC1v Invalid tag specified in
				annotation. (The tag used)} */
			default:
				throw new InvalidClassFormatException(
					String.format("JC1v %c", tag));
		}
	}
}

