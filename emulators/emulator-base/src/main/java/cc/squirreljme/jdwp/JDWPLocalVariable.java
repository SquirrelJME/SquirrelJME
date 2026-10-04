// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a local variable within JDWP.
 *
 * @since 2022/09/21
 */
public final class JDWPLocalVariable
{
	/** The local variable slot. */
	public final int localSlot;
	
	/** The start PC address. */
	public final long startPc;
	
	/** The end PC address. */
	public final int length;
	
	/** The field descriptor. */
	public final String fieldDescriptor;
	
	/** The variable name. */
	public final String variableName;
	
	/**
	 * Initializes the local variable info.
	 * 
	 * @param __localSlot The local variable slot.
	 * @param __startPc The start PC address.
	 * @param __length The number of PC addresses used.
	 * @param __fieldDescriptor The field descriptor.
	 * @param __variableName The variable name.
	 * @since 2022/09/21
	 */
	public JDWPLocalVariable(int __localSlot, long __startPc, int __length,
		String __fieldDescriptor, String __variableName)
	{
		this.localSlot = __localSlot;
		this.startPc = __startPc;
		this.length = __length;
		this.fieldDescriptor = __fieldDescriptor;
		this.variableName = __variableName;
	}
}
