// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.constants;

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
	
	/** The address where the ROM is located. */
	byte ROM_ADDRESS =
		2;
	
	/** The number of attributes. */
	byte NUM_RUNTIME_ATTRIBUTES =
		3;
}
