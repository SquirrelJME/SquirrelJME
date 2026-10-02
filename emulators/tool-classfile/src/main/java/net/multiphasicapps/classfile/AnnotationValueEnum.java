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

/**
 * This is an annotation value which represents an enumeration.
 *
 * @since 2018/06/16
 */
public final class AnnotationValueEnum
	implements AnnotationValue
{
	/** The type name. */
	protected final FieldDescriptor type;
	
	/** The enum name. */
	protected final FieldName name;
	
	/**
	 * Initializes the annotation enumeration value.
	 *
	 * @param __type The type used.
	 * @param __name The name of the enumeration key.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/01
	 */
	public AnnotationValueEnum(FieldDescriptor __type, FieldName __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.name = __name;
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
	public final String toString()
	{
		throw Debugging.todo();
	}
}

