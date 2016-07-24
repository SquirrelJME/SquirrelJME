// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe;

/**
 * This is the base interface for all executable outputs that the target
 * builder will use to generate a binary.
 *
 * Instances of this class should not be reused.
 *
 * @since 2016/07/23
 */
public interface ExecutableOutput
{
	/**
	 * Adds a system property to be included in the target binary.
	 *
	 * @param __k The the key.
	 * @param __v The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public abstract void addSystemProperty(String __k, String __v)
		throws NullPointerException;
}

