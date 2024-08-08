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
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
@SquirrelJMEVendorApi
public interface StandardPipeType
{
	/** Standard input. */
	@SquirrelJMEVendorApi
	byte STDIN =
		0;
	
	/** Standard output. */
	@SquirrelJMEVendorApi
	byte STDOUT =
		1;
	
	/** Standard error. */
	@SquirrelJMEVendorApi
	byte STDERR =
		2;
	
	/** The number of standard pipes. */
	@SquirrelJMEVendorApi
	byte NUM_STANDARD_PIPES =
		3;
}
