// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out;

import cc.squirreljme.fontcompile.in.GlyphInfo;
import cc.squirreljme.fontcompile.out.rafoces.ChainList;
import cc.squirreljme.fontcompile.out.rafoces.HuffBits;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A single compiled glyph.
 *
 * @since 2024/06/03
 */
public final class CompiledGlyph
{
	/**
	 * Finalizes a compressed glyph.
	 *
	 * @param __glyph The glyph to finalize.
	 * @param __chains The chains for this glyph.
	 * @param __huffedChains The glyph chains.
	 * @return The compiled glyph.
	 * @since 2024/06/03
	 */
	static CompiledGlyph __finalize(GlyphInfo __glyph,
		VectorChain[] __chains, Map<ChainList, List<HuffBits>> __huffedChains)
	{
		if (__glyph == null || __chains == null || __huffedChains == null)
			throw new NullPointerException("NARG");
		
		// Get uncompressed bitmap
		List<HuffBits> raw = __glyph.bitmap().uncompressedBits();
		
		// Get naive compressed total
		List<HuffBits> naive = new ArrayList<>();
		for (VectorChain chain : __chains)
			naive.addAll(__huffedChains.get(chain.codes));
		
		Debugging.debugNote("Glyph %s: raw=%d naive=%d",
			__glyph.codepoint(),
			HuffBits.length(raw), HuffBits.length(naive));
		
		return null;
	}
}
