// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Huffman splice table.
 *
 * @since 2024/06/03
 */
public final class HuffSpliceTable
	implements Iterable<HuffSpliceItem>
{
	/** The splice segment size. */
	private static final int _SEGMENT =
		16;
	
	/** The minimum number of counts needed to make it to the table. */
	private static final int _CUTOFF_COUNT =
		32;
	
	/**
	 * The minimum number of codes that must be in a chain before it can be
	 * used to reduce. This is here so that
	 */
	private static final int _NO_REDUCE_LIMIT =
		3;
	
	/** Huffman table size limit. */
	private static final int _TABLE_LIMIT =
		128;
	
	/* Only reduce to a table of this size initially. */
	private static final int _REDUCE_CAP_LIMIT =
		HuffSpliceTable._TABLE_LIMIT * 3;
	
	/** The code counts. */
	private final Map<ChainList, Integer> _counts =
		new LinkedHashMap<>();
	
	/**
	 * Returns all the huffman splice items.
	 *
	 * @return All the splice items. 
	 * @since 2024/06/03
	 */
	public List<HuffSpliceItem> all()
	{
		List<HuffSpliceItem> result = new ArrayList<>();
		for (Map.Entry<ChainList, Integer> entry : this._counts.entrySet())
			result.add(new HuffSpliceItem(entry.getKey(), entry.getValue()));
		
		return result;
	}
	
	/**
	 * Returns all chain codes, sorted from most common longest to least
	 * common.
	 *
	 * @return The chain codes sorted accordingly.
	 * @since 2024/06/03
	 */
	public List<HuffSpliceItem> allSorted()
	{
		// Sort everything with the given comparator
		List<HuffSpliceItem> all = this.all();
		Collections.sort(all, new __HuffSpliceSort__());
		
		// Use this
		return all;
	}
	
	/**
	 * Attempts optimization of the huffman sorted count.
	 *
	 * @return The optimized entry set of chain entries.
	 * @since 2024/06/03
	 */
	public List<HuffSpliceItem> allOptimized()
	{
		// Get all sorted values
		List<HuffSpliceItem> all = this.allSorted();
		
		// The most common chain code
		HuffSpliceItem mostCommon = all.get(0);
		ChainList mostCommonList = mostCommon.getList();
		
		// First get rid of everything that has a count lower than the cut-off
		// since these are not very common and as such will just waste time
		// with the calculation or otherwise
		for (int i = all.size() - 1;
			 i >= 0 && all.size() > HuffSpliceTable._REDUCE_CAP_LIMIT; i--)
		{
			// What might be reduced?
			ChainList list = all.get(i).getList();
			int count = all.get(i).count();
			
			// Reduce anything smaller than the cutoff
			if (count < HuffSpliceTable._CUTOFF_COUNT)
				all.remove(i);
		}
		
		// Example splice allSorted()
		// DB: Splice sorted: {LL=61812, LLL=47766, LS=24726, LLLL=23883,
		// LR=21382, S=18809, LRL=12810, LSS=11556, LLS=9904, LSL=8490, 
		// SL=8426, RL=8390, LRS=8318, SS=8303, R=7778, LLR=7489, LRLL=7031, 
		// LSSS=6055, LRLLR=5490, LRSL=4804, LLSL=4792, LSSSS=4668, LSLLS=4474, 
		// SLLS=3633, LSR=3605, SSL=3329, LLRR=3293, LSSL=3253, SSS=3249, 
		// LRSLL=3227, LLSS=3066, 
		
		// Skip the first as it would be the most common single sequence code
		// We also only need to reduce to make the table as small as possible
		for (int i = all.size() - 1;
			 i >= 1 && all.size() > HuffSpliceTable._TABLE_LIMIT; i--)
		{
			// What are we operating on?
			ChainList list = all.get(i).getList();
			int count = all.get(i).count();
			
			// Go through the entire table, if a code before this represents
			// this code then we can remove it
			// for example LRLL=6374 and LRLLR=5080, both contain LRLL which
			// the shorter sequence is more common... so remove the current
			// one
			for (int j = i - 1; j >= 1; j--)
			{
				ChainList other = all.get(j).getList();
				
				// Without this, very viable chains could get reduced
				// and all of them because they all would contain the
				// most common small sets
				if (other.size() <= HuffSpliceTable._NO_REDUCE_LIMIT)
					continue;
				
				// Is this in the sequence? If it is then remove it
				if (list.indexOf(other) > 0)
				{
					all.remove(i);
					break;
				}
			}
		}
		
		// Remove all entries which are beyond the reduce limit, but never
		// the first one as that is the special zero-zero code. This is done
		// so that way very small values can just be used with normal
		// uncompressed variable width sequences instead of relying on
		// the table
		for (int i = all.size() - 1;
			 i >= 1 && all.size() > HuffSpliceTable._TABLE_LIMIT; i--)
		{
			int count = all.get(i).count();
			if (count < HuffSpliceTable._NO_REDUCE_LIMIT)
				all.remove(i);
		}
		
		// Go through the table and make all singlets first, this is so they
		// take up the least amount of bit space
		// These also need to become the most common, otherwise the mean
		// splitting will break, give them more weight, so they take less bits?
		for (int i = all.size() - 1, splat = 0; i >= 3; i--)
		{
			HuffSpliceItem item = all.get(i);
			if (item.list.size() == 1)
			{
				// Remove from this spot
				all.remove(i);
				
				// Then add it back in
				all.add(0, new HuffSpliceItem(item.list,
					(mostCommon.count - (splat++)) * 2));
			}
		}
		
		// Remove everything that is past the end of the table, since we do
		// not want a very large huffman table
		while (all.size() > HuffSpliceTable._TABLE_LIMIT)
			all.remove(all.size() - 1);
		
		// Put back into map form
		return all;
	}
	
	/**
	 * Returns the huffman table to use for compression.
	 *
	 * @return The table to use for compression.
	 * @since 2024/06/03
	 */
	public HuffTable huffmanTable()
	{
		// Start with the optimized table
		List<HuffSpliceItem> all = this.allOptimized();
		
		// Recursive split of the tree
		Map<ChainList, HuffBits> result = new LinkedHashMap<>();
		HuffSpliceTable.__huffmanDive(result, all, null);
		
		// Use this tree for compression
		return new HuffTable(result);
	}
	
	/**
	 * Injects the chain codes into the list.
	 *
	 * @param __codes The codes to inject.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public void inject(ChainList __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		Map<ChainList, Integer> counts = this._counts;
		Integer current = counts.get(__codes);
		counts.put(__codes, (current == null ? 1 : current + 1));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public Iterator<HuffSpliceItem> iterator()
	{
		return this.all().iterator();
	}
	
	/**
	 * Splices the chain list into the splice table.
	 *
	 * @param __codes The codes to splice.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public void spliceFrom(ChainList __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		// Splice completely on segment bounds and in individual segments
		int len = __codes.size();
		for (int base = 0; base < len; base += HuffSpliceTable._SEGMENT)
			for (int at = base, end = Math.min(len,
				base + HuffSpliceTable._SEGMENT); at < end; at++)
			{
				if (end - at > 0)
					this.inject(__codes.subSequence(at, end - at));
				if (at - base > 0)
					this.inject(__codes.subSequence(0, at - base));
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		return this._counts.toString();
	}
	
	/**
	 * Dives into and builds the huffman tree.
	 *
	 * @param __result The resultant map of codes.
	 * @param __slice The slice to calculate.
	 * @param __baseBits The base bits to use for the tree, if {@code null}
	 * then this is the very top of the tree.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private static void __huffmanDive(Map<ChainList, HuffBits> __result,
		List<HuffSpliceItem> __slice, HuffBits __baseBits)
		throws NullPointerException
	{
		if (__result == null || __slice == null)
			throw new NullPointerException("NARG");
		
		// There is only a single entry in the slice, so this is a node
		if (__slice.size() == 1)
		{
			// Debug
			Debugging.debugNote("Huff INJECT: [%s] <- %s",
				__baseBits, __slice);
			
			__result.put(__slice.get(0).list, __baseBits);
			return;
		}
		
		// Get the total count for this entire slice
		int total = 0;
		int count = __slice.size();
		for (HuffSpliceItem item : __slice)
			total += item.count;
		
		// Determine the index that is the closest to the given mean
		int mean = total / count;
		int meanDx = 0;
		int meanDist = Integer.MAX_VALUE;
		for (int i = 0; i < count; i++)
		{
			// If the difference from the mean is smaller then get to the
			// closest possible
			int dist = Math.abs(mean - __slice.get(i).count);
			if (dist < meanDist)
			{
				meanDx = i;
				meanDist = dist;
			}
		}
		
		// If the mean index is zero, always make it one otherwise we would
		// dive with a zero size list
		if (meanDx == 0)
			meanDx = 1;
		
		// Split the tree in half
		List<HuffSpliceItem> left = __slice.subList(0, meanDx);
		List<HuffSpliceItem> right = __slice.subList(meanDx, __slice.size());
		
		// Which bits for the left and right side?
		HuffBits leftBits = (__baseBits == null ?
			HuffBits.of(0, 1) : __baseBits.shiftIn(false));
		HuffBits rightBits = (__baseBits == null ?
			HuffBits.of(1, 1) : __baseBits.shiftIn(true));
		
		// Debug
		/*Debugging.debugNote("Huff split: (%d~%d~%d) [%s]|[%s]; %s|%s",
			0, meanDx, __slice.size(),
			leftBits, rightBits, left, right);*/
		
		// Dive deeper into the tree on both sides
		HuffSpliceTable.__huffmanDive(__result, left, leftBits);
		HuffSpliceTable.__huffmanDive(__result, right, rightBits);
	}
}
