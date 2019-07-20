// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.pool.BasicPoolBuilder;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This contains the encoder for dual pools.
 *
 * @since 2019/07/17
 */
public final class DualPoolEncoder
{
	/** The size of the table entries. */
	public static final int TABLE_ENTRY_SIZE =
		8;
	
	/**
	 * Not used.
	 *
	 * @since 2019/07/17
	 */
	private DualPoolEncoder()
	{
	}
	
	/**
	 * Encodes the dual pool to the given output stream and returns the
	 * result.
	 *
	 * @param __dp The dual-pool to encode.
	 * @param __out The stream to write to.
	 * @return The result with size information.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final DualPoolEncodeResult encode(
		DualClassRuntimePoolBuilder __dp, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__dp == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Wrap output
		DataOutputStream tdos = new DataOutputStream(__out);
		
		// Return values
		int staticpooloff = 0,
			staticpoolsize = 0,
			runtimepooloff = 0,
			runtimepoolsize = 0;
		
		// Write both of the pools
		for (boolean isruntime = false;; isruntime = true)
		{
			// Are we encoding the static or run-time pool?
			BasicPoolBuilder pool = (isruntime ? __dp.runtimePool() :
				__dp.classPool());
			
			// The size of the pool, used for the first entry
			int poolsize = pool.size();
			
			// This is the base offset where the pool is located
			if (isruntime)
				runtimepooloff = tdos.size();
			else
				staticpooloff = tdos.size();
			
			// Determine the relative offset to where the entry data will
			// exist
			int reloff = poolsize + TABLE_ENTRY_SIZE;
			
			// The value data is appended right after the pool data
			try (ByteArrayOutputStream vaos = new ByteArrayOutputStream();
				DataOutputStream vdos = new DataOutputStream(vaos))
			{
				// Write individual pool entries
				for (BasicPoolEntry e : pool)
				{
					// Determine the type of entry this is
					Object ev = e.value;
					MinimizedPoolEntryType etype =
						MinimizedPoolEntryType.ofClass(ev.getClass());
					
					// {@squirreljme.error JC4d Cannot store the given entry
					// because it not compatible with the static/run-time
					// state. (The pool type; The value type; Is the run-time
					// pool being processed?)}
					if (isruntime != etype.isRuntime())
						throw new IllegalStateException("JC4d " +
							etype + " " + ev + " " + isruntime);
					
					throw new todo.TODO();
				}
				
				// Merge the value data on top of the base
				vaos.writeTo(tdos);
			}
			
			// The size of the pool in bytes
			if (isruntime)
				runtimepoolsize = tdos.size() - runtimepooloff;
			else
				staticpoolsize = tdos.size() - staticpooloff;
			
			// Stop processing after the run-time is done
			if (isruntime)
				break;
		}
		
		// Return the location of the data
		return new DualPoolEncodeResult(staticpooloff, staticpoolsize,
			runtimepooloff, runtimepoolsize);
	}
}

