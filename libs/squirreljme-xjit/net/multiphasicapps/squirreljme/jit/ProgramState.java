// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * This contains the entire state of the program. The program consists of
 * multiple {@link BasicBlockZone}s. Each basic block as an entry state and
 * encompasses a set of instructions. Control flows from one basic block to
 * another depending on the output and input alias state. The program is parsed
 * in a queue order generating virtual instructions as needed for each region
 * of basic blocks for differing aliasing types.
 *
 * @since 2017/05/13
 */
public class ProgramState
	implements Runnable
{
	/** The byte code for the method. */
	protected final ByteCode code;
	
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/** The link table for imports. */
	protected final LinkTable linktable;
	
	/** This contains the basic block zones, sorted at zone start address. */
	private final BasicBlockZone[] _zones;
	
	/**
	 * Initializes the program state.
	 *
	 * @param __code The method byte code.
	 * @param __smtdata The stack map data.
	 * @param __smtmodern Is the stack map table a modern one?
	 * @param __em The method this program is for.
	 * @param __conf The JIT configuration.
	 * @param __lt The link table used for imports and exports.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/14
	 */
	ProgramState(ByteCode __code, byte[] __smtdata, boolean __smtmodern,
		ExportedMethod __em, JITConfig __conf, LinkTable __lt)
		throws IOException, NullPointerException
	{
		// Check
		if (__code == null || __smtdata == null || __em == null ||
			__conf == null || __lt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.code = __code;
		this.linktable = __lt;
		int codelen = __code.length();
		
		// Parse the stack map to determine the starting state of each
		// basic block zone
		__StackMapParser__ smp = new __StackMapParser__(__code, __smtdata,
			__smtmodern, __em);
		
		// Initialize the basic block zones which determines which sections
		// of the program will be parsed as a single unit
		List<BasicBlockZone> zones = new ArrayList<>();
		int baseat = 0;
		JumpTarget[] jumptargets = __code.jumpTargets();
		for (int i = 0, n = jumptargets.length; i <= n; i++)
		{
			// Ignore this address if it matches the jump target
			int jumpat = (i == n ? codelen : jumptargets[i].address());
			if (jumpat == baseat)
				continue;
			
			// Debug
			System.err.printf("DEBUG -- BBZ %d - %d%n", baseat, jumpat);
			
			// Add new zone
			zones.add(new BasicBlockZone(__code, baseat, jumpat,
				smp.get(baseat)));
			
			// New base address
			baseat = jumpat;
		}
		this._zones = zones.<BasicBlockZone>toArray(
			new BasicBlockZone[zones.size()]);
	}
	
	/**
	 * Returns the zone at the given address.
	 *
	 * @param __a The address to get the zone for.
	 * @return The zone at the given address.
	 * @throws IndexOutOfBoundsException If the address is a out of range.
	 * @since 2017/05/20
	 */
	public BasicBlockZone getZone(int __a)
		throws IndexOutOfBoundsException
	{
		// Out of range?
		if (__a < 0 || __a >= this.code.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Binary search for the given zone
		BasicBlockZone[] zones = this._zones;
		for (int n = zones.length, l = 0, r = n, p = n >> 1;;)
		{
			BasicBlockZone zone = zones[p];
			int cr = zone.compareAddressRange(__a);
			
			// Match?
			if (cr == 0)
				return zone;
			
			// Go left?
			else if (cr < 0)
				r = p;
			
			// Go right?
			else
				l = p;
			
			// Partition in the middle
			p = l + ((r - l) >> 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public void run()
	{
		BasicBlockZone entryzone = getZone(0);
		
		throw new todo.TODO();
	}
}

