// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the parsed node table which defines the structure for
 * each entry within the frame tree. A node represents a single frame.
 *
 * @since 2018/11/11
 */
final class __NodeTable__
{
	/** Linear set of frame nodes. */
	private final List<ProfiledFrame> _linear = 
		new ArrayList<>();
	
	/** Offsets for each node. */
	private final Map<ProfiledFrame, __Position__> _offsets =
		new HashMap<>();
	
	/** Current position. */
	private final __Position__ _at =
		new __Position__();
	
	/**
	 * Parses the frames and loads into a node table.
	 *
	 * @param __fs The input frames to map.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void parse(Iterable<ProfiledFrame> __fs)
		throws NullPointerException
	{
		if (__fs == null)
			throw new NullPointerException("NARG");
			
		List<ProfiledFrame> linear = this._linear;
		Map<ProfiledFrame, __Position__> offsets = this._offsets;
		
		// Determine positions for all nodes
		__Position__ at = this._at;
		for (ProfiledFrame f : __fs)
		{
			// Will need to know how many sub-frames there are
			Collection<ProfiledFrame> sub = f._frames.values();
			
			// Track position of this frame
			linear.add(f);
			offsets.put(f, at.increment(sub.size()));
			
			// Handle sub-frames for this frame
			this.parse(sub);
		}
	}
	
	/**
	 * Writes the node table to the given stream.
	 *
	 * @param __os The stream to write to.
	 * @param __mids Node IDs.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void writeTo(OutputStream __os,
		Map<FrameLocation, Integer> __mids)
		throws IOException, NullPointerException
	{
		if (__os == null || __mids == null)
			throw new NullPointerException("NARG");
		
		DataOutputStream dos = new DataOutputStream(__os);
		
		throw new todo.TODO();
	}
	
	/**
	 * Position of a node within the node table.
	 *
	 * @since 2018/11/11
	 */
	private static final class __Position__
	{
		/** Narrow position. */
		int _narrow;
		
		/** Wide position. */
		int _wide;
		
		/**
		 * Initializes default position.
		 *
		 * @since 2018/11/11
		 */
		__Position__()
		{
		}
		
		/**
		 * Initializes for the given positions.
		 *
		 * @param __n The narrow position.
		 * @param __w The wide position.
		 * @since 2018/11/11
		 */
		__Position__(int __n, int __w)
		{
			this._narrow = __n;
			this._wide = __w;
		}
		
		/**
		 * Increments this position by the given number of sub-node units
		 * and returns the old one.
		 *
		 * @param __num The number of sub-node references to increment by.
		 * @return The old position in a new object.
		 * @throws IllegalArgumentException If the number of nodes is negative.
		 * @since 2018/11/11
		 */
		public __Position__ increment(int __num)
			throws IllegalArgumentException
		{
			// {@squirreljme.error AH08 Cannot write negative number of
			// nodes.}
			if (__num < 0)
				throw new IllegalArgumentException("AH08");
			
			// Store the old position first
			int narrow = this._narrow,
				wide = this._wide;
			__Position__ rv = new __Position__(narrow, wide);
			
			// Increment with new offsets
			this._narrow = narrow + 28 + (__num * 3);
			this._wide = wide + 28 + (__num * 4);
			
			return rv;
		}
	}
}

