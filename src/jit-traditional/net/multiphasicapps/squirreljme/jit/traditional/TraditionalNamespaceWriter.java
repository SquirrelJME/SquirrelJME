// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.traditional;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITTriplet;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This provides writing of namespaces which are to be used on traditional
 * CPUs. The data here is contained within blobs which contain a table of
 * contents, native machine code, and other details needed for code execution.
 *
 * @since 2016/07/08
 */
public abstract class TraditionalNamespaceWriter
	implements JITNamespaceWriter
{
	/** The output configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/** The target triplet. */
	protected final JITTriplet triplet;
	
	/** The endianess of the CPU. */
	protected final JITCPUEndian endian;
	
	/** The number of bits it uses. */
	protected final int bits;
	
	/** The namespace being written. */
	protected final String namespace;
	
	/**
	 * This initializes the base traditional namespace writer.
	 *
	 * @param __ns The namespace being written.
	 * @param __conf The output configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/08
	 */
	public TraditionalNamespaceWriter(String __ns,
		JITOutputConfig.Immutable __conf)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __conf == null)
			throw new NullPointerException("NARG");
		
		// Get triplet to determine CPU details
		this.config = __conf;
		JITTriplet triplet = __conf.triplet();
		this.triplet = triplet;
		
		// Set
		this.namespace = __ns;
		this.endian = triplet.endianess();
		this.bits = triplet.bits();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/08
	 */
	@Override
	public JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/08
	 */
	@Override
	public OutputStream beginResource(String __name)
		throws JITException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/08
	 */
	@Override
	public void close()
	{
	}
}

