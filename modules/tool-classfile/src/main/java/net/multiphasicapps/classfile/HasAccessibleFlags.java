// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface represents anything that has accessible flags.
 *
 * @since 2018/09/09
 */
public interface HasAccessibleFlags
{
	/**
	 * Returns accessible flags.
	 *
	 * @return The accessible flags.
	 * @since 2018/09/09
	 */
	public abstract AccessibleFlags flags();
}

