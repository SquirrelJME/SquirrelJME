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
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final AnnotationValue get(int __i)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/06/16
	 */
	@Override
	public final int size()
	{
		throw Debugging.todo();
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
}

