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
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This represents an annotation along with its value.
 *
 * @since 2018/03/06
 */
@Deprecated
public final class AnnotationElement
{
	/** The annotation type. */
	protected final BinaryName type;
	
	/** The visibility of this annotation. */
	protected final AnnotationRuntimeVisibility visibility;
	
	/** Annotation value pairs. */
	private final Map<MethodName, AnnotationValuePair> _pairs;
	
	/**
	 * Initializes the annotation value with the given value sets.
	 *
	 * @param __v The visibiity of the annotation.
	 * @param __type The class name indicating the type of the annotation.
	 * @param __vals The values for the annotation.
	 * @throws InvalidClassFormatException If the annotation value is not of
	 * the correct type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public AnnotationElement(AnnotationRuntimeVisibility __v,
		BinaryName __type, AnnotationValuePair... __vals)
		throws InvalidClassFormatException, NullPointerException
	{
		this(__v, __type, Arrays.<AnnotationValuePair>asList(
			(__vals != null ? __vals : new AnnotationValuePair[0])));
	}
	
	/**
	 * Initializes the annotation value with the given value sets.
	 *
	 * @param __v The visibiity of the annotation.
	 * @param __type The class name indicating the type of the annotation.
	 * @param __vals The values for the annotation.
	 * @throws InvalidClassFormatException If the annotation value is not of
	 * the correct type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public AnnotationElement(AnnotationRuntimeVisibility __v,
		BinaryName __type, Iterable<AnnotationValuePair> __vals)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__v == null || __vals == null)
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
	public final BinaryName type()
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
	 * Returns the value pairs that are used by this annotation as a map with
	 * the method name as the key.
	 *
	 * @return The annotation value pairs.
	 * @since 2018/05/14
	 */
	public final Map<MethodName, AnnotationValuePair> valueMap()
	{
		return UnmodifiableMap.<MethodName, AnnotationValuePair>of(
			this._pairs);
	}
	
	/**
	 * Returns only the value pairs that are used which additionally have
	 * the key value specified.
	 *
	 * @return The value pairs that are used.
	 * @since 2018/06/16
	 */
	public final Collection<AnnotationValuePair> valuePairs()
	{
		return UnmodifiableCollections.<AnnotationValuePair>of(
			this._pairs.values());
	}
	
	/**
	 * Returns the visibility of this annotation.
	 *
	 * @return The visibility of the annotation.
	 * @since 2018/05/15
	 */
	public final AnnotationRuntimeVisibility visibility()
	{
		return this.visibility;
	}
	
	/**
	 * Parses a single annotation.
	 *
	 * @param __vis The visibility of this annotation.
	 * @param __pool The constant pool.
	 * @param __in The input stream for the data.
	 * @return The read and parsed annotation.
	 * @throws InvalidClassFormatException If the class format is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public static AnnotationElement parse(AnnotationRuntimeVisibility __vis,
		Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__vis == null || __pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

