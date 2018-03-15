// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

/**
 * This represents the base class for all array accessors.
 *
 * @since 2018/02/21
 */
public interface Array
{
	/**
	 * Returns the length of the array.
	 *
	 * @return The array length.
	 * @since 2018/03/01
	 */
	public abstract int length();
}

