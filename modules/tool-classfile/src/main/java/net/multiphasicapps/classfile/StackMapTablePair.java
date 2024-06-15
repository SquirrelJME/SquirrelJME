// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.UnmodifiableIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a pair of stack map tables, for input and output.
 *
 * @since 2023/07/03
 */
public final class StackMapTablePair
	implements Contexual, Iterable<StackMapTableState>
{
	/** The input table. */
	public final StackMapTableState input;
	
	/** The output table. */
	public final StackMapTableState output;
	
	/**
	 * Initializes the stack map pair.
	 * 
	 * @param __input The input.
	 * @param __output The output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public StackMapTablePair(StackMapTableState __input,
		StackMapTableState __output)
		throws NullPointerException
	{
		if (__input == null || __output == null)
			throw new NullPointerException("NARG");
		
		this.input = __input;
		this.output = __output;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	public Iterator<StackMapTableState> iterator()
	{
		return UnmodifiableIterator.of(this.input, this.output);
	}
}
