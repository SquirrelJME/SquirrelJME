// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This interface represents any array instance.
 *
 * @since 2019/01/10
 */
public interface ArrayInstance
	extends Instance
{
	/**
	 * Sets the index to the specified value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public abstract void set(int __i, Value __v)
		throws NullPointerException;
}

