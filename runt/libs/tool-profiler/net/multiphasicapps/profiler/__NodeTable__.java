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
import java.util.Arrays;
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
	
	/** Overflowed narrow point? */
	private boolean _overflowed;
	
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
		
		// Overflow check for wider offsets
		boolean overflowed = false;
		
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
			
			// If these many bytes were written we overflow now
			if (at._narrow > 16777215)
				overflowed = true;
		}
		
		// Did we overflow?
		if (overflowed)
			this._overflowed = true;
	}
	
	/**
	 * Parses the frames and loads into a node table.
	 *
	 * @param __t The thread to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void parseThread(ProfiledThread __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Iterable<ProfiledFrame> frames = __t._frames.values();
		
		// Create virtual frame since the root actually can have multiple
		// methods forking from it and the profiler format can only handle
		// a single root
		ProfiledFrame vframe = new ProfiledFrame(FrameLocation.ENTRY_POINT);
		
		// Only ever called once
		vframe._numcalls = 1;
		
		// Initialize frame times with thread times
		vframe._traceselftime = __t._totaltime;
		vframe._tracecputime = __t._cputime;
		
		// There is no self time since this is purely a virtual node
		vframe._frameselftime = 0;
		vframe._framecputime = 0;
		
		// Store the thread sub-frames into this virtual thread
		Map<FrameLocation, ProfiledFrame> vsubf = vframe._frames;
		for (ProfiledFrame f : frames)
			vsubf.put(f.location, f);
		
		// Parse this special frame, which will then parse its sub-frames
		// accordingly
		this.parse(Arrays.<ProfiledFrame>asList(vframe));
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
		
		// If there were a large number of entries written then sub-node
		// offsets use more bytes
		List<ProfiledFrame> linear = this._linear;
		boolean wide = this._overflowed;
		
		// Just go through every frame and write it using a simple linear
		// index
		Map<ProfiledFrame, __Position__> offsets = this._offsets;
		for (ProfiledFrame f : linear)
		{
			// The frame location ID, this is data stored in a previous table
			dos.writeShort(__mids.get(f.location));
			
			// Number of calls
			dos.writeInt((int)Math.min(Integer.MAX_VALUE, f._numcalls));
			
			// Time spent in this frame including children
			__NodeTable__.__writeLong40(dos, f._traceselftime);
			__NodeTable__.__writeLong40(dos, f._tracecputime);
			
			// Time spent in this frame excluding children
			__NodeTable__.__writeLong40(dos, f._frameselftime);
			__NodeTable__.__writeLong40(dos, f._framecputime);
			
			// Write offsets to the sub-frame nodes
			Collection<ProfiledFrame> sub = f._frames.values();
			dos.writeShort(sub.size());
			for (ProfiledFrame sf : sub)
			{
				__Position__ p = offsets.get(sf);
				
				// Nodes will either be wide or narrow
				if (wide)
					dos.writeInt(p._wide);
				else
					__NodeTable__.__writeInt24(dos, p._narrow);
			}
		}
	}
	
	/**
	 * Writes a 24-bit int.
	 *
	 * @param __dos The output stream.
	 * @param __v The value to write.
	 * @throws IOException On write errors.
	 * @since 2018/11/11
	 */
	static final void __writeInt24(DataOutputStream __dos, int __v)
		throws IOException
	{
		// Only unsigned
		if (__v < 0)
			__v = 0;
		
		// This is limited to 24 bits, so cap it so there
		else if (__v > 16777215)
			__v = 16777215;
		
		// Write values
		__dos.writeByte((byte)(__v >>> 16));
		__dos.writeByte((byte)(__v >>> 8));
		__dos.writeByte((byte)(__v));
	}
	
	/**
	 * Writes a 40 bit long.
	 *
	 * @param __dos The output stream.
	 * @param __v The value to write.
	 * @throws IOException On write errors.
	 * @since 2018/11/11
	 */
	static final void __writeLong40(DataOutputStream __dos, long __v)
		throws IOException
	{
		// Only unsigned
		if (__v < 0)
			__v = 0;
		
		// This is limited to 40 bits, so cap it so there
		else if (__v > 1099511627775L)
			__v = 1099511627775L;
		
		// Write values
		__dos.writeByte((byte)(__v >>> 32));
		__dos.writeByte((byte)(__v >>> 24));
		__dos.writeByte((byte)(__v >>> 16));
		__dos.writeByte((byte)(__v >>> 8));
		__dos.writeByte((byte)(__v));
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

