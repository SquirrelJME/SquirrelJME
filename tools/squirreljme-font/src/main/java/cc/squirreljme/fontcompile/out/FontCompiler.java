// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out;

import cc.squirreljme.fontcompile.InvalidFontException;
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
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Font compiler.
 *
 * @since 2024/05/19
 */
public class FontCompiler
{
	/** Initial reject division. */
	private static final int _REJECT_DIV =
		64;
	
	/** Limit on the reject division. */
	private static final int _REJECT_DIV_LIMIT =
		8;
	
	/** The input font. */
	protected final FontInfo in;
	
	/**
	 * Initializes the font compiler.
	 *
	 * @param __in The input font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/19
	 */
	public FontCompiler(FontInfo __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * Compiles the input font.
	 * 
	 * @return The resultant compiled font.
	 * @throws InvalidFontException If the font is not valid.
	 * @since 2024/06/03
	 */
	public CompiledFont run()
		throws InvalidFontException
	{
		// First visible compiled glyph, only used for incompressible glyphs
		Map<GlyphId, CompiledGlyph> firstSeen = new LinkedHashMap<>();
		
		// Fonts that are okay and all the rejects
		List<CompiledGlyph> okay = new ArrayList<>();
		List<CompiledGlyph> toOkay = new ArrayList<>();
		List<GlyphInfo> runRejects = new ArrayList<>();
		List<GlyphInfo> reject = new ArrayList<>();
		
		// Initially put every glyph into the reject list
		for (GlyphInfo glyphInfo : this.in)
			runRejects.add(glyphInfo);
		
		// Final totals
		int normal = 0;
		int forced = 0;
		
		// For tracking
		int lastOkay = 0;
		
		// Run while rejects still exist
		int runCount = 0;
		int rejectDiv = FontCompiler._REJECT_DIV;
		while (!runRejects.isEmpty() || !reject.isEmpty())
		{
			// On the first run, process all rejects because everything is
			// basically fresh
			if (true || runCount > 0)
			{
				// Otherwise on subsequent runs, do not run too many glyphs
				// together, only up to the reject division to try to keep it
				// filled as much as possible
				while (runRejects.size() < rejectDiv && !reject.isEmpty())
				{
					// Reject FIFO
					GlyphInfo glyph = reject.remove(0);
					runRejects.add(glyph);
				}
				
				// Otherwise, too many rejects being run at once
				while (runRejects.size() > rejectDiv)
				{
					// Reject FIFO
					GlyphInfo glyph = runRejects.remove(
						runRejects.size() - 1);
					reject.add(0, glyph);
				}
			}
			
			// Progress
			if (lastOkay + FontCompiler._REJECT_DIV < okay.size())
			{
				Debugging.debugNote(
					"OKAY % 6d; WAITING % 6d",
					okay.size(), reject.size());
				lastOkay = okay.size();
			}
			
			// Compile the font
			CompiledFont compiled = this.__run(runRejects);
			
			// This is used to determine if nothing was compressed
			int okayWas = okay.size();
			
			// Go through all glyphs in the font and find rejects
			toOkay.clear();
			for (CompiledGlyph compiledGlyph : compiled)
			{
				// Glyph seen for the first time?
				GlyphInfo glyph = compiledGlyph.glyph;
				if (!firstSeen.containsKey(glyph.codepoint()))
					firstSeen.put(glyph.codepoint(), compiledGlyph);
				
				// If it compressed fine, move it to the okay pile
				// Did not compress well?
				if (!compiledGlyph.isReject())
					toOkay.add(compiledGlyph);
			}
			
			// Were enough glyphs okay? We do not want to create too many
			// huffman tables
			if (runCount == 0 || (!toOkay.isEmpty() &&
				toOkay.size() >= Math.max(1, runRejects.size() / 4)))
			{
				for (CompiledGlyph compiledGlyph : toOkay)
				{
					// Normal!
					normal++;
					
					// Put it in
					okay.add(compiledGlyph);
					
					// Not a reject anymore, so remove from both!
					GlyphInfo glyph = compiledGlyph.glyph;
					reject.remove(glyph);
					runRejects.remove(glyph);
				}
			}
				
			// No glyphs were compressed
			if (okayWas == okay.size())
			{
				// Tried very hard to compress and nothing really worked
				if (rejectDiv == FontCompiler._REJECT_DIV_LIMIT)
				{
					// Just make all of these glyphs okay
					for (GlyphInfo glyph : runRejects)
					{
						// If a glyph was not seen, it was blank
						CompiledGlyph seen = firstSeen.get(glyph.codepoint());
						if (seen == null)
							continue;
						
						okay.add(seen);
					}
					
					// Debug
					/*Debugging.debugNote("Forced %d glyphs as okay.",
						runRejects.size());*/
					forced += runRejects.size();
					
					// Clear out
					runRejects.clear();
					
					// Reset
					rejectDiv = FontCompiler._REJECT_DIV;
				}
					
				// Try with fewer glyphs
				else
					rejectDiv >>>= 1;
			}
				
			// Reset the glyph division to grab as much as possible
			else
				rejectDiv = FontCompiler._REJECT_DIV;
			
			// Run counter for further squishing
			runCount++;
		}
		
		// Count the number of unique huffman tables
		Set<HuffTable> tables = new LinkedHashSet<>();
		for (CompiledGlyph compiledGlyph : okay)
			if (!compiledGlyph.isReject())
				tables.add(compiledGlyph.huffman);
		
		// Debug
		System.err.println('!');
		Debugging.debugNote("DONE: Compressed %d; Failed %d! " +
			"There are %d huffman tables...",
			normal, forced, tables.size());
		
		// Setup font with only okay glyphs
		return new CompiledFont(okay);
	}
	
	/**
	 * Compress the chains for every glyph.
	 *
	 * @param __glyphVectors The vectors to list.
	 * @param __huffman The input huffman tree.
	 * @return The compressed chains.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private Map<ChainList, List<HuffBits>> __compressChains(
		Map<GlyphInfo, VectorChain[]> __glyphVectors,
		HuffTable __huffman)
		throws NullPointerException
	{
		if (__glyphVectors == null || __huffman == null)
			throw new NullPointerException("NARG");
		
		// Pre-compressed duplicate chains
		Map<ChainList, List<HuffBits>> result = new SortedTreeMap<>();
		
		// Compress all vector chains for every glyph
		for (Map.Entry<GlyphInfo, VectorChain[]> entry :
			__glyphVectors.entrySet())
		{
			// What are we working on?
			GlyphInfo glyph = entry.getKey();
			VectorChain[] vectors = entry.getValue();
			
			// Compress individual chains
			for (VectorChain chain : vectors)
			{
				// Does this chain need compressing?
				List<HuffBits> bits = result.get(chain.codes);
				if (bits == null)
				{
					// Compress and cache
					bits = this.__compressSingle(__huffman, chain.codes);
					result.put(chain.codes, bits);
					
					// Note new chain
					/*Debugging.debugNote("Compressed %s -> %s",
						chain.codes, bits);*/
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Compresses a single vector chain.
	 *
	 * @param __huffman The huffman table to compress with.
	 * @param __codes The codes to compress.
	 * @return The resultant compressed bits.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private List<HuffBits> __compressSingle(HuffTable __huffman,
		ChainList __codes)
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
			
			// Did not find anything here?
			if (quickSplit.get(at) == null)
				throw new IllegalStateException(String.format(
					"Code %s missing from huffman table? %s",
					__codes.subSequence(at, 1), __huffman));
		}
		
		// Use found items from the split list
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
			
			// Should not occur
			throw new IllegalStateException(String.format(
				"No huffman sequence for %s (at: %d; len: %d, bits: %s)",
				__codes, at, len, quickSplit));
		}
		
		// Use final result
		return result;
	}
	
	/**
	 * Compiles the input font.
	 * 
	 * @return The resultant compiled font.
	 * @throws InvalidFontException If the font is not valid.
	 * @since 2024/05/19
	 */
	private CompiledFont __run(Iterable<GlyphInfo> __input)
		throws InvalidFontException
	{
		// Glyphs
		Map<GlyphInfo, VectorChain[]> glyphVectors = new LinkedHashMap<>();
		Set<VectorPoint> allPoints = new LinkedHashSet<>();
		
		// Codes as input for huffman table construction
		Set<ChainList> allCodes = new LinkedHashSet<>();
		
		// Used to build a huffman table with vector splices
		HuffSpliceTable splice = new HuffSpliceTable();
		
		// Process vectors for all glyphs
		this.__runGlyphs(__input, glyphVectors, allPoints, allCodes, splice);
		
		// Debug
		HuffTable huffman = splice.huffmanTable();
		/*Debugging.debugNote("Huffman: %d -> %s",
			huffman.size(), huffman);*/
		
		// Compress all the various chains for every glyph
		Map<ChainList, List<HuffBits>> huffedChains =
			this.__compressChains(glyphVectors, huffman);
		
		// Build finalized compiled font
		return CompiledFont.__finalize(glyphVectors, allPoints, huffman,
			huffedChains);
	}
	
	/**
	 * Runs through and calculates all the vector chain codes for every
	 * glyph.
	 *
	 * @param __input The input glyphs to use.
	 * @param __glyphVectors The output glyph vectors.
	 * @param __allPoints The output points.
	 * @param __allCodes The output codes.
	 * @param __splice The output huffman splices.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private void __runGlyphs(Iterable<GlyphInfo> __input,
		Map<GlyphInfo, VectorChain[]> __glyphVectors,
		Set<VectorPoint> __allPoints, Set<ChainList> __allCodes,
		HuffSpliceTable __splice)
		throws NullPointerException
	{
		if (__glyphVectors == null || __allPoints == null ||
			__allCodes == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*Debugging.debugNote("Calculating on %d glyphs.",
			__input.size());*/
		
		// Go through each glyph
		for (GlyphInfo glyph : __input)
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
		/*Debugging.debugNote("%d non-blank glyphs.",
			__glyphVectors.size());
		Debugging.debugNote("%d unique point starts.",
			__allPoints.size());
		Debugging.debugNote("%d unique code chains.",
			__allCodes.size());*/
	}
}
