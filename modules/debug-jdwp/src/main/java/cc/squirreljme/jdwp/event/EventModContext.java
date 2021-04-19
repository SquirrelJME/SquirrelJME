// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
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
	
	/* End. */
	;
}
