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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents an annotation along with its value.
 *
 * @since 2018/03/06
 */
public final class AnnotatedValue
{
	/** The annotation type. */
	protected final ClassName type;
	
	/**
	 * Initializes the annotation value with the given value sets.
	 *
	 * @param __type The class name indicating the type of the annotation.
	 * @param __vals The values for the annotation.
	 * @throws InvalidClassFormatException If the annotation value is not of
	 * the correct type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public AnnotatedValue(ClassName __type, Map<MethodName, Object> __vals)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__vals == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the annotation type.
	 *
	 * @return The annotation type.
	 * @since 2018/03/06
	 */
	public final ClassName type()
	{
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Decodes the input annotations and returns them.
	 *
	 * @param __pool The constant pool.
	 * @param __in The input data stream.
	 * @return The parsed annotation values.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public static final AnnotatedValue[] decode(Pool __pool,
		DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

