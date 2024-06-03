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
import cc.squirreljme.fontcompile.out.rafoces.HuffSplit;
import cc.squirreljme.fontcompile.out.rafoces.HuffSpliceTable;
import cc.squirreljme.fontcompile.out.rafoces.HuffTable;
import cc.squirreljme.fontcompile.out.rafoces.PixelScan;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.fontcompile.out.rafoces.VectorPoint;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Debug;

/**
 * Font compiler.
 *
 * @since 2024/05/19
 */
public class FontCompiler
	implements Runnable
{
	/** Zero One sequence, used for not in table sequences. */
	private static final HuffBits _ZERO_ONE =
		HuffBits.of(0b01, 2);
	
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
		// Glyphs
		Map<GlyphInfo, VectorChain[]> glyphVectors = new LinkedHashMap<>();
		Set<VectorPoint> allPoints = new LinkedHashSet<>();
		
		// Codes as input for huffman table construction
		Set<ChainList> allCodes = new LinkedHashSet<>();
		
		// Used to build a huffman table with vector splices
		HuffSpliceTable splice = new HuffSpliceTable();
		
		// Process vectors for all glyphs
		this.__runGlyphs(glyphVectors, allPoints, allCodes, splice);
		
		// Debug
		HuffTable huffman = splice.huffmanTable();
		Debugging.debugNote("Huffman: %d -> %s",
			huffman.size(), huffman);
		
		// Pre-compressed duplicate chains
		Map<ChainList, List<HuffBits>> preCompressed = new SortedTreeMap<>(); 
		
		// Compress all vector chains for every glyph
		for (Map.Entry<GlyphInfo, VectorChain[]> entry :
			glyphVectors.entrySet())
		{
			// What are we working on?
			GlyphInfo glyph = entry.getKey();
			VectorChain[] vectors = entry.getValue();
			
			// Compress individual chains
			for (VectorChain chain : vectors)
			{
				// Does this chain need compressing?
				List<HuffBits> bits = preCompressed.get(chain.codes);
				if (bits == null)
				{
					// Compress and cache
					bits = this.__compress(huffman, chain.codes);
					preCompressed.put(chain.codes, bits);
					
					// Note new chain
					Debugging.debugNote("Compressed %s -> %s",
						chain.codes, bits);
				}
			}
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * Compresses all vector chains.
	 *
	 * @param __huffman The huffman table to compress with.
	 * @param __codes The codes to compress.
	 * @return The resultant compressed bits.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private List<HuffBits> __compress(HuffTable __huffman, ChainList __codes)
		throws NullPointerException
	{
		if (__huffman == null || __codes == null)
			throw new NullPointerException("NARG");
		
		// Setup blank split list for positions which have bits assigned
		int codeLen = __codes.size();
		List<HuffSplit> quickSplit = new ArrayList<>(codeLen);
		for (int i = 0; i < codeLen; i++)
			quickSplit.add(null);
		
		// Try to find the longest sequences for each set of bits
		int len = __codes.size();
		for (int at = 0; at < len; at++)
		{
			// Position was already claimed, so ignore it
			if (quickSplit.get(at) != null)
				continue;
			
			// Find longest fitting sequence
			for (int seqLen = (len - at); seqLen >= 1; seqLen--)
			{
				// Get sub-sequence to try
				ChainList seq = __codes.subSequence(at, seqLen);
				
				// If we find it, use it!
				HuffBits bits = __huffman.find(seq);
				if (bits != null)
				{
					// Fill in all bits with this sequence
					HuffSplit fill = new HuffSplit(bits, seqLen);
					for (int sub = at, end = sub + seqLen; sub < end; sub++)
						quickSplit.set(sub, fill);
					
					// No longer need to fill in
					break;
				}
			}
		}
		
		// Use found items from the split list, if none was found then
		// full sequences are stored instead with a zero-one sequence
		List<HuffBits> result = new ArrayList<>();
		for (int at = 0; at < len;)
		{
			// Use split directly
			HuffSplit split = quickSplit.get(at);
			if (split != null)
			{
				// Use these bits
				result.add(split.bits);
				
				// Skip the rest
				at += split.sequenceLen;
				
				// Do not need a zero-one
				continue;
			}
			
			throw Debugging.todo();
		}
		
		// Use final result
		return result;
	}
	
	/**
	 * Runs through and calculates all the vector chain codes for every
	 * glyph.
	 *
	 * @param __glyphVectors The output glyph vectors.
	 * @param __allPoints The output points.
	 * @param __allCodes The output codes.
	 * @param __splice The output huffman splices.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private void __runGlyphs(Map<GlyphInfo, VectorChain[]> __glyphVectors,
		Set<VectorPoint> __allPoints, Set<ChainList> __allCodes,
		HuffSpliceTable __splice)
		throws NullPointerException
	{
		if (__glyphVectors == null || __allPoints == null ||
			__allCodes == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("Calculating on %d glyphs.",
			this.in.size());
		
		// Go through each glyph
		for (GlyphInfo glyph : this.in)
		{
			// Process vectors
			PixelScan scan = new PixelScan(glyph.bitmap());
			
			// Calculate chains
			VectorChain[] vectors = scan.calculate();
			if (vectors.length == 0)
				continue;
			
			// Remember individual vectors for later
			__glyphVectors.put(glyph, vectors);
			
			// Store all unique chains
			for (VectorChain vector : vectors)
			{
				ChainList codes = vector.codes;
				
				// Add base vectors
				__allPoints.add(vector.point);
				__allCodes.add(codes);
				
				// Register into the splice table
				__splice.spliceFrom(codes);
			}
		}
		
		// Debug
		Debugging.debugNote("%d non-blank glyphs.",
			__glyphVectors.size());
		Debugging.debugNote("%d unique point starts.",
			__allPoints.size());
		Debugging.debugNote("%d unique code chains.",
			__allCodes.size());
	}
}
