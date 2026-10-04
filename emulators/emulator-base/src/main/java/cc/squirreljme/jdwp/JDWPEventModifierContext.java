// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This is the context used for an event modifier, this changes how a value
 * should be interpreted when it is checked.
 *
 * @since 2021/04/17
 */
public enum JDWPEventModifierContext
{
	/** Ensnares an argument for later usage. */
	ENSNARE_ARGUMENT,
	
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
	
	/** Exception was tossed. */
	TOSSED_EXCEPTION,
	
	/* End. */
	;
}
