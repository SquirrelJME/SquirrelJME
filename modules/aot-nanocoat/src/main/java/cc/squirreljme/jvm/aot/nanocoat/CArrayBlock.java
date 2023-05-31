// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Array data block.
 *
 * @since 2023/05/31
 */
public class CArrayBlock
	extends CBlock
{
	/** The current array index. */
	private volatile int _index;
	
	/**
	 * Initializes the C array Block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CArrayBlock(Reference<CSourceWriter> __ref)
		throws NullPointerException
	{
		super(__ref, "}");
	}
	
	/**
	 * Writes a struct to the array.
	 * 
	 * @return The struct block.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public CStructVariableBlock struct()
		throws IOException
	{
		CSourceWriter writer = this.writer();
		
		// Prefix element with comma?
		int index = this._index;
		if (index > 0)
			writer.token(",");
		
		// Open block
		CStructVariableBlock rv = new CStructVariableBlock(
			writer._selfRef, "}");
		writer.token("{");
		
		// Push it to the writer
		return writer.__pushBlock(rv);
	}
}
