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
 * The type of memory profile that is used.
 *
 * @since 2021/02/19
 */
@SquirrelJMEVendorApi
public interface MemoryProfileType
{
	/** Minimal memory. */
	@SquirrelJMEVendorApi
	byte MINIMAL =
		-1;
	
	/** Normal memory. */
	@SquirrelJMEVendorApi
	byte NORMAL =
		0;
}
