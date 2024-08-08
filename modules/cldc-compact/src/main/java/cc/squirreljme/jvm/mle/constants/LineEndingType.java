// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Constants for line ending.
 *
 * @since 2020/06/09
 */
@SquirrelJMEVendorApi
public interface LineEndingType
{
	/** Unknown. */
	@SquirrelJMEVendorApi
	byte UNSPECIFIED =
		0;
	
	/** LF. */
	@SquirrelJMEVendorApi
	byte LF =
		1;
	
	/** CR. */
	@SquirrelJMEVendorApi
	byte CR =
		2;
	
	/** CRLF. */
	@SquirrelJMEVendorApi
	byte CRLF =
		3;
		
	/** Number of line types. */
	@SquirrelJMEVendorApi
	byte NUM_LINE_ENDINGS =
		4;
}
