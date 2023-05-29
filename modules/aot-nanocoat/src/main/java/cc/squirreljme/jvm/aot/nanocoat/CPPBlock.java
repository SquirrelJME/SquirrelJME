// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Used to handle preprocessor if-blocks.
 *
 * @since 2023/05/29
 */
public class CPPBlock
	extends CBlock
{
	/** On final else? */
	private boolean _finalElse;
	
	/**
	 * Initializes the C Block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CPPBlock(Reference<CSourceWriter> __ref)
		throws NullPointerException
	{
		super(__ref, null);
	}
	
	/**
	 * Performs an else of the preprocessor.
	 * 
	 * @throws IOException On write errors.
	 * @throws IllegalStateException If else was already done.
	 * @since 2023/05/29
	 */
	protected void preprocessorElse()
		throws IOException, IllegalStateException
	{
		// {@squirreljme.error NC08 Cannot else else the preprocessor.}
		if (this._finalElse)
			throw new IllegalStateException("NC08");
		
		// Add line
		this.writer().preprocessorLine("else");
		
		// We cannot else anymore
		this._finalElse = true;
	}
	
	/**
	 * Performs an else if of the preprocessor.
	 * 
	 * @param __condition The condition of the else if.
	 * @throws IOException On write errors.
	 * @throws IllegalStateException If else was already done.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	protected void preprocessorElseIf(Object... __condition)
		throws IOException, IllegalStateException, NullPointerException
	{
		if (__condition == null || __condition.length == 0)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC08 Cannot else else the preprocessor.}
		if (this._finalElse)
			throw new IllegalStateException("NC08");
		
		// Add line
		this.writer().preprocessorLine("elif", __condition);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	void __finish(CSourceWriter __writer)
		throws IOException, NullPointerException
	{
		// Just end with this endif
		__writer.preprocessorLine("endif");
	}
}
