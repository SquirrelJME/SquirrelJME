// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This interface is used for classes which can get their endianess obtained.
 *
 * @since 2016/08/11
 */
public interface GettableEndianess
{
	/**
	 * Obtains the current default endianess of the data.
	 *
	 * @return The current endianess.
	 * @since 2016/07/10
	 */
	public abstract DataEndianess getEndianess();
}

