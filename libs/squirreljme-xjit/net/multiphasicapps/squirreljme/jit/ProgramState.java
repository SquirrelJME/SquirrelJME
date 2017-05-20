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
import java.util.Arrays;
import java.util.ArrayList;
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
{
	/** The byte code for the method. */
	protected final ByteCode code;
	
	/** This contains the basic block zones, sorted at zone start address. */
	private final BasicBlockZone[] _zones;
	
	/**
	 * Initializes the program state.
	 *
	 * @param __code The method byte code.
	 * @param __smtdata The stack map data.
	 * @param __smtmodern Is the stack map table a modern one?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/14
	 */
	ProgramState(ByteCode __code, byte[] __smtdata, boolean __smtmodern)
		throws IOException, NullPointerException
	{
		// Check
		if (__code == null || __smtdata == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.code = __code;
		int codelen = __code.length();
		
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
			zones.add(new BasicBlockZone(__code, baseat, jumpat));
			
			// New base address
			baseat = jumpat;
		}
		this._zones = zones.<BasicBlockZone>toArray(
			new BasicBlockZone[zones.size()]);
		
		throw new todo.TODO();
	}
}

