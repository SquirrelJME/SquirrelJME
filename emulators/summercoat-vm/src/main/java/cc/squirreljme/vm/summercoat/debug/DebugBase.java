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
import cc.squirreljme.vm.summercoat.MachineState;
import cc.squirreljme.vm.summercoat.MemHandle;
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
	final MemHandle __getHandle(MemHandle __handle, int __dx)
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
	final int __getInteger(MemHandle __handle, int __dx)
		throws NullPointerException
	{
		return DebugBase.getInteger(this.__machine(), __handle, __dx);
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
