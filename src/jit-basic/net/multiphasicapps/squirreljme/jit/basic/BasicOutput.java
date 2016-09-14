// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutput;

/**
 * This is the output that is used to write to the JIT.
 *
 * @since 2016/09/10
 */
public class BasicOutput
	implements JITOutput
{
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/**
	 * Initializes the basic output.
	 *
	 * @param __conf The configuration used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public BasicOutput(JITConfig __conf)
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITNamespaceWriter beginNamespace(String __ns, OutputStream __os)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new BasicNamespaceWriter(this.config, __ns, __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITConfig config()
	{
		return this.config;
	}
}

