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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This represents an annotation which has values assigned to keys.
 *
 * @since 2018/06/16
 */
public final class Annotation
	implements AnnotationValue
{
	/** The values for the annotation. */
	private final Map<MethodName, AnnotationValue> _values =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the annotation.
	 *
	 * @param __tn The type name to use.
	 * @param __vs The value mappings.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	private Annotation(ClassName __tn, Map<MethodName, AnnotationValue> __vs)
		throws NullPointerException
	{
		if (__tn == null || __vs == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
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
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type of this annotation.
	 *
	 * @return The annotation type.
	 * @since 2018/06/16
	 */
	public final ClassName type()
	{
		throw new todo.TODO();
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
	public static final Annotation parse(Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read the type name, which is a class
		String rawtypename;
		ClassName typename = new FieldDescriptor((rawtypename = __pool.
			<UTFConstantEntry>get(UTFConstantEntry.class,
			__in.readUnsignedShort()).toString())).className();
		
		// {@squirreljme.error JC26 Annotation type is not correct. (The type)}
		if (typename == null)
			throw new InvalidClassFormatException(String.format("JC26 %s",
				rawtypename));
		
		// Read element table
		Map<MethodName, AnnotationValue> values = new LinkedHashMap<>();
		int n = __in.readUnsignedShort();
		for (int i = 0; i < n; i++)
		{
			// Read element name
			MethodName elemname = new MethodName(__pool.<UTFConstantEntry>get(
				UTFConstantEntry.class, __in.readUnsignedShort()).toString());
			
			// Read tag, which represents the type of
			int tag = __in.readUnsignedByte();
			switch (tag)
			{
				case 's':
					throw new todo.TODO();
				
				case 'e':
					throw new todo.TODO();
				
				case 'c':
					throw new todo.TODO();
				
				case '@':
					throw new todo.TODO();
				
				case '[':
					throw new todo.TODO();
				
					// {@squirreljme.error JC25 Invalid tag specified in
					// annotation. (The tag used)}
				default:
					throw new InvalidClassFormatException(
						String.format("JC25 %c", tag));
			}
		}
		
		return new Annotation(typename, values);
	}
}

