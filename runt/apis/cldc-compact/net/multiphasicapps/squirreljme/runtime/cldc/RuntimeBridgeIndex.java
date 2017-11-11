// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This contains indexes to be used for run-time bridge access.
 *
 * @since 2017/11/10
 */
public interface RuntimeBridgeIndex
{
	/** Access to the clock. */
	public static final int CLOCK =
		1;
	
	/** High memory access. */
	public static final int HIGH_MEMORY =
		2;
	
	/** Controls objects. */
	public static final int OBJECT =
		3;
	
	/** Standard process pipes. */
	public static final int PIPE =
		4;
	
	/** Access to processes. */
	public static final int PROCESS =
		5;
	
	/** Version information. */
	public static final int VERSION =
		6;
}

