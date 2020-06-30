// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * The type of signal generated from the monitor.
 *
 * @since 2020/06/22
 */
public interface MonitorResultType
{
	/** NOT_INTERRUPTED. */
	byte NOT_INTERRUPTED =
		1;
	
	/** Interrupted. */
	byte INTERRUPTED =
		0;
	
	/** The object is not owned. */
	byte NOT_OWNED =
		-1;
}
