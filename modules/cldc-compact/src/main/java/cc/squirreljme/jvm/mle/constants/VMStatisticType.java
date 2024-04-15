// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	
	/**
	 * The number of possible threads, if the virtual machine is
	 * {@link ThreadModelType#SINGLE_COOP_THREAD} then this should always
	 * return 1.
	 */
	byte CPU_THREAD_COUNT =
		4;
	
	/** The number of statistics. */
	byte NUM_STATISTICS =
		5;
}
