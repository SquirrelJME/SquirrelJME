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
 * This is an annotation value which represents a class.
 *
 * @since 2018/06/16
 */
public final class AnnotationValueClass
	implements AnnotationValue
{
	/** The class name. */
	protected final FieldDescriptor name;
	
	/**
	 * Initializes the class annotation.
	 *
	 * @param __n The name used
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/01
	 */
	public AnnotationValueClass(FieldDescriptor __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
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

