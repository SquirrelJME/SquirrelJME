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
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;

/**
 * This is the factory which creates instances of the basic JIT which outputs
 * using the common native code generation system.
 *
 * @since 2016/09/10
 */
public class BasicOutputFactory
	implements JITOutputFactory
{
	/** This property specifies the native code writer factory to use. */
	public static final String NATIVE_CODE_PROPERTY =
		"net.multiphasicapps.squirreljme.jit.basic.nativecode";
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITOutput create(JITConfig __conf)
		throws JITException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new BasicOutput(__conf);
	}
}

