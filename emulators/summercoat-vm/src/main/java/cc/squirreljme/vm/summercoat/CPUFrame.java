// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import dev.shadowtail.classfile.nncc.NativeCode;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.LinkedList;

/**
 * This represents a single frame in the execution stack.
 *
 * @since 2019/04/21
 */
public final class CPUFrame
{
	/** The array base. */
	protected final int arrayBase;
	
	/** The memory handle manager, for easy handle access. */
	private final Reference<MemHandleManager> _handleManager;
	
	/** Execution slices. */
	final Deque<ExecutionSlice> _execslices;
	
	/** Registers for this frame. */
	private final int[] _registers =
		new int[NativeCPU.MAX_REGISTERS];
	
	/** The last native operation. */
	int _lastNativeOp;
	
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
	
	/**
	 * Initializes the frame.
	 * 
	 * @param __man The manager used to obtain handles.
	 * @param __arrayBase The array base.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/18
	 */
	public CPUFrame(MemHandleManager __man, int __arrayBase)
		throws NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		this.arrayBase = __arrayBase;
		this._handleManager = new WeakReference<>(__man);
	}
	
	/**
	 * Returns the value of the given register.
	 * 
	 * @param __r The register to read from.
	 * @return The value of the given register.
	 * @since 2021/01/18
	 */
	public int get(int __r)
	{
		return this._registers[__r];
	}
	
	/**
	 * Returns a handle for the given register.
	 * 
	 * @param __r The register to read from.
	 * @return The handle value.
	 * @since 2021/01/18
	 */
	public MemHandle getHandle(int __r)
	{
		int val = this._registers[__r];
		return (val == 0 ? null : this.__handleManager().get(val));
	}
	
	/**
	 * Returns all of the registers.
	 * 
	 * @return The registers.
	 * @since 2021/01/18
	 */
	@Deprecated
	public int[] getRegisters()
	{
		return this._registers;
	}
	
	/**
	 * Reads a value from the pool.
	 * 
	 * @param __dx The index to read.
	 * @return The value of the index. 
	 * @throws VMException If this is not a constant pool or the index is out
	 * of the constant pool bounds.
	 * @since 2021/01/18
	 */
	public int pool(int __dx)
		throws VMException
	{
		// This must be a pool handle
		MemHandle poolHandle = this.getHandle(NativeCode.POOL_REGISTER);
		if (poolHandle == null || poolHandle.kind != MemHandleKind.POOL)
			throw new VMException("Not a pool handle: " + poolHandle);
		
		// Ensure it is in the pool
		int arrayBase = this.arrayBase;
		if (__dx < 0 || __dx >= ((poolHandle.size - arrayBase) / 4))
			throw new VMException("Out of bounds pool read: " + __dx);
		
		// Return the value of the entry
		int rv = poolHandle.memReadInt(arrayBase + (__dx * 4));
		
		// Debug
		if (NativeCPU.ENABLE_DEBUG)
			Debugging.debugNote("pool[%d] = %d (0x%08x)", __dx, rv, rv);
		
		return rv;
	}
	
	/**
	 * Sets the value of the given register.
	 * 
	 * @param __r The register to set.
	 * @param __v The value to set.
	 * @since 2021/01/18
	 */
	public final void set(int __r, int __v)
	{
		this._registers[__r] = __v;
	}
	
	/**
	 * Returns the memory handle manager.
	 * 
	 * @return The handle manager.
	 * @since 2021/01/18
	 */
	private MemHandleManager __handleManager()
	{
		MemHandleManager rv = this._handleManager.get();
		if (rv == null)
			throw new IllegalStateException("Handle manager was GCed.");
		
		return rv;
	}
}
