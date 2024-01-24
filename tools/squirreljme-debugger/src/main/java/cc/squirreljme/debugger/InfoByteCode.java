// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Stores remote byte code information.
 *
 * @since 2024/01/23
 */
public class InfoByteCode
	extends Info
{
	/** The byte code instructions. */
	private final InstructionViewer[] _instructions;
	
	/**
	 * Initializes the byte code information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __instructions Instructions in the byte code.
	 * @since 2024/01/23
	 */
	public InfoByteCode(DebuggerState __state, JDWPId __id,
		InstructionViewer[] __instructions)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.BYTE_CODE);
		
		this._instructions = __instructions.clone();
	}
	
	/**
	 * Returns the byte code instructions.
	 *
	 * @return The byte code instructions.
	 * @since 2024/01/23
	 */
	public InstructionViewer[] instructions()
	{
		return this._instructions.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		return true;
	}
}
