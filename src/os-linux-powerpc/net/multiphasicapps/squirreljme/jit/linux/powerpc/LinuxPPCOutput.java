// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.linux.powerpc;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.powerpc.PPCLogicAcceptor;

/**
 * This is an output which generates either cached binaries or directly
 * executable PowerPC code fragments.
 *
 * @since 2016/07/06
 */
public class LinuxPPCOutput
	implements JITOutput
{
	/** The configuration used. */
	protected final JITOutputConfig.Immutable config;
	
	/**
	 * Creates a new output which targets PowerPC systems.
	 *
	 * @param __conf The configuration to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public LinuxPPCOutput(JITOutputConfig.Immutable __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public JITNamespaceWriter beginNamespace(String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new LinuxPPCNamespaceWriter(__ns, this.config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public String executableName()
	{
		// Always starts as SquirrelJME
		StringBuilder sb = new StringBuilder("squirreljme");
		
		// Then just append the triplet since Linux supports the file name
		// with no issues
		sb.append('_');
		sb.append(config.triplet().toString());
		
		// Finish
		return sb.toString();
	}
}

