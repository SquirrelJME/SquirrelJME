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
 * Used to get a statistic from the VM.
 *
 * @since 2020/06/17
 */
public interface VMStatisticType
{
	/** Unspecified. */
	byte UNSPECIFIED =
		0;
	
	/** The amount of free memory. */
	byte MEM_FREE =
		1;
	
	/** The maximum amount of memory. */
	byte MEM_MAX =
		2;
	
	/** The amount of used memory. */
	byte MEM_USED =
		3;
	
	/** The number of statistics. */
	byte NUM_STATISTICS =
		4;
}
