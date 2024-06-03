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
import cc.squirreljme.fontcompile.out.rafoces.HuffSpliceItem;
import cc.squirreljme.fontcompile.out.rafoces.HuffSpliceTable;
import cc.squirreljme.fontcompile.out.rafoces.PixelScan;
import cc.squirreljme.fontcompile.out.rafoces.VectorChain;
import cc.squirreljme.fontcompile.out.rafoces.VectorPoint;
import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.OutputStream;
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
		// Glyphs
		Map<GlyphInfo, VectorChain[]> glyphVectors = new LinkedHashMap<>();
		long bytesGlyphs = 0;
		
		// Vectors and points
		int total = 0;
		Set<VectorChain> allVectors = new LinkedHashSet<>();
		Set<VectorPoint> allPoints = new LinkedHashSet<>();
		
		// Codes
		Set<ChainList> allCodes = new LinkedHashSet<>();
		long bitsFlat = 0;
		
		// Used to build a huffman table with vector splices
		HuffSpliceTable splice = new HuffSpliceTable();
		
		// Debug
		Debugging.debugNote("Calculating on %d glyphs.",
			this.in.size());
		
		// Go through each glyph
		for (GlyphInfo glyph : this.in)
		{
			// Add bytes to total
			GlyphBitmap bitmap = glyph.bitmap();
			bytesGlyphs += bitmap.byteSize();
			
			// Process vectors
			PixelScan scan = new PixelScan(bitmap);
			
			// Calculate chains
			VectorChain[] vectors = scan.calculate();
			if (vectors.length == 0)
				continue;
			
			// Remember individual vectors for later
			total += vectors.length;
			glyphVectors.put(glyph, vectors);
			
			// Store all unique chains
			for (VectorChain vector : vectors)
			{
				ChainList codes = vector.codes;
				
				// Add base vectors
				allVectors.add(vector);
				allPoints.add(vector.point);
				allCodes.add(codes);
				
				// Size estimation
				bitsFlat += codes.bitsFlat();
				
				// Register into the splice table
				splice.spliceFrom(codes);
			}
		}
		
		// Debug
		Debugging.debugNote("%d non-blank glyphs (Uncompressed: %dkiB).",
			glyphVectors.size(), bytesGlyphs / 1024);
		Debugging.debugNote("%d total vector chains (w/ point).",
			total);
		Debugging.debugNote("%d unique vector chains (w/ point).",
			allVectors.size());
		Debugging.debugNote("%d unique point starts.",
			allPoints.size());
		Debugging.debugNote("%d unique code chains " +
			"(Flat: %dkiB).",
			allCodes.size(),
			bitsFlat / 8192);
		
		// Debug
		List<HuffSpliceItem> optimized = splice.allOptimized();
		Debugging.debugNote("Splice: %d -> %s",
			optimized.size(), optimized);
		
		throw Debugging.todo();
	}
}
