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
import cc.squirreljme.fontcompile.out.rafoces.ChainList;
import cc.squirreljme.fontcompile.out.rafoces.HuffBits;
import cc.squirreljme.fontcompile.out.rafoces.HuffTable;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.fontcompile.out.rafoces.VectorPoint;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Iterator;
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
	public final Map<GlyphId, CompiledGlyph> glyphs;
	
	/** The original font. */
	public final FontInfo original;
	
	/**
	 * Initializes the compiled font.
	 *
	 * @param __glyphs The resultant glyph map.
	 * @param __original The original font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private CompiledFont(Map<GlyphId, CompiledGlyph> __glyphs,
		FontInfo __original)
		throws NullPointerException
	{
		if (__glyphs == null || __original == null)
			throw new NullPointerException("NARG");
		
		this.original = __original;
		this.glyphs = UnmodifiableMap.of(__glyphs);
	}
	
	/**
	 * Initializes compiled glyphs.
	 *
	 * @param __glyphs The input glyphs.
	 * @param __original The original font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public CompiledFont(List<CompiledGlyph> __glyphs, FontInfo __original)
		throws NullPointerException
	{
		if (__glyphs == null || __original == null)
			throw new NullPointerException("NARG");
		
		Map<GlyphId, CompiledGlyph> glyphs = new SortedTreeMap<>();
		for (CompiledGlyph glyph : __glyphs)
			glyphs.put(glyph.glyph.codepoint(), glyph);
		
		this.original = __original;
		this.glyphs = UnmodifiableMap.of(glyphs);
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
	 * @param __original The original font.
	 * @return The resultant compiled font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	static CompiledFont __finalize(
		Map<GlyphInfo, VectorChain[]> __glyphVectors,
		Set<VectorPoint> __allPoints, HuffTable __huffman,
		Map<ChainList, List<HuffBits>> __huffedChains, FontInfo __original)
		throws NullPointerException
	{
		if (__glyphVectors == null || __allPoints == null ||
			__huffman == null || __huffedChains == null)
			throw new NullPointerException("NARG");
		
		// Process each glyph
		Map<GlyphId, CompiledGlyph> result = new SortedTreeMap<>();
		for (Map.Entry<GlyphInfo, VectorChain[]> entry :
			__glyphVectors.entrySet())
		{
			// Finalize glyph
			CompiledGlyph compiled = CompiledGlyph.__finalize(
				entry.getKey(), entry.getValue(), __huffman,
				__huffedChains);
			
			// Store info
			result.put(entry.getKey().codepoint(), compiled);
		}
		
		// Final compiled font
		return new CompiledFont(result, __original);
	}
}
