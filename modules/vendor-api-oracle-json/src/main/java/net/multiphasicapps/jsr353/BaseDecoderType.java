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
 * The type of thing that was just read.
 *
 * @since 2014/08/03
 */
public enum BaseDecoderType
{
	/** Start of an object. */
	START_OBJECT,
	
	/** End of object. */
	END_OBJECT,
	
	/** Start of an array. */
	START_ARRAY,
	
	/** End of array. */
	END_ARRAY,
	
	/** A string (quoted). */
	STRING,
	
	/** A literal (number, true, false, null). */
	LITERAL,
	
	/** A colon. */
	COLON,
	
	/** A comma. */
	COMMA,
	
	/** End of stream. */
	END_OF_STREAM,
	
	/** End. */
	;
}
