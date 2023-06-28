// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;

/**
 * Handles C switch case.
 *
 * @since 2023/05/31
 */
public class CSwitchBlock
	extends CFunctionBlock
{
	/**
	 * Initializes the C switch case block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CSwitchBlock(CSourceWriter __ref)
		throws NullPointerException
	{
		super(__ref);
	}
	
	/**
	 * Writes a break to the case.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void breakCase()
		throws IOException
	{
		this.tokens("break", ";");
	}
	
	/**
	 * Starts the next case statement.
	 * 
	 * @param __condition The condition.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public void nextCase(Object... __condition)
		throws IOException, NullPointerException
	{
		if (__condition == null || __condition.length == 0)
			throw new NullPointerException("NARG");
		
		this.tokens("case", __condition, ":");
	}
}
