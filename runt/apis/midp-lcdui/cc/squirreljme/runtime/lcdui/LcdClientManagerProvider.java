// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.service.ServiceClientProvider;

/**
 * This class contains the client used to initialize the LCD client.
 *
 * @since 2018/03/15
 */
public final class LcdClientManagerProvider
	implements ServiceClientProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/15
	 */
	@Override
	public final Object initializeClient(ServiceCaller __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

