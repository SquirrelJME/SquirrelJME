// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.constants;

import cc.squirreljme.jvm.mle.constants.ByteOrderType;
import cc.squirreljme.jvm.mle.constants.MemoryProfileType;
import cc.squirreljme.runtime.cldc.lang.OperatingSystemType;

/**
 * Virtual machine attributes available at run-time.
 *
 * @since 2021/01/31
 */
public interface RuntimeVmAttribute
{
	/** Unknown. */
	byte UNKNOWN =
		0;
	
	/** The {@link OperatingSystemType} this is running on. */
	byte OPERATING_SYSTEM =
		1;
	
	/** The address where the ROM is located, low word. */
	byte ROM_ADDRESS_LOW =
		2;
	
	/** The address where the ROM is located, high word. */
	byte ROM_ADDRESS_HIGH =
		3;
	
	/** The {@link ByteOrderType} of the system. */
	byte BYTE_ORDER =
		4;
	
	/** The {@link MemoryProfileType} to use. */
	byte MEMORY_PROFILE =
		5;
	
	/** The number of attributes. */
	byte NUM_RUNTIME_ATTRIBUTES =
		6;
}
