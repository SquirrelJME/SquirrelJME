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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableArrayList;

/**
 * This represents an annotation along with its value.
 *
 * @since 2018/03/06
 */
public final class AnnotationElement
{
	/** The annotation type. */
	protected final ClassName type;
	
	/** Annotation value pairs. */
	private final AnnotationValuePair[] _pairs;
	
	/**
	 * Initializes the annotation value with the given value sets.
	 *
	 * @param __type The class name indicating the type of the annotation.
	 * @param __vals The values for the annotation.
	 * @throws InvalidClassFormatException If the annotation value is not of
	 * the correct type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public AnnotationElement(ClassName __type, AnnotationValuePair... __vals)
		throws InvalidClassFormatException, NullPointerException
	{
		this(__type, Arrays.<AnnotationValuePair>asList(
			(__vals != null ? __vals : new AnnotationValuePair[0])));
	}
	
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
	public AnnotationElement(ClassName __type,
		Iterable<AnnotationValuePair> __vals)
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
	 * Returns the value pairs that are used by this annotation.
	 *
	 * @return The annotation value pairs.
	 * @since 2018/05/14
	 */
	public final List<AnnotationValuePair> valuePairs()
	{
		return UnmodifiableArrayList.<AnnotationValuePair>of(
			this._pairs);
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
	public static final AnnotationElement[] decode(Pool __pool,
		DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

