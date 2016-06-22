// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.ci.CIByteBuffer;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents a single operation in the byte code.
 *
 * @since 2016/05/11
 */
public final class BCOperation
{
	/** The owning byte code. */
	protected final BCByteCode owner;
	
	/** The raw operation data. */
	protected final BCRawOperation rop;
	
	/** The logical position. */
	protected final int logicaladdress;
	
	/** The instruction ID. */
	protected final int instructionid;
	
	/** The continued result of verification. */
	protected final BCStateVerification verifresult;
	
	/** The verification used on entry of this operation. */
	private volatile Reference<BCStateVerification> _entryverif;
	
	/** Cached micro-operations this instruction performs. */
	private volatile Reference<BCMicroOperations> _microps;
	
	/** The string representation of this operation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the operation data.
	 *
	 * @param __bc The owning byte code.
	 * @param __bb The buffer which contains code.
	 * @throws BCException If the operation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	BCOperation(BCByteCode __bc, BCRawOperation __rop)
		throws BCException, NullPointerException
	{
		// Check
		if (__bc == null || __rop == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __bc;
		this.rop = __rop;
		this.logicaladdress = __rop.logicalAddress();
		
		// Set the instruction ID
		int iid = (int)__rop.get(0);
		this.instructionid = iid;
		
		// Get micro operations to check verification state
		BCMicroOperations microps = microOps();
		
		// Modify the verification state depending on the operations
		throw new Error("TODO");
		
		/*
		// Read opcode
		int phy = __bc.logicalToPhysical(__lp);
		int opcode = __bb.readUnsignedByte(phy);
		if (opcode == BCInstructionID.WIDE)
			opcode = (opcode << 8) | __bb.readUnsignedByte(phy, 1);
		
		// Set
		this.instructionid = opcode;
		
		// Determine the entry verification state
		BCStateVerification expv = __bc.explicitVerification().get(__lp);
		if (expv != null)
			verification = expv;
		
		// Otherwise, the entry state is derived from the source operation (the
		// operation which precedes this one during execution)
		else
			verification = (expv = __bc.get(__lp - 1).verificationOutput());
		
		// Initialize operation data
		__OpInitData__ out = new __OpInitData__(this);
		switch (opcode)
		{
			case BCInstructionID.DUP: __OpInit__.dup(out); break;
				
			case BCInstructionID.INVOKEINTERFACE:
				__OpInit__.invoke(out, BCInvokeType.INTERFACE);
				break;
			
			case BCInstructionID.INVOKESPECIAL:
				__OpInit__.invoke(out, BCInvokeType.SPECIAL);
				break;
				
			case BCInstructionID.INVOKESTATIC:
				__OpInit__.invoke(out, BCInvokeType.STATIC);
				break;
				
			case BCInstructionID.INVOKEVIRTUAL:
				__OpInit__.invoke(out, BCInvokeType.VIRTUAL);
				break;
				
			case BCInstructionID.NEW: __OpInit__.new_(out); break;
				
				// {@squirreljme.error AX05 The instruction identifier for the
				// specified position is not valid or is not yet supported.
				// (The logical instruction position; The operation code)}
			default:
				throw new BCException(String.format("AX05 %d %d", __lp,
					opcode));
		}
		
		// Set
		arguments = out.getArguments();
		localaccess = out.getLocalAccess();
		stackpop = out.getStackPop();
		stackpush = out.getStackPush();
		rwinstructionid = out.getRewrite();
		
		// Determine the result of this operation for targets if applicable
		verifresult = expv.derive(this);
		*/
	}
	
	/**
	 * Returns the address of this operation.
	 *
	 * @return The operation address.
	 * @since 2016/05/13
	 */
	public int address()
	{
		return this.logicaladdress;
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
	 * Returns the micro-operations that this Java operation performs.
	 *
	 * @return The list of micro operations.
	 * @since 2016/06/22
	 */
	public BCMicroOperations microOps()
	{
		// Get
		Reference<BCMicroOperations> ref = this._microps;
		BCMicroOperations rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
		{
			// Need to know the verification state of this instruction, either
			// this one or the one before it
			BCStateVerification entv = verificationInput();
			
			// Depends on the instruction
			int iid = this.instructionid;
			switch (iid)
			{
					// Allocate new object
				case BCInstructionID.NEW:
					if (true)
						throw new Error("TODO");
					break;
				
					// {@squirreljme.error AX11 The specified operation cannot
					// be handled because it is not known. (The instruction
					// opcode)}
				default:
					throw new BCException(String.format("AX11 %d", iid));
			}
			
			// Store
			this._microps = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the owner of of this operation.
	 *
	 * @return The operation owner.
	 * @since 2016/05/13
	 */
	public BCByteCode owner()
	{
		return this.owner;
	}
	
	/**
	 * Returns the physical address of the operation.
	 *
	 * @return The operation physical address.
	 * @since 2016/05/13
	 */
	public int physicalAddress()
	{
		return this.owner.logicalToPhysical(this.logicaladdress);
	}
	
	/**
	 * Returns the raw operation data.
	 *
	 * @return The raw operation data.
	 * @since 2016/06/22
	 */
	public BCRawOperation rawOperation()
	{
		return this.rop;
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
			_string = new WeakReference<>((rv = "(id=" + instructionId() +
				", rop=" + rawOperation() +
				", iv=" + verificationInput() +
				", ov=" + verificationOutput() + ")"));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the verification state of this operation.
	 *
	 * @return The verification state.
	 * @since 2016/05/12
	 */
	public BCStateVerification verificationInput()
	{
		// Check
		Reference<BCStateVerification> ref = this._entryverif;
		BCStateVerification rv;
		
		// Locate it?
		if (ref == null || null == (rv = ref.get()))
		{
			// An explicit one is used?
			BCByteCode owner = this.owner;
			int la = this.logicaladdress;
			BCStateVerification expv = owner.explicitVerification(la);
			if (expv != null)
				rv = expv;
		
			// Otherwise, the entry state is derived from the source operation
			// (the operation which precedes this one during execution)
			else
				rv = (expv = owner.get(la - 1).verificationOutput());
			
			// Cache
			this._entryverif = new WeakReference<>(rv);
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the verification state that would be under affect when this
	 * operation has been executed.
	 *
	 * @return The resulting verification state.
	 * @since 2016/05/12
	 */
	public BCStateVerification verificationOutput()
	{
		return this.verifresult;
	}
}

