// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Used to get a statistic from the VM.
 *
 * @since 2020/06/17
 */
@SquirrelJMEVendorApi
public interface VMStatisticType
{
	/** Unspecified. */
	@SquirrelJMEVendorApi
	byte UNSPECIFIED =
		0;
	
	/** The amount of free memory. */
	@SquirrelJMEVendorApi
	byte MEM_FREE =
		1;
	
	/** The maximum amount of memory. */
	@SquirrelJMEVendorApi
	byte MEM_MAX =
		2;
	
	/** The amount of used memory. */
	@SquirrelJMEVendorApi
	byte MEM_USED =
		3;
	
	/**
	 * The number of possible threads, if the virtual machine is
	 * {@link ThreadModelType#SINGLE_THREAD_COOP} then this should always
	 * return 1.
	 */
	@SquirrelJMEVendorApi
	byte CPU_THREAD_COUNT =
		4;
	
	/**
	 * The root instance identifier of the root virtual machine. This is
	 * generally a process ID or other unique identifier.
	 * 
	 * All virtual machines launched, whether they have the same process and
	 * thread, a different thread, or a different thread will have the same
	 * root instance ID.
	 */
	@SquirrelJMEVendorApi
	byte ROOT_INSTANCE_ID =
		5;
	
	/** The number of statistics. */
	@SquirrelJMEVendorApi
	byte NUM_STATISTICS =
		6;
}
