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
import cc.squirreljme.fontcompile.in.GlyphInfo;
import cc.squirreljme.fontcompile.out.rafoces.PixelScan;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
		Set<VectorChain> allChains = new LinkedHashSet<>();
		
		// Go through each glyph
		for (GlyphInfo glyph : this.in)
		{
			// Process vectors
			PixelScan scan = new PixelScan(glyph.bitmap());
			
			// Calculate chains
			VectorChain[] chains = scan.calculate();
			if (chains.length == 0)
				continue;
			
			// Store all unique chains
			allChains.addAll(Arrays.asList(chains));
			
			throw Debugging.todo();
		}
		
		throw Debugging.todo();
	}
}
