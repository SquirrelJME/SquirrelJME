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
	/** Did the default case? */
	private boolean _didDefault;
	
	/** Are we at the base indentation? */
	private boolean _atBaseIndentation =
		true;
	
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
		
		// Return to the base indentation
		this.indent(-1);
		this._atBaseIndentation = true;
		this.extraIndent = 0;
	}
	
	/**
	 * Starts the default case.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/07/04
	 */
	public void defaultCase()
		throws IOException
	{
		/* {@squirreljme.error CW0b There may only be a single default case.} */
		if (this._didDefault)
			throw new IllegalStateException("CW0b");
		this._didDefault = true;
		
		// Emit label
		this.__caseToken("default", ":");
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
		
		/* {@squirreljme.error CW0c Already placed default case.} */
		if (this._didDefault)
			throw new IllegalStateException("CW0c");
		
		// Emit label
		this.__caseToken("case", __condition, ":");
	}
	
	/**
	 * Describe this. T
	 *
	 * @param __tokens The tokens to emit.
	 * @throws IOException On write errors.
	 * @since 2023/07/21
	 */
	private void __caseToken(Object... __tokens)
		throws IOException
	{
		// Indent down to make the case label, so that it is aligned with
		// the other cases...
		if (!this._atBaseIndentation)
			this.indent(-1);
		
		// Emit the tokens
		this.tokens(__tokens);
		
		// Indent up for what is in the case block, this way it is more
		// nicely formatted
		this.indent(1);
		
		// Indicate that we indented extra in this block, so we do not
		// just constantly fall to the right
		this.extraIndent = 1;
		
		// No longer the first case, do not down indent before
		this._atBaseIndentation = false;
	}
}
