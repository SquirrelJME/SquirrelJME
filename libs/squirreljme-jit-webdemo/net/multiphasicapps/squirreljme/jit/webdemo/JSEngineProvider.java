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
import net.multiphasicapps.squirreljme.jit.JITStateAccessor;
import net.multiphasicapps.squirreljme.jit.TranslationEngine;
import net.multiphasicapps.squirreljme.jit.TranslationEngineProvider;

/**
 * This provides translation engines to the JavaScript JIT.
 *
 * @since 2017/03/14
 */
public class JSEngineProvider
	implements TranslationEngineProvider
{
	/** The target configuration. */
	protected final JITConfig config;
	
	/**
	 * Initializes the Javascript engine provider.
	 *
	 * @since 2017/03/14
	 */
	public JSEngineProvider()
	{
		// Setup config
		this.config = new JSConfig();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public TranslationEngine createEngine(JITStateAccessor __jsa)
	{
		return new JSEngine(this.config, __jsa);
	}
}

