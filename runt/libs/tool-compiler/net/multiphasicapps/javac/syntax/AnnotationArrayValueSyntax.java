// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents an annotation value that represents multiple values in an
 * array.
 *
 * @since 2018/05/02
 */
public final class AnnotationArrayValueSyntax
	implements AnnotationValueSyntax
{
	/** The value which make up this value. */
	private final AnnotationValueSyntax[] _values;
	
	/**
	 * Initializes the array value annotation.
	 *
	 * @param __v The values which make up the annotation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/02
	 */
	public AnnotationArrayValueSyntax(AnnotationValueSyntax... __v)
		throws NullPointerException
	{
		this(Arrays.<AnnotationValueSyntax>asList((__v == null ?
			new AnnotationValueSyntax[0] : __v)));
	}
	
	/**
	 * Initializes the array value annotation.
	 *
	 * @param __v The values which make up the annotation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/02
	 */
	public AnnotationArrayValueSyntax(Iterable<AnnotationValueSyntax> __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		List<AnnotationValueSyntax> values = new ArrayList<>();
		for (AnnotationValueSyntax v : __v)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			values.add(v);
		}
		
		this._values = values.<AnnotationValueSyntax>toArray(
			new AnnotationValueSyntax[values.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof AnnotationArrayValueSyntax))
			return false;
		
		AnnotationArrayValueSyntax o = (AnnotationArrayValueSyntax)__o;
		return Arrays.equals(this._values, o._values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final int hashCode()
	{
		int hash = 0;
		for (Object v : this._values)
			hash ^= v.hashCode();
		return hash;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

