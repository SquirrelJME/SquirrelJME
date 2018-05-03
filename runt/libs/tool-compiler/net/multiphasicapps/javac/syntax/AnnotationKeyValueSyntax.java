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

import net.multiphasicapps.classfile.MethodName;

/**
 * This represents an annotation value which is a value assigned to a key.
 *
 * @since 2018/05/03
 */
public final class AnnotationKeyValueSyntax
	implements AnnotationValueSyntax
{
	/** The key. */
	protected final MethodName key;
	
	/** The value. */
	protected final AnnotationValueSyntax value;
	
	/**
	 * Initializes the key/value pair.
	 *
	 * @param __k The key.
	 * @param __v The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public AnnotationKeyValueSyntax(MethodName __k, AnnotationValueSyntax __v)
		throws NullPointerException
	{
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		this.key = __k;
		this.value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

