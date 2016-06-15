// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim.os.linux;

import net.multiphasicapps.squirrelsim.OSProvider;

/**
 * This provides an interface which is used for simulating a Linux based
 * user space application.
 *
 * @since 2016/06/14
 */
public class LinuxProvider
	implements OSProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/14
	 */
	@Override
	public String name()
	{
		return "linux";
	}
}

