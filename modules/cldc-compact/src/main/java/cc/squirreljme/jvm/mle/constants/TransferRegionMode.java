// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * The mode to use when transferring regions from one pencil to another.
 *
 * @since 2026/10/20
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface TransferRegionMode
{
	/**
	 * Disregard the current blending mode and force SRC when drawing
	 * ontop of the destination.
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC_FORCE = 1;
	
	/**
	 * Optimized transfer of regions going scanline by scanline in the
	 * fashion of @{code memmove()}, any transformations are
	 * disregarded.
	 *
	 * This requires that the source and destination pixel format have the
	 * same density, so a 16-bit pixel format may only ever be copied to
	 * another 16-bit pixel format.
	 *
	 * This implies {@link #SRC_FORCE}.
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MEMMOVE = 2;
}
