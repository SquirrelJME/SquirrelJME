// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.debug;

import cc.squirreljme.jdwp.views.JDWPView;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.jvm.summercoat.constants.StaticVmAttribute;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemoryInputStream;
import cc.squirreljme.jvm.summercoat.ld.mem.WritableMemory;
import cc.squirreljme.vm.summercoat.MachineState;
import cc.squirreljme.vm.summercoat.MemHandle;
import cc.squirreljme.vm.summercoat.VMMemoryAccessException;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Base for debugging.
 *
 * @since 2021/05/11
 */
public abstract class DebugBase
	implements JDWPView
{
	/** The machine to refer to. */
	private final Reference<MachineState> _machine;
	
	/**
	 * Initializes the debug base.
	 * 
	 * @param __machine The machine used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/11
	 */
	public DebugBase(Reference<MachineState> __machine)
		throws NullPointerException
	{
		if (__machine == null)
			throw new NullPointerException("NARG");
		
		this._machine = __machine; 
	}
	
	/**
	 * Obtains a handle value.
	 * 
	 * @param __handle The handle to read from.
	 * @param __dx The index to read at.
	 * @return The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/11
	 */
	public final MemHandle getHandle(MemHandle __handle, int __dx)
		throws NullPointerException
	{
		return DebugBase.getHandle(this.__machine(), __handle, __dx);
	}
	
	/**
	 * Obtains an integer value.
	 * 
	 * @param __handle The handle to read from.
	 * @param __dx The index to read at.
	 * @return The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/11
	 */
	public final int getInteger(MemHandle __handle, int __dx)
		throws NullPointerException
	{
		return DebugBase.getInteger(this.__machine(), __handle, __dx);
	}
	
	/**
	 * Returns the given long value from a handle.
	 * 
	 * @param __handle The handle to read from.
	 * @param __dx The property, this should be the low value as these
	 * properties are in low+high order.
	 * @return The long value.
	 * @since 2021/07/05
	 */
	public final long getLong(MemHandle __handle, int __dx)
	{
		return (this.getInteger(__handle, __dx) & 0xFFFFFFFFL) |
			((this.getInteger(__handle, __dx + 1) & 0xFFFFFFFFL) << 32);
	}
	
	/**
	 * Safely reads a UTF-8 string value.
	 * 
	 * @param __addr The address to read from.
	 * @return The read string.
	 * @since 2021/07/05
	 */
	public String readUtfSafe(long __addr)
	{
		// Read length to figure out how long the string is
		WritableMemory memory = this.__machine().memory();
		int strlen = -1;
		try
		{
			strlen = memory.memReadShort(__addr) & 0xFFFF;
			
			// Decode string data
			try (DataInputStream dis = new DataInputStream(
				new ReadableMemoryInputStream(memory, __addr,
					strlen + 2)))
			{
				return dis.readUTF();
			}
		}
		
		// Could not read string, use some other string form
		catch (IOException | VMMemoryAccessException e)
		{
			return String.format("@%08x/%d???", __addr, strlen);
		}
	}
	
	/**
	 * Returns the used machine.
	 * 
	 * @return The machine state.
	 * @throws IllegalStateException If the machine was GCed.
	 * @since 2021/05/11
	 */
	final MachineState __machine()
		throws IllegalStateException
	{
		MachineState rv = this._machine.get();
		if (rv == null)
			throw new IllegalStateException("Machine GCed.");
		return rv;
	}
	
	/**
	 * Returns the object as a memory handle.
	 * 
	 * @param __which The object to get the handle of.
	 * @param __kind The {@link MemHandleKind}, if
	 * {@link MemHandleKind#UNDEFINED} this will not be checked.
	 * @return The memory handle.
	 * @throws IllegalArgumentException If this is not a memory handle.
	 * @since 2021/05/11
	 */
	public static MemHandle handle(Object __which, int __kind)
		throws IllegalArgumentException
	{
		if (!(__which instanceof MemHandle))
			throw new IllegalArgumentException("Not a handle: " + __which);
		
		MemHandle rv = (MemHandle)__which;
		if (__kind != MemHandleKind.UNDEFINED && rv.kind() != __kind)
			throw new IllegalArgumentException(
				String.format("Mismatched Kind: %s is not %d",
					__which, __kind));
		
		return rv;
	}
	
	/**
	 * Checks to ensure this is a type based handle.
	 * 
	 * @param __which Object to get the handle of.
	 * @return The type based handle.
	 * @since 2021/07/05
	 */
	public static MemHandle handleType(Object __which)
	{
		return DebugBase.handle(__which, MemHandleKind.CLASS_INFO);
	}
	
	/**
	 * Obtains a handle value.
	 * 
	 * @param __machine The machine used.
	 * @param __handle The handle to read from.
	 * @param __dx The index to read at.
	 * @return The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/11
	 */
	public static MemHandle getHandle(MachineState __machine,
		MemHandle __handle, int __dx)
		throws NullPointerException
	{
		if (__machine == null || __handle == null)
			throw new NullPointerException("NARG");
		
		// Make sure this is valid
		int id = DebugBase.getInteger(__machine, __handle, __dx);
		if (id == 0)
			return null;
		
		return __machine.memHandles().get(id);
	}
	
	/**
	 * Obtains an integer value.
	 * 
	 * @param __machine The machine used.
	 * @param __handle The handle to read from.
	 * @param __dx The index to read at.
	 * @return The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/11
	 */
	public static int getInteger(MachineState __machine,
		MemHandle __handle, int __dx)
		throws NullPointerException
	{
		if (__machine == null || __handle == null)
			throw new NullPointerException("NARG");
		
		// Read the list value
		return __handle.memReadInt((4L * __dx) +
			__machine.staticAttribute(StaticVmAttribute.SIZE_BASE_ARRAY));
	}
}
