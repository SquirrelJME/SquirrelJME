// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This represents a single frame in the execution stack.
 *
 * @since 2019/04/21
 */
public final class CPUFrame
{
	/** Execution slices. */
	final Deque<ExecutionSlice> _execslices;
	
	/** Registers for this frame. */
	final int[] _registers =
		new int[NativeCPU.MAX_REGISTERS];
	
	/** The entry PC address. */
	int _entrypc;
	
	/** The PC address for this frame. */
	volatile int _pc;
	
	/** Last executed address. */
	int _lastpc;
	
	/** The executing class. */
	String _inclass;
	
	/** Executing class name pointer. */
	int _inclassp;
	
	/** The executing method name. */
	String _inmethodname;
	
	/** Executing method name pointer. */
	int _inmethodnamep;
	
	/** The executing method type. */
	String _inmethodtype;
	
	/** Executing method type pointer. */
	int _inmethodtypep;
	
	/** Source file. */
	String _insourcefile;
	
	/** Source file pointer. */
	int _insourcefilep;
	
	/** The current line. */
	int _inline;
	
	/** The current Java operation. */
	int _injop;
	
	/** The current Java address. */
	int _injpc;
	
	/** The current task ID. */
	int _taskid;
	
	/**
	 * Potential initialization.
	 */
	{
		this._execslices = (NativeCPU.ENABLE_DEBUG ?
			new LinkedList<ExecutionSlice>() :
			(Deque<ExecutionSlice>)null);
	}
}
