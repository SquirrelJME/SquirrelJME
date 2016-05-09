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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIMethod;

/**
 * This represents a Java byte code program.
 *
 * @since 2016/04/27
 */
public class NRProgram
	extends AbstractList<NRBasicBlock>
{
	/** The operation count. */
	protected final int count;
	
	/** Blocks which are available. */
	private final NRBasicBlock[] _blocks;
	
	/**
	 * Initializes the program representation.
	 *
	 * @param __blocks The basic blocks which make up the program.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	public NRProgram(List<NRBasicBlock> __blocks)
		throws NullPointerException
	{
		// Check
		if (__blocks == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public NRBasicBlock get(int __i)
	{
		return _blocks[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public int size()
	{
		return count;
	}
}

