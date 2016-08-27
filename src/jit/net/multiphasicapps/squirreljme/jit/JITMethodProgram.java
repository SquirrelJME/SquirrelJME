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

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a representation of the program to convert into native machine code
 * or another form.
 *
 * @since 2016/08/24
 */
public final class JITMethodProgram
	extends AbstractMap<JITBlockLabel, JITBasicBlock>
{
	/** Internal storage. */
	private final Map<JITBlockLabel, JITBasicBlock> _blocks =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the program.
	 *
	 * @since 2016/08/24
	 */
	public JITMethodProgram()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final Set<Map.Entry<JITBlockLabel, JITBasicBlock>> entrySet()
	{
		return this._blocks.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final JITBasicBlock get(Object __o)
	{
		return this._blocks.get(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final JITBasicBlock put(JITBlockLabel __k, JITBasicBlock __v)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Store
		return this._blocks.put(__k, __v);
	}
}

