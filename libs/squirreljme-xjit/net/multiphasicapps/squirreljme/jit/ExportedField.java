// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.link.FieldSymbol;
import net.multiphasicapps.squirreljme.jit.link.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.link.ClassExport;
import net.multiphasicapps.squirreljme.jit.link.Export;
import net.multiphasicapps.squirreljme.jit.link.FieldFlags;

/**
 * This contains a field that is exported by a class.
 *
 * @since 2017/04/04
 */
public final class ExportedField
	extends ExportedMember
{
	/** The constant value of the field, {@code null} if not set. */
	volatile Object _value;
	
	/**
	 * Initializes the exported field.
	 *
	 * @param __ce The containing class.
	 * @param __flags The flags for the field.
	 * @param __name The name of the field.
	 * @param __type The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	ExportedField(ClassExport __ce, FieldFlags __flags,
		IdentifierSymbol __name, FieldSymbol __type)
		throws NullPointerException
	{
		super(__ce, __flags, __name, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		return (__o instanceof ExportedField) && super.equals(__o);
	}
}

