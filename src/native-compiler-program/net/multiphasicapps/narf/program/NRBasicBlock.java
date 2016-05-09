// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.program;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a basic block within the program which performs functions.
 *
 * @since 2016/04/27
 */
public class NRBasicBlock
	extends AbstractList<NROp>
{
	/** The operation count. */
	protected final int count;
	
	/** Operations which are available. */
	private final NROp[] _ops;
	
	/**
	 * Initializes the basic block representation.
	 *
	 * @param __ops Input operations.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/09
	 */
	public NRBasicBlock(List<NROp> __ops)
		throws NullPointerException
	{
		// Check
		if (__ops == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/09
	 */
	@Override
	public NROp get(int __i)
	{
		return _ops[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/09
	 */
	@Override
	public int size()
	{
		return count;
	}
}

