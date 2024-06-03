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
import cc.squirreljme.fontcompile.out.rafoces.HuffTable;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.fontcompile.out.rafoces.VectorPoint;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * Contains details of a font which has been compiled.
 *
 * @since 2024/06/03
 */
public class CompiledFont
	implements Iterable<CompiledGlyph>
{
	/** The compiled glyphs. */
	public final Map<GlyphInfo, CompiledGlyph> glyphs;
	
	/**
	 * Initializes the compiled font.
	 *
	 * @param __glyphs The resultant glyph map.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private CompiledFont(Map<GlyphInfo, CompiledGlyph> __glyphs)
		throws NullPointerException
	{
		if (__glyphs == null)
			throw new NullPointerException("NARG");
		
		this.glyphs = UnmodifiableMap.of(__glyphs);
	}
	
	/**
	 * Initializes compiled glyphs.
	 *
	 * @param __glyphs The input glyphs.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public CompiledFont(List<CompiledGlyph> __glyphs)
		throws NullPointerException
	{
		if (__glyphs == null)
			throw new NullPointerException("NARG");

		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public Iterator<CompiledGlyph> iterator()
	{
		return this.glyphs.values().iterator();
	}
	
	/**
	 * Runs finalization for font compression.
	 *
	 * @param __glyphVectors The input glyphs and vector chains.
	 * @param __allPoints All the input points.
	 * @param __huffman The huffman table used.
	 * @param __huffedChains The huffed chains.
	 * @return The resultant compiled font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	static CompiledFont __finalize(
		Map<GlyphInfo, VectorChain[]> __glyphVectors,
		Set<VectorPoint> __allPoints, HuffTable __huffman,
		Map<ChainList, List<HuffBits>> __huffedChains)
		throws NullPointerException
	{
		if (__glyphVectors == null || __allPoints == null ||
			__huffman == null || __huffedChains == null)
			throw new NullPointerException("NARG");
		
		// Process each glyph
		Map<GlyphInfo, CompiledGlyph> result = new LinkedHashMap<>();
		for (Map.Entry<GlyphInfo, VectorChain[]> entry :
			__glyphVectors.entrySet())
		{
			// Finalize glyph
			CompiledGlyph compiled = CompiledGlyph.__finalize(
				entry.getKey(), entry.getValue(), __huffman,
				__huffedChains);
			
			// Store info
			result.put(entry.getKey(), compiled);
		}
		
		// Final compiled font
		return new CompiledFont(result);
	}
}
