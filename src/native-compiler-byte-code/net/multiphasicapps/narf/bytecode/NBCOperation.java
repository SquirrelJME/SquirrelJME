// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents a single operation in the byte code.
 *
 * @since 2016/05/11
 */
public final class NBCOperation
{
	/** The owning byte code. */
	protected final NBCByteCode owner;
	
	/** The logical position. */
	protected final int logicaladdress;
	
	/** The instruction ID. */
	protected final int instructionid;
	
	/** The local variables which are accessed. */
	protected final List<NBCLocalAccess> localaccess;
	
	/** Variables types which are popped from the stack. */
	protected final List<NBCVariableType> stackpop;
	
	/** Variable types which are pushed to the stack. */
	protected final List<NBCVariablePush> stackpush;
	
	/** The string representation of this operation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the operation data.
	 *
	 * @param __bc The owning byte code.
	 * @param __bb The buffer which contains code.
	 * @param __lp The logical position of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	NBCOperation(NBCByteCode __bc, NCIByteBuffer __bb, int __lp)
		throws NullPointerException
	{
		// Check
		if (__bc == null || __bb == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __bc;
		this.logicaladdress = __lp;
		
		// Read opcode
		int phy = __bc.logicalToPhysical(__lp);
		int opcode = __bb.readUnsignedByte(phy);
		if (opcode == NBCInstructionID.WIDE)
			opcode = (opcode << 8) | __bb.readUnsignedByte(phy, 1);
		
		// Set
		this.instructionid = opcode;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the instruction ID of this instruction.
	 *
	 * @return The instruction ID.
	 * @since 2016/05/12
	 */
	public int instructionId()
	{
		return this.instructionid;
	}
	
	/**
	 * Returns the list of local variables which are accessed by this
	 * operation.
	 *
	 * @return The local variable access list.
	 * @since 2016/05/12
	 */
	public List<NBCLocalAccess> localAccesses()
	{
		return localaccess;
	}
	
	/**
	 * Returns the list of variable types which are popped from this
	 * instruction.
	 *
	 * @return The type of values to pop.
	 * @since 2016/05/12
	 */
	public List<NBCVariableType> stackPops()
	{
		return stackpop;
	}
	
	/**
	 * Returns the list of variable types and value changes which are to be
	 * pushed to the stack.
	 *
	 * @return The push types and value assignment used.
	 * @since 2016/05/12
	 */
	public List<NBCVariablePush> stackPushes()
	{
		return stackpush;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/11
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "(" + instructionid + ")"));
		
		// Return it
		return rv;
	}
}

