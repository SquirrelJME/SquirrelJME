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
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * This represents an annotation which has values assigned to keys.
 *
 * @since 2018/06/16
 */
public final class Annotation
	extends AbstractMap<MethodName, AnnotationValue>
	implements AnnotationValue
{
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
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final Set<Map.Entry<MethodName, AnnotationValue>> entrySet()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final AnnotationValue get(Object __n)
	{
		// Will never be found
		if (__n == null || !(__n instanceof MethodName))
			return null;
		
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
	public final int size()
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
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/06/16
	 */
	public static final Annotation parse(Pool __pool, DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__pool == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

