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

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This represents an annotation value that is an array.
 *
 * @since 2018/06/16
 */
public final class AnnotationValueArray
	extends AbstractList<AnnotationValue>
	implements AnnotationValue, RandomAccess
{
	/** The elements of the array. */
	private final AnnotationValue[] _values;
	
	/**
	 * Initializes the values.
	 *
	 * @param __vs The input values.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/01
	 */
	public AnnotationValueArray(AnnotationValue... __vs)
		throws NullPointerException
	{
		for (AnnotationValue v : (__vs = (__vs == null ?
			new AnnotationValue[0] : __vs.clone())))
			if (v == null)
				throw new NullPointerException("NARG");
		
		this._values = __vs;
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
	public final AnnotationValue get(int __i)
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
}

