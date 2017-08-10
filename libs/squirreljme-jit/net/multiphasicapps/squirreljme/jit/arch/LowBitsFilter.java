// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

/**
 * This is a filter for the machine code output which is used to translate
 * operations which require more bits than the CPU can handle.
 *
 * This is only needed for targets which have registers with a bit size lower
 * than 64-bits as they need operations to be performed using more locations.
 * This class makes it so the native output can fail fast if an unsupported
 * register size is used. This also changes the representation of values
 * between the translator and the native system to handle a lower number of
 * bits.
 *
 * @since 2017/08/10
 */
public class LowBitsFilter
	extends MachineCodeOutput
{
	/**
	 * Initializes the filter.
	 *
	 * @param __o The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public LowBitsFilter(MachineCodeOutput __o)
		throws NullPointerException
	{
		super(__o.config());
		
		throw new todo.TODO();
	}
}

