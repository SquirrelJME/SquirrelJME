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
 * The type of signal generated from the monitor.
 *
 * @since 2020/06/22
 */
@Exported
public interface MonitorResultType
{
	/** NOT_INTERRUPTED. */
	@Exported
	byte NOT_INTERRUPTED =
		1;
	
	/** Interrupted. */
	@Exported
	byte INTERRUPTED =
		0;
	
	/** The object is not owned. */
	@Exported
	byte NOT_OWNED =
		-1;
}
