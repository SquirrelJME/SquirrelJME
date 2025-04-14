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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * A single compiled glyph.
 *
 * @since 2024/06/03
 */
public final class CompiledGlyph
{
	/** The direct raw bits. */
	public final List<HuffBits> bitsRaw;
	
	/** The naively packed bits. */
	public final List<HuffBits> bitNaive;
	
	/** The glyph used. */
	public final GlyphInfo glyph;
	
	/** The huffman table used. */
	public final HuffTable huffman;
	
	/**
	 * Initializes the compiled glyph.
	 *
	 * @param __glyph The input glyph.
	 * @param __huffman The huffman table used.
	 * @param __raw The raw bits.
	 * @param __naive The naively packed bits.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private CompiledGlyph(GlyphInfo __glyph, HuffTable __huffman,
		List<HuffBits> __raw, List<HuffBits> __naive)
		throws NullPointerException
	{
		if (__glyph == null || __huffman == null || __raw == null ||
			__naive == null)
			throw new NullPointerException("NARG");
		
		this.glyph = __glyph;
		this.huffman = __huffman;
		this.bitsRaw = UnmodifiableList.of(__raw);
		this.bitNaive = UnmodifiableList.of(__naive);
	}
	
	/**
	 * Is this a reject compilation, that is huffman coding is larger than
	 * the actual glyph bitmap itself? 
	 *
	 * @return If this is a reject.
	 * @since 2024/06/03
	 */
	public boolean isReject()
	{
		return HuffBits.length(this.bitNaive) >
			HuffBits.length(this.bitsRaw);
	}
	
	/**
	 * Finalizes a compressed glyph.
	 *
	 * @param __glyph The glyph to finalize.
	 * @param __chains The chains for this glyph.
	 * @param __huffman The huffman table used.
	 * @param __huffedChains The glyph chains.
	 * @return The compiled glyph.
	 * @since 2024/06/03
	 */
	static CompiledGlyph __finalize(GlyphInfo __glyph,
		VectorChain[] __chains, HuffTable __huffman,
		Map<ChainList, List<HuffBits>> __huffedChains)
	{
		if (__glyph == null || __chains == null || __huffman == null ||
			__huffedChains == null)
			throw new NullPointerException("NARG");
		
		// Get uncompressed bitmap
		List<HuffBits> raw = __glyph.bitmap().uncompressedBits();
		
		// Get naive compressed total
		List<HuffBits> naive = new ArrayList<>();
		for (VectorChain chain : __chains)
			naive.addAll(__huffedChains.get(chain.codes));
		
		// Debug
		/*Debugging.debugNote("Glyph %s: raw=%d naive=%d",
			__glyph.codepoint(),
			HuffBits.length(raw), HuffBits.length(naive));*/
		
		// Initialize glyph
		return new CompiledGlyph(__glyph, __huffman, raw, naive);
	}
}
