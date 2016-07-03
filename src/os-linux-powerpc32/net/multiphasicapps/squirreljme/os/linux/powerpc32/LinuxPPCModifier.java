// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.linux.powerpc32;

import net.multiphasicapps.squirreljme.jit.JIT;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITOSModifier;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.powerpc32.PPCJIT;

/**
 * Modifies the JIT for 32-bit PowerPC systems.
 *
 * @since 2016/07/03
 */
public class LinuxPPCModifier
	extends JITOSModifier
{
	/**
	 * Initializes the modifier.
	 *
	 * @since 2016/07/03
	 */
	public LinuxPPCModifier()
	{
		super("powerpc32", "linux");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/03
	 */
	@Override
	public JITCPUEndian defaultEndianess()
	{
		return JITCPUEndian.BIG;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/03
	 */
	@Override
	protected void modify(JIT __jit)
		throws NullPointerException
	{
		// Check
		if (__jit == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DX01 Only a specific PowerPC JIT may be
		// modified.}
		if (!(__jit instanceof PPCJIT))
			throw new JITException("DX01");
		PPCJIT jit = (PPCJIT)__jit;
	}
}

