// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out;

import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.OutputStream;

/**
 * Font compiler.
 *
 * @since 2024/05/19
 */
public class FontCompiler
	implements Runnable
{
	/** The input font. */
	protected final FontInfo in;
	
	/** The output SQF. */
	protected final OutputStream out;
	
	/**
	 * Initializes the font compiler.
	 *
	 * @param __in The input font.
	 * @param __out The output font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/19
	 */
	public FontCompiler(FontInfo __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/19
	 */
	@Override
	public void run()
	{
		throw Debugging.todo();
	}
}
