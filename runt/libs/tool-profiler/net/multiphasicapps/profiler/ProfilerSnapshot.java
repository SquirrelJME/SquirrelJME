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
	private final Map<String, ProfiledThread> _threads =
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
	 * Writes the snapshot information to the given output stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// We write to this because we need the data
		DataOutputStream cont = new DataOutputStream(__os);
		
		// Magic number
		cont.writeBytes("nBpRoFiLeR");
		
		// Version
		cont.write(1);
		cont.write(2);
		
		// Type
		cont.writeInt(1);
		
		// Write compressed snapshot data to the byte array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(2048))
		{
			// Compress the CPU data
			ZLibCompressor olddefl;
			try (ZLibCompressor defl = new ZLibCompressor(baos);
				DataOutputStream cpu = new DataOutputStream(defl))
			{
				// Used to get compressed size and such
				olddefl = defl;
				
				// Write header
				cpu.writeInt(1);
				
				// Timestamp and duration
				long start = this.startmillis;
				cpu.writeLong(start);
				cpu.writeLong(System.currentTimeMillis() - start);
				
				// Thread time is always measured
				cpu.writeBoolean(true);
				
				// Build and write the instrumented method table
				Map<FrameLocation, Integer> mids = this.__doMethodTable();
				cpu.writeInt(mids.size());
				for (FrameLocation loc : mids.keySet())
				{
					cpu.writeUTF(loc.inclass);
					cpu.writeUTF(loc.methodname);
					cpu.writeUTF(loc.methodtype);
				}
				
				// Write thread data
				Map<String, ProfiledThread> threads = this._threads;
				synchronized (threads)
				{
					cpu.writeInt(threads.size());
				
					// Write individual thread
					for (ProfiledThread t : threads.values())
						this.__doWriteThread(cpu, t, mids);
				}
			}
			
			// Store sizes, which are important
			cont.writeInt(baos.size());
			cont.writeInt((int)Math.min(Integer.MAX_VALUE,
				olddefl.uncompressedBytes()));
			
			// Store the compressed data
			baos.writeTo(cont);
		}
		
		// Write properties of the snapshot
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(2048))
		{
			// Write them all
			try (PrintStream ps = new PrintStream(baos, true))
			{
				// Make sure it is flushed
				ps.flush();
			}
			
			// Store data
			cont.writeInt(baos.size());
			baos.writeTo(cont);
		}
		
		// Write comment
		cont.writeUTF("Generated by SquirrelJME " +
			"<https://multiphasicapps.net/>\n" +
			"Copyright (C) Stephanie Gawroriski 2016-2019\n" +
			"\n" +
			"Record date: " + new Date());
		
		// Make sure everything is flushed
		cont.flush();
	}
	
	/**
	 * Builds the the method table.
	 *
	 * @return The table of instrumented methods.
	 * @since 2018/11/11
	 */
	private final Map<FrameLocation, Integer> __doMethodTable()
	{
		// The table and the ID, which must be passable to sub-builders
		Map<FrameLocation, Integer> rv = new LinkedHashMap<>();
		int[] next = new int[1];
		
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
	private final Map<FrameLocation, Integer> __doMethodTableSub(
		Map<FrameLocation, Integer> __rv,
		int[] __nid, Iterable<ProfiledFrame> __fs)
		throws NullPointerException
	{
		if (__rv == null || __nid == null || __fs == null)
			throw new NullPointerException("NARG");
		
		// Handle each frame
		for (ProfiledFrame f : __fs)
		{
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
	 * Writes the thread information to the stream.
	 *
	 * @param __cpu The stream to write to.
	 * @param __t The thread information to write.
	 * @param __mids The method IDs.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	private final void __doWriteThread(DataOutputStream __cpu,
		ProfiledThread __t, Map<FrameLocation, Integer> __mids)
		throws IOException, NullPointerException
	{
		if (__cpu == null || __t == null || __mids == null)
			throw new NullPointerException("NARG");
		
		// ID and name
		__cpu.writeInt(__t.name.hashCode());
		__cpu.writeUTF(__t.name);
		
		// Always measure thread time
		__cpu.writeBoolean(true);
		
		// Parse the node table for this thread
		__NodeTable__ nodes = new __NodeTable__();
		nodes.parse(__t._frames.values());
		
		// Write the node table
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024))
		{
			// Write to the temporary buffer
			nodes.writeTo(baos, __mids);
			
			// Store the node table
			__cpu.writeInt(baos.size());
			baos.writeTo(__cpu);
		}
		
		// Node size is always 28
		__cpu.writeInt(28);
		
		// Whole graph time
		__cpu.writeLong(__t._totaltime);
		__cpu.writeLong(__t._cputime);
		
		// No injected methods used
		__cpu.writeLong(0);
		__cpu.writeLong(0);
		
		// Pure time (always seems to be max value)
		__cpu.writeLong(Integer.MAX_VALUE);
		__cpu.writeLong(Integer.MAX_VALUE);
		
		// Net time
		__cpu.writeLong(__t._totaltime);
		__cpu.writeLong(__t._cputime);
		
		// The number of methods invoked
		__cpu.writeLong(__t._invtotal);
		
		// Always display CPU time
		__cpu.writeBoolean(true);
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
	private static final void __dumpFrames(PrintStream __ps, int __tab,
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
				f._numcalls,
				f._traceselftime,
				f._frameselftime);
			__ps.println();
			
			// Go into this frame's frames
			ProfilerSnapshot.__dumpFrames(__ps, __tab + 1, f._frames.values());
		}
	}
}

