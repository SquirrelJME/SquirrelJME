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

import net.multiphasicapps.squirreljme.classformat.StackMapType;

/**
 * This specifies the reason for the change in an active binding's binding.
 *
 * @since 2017/03/04
 */
@Deprecated
public enum ActiveBindingChangeType
{
	/** The binding is to be cleared, no value is stored here. */
	CLEARED(null),
	
	/** Change to store integer. */
	TO_INTEGER(StackMapType.INTEGER),
	
	/** Change to store integer. */
	TO_LONG(StackMapType.LONG),
	
	/** Change to store integer. */
	TO_FLOAT(StackMapType.FLOAT),
	
	/** Change to store integer. */
	TO_DOUBLE(StackMapType.DOUBLE),
	
	/** Change to store integer. */
	TO_OBJECT(StackMapType.OBJECT),
	
	/** End. */
	;
	
	/** The target type. */
	protected final StackMapType target;
	
	/**
	 * Sets the target type for the binding.
	 *
	 * @param __t The target type used.
	 * @since 2017/03/04
	 */
	private ActiveBindingChangeType(StackMapType __t)
	{
		this.target = __t;
	}
	
	/**
	 * Returns the target type.
	 *
	 * @return The target type.
	 * @since 2017/03/04
	 */
	public final StackMapType target()
	{
		return this.target;
	}
}

