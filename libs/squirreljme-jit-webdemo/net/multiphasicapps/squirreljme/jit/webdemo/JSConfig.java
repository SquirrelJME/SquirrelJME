// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.webdemo;

import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigSerializer;

/**
 * Javascript configuration.
 *
 * @since 2017/03/14
 */
public class JSConfig
	extends JITConfig<JSConfig>
{
	/**
	 * Initializes the configuration.
	 *
	 * @sine 2017/03/14
	 */
	public JSConfig()
	{
		super("generic.bits", "32");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public JITConfigSerializer<JSConfig> serializer()
	{
		throw new todo.TODO();
	}
}

