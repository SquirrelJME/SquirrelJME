// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This interface is used to refer to classes and resources which may be
 * part of a group.
 *
 * @since 2017/10/09
 */
public interface Groupable
{
	/**
	 * Returns the group that this instance belongs to.
	 *
	 * @return The owning group.
	 * @since 2017/10/09
	 */
	public abstract String group();
}

