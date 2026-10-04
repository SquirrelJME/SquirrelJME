// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Constants for line ending.
 *
 * @since 2020/06/09
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface LineEndingType
{
	/** Unknown. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSPECIFIED =
		0;
	
	/** LF. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LF =
		1;
	
	/** CR. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CR =
		2;
	
	/** CRLF. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CRLF =
		3;
		
	/** Number of line ending types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_LINE_ENDINGS =
		4;
}
