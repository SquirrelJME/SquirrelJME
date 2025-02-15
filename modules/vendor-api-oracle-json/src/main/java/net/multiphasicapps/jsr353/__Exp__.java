// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

/**
 * An expectation, replaces tons of booleans.
 *
 * @since 2015/04/12
 */
public enum __Exp__
{
	/** Expecting a key. */
	KEY,
	
	/** Name of a key or end of object. */
	KEY_OR_END,
	
	/** Expect a colon. */
	COLON,
	
	/** A kind of value. */
	VALUE,
	
	/** Value or end of array. */
	VALUE_OR_END,
	
	/** Expecting comma or end. */
	COMMA_OR_END,
	
	/** End. */
	;
}
