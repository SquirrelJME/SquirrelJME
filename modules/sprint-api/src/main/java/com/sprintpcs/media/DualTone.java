// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.media;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;

public class DualTone
{
	public DualTone(int[] __unknownA, int[] __unknownB, int[] __unknownC,
		int __unknownD, int __unknownE)
	{
		// __unknownA = first frequency??
		//     new int[]{1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, ...};
		// __unknownB = second frequency??
		//     new int[]{1047, 988, 932, 880, 830, 784, 740, 698, 659, ...};
		// __unknownC = duration??
		//     new int[]{120, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 400};
		// __unknownD = always zero??
		// __unknownE = always one or zero??
		
		if (__unknownA == null || __unknownB == null || __unknownC == null)
			throw Debugging.todo("Throw NullPointerException???");
		
		if (__unknownA.length != __unknownB.length ||
			__unknownA.length != __unknownC.length)
			throw Debugging.todo("Unequal lengths???");
		
		if (__unknownD != 0)
			throw Debugging.todo("D is not zero???");
		
		if (__unknownE != 0 && __unknownE != 1)
			throw Debugging.todo("E is not zero or one???");
		
		Debugging.todoNote("DualTone(%s, %s, %s, %d, %d)",
			IntegerArrayList.toString(__unknownA),
			IntegerArrayList.toString(__unknownB),
			IntegerArrayList.toString(__unknownC),
			__unknownD, __unknownE);
	}
}
