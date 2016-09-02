// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic.mips;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.generic.GenericMethodWriter;
import net.multiphasicapps.squirreljme.jit.generic.GenericOutput;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This is the output which is used to generate MIPS machine code.
 *
 * @since 2016/08/07
 */
public class MIPSOutput
	extends GenericOutput
{
	/**
	 * Initializes the MIPS output.
	 *
	 * @param __conf The configuration used.
	 * @since 2016/08/07
	 */
	public MIPSOutput(JITOutputConfig.Immutable __conf)
	{
		super(__conf);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	protected GenericMethodWriter methodWriter(OutputStream __os)
		throws JITException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new MIPSMethodWriter(this.config, __os);
	}
}

