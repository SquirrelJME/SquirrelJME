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

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;

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
	
	/** The code writer factory. */
	protected final NativeCodeWriterFactory codewriter;
	
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
		
		// {@squirreljme.error BV01 No native code factory was set in the
		// configuration. (The configuration)}
		NativeCodeWriterFactory codewriter = __conf.<NativeCodeWriterFactory>
			getAsClass(BasicOutputFactory.NATIVE_CODE_PROPERTY,
			NativeCodeWriterFactory.class);
		if (codewriter == null)
			throw new JITException(String.format("BV01 %s", __conf));
		this.codewriter = codewriter;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITNamespaceWriter beginNamespace(String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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

