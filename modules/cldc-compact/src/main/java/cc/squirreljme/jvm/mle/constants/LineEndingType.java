// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Constants for line ending.
 *
 * @since 2020/06/09
 */
public interface LineEndingType
{
	/** Unknown. */
	byte UNSPECIFIED =
		0;
	
	/** LF. */
	byte LF =
		1;
	
	/** CR. */
	byte CR =
		2;
	
	/** CRLF. */
	byte CRLF =
		3;
		
	/** Number of line types. */
	byte NUM_LINE_ENDINGS =
		4;
}
