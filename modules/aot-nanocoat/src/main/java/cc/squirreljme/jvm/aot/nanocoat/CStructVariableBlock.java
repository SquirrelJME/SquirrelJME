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
 * Defines a struct variable block.
 *
 * @since 2023/05/29
 */
public class CStructVariableBlock
	extends CBlock
{
	/** Which member index is this? */
	private volatile int _index;
	
	/**
	 * Initializes the struct variable writer.
	 * 
	 * @param __writer The writer used.
	 * @since 2023/05/29
	 */
	CStructVariableBlock(Reference<CSourceWriter> __writer)
	{
		super(__writer, "};");
	}
	
	/**
	 * Writes a member of a struct.
	 * 
	 * @param __memberName The member name.
	 * @param __value The value to write.
	 * @return {@code this}.
	 * @throws IOException on read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CStructVariableBlock memberSet(String __memberName,
		Object... __value)
		throws IOException, NullPointerException
	{
		if (__memberName == null || __value == null || __value.length == 0)
			throw new NullPointerException("NARG");
		
		CSourceWriter writer = this.writer();
		
		// Do we need to prefix with a comma?
		int index = this._index;
		if (index > 0)
			writer.token(",");
		
		// Write out member setting
		writer.tokens("." + __memberName, __value);
		
		// For later
		this._index = index + 1;
		
		throw Debugging.todo();
	}
}
