// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;

@Api
public class DualTone
{
	@Api
	public DualTone(int __aFreq, int __bFreq, int __duration, int __priority,
		int __vibration)
	{
		throw Debugging.todo();
	}
	
	@Api
	public DualTone(int[] __aFreq, int[] __bFreq, int[] __duration,
		int __priority, int __vibration)
	{
		// __aFreq = first frequency??
		//     new int[]{1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, ...};
		// __bFreq = second frequency??
		//     new int[]{1047, 988, 932, 880, 830, 784, 740, 698, 659, ...};
		// __duration = duration??
		//     new int[]{120, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 400};
		// __priority = always zero??
		// __vibration = always one or zero??
		
		if (__aFreq == null || __bFreq == null || __duration == null)
			throw Debugging.todo("Throw NullPointerException???");
		
		if (__aFreq.length != __bFreq.length ||
			__aFreq.length != __duration.length)
			throw Debugging.todo("Unequal lengths???");
		
		if (__priority != 0)
			throw Debugging.todo("D is not zero???");
		
		if (__vibration != 0 && __vibration != 1)
			throw Debugging.todo("E is not zero or one???");
		
		Debugging.todoNote("DualTone(%s, %s, %s, %d, %d)",
			IntegerArrayList.toString(__aFreq),
			IntegerArrayList.toString(__bFreq),
			IntegerArrayList.toString(__duration),
			__priority, __vibration);
	}
}
