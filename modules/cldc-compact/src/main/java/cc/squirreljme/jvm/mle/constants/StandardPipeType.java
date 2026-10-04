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
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface StandardPipeType
{
	/** Standard input. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STDIN =
		0;
	
	/** Standard output. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STDOUT =
		1;
	
	/** Standard error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STDERR =
		2;
	
	/** The number of standard pipes. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_STANDARD_PIPES =
		3;
}
