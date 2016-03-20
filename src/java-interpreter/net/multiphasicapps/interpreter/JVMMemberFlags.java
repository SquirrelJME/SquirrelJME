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

import java.util.List;

/**
 * This is the base class for member type flags.
 *
 * @param <F> The type of flags to use.
 * @since 2016/03/19
 */
public abstract class JVMMemberFlags<F extends JVMMemberFlag>
	extends JVMFlags<F>
{
	/**
	 * Initializes the member flags.
	 *
	 * @param __b The input bits.
	 * @param __type The flag type.
	 * @param __all All available flags.
	 * @since 2016/03/19
	 */
	public JVMMemberFlags(int __b, Class<F> __type, List<F> __all)
	{
		super(__b, __type, __all);
	}
}

