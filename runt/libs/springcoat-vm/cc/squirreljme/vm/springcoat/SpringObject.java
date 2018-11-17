// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This represents anything that is treated by the virtual machine as a kind
 * of object reference. This is needed for SquirrelJME since it exposes a
 * pointer logic which is magically handled by the virtual machine.
 *
 * @since 2018/09/08
 */
public interface SpringObject
{
	/**
	 * Returns the monitor for this object.
	 *
	 * @return This object's monitor.
	 * @since 2018/09/15
	 */
	public abstract SpringMonitor monitor();
	
	/**
	 * Returns the object type.
	 *
	 * @return The object type.
	 * @since 2018/09/09
	 */
	public abstract SpringClass type();
}

