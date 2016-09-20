// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceAccessor;

/**
 * This is used to launch programs that want to be run on the Java SE VM.
 *
 * @since 2016/09/20
 */
public class BridgedLauncher
	implements BootLauncher
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/20
	 */
	@Override
	public boolean launch(ResourceAccessor __ra, String __main,
		String... __args)
		throws NullPointerException
	{
		// Check
		if (__ra == null || __main == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

