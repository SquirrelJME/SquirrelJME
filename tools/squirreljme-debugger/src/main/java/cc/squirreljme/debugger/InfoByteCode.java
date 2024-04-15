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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Pool;

/**
 * Stores remote byte code information.
 *
 * @since 2024/01/23
 */
public class InfoByteCode
	extends Info
{
	/** The referenced method. */
	protected final WeakReference<InfoMethod> method;
	
	/** The constant pool of the class this is in. */
	protected final KnownValue<Pool> constantPool;
	
	/** The byte code of the method. */
	private final byte[] _byteCode;
	
	/** The byte code instructions. */
	private volatile InstructionViewer[] _instructions;
	
	/**
	 * Initializes the byte code information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __method The owning method.
	 * @param __constantPool The constant pool.
	 * @param __byteCode The raw byte code.
	 * @since 2024/01/23
	 */
	public InfoByteCode(DebuggerState __state, JDWPId __id,
		InfoMethod __method, KnownValue<Pool> __constantPool,
		byte[] __byteCode)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.BYTE_CODE);
		
		this.method = new WeakReference<>(__method);
		this.constantPool = __constantPool;
		this._byteCode = __byteCode.clone();
	}
	
	
	/**
	 * Returns the byte code instructions.
	 *
	 * @return The byte code instructions.
	 * @since 2024/01/23
	 */
	public InstructionViewer[] instructions()
	{
		// Use pre-cached value?
		InstructionViewer[] result = this._instructions;
		if (result != null)
			return result.clone();
		
		// Resultant instructions
		List<InstructionViewer> output = new ArrayList<>();
		
		// Used to refer to the instructions
		KnownValue<Pool> pool = this.constantPool;
		
		// We will be referencing this much!
		byte[] byteCode = this._byteCode;
		for (int at = 0, limit = byteCode.length; at < limit;)
		{
			// Setup instruction at this point
			InstructionViewer instruction = new RemoteInstructionViewer(
				this.internalState(), pool, byteCode, at);
			
			// Move the pointer up
			at += instruction.length();
			
			// Store it for later usage
			output.add(instruction);
		}
		
		// Cache for later usage
		result = output.toArray(
			new InstructionViewer[output.size()]);
		this._instructions = result;
		
		// Return all the instructions.
		return result.clone();
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
