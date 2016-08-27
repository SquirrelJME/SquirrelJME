// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents a basic block within a program. A basic block contains
 * micro operations.
 *
 * @since 2016/08/27
 */
@Deprecated
public final class JITBasicBlock
	extends AbstractList<JITMicroOp>
{
	/** Storage of micro-operations. */
	private final List<JITMicroOp> _ops =
		new ArrayList<>();
	
	/**
	 * Initializes the basic block.
	 *
	 * @since 2016/08/27
	 */
	public JITBasicBlock()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final void add(int __i, JITMicroOp __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		this._ops.add(__i, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final JITMicroOp get(int __i)
	{
		return this._ops.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final JITMicroOp set(int __i, JITMicroOp __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		return this._ops.set(__i, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public JITMicroOp remove(int __i)
	{
		return this._ops.remove(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final int size()
	{
		return this._ops.size();
	}
}

