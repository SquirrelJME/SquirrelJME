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
import cc.squirreljme.fontcompile.out.rafoces.VectorPoint;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains details of a font which has been compiled.
 *
 * @since 2024/06/03
 */
public class CompiledFont
{
	/**
	 * Runs finalization for font compression.
	 *
	 * @param __glyphVectors The input glyphs and vector chains.
	 * @param __allPoints All the input points.
	 * @param __huffedChains The huffed chains.
	 * @return The resultant compiled font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	static CompiledFont __finalize(
		Map<GlyphInfo, VectorChain[]> __glyphVectors,
		Set<VectorPoint> __allPoints,
		Map<ChainList, List<HuffBits>> __huffedChains)
		throws NullPointerException
	{
		if (__glyphVectors == null || __allPoints == null ||
			__huffedChains == null)
			throw new NullPointerException("NARG");
		
		// Process each glyph
		for (Map.Entry<GlyphInfo, VectorChain[]> entry :
			__glyphVectors.entrySet())
		{
			CompiledGlyph compiled = CompiledGlyph.__finalize(
				entry.getKey(), entry.getValue(), __huffedChains);
		}
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
