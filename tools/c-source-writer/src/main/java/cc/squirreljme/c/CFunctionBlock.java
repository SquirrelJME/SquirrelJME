// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.StringCollectionCTokenOutput;
import java.io.IOException;
import java.util.ArrayList;

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
	CFunctionBlock(CSourceWriter __ref)
		throws NullPointerException
	{
		this(__ref, "}");
	}
	
	/**
	 * Initializes the C function block.
	 *
	 * @param __ref The reference to use.
	 * @param __closing The closing token.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	CFunctionBlock(CSourceWriter __ref, String __closing)
		throws NullPointerException
	{
		super(__ref, __closing, true);
	}
	
	/**
	 * Starts an {@code if} comparison.
	 * 
	 * @param __condition The expression to check.
	 * @return The block for the {@code if}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public CIfBlock branchIf(CExpression __condition)
		throws IOException, NullPointerException
	{
		if (__condition == null)
			throw new NullPointerException("NARG");
		
		// Write up tokens for the switch
		this.tokens("if", "(", __condition, ")", "{");
		
		// Push
		CIfBlock rv = new CIfBlock(this);
		return this._file.__pushBlock(rv, true);
	}
	
	/**
	 * Splices the function so that multiple regions may be written at once,
	 * when the returned splices are closed then the tokens will be
	 * written to the output. This is
	 * so that there can be regions written at the same time such as local
	 * variable or the main function body.
	 * 
	 * @return The output splices.
	 * @throws IllegalArgumentException If the count is zero or negative.
	 * @throws IOException On write errors.
	 * @since 2023/07/15
	 */
	public CFunctionBlockSplices splice(int __count)
		throws IllegalArgumentException, IOException
	{
		/* {@squirreljme.error CW0m Splice with zero or negative outputs.} */
		if (__count <= 0)
			throw new IllegalArgumentException("CW0m");
		
		// Setup outputs
		__CFunctionSplice__[] blocks = new __CFunctionSplice__[__count];
		for (int i = 0; i < __count; i++)
		{
			StringCollectionCTokenOutput outTokens =
				new StringCollectionCTokenOutput(new ArrayList<String>(),
					true);
			CFile outFile = new CFile(outTokens);
			
			// Set output accordingly
			blocks[i] = new __CFunctionSplice__(outFile, outTokens,
				this);
		}
		
		// Self
		return new CFunctionBlockSplices(blocks);
	}
	
	/**
	 * Initializes the switch case.
	 * 
	 * @param __condition The condition.
	 * @return The switch case writer.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public CSwitchBlock switchCase(CExpression __condition)
		throws IOException, NullPointerException
	{
		if (__condition == null)
			throw new NullPointerException("NARG");
		
		// Write up tokens for the switch
		this.tokens("switch", "(", __condition, ")", "{");
		
		// Push
		CSwitchBlock rv = new CSwitchBlock(this);
		return this._file.__pushBlock(rv, true);
	}
}
