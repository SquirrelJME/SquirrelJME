// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;

/**
 * This contains the just in time compiler.
 *
 * A JIT may only be used once for any input class, however the output of the
 * JIT may be used multiple times so that multiple JARs may be combined into
 * a single binary.
 *
 * @since 2016/07/02
 */
public abstract class JIT
	implements Runnable
{
	/** The endian of the CPU. */
	protected final JITCPUEndian endian;
	
	/** The variant of the CPU. */
	protected final JITCPUVariant cpuvariant;
	
	/** One time lock. */
	private final Object _oncelock =
		new Object();
	
	/** One time only. */
	private volatile boolean _once;
	
	/**
	 * Initializes the JIT.
	 *
	 * This performs basic sanity checks before the actual compilation of a JIT
	 * is performed.
	 *
	 * @param __fp The producer which generated this JIT.
	 * @apram __ns The namespace of the class.
	 * @param __ic The input stream of the class data.
	 * @throws JITException If the class is not correctly formed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public JIT(JITFactory.Producer __fp, String __ns, InputStream __ic)
		throws JITException, NullPointerException
	{
		// Check
		if (__fp == null || __ns == null || __ic == null)
			throw new NullPointerException("NARG");
		
		// Get some details
		this.endian = __fp.endian();
		this.cpuvariant = __fp.architectureVariant();
		
		throw new Error("TODO");
	}
	
	/**
	 * Runs the JIT compilation.
	 *
	 * @since 2016/07/03
	 */
	@Override
	public final void run()
	{
		throw new Error("TODO");
	}
}

