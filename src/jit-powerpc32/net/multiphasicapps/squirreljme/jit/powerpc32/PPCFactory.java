// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.powerpc32;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.jit.JIT;
import net.multiphasicapps.squirreljme.jit.JITFactory;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITCPUVariant;

/**
 * This factory produces JITs which generate 32-bit PowerPC code.
 *
 * @since 2016/07/02
 */
public class PPCFactory
	extends JITFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @sicne 2016/07/03
	 */
	public PPCFactory()
	{
		super("powerpc32");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/03
	 */
	@Override
	public JITCPUVariant defaultArchitectureVariant()
	{
		return PPCVariant.G1;
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
	protected JIT produceJIT(JITFactory.Producer __fp,
		String __ns, InputStream __src)
	{
		return new PPCJIT(__fp, __ns, __src);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/03
	 */
	@Override
	public boolean supportsEndianess(JITCPUEndian __end)
		throws NullPointerException
	{
		// Check
		if (__end == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__end)
		{
				// Supports big and little endian
			case BIG:
			case LITTLE:
				return true;
				
				// Unknown
			default:
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/03
	 */
	@Override
	public JITCPUVariant[] variants()
	{
		return PPCVariant.values();
	}
}

