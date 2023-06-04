// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;
import java.lang.ref.Reference;

/**
 * C function.
 *
 * @since 2023/05/31
 */
public class CFunctionBlock
	extends CBlock
{
	/**
	 * Initializes the C function block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CFunctionBlock(Reference<CSourceWriter> __ref)
		throws NullPointerException
	{
		super(__ref, "}");
	}
	
	/**
	 * Initializes the switch case.
	 * 
	 * @param __condition The condition.
	 * @return The switch case writer.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public CSwitch switchCase(Object... __condition)
		throws IOException
	{
		CSourceWriter writer = this.writer();
		
		// Write up tokens for the switch
		writer.tokens("switch", "(", __condition, ")", "{");
		
		// Push
		CSwitch rv = new CSwitch(writer._selfRef);
		return writer.__pushBlock(rv);
	}
}
