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
 * The type of memory profile that is used.
 *
 * @since 2021/02/19
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface MemoryProfileType
{
	/** Minimal memory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MINIMAL =
		-1;
	
	/** Normal memory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NORMAL =
		0;
}
