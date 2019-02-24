// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import net.multiphasicapps.classfile.FieldReference;

/**
 * Reference to a static field.
 *
 * @since 2019/02/24
 */
public final class StaticFieldReference
	implements MemoryLocation
{
	/** The field this refers to. */
	protected final FieldReference fieldref;
	
	/**
	 * Initializes the field reference.
	 *
	 * @param __f The field to refer to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public StaticFieldReference(FieldReference __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		this.fieldref = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof StaticFieldReference))
			return false;
		
		return this.fieldref.equals(((StaticFieldReference)__o).fieldref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final int hashCode()
	{
		return this.fieldref.hashCode();
	}
}

