// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.JITNamespaceProcessorProgress;

/**
 * This is used to report progress on the JIT.
 *
 * @since 2016/09/02
 */
class __JITProgress__
	implements JITNamespaceProcessorProgress
{
	/** Where the JIT progress goes. */
	protected final PrintStream out;
	
	/**
	 * Writes JIT progress to the specified stream.
	 *
	 * @param __ps The stream to write progress to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	__JITProgress__(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __ps;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/23
	 */
	@Override
	public void progressClass(String __cl)
	{
		// Print it
		if (__cl != null)
			this.out.println(__cl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/23
	 */
	@Override
	public void progressNamespace(String __ns)
	{
		// Print it
		if (__ns != null)
			this.out.println(__ns);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/23
	 */
	@Override
	public void progressResource(String __rs)
	{
		// Print it
		if (__rs != null)
			this.out.println(__rs);
	}
}

