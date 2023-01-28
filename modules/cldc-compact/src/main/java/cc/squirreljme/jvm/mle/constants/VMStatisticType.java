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
 * Used to get a statistic from the VM.
 *
 * @since 2020/06/17
 */
@Exported
public interface VMStatisticType
{
	/** Unspecified. */
	@Exported
	byte UNSPECIFIED =
		0;
	
	/** The amount of free memory. */
	@Exported
	byte MEM_FREE =
		1;
	
	/** The maximum amount of memory. */
	@Exported
	byte MEM_MAX =
		2;
	
	/** The amount of used memory. */
	@Exported
	byte MEM_USED =
		3;
	
	/**
	 * The number of possible threads, if the virtual machine is
	 * {@link ThreadModelType#SINGLE_COOP_THREAD} then this should always
	 * return 1.
	 */
	@Exported
	byte CPU_THREAD_COUNT =
		4;
	
	/** The number of statistics. */
	@Exported
	byte NUM_STATISTICS =
		5;
}
