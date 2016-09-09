// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITMethodReference;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This is the base class for generic blob output which is shared across
 * modern CPU architectures.
 *
 * @since 2016/07/26
 */
public class GenericOutput
	implements JITOutput
{
	/** The output configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/**
	 * Initializes the generic output.
	 *
	 * @param __conf The configuration used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public GenericOutput(JITOutputConfig.Immutable __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Initializes a method writer which is used to output machine code which
	 * was compiled from byte code.
	 *
	 * @param __dos The stream where machine code is to be written to.
	 * @param __mr Contains the class, name, and type of the current method.
	 * @return The generic method writer.
	 * @throws JITException If it could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	protected final GenericMethodWriter methodWriter(OutputStream __os,
		JITMethodReference __mr)
		throws JITException, NullPointerException
	{
		return new GenericMethodWriter(this.config, __os, __mr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	public final JITNamespaceWriter beginNamespace(String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new GenericNamespaceWriter(this, __ns);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public final JITOutputConfig.Immutable config()
	{
		return this.config;
	}
	
	/**
	 * Initializes a method writer which is used to output machine code which
	 * was compiled from byte code.
	 *
	 * @param __os The stream to write machine code to.
	 * @param __mr Contains the class, name, and type of the current method.
	 * @return The generic method writer.
	 * @since 2016/08/21
	 */
	final GenericMethodWriter __methodWriter(OutputStream __os,
		JITMethodReference __mr)
	{
		return methodWriter(__os, __mr);
	}
}

