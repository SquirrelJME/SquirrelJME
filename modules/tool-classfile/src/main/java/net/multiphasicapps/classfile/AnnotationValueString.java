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

/**
 * This represents an annotation value which is a string.
 *
 * @since 2018/06/16
 */
public final class AnnotationValueString
	implements AnnotationValue
{
	/** The string value. */
	protected final String value;
	
	/**
	 * Initializes the string value.
	 *
	 * @param __v The value used
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/01
	 */
	public AnnotationValueString(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this.value = __v;
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
	public final String toString()
	{
		throw new todo.TODO();
	}
}

