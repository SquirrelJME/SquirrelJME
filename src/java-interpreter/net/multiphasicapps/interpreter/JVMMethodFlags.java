// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This is the set of method flags.
 *
 * @since 2016/03/19
 */
public final class JVMMethodFlags
	extends JVMMemberFlags<JVMMethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __b The bits used for the method.
	 * @since 2016/03/19
	 */
	public JVMMethodFlags(int __b)
	{
		super(__b, JVMMethodFlag.class, JVMMethodFlag.allFlags());
	}
}

