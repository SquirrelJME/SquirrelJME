// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This class contains immutable launch parameters which were constructed by
 * the builder.
 *
 * @since 2016/11/08
 */
class __ImmutableLaunchParameters__
	implements KernelLaunchParameters
{
	/**
	 * Constructs the launch parameters.
	 *
	 * @param __b The source builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	__ImmutableLaunchParameters__(KernelLaunchParametersBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
	}
}

