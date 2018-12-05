// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This class contains static methods which are replaced by the compiler to
 * provide native access to memory.
 *
 * @since 2017/12/27
 */
public final class MemoryAccess
{
	/**
	 * Not used.
	 *
	 * @since 2017/12/27
	 */
	private MemoryAccess()
	{
	}
	
	/**
	 * Returns the amount of memory that is available for the VM to use.
	 *
	 * @return The amount of free memory.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native long freeMemory();
	
	/**
	 * Performs or hints to the garbage collector that it should run.
	 *
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native void gc();
	
	/**
	 * Returns the maximum amount of memory that the virtual machine will
	 * attempt to use, if there is no limit then {@link Long#MAX_VALUE} will
	 * be used.
	 *
	 * @return The maximum amount of memory available to the virtual machine.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native long maxMemory();
	
	/**
	 * Returns the total amount of memory that is being used by the virtual
	 * machine. This is a count of all the memory claimed by the virtual
	 * machine itself for its memory pools and such.
	 *
	 * @return The amount of memory being used by the virtual machine.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native long totalMemory();
}

