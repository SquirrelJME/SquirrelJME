// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.CTokenOutput;
import cc.squirreljme.c.out.StringCollectionCTokenOutput;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Stores spliced output accordingly for writing functions.
 *
 * @since 2023/07/15
 */
class __CFunctionSplice__
	extends CFunctionBlock
{
	/** The output for tokes. */
	private final StringCollectionCTokenOutput _output;
	
	/** The target to write to when closed. */
	private final Reference<CFunctionBlock> _target;
	
	/** Has this been closed yet? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes a function splice.
	 * 
	 * @param __outFile The output file.
	 * @param __tokenOut The token output.
	 * @param __target The target to write to when closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	__CFunctionSplice__(CFile __outFile,
		StringCollectionCTokenOutput __tokenOut,
		CFunctionBlock __target)
		throws NullPointerException
	{
		super(__outFile, null);
		
		if (__target == null)
			throw new NullPointerException("NARG");
		
		this._output = __tokenOut;
		this._target = new WeakReference<>(__target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public void close()
		throws IOException
	{
		// Do nothing here
	}
	
	/**
	 * Internal close logic, this actually adds the tokens to the output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/07/15
	 */
	final void __close()
		throws IOException
	{
		// Only close once
		if (this._isClosed)
			return;
		this._isClosed = true;
		
		/* {@squirreljme.error CW0p Target has been garbage collected.} */
		CFunctionBlock target = this._target.get();
		if (target == null)
			throw new IOException("CW0p");
		
		// This just pushes the tokens to the true output and does not
		// actually close via the superclass
		for (String token : this._output.output())
		{
			// Space?
			if (token.equals(" "))
				target.token(" ", false);
			
			// Tab
			else if (token.equals("\t"))
				target.token("\t", false);
			
			// Indentation adjustment?
			else if (token.length() == 2 && token.charAt(0) == '\t')
				target.indent((short)token.charAt(1));
			
			// Newline
			else if (token.equals("\n"))
			{
				// noinspection StringEquality,ConstantValue
				target.token("\n", token ==
					StringCollectionCTokenOutput.FORCED_NEWLINE);
				
			}
			
			// Forward normally
			else
				target.token(token, false);
		}
	}
}
