// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.profiler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.io.ZLibCompressor;

/**
 * This represents the main profiler snapshot which contains all of the data
 * within what is to be profiled, it is mutable and accordingly allows for
 * export to NPS formats.
 *
 * @since 2018/11/10
 */
public final class ProfilerSnapshot
{
	/** The start time of the snapshot. */
	protected final long startmillis =
		System.currentTimeMillis();
	
	/** Threads that are being measured. */
	final Map<String, ProfiledThread> _threads =
		new LinkedHashMap<>();
	
	/**
	 * Starts profiling the given thread.
	 *
	 * @param __name The name of the thread.
	 * @return A class to handle the profiling of threads via the call stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public final ProfiledThread measureThread(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		ProfiledThread rv = new ProfiledThread(__name);
		
		// Although the frames are not thread safe, this may be called at
		// any time from any thread although the returned class is only inteded
		// to be used by a single thread
		Map<String, ProfiledThread> threads = this._threads;
		synchronized (threads)
		{
			threads.put(__name, rv);
		}
		
		return rv;
	}
	
	/**
	 * Exits all threads and all frames using the current time.
	 *
	 * @since 2019/06/30
	 */
	public final void exitAll()
	{
		this.exitAll(System.nanoTime());
	}
	
	/**
	 * Exits all threads and all frames.
	 *
	 * @param __ns The current time.
	 * @since 2019/04/19
	 */
	public final void exitAll(long __ns)
	{
		// Exit every single thread
		Map<String, ProfiledThread> threads = this._threads;
		synchronized (threads)
		{
			for (ProfiledThread thread : threads.values())
				thread.exitAll(__ns);
		}
	}
	
	/**
	 * Writes snapshot information to the given stream.
	 *
	 * @param __ps The resulting stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void writeDumpTo(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		__ps.println("Profiler snapshot:");
		
		// Dump every thread
		Map<String, ProfiledThread> threads = this._threads;
		synchronized (threads)
		{
			for (ProfiledThread t : threads.values())
			{
				// Banner
				__ps.print("  Thread ");
				__ps.print(t.name);
				__ps.println(':');
				
				// Dump frame info
				ProfilerSnapshot.__dumpFrames(__ps, 4, t._frames.values());
			}
		}
	}
	
	/**
	 * Builds the method table.
	 *
	 * @return The table of instrumented methods.
	 * @since 2018/11/11
	 */
	Map<FrameLocation, Integer> __doMethodTable()
	{
		// The table and the ID, which must be passable to sub-builders
		Map<FrameLocation, Integer> rv = new LinkedHashMap<>();
		int[] next = new int[1];
		
		// Need root frame for the entry point, used virtually to refer to
		// multiple initial points in the entry frame since that is a thing
		rv.put(FrameLocation.ENTRY_POINT, next[0]++);
		
		// Have to go through all threads recursively due to all the frames
		// that exist to build IDs
		Map<String, ProfiledThread> threads = this._threads;
		synchronized (threads)
		{
			// Go through all threads
			for (ProfiledThread t : threads.values())
				this.__doMethodTableSub(rv, next, t._frames.values());
		}
		
		return rv;
	}
	
	/**
	 * Builds the method table for frames.
	 *
	 * @param __rv The table to write to.
	 * @param __nid The next ID.
	 * @param __fs The frames to process.
	 * @return The table of instrumented methods.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	private Map<FrameLocation, Integer> __doMethodTableSub(
		Map<FrameLocation, Integer> __rv,
		int[] __nid, Iterable<ProfiledFrame> __fs)
		throws NullPointerException
	{
		if (__rv == null || __nid == null || __fs == null)
			throw new NullPointerException("NARG");
		
		// Handle each frame
		for (ProfiledFrame f : __fs)
		{
			// Ignore if limit has been reached
			if (f._depth >= ProfiledFrame.MAX_STACK_DEPTH)
				continue;
			
			// If the location is not in the map, set the ID
			FrameLocation loc = f.location;
			if (!__rv.containsKey(loc))
			{
				// Generate new ID
				int id = __nid[0]++;
				
				// Store
				__rv.put(loc, id);
			}
			
			// Go into this frame's sub-frames
			this.__doMethodTableSub(__rv, __nid, f._frames.values());
		}
		
		return __rv;
	}
	
	/**
	 * Dumps the frames to the given stream.
	 *
	 * @param __ps The stream to print to.
	 * @param __tab The current tab level.
	 * @param __fs The frames to dump.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	private static void __dumpFrames(PrintStream __ps, int __tab,
		Iterable<ProfiledFrame> __fs)
		throws NullPointerException
	{
		if (__ps == null || __fs == null)
			throw new NullPointerException("NARG");
		
		// Go through each frame
		for (ProfiledFrame f : __fs)
		{
			// Write tabs first
			for (int i = 0; i < __tab; i++)
				__ps.print(' ');
			
			// Write the frame itself
			__ps.print(f.location);
			
			// Write information on the frame
			__ps.printf(" [n=%d, t=%d, s=%d]",
				f._numCalls,
				f._totalTime,
				f._selfTime);
			__ps.println();
			
			// Go into this frame's frames
			ProfilerSnapshot.__dumpFrames(__ps,
				__tab + 1, f._frames.values());
		}
	}
}

