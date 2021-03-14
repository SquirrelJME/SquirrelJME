// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Indicators for what gets updated in the debugging state.
 *
 * @since 2021/03/13
 */
public enum JDWPUpdateWhat
{
	/** Thread groups. */
	THREAD_GROUPS,
	
	/** Threads. */
	THREADS,
	
	/** Loaded classes. */
	LOADED_CLASSES,
	
	/* End. */
	;
}
