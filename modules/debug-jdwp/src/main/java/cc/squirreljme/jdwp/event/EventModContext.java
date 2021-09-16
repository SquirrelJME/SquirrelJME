// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

/**
 * This is the context used for an event modifier, this changes how a value
 * should be interpreted when it is checked.
 *
 * @since 2021/04/17
 */
public enum EventModContext
{
	/** Parameter with a type. */
	PARAMETER_TYPE,
	
	/** Parameter for stepping checking. */
	PARAMETER_STEPPING,
	
	/** Parameter with a type or field. */
	PARAMETER_TYPE_OR_FIELD,
	
	/** Parameter with a field. */
	PARAMETER_FIELD,
	
	/** Current thread. */
	CURRENT_THREAD,
	
	/** Current location in code. */
	CURRENT_LOCATION,
	
	/** The current type we are calling from. */
	CURRENT_TYPE,
	
	/** The current instance we are calling from. */
	CURRENT_INSTANCE,
	
	/* End. */
	;
}
