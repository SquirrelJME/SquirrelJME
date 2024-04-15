// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents the name of a field. It has the same constraints as
 * identifiers.
 *
 * @since 2017/07/07
 */
public final class FieldName
	extends Identifier
	implements Comparable<FieldName>, MemberName
{
	/**
	 * Initializes the field name.
	 *
	 * @param __s The field name.
	 * @since 2017/07/07
	 */
	public FieldName(String __s)
	{
		super(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
	 */
	@Override
	public int compareTo(FieldName __o)
	{
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		return (__o instanceof FieldName) && super.equals(__o);
	}
}

