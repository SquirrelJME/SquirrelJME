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
 * This interface is used to represent generic access to a program that exists
 * within the kernel.
 *
 * Since the kernel always uses tasks for permission checking, the instance
 * of this class should pass that information to the kernel as needed.
 *
 * Instances of this class will be used as keys so it must implement
 * {@link #equals(Object)} and {@link #hashCode()} to where even two
 * instances of this class which point to the same program refer to that
 * instance.
 *
 * @since 2017/12/10
 */
public interface SystemProgram
{
	/**
	 * Returns the index of the program.
	 *
	 * @return The program index.
	 * @since 2017/12/27
	 */
	public abstract int index();
}

