// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * The type of memory profile that is used.
 *
 * @since 2021/02/19
 */
@Exported
public interface MemoryProfileType
{
	/** Minimal memory. */
	@Exported
	byte MINIMAL =
		-1;
	
	/** Normal memory. */
	@Exported
	byte NORMAL =
		0;
}
