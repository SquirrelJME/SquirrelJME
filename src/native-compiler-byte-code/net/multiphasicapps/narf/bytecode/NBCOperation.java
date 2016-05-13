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
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIClassReference;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.util.empty.EmptyList;
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
	
	/** Arguments of the operation. */
	protected final List<Object> arguments;
	
	/** The local variables which are accessed. */
	protected final List<NBCLocalAccess> localaccess;
	
	/** Variables types which are popped from the stack. */
	protected final List<NBCVariableType> stackpop;
	
	/** Variable types which are pushed to the stack. */
	protected final List<NBCVariablePush> stackpush;
	
	/** The verification state of this operation. */
	protected final NBCStateVerification verification;
	
	/** The continued result of verification. */
	protected final NBCStateVerification verifresult;
	
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
		
		// Constant pool to work with
		NCIPool pool = __bc.constantPool();
		NCILookup lookup = __bc.lookup();
		
		// Determine the entry verification state
		NBCStateVerification expv = __bc.explicitVerification().get(__lp);
		if (expv != null)
			verification = expv;
		
		// Otherwise, the entry state is derived from the source operation (the
		// operation which precedes this one during execution)
		else
			verification = (expv = __bc.get(__lp - 1).verificationOutput());
		
		// New
		if (opcode == NBCInstructionID.NEW)
		{
			// Get the type of class to allocate
			NCIClassReference ref = pool.<NCIClassReference>requiredAs(
				__bb.readUnsignedShort(phy, 1), NCIClassReference.class);
			arguments = __arguments(ref);
			
			// Lookup the class
			NCIClass ncl = lookup.lookup(ref.get());
			
			// {@squirreljme.error AX0a Byte code refers to a class to be
			// allocated, however it does not exist. (The class name)}
			if (ncl == null)
				throw new NBCException(NBCException.Issue.MISSING_CLASS,
					String.format("AX0a %s", ref));
			
			// {@squirreljme.error AX0b The byte code would have attempted to
			// allocate a class which was either abstract or an interface.
			// (The class name; The class flags)}
			NCIClassFlags fl = ncl.flags();
			if (fl.isAbstract() || fl.isInterface())
				throw new NBCException(NBCException.Issue.INIT_ABSTRACT_CLASS,
					String.format("AX0b %s %s", ref, fl));
			
			// {@squirreljme.error AX0f The specified class cannot be accessed
			// and cannot be allocated. (The class to access)}
			if (!__bc.canAccess(ncl))
				throw new NBCException(NBCException.Issue.CANNOT_ACCESS_CLASS,
					String.format("AX0f %s", ncl.thisName()));
			
			// Nothing is popped and no variables are touched
			stackpop = __stackPop();
			localaccess = __localAccess();
			
			// A new object is pushed
			stackpush = __stackPush(NBCVariablePush.newObject());
		}
		
		// {@squirreljme.error AX05 The instruction identifier for the
		// specified position is not valid or is not yet supported. (The
		// logical instruction position; The operation code)}
		else
			throw new NBCException(NBCException.Issue.ILLEGAL_OPCODE,
				String.format("AX05 %d %d", __lp, opcode));
		
		// Determine the result of this operation for targets if applicable
		verifresult = expv.derive(this);
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
	 * Returns the arguments to this instruction.
	 *
	 * @return The list of arguments.
	 * @since 2016/05/12
	 */
	public List<Object> arguments()
	{
		return this.arguments;
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
		return this.localaccess;
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
		return this.stackpop;
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
		return this.stackpush;
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
				", args=" + arguments() +
				", la=" + localAccesses() + ", pop=" + stackPops() +
				", push=" + stackPushes() + ", iv=" + verificationInput() +
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
	public NBCStateVerification verificationInput()
	{
		return this.verification;
	}
	
	/**
	 * Returns the verification state that would be under affect when this
	 * operation has been executed.
	 *
	 * @return The resulting verification state.
	 * @since 2016/05/12
	 */
	public NBCStateVerification verificationOutput()
	{
		return this.verifresult;
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @since 2016/05/12
	 */
	private static List<Object> __arguments(Object... __a)
	{
		if (__a == null || __a.length <= 0)
			return EmptyList.<Object>empty();
		return UnmodifiableList.<Object>of(Arrays.<Object>asList(__a));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @since 2016/05/12
	 */
	private static List<NBCLocalAccess> __localAccess(NBCLocalAccess... __a)
	{
		if (__a == null || __a.length <= 0)
			return EmptyList.<NBCLocalAccess>empty();
		return UnmodifiableList.<NBCLocalAccess>of(Arrays.<NBCLocalAccess>
			asList(__a));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @since 2016/05/12
	 */
	private static List<NBCVariableType> __stackPop(NBCVariableType... __a)
	{
		if (__a == null || __a.length <= 0)
			return EmptyList.<NBCVariableType>empty();
		return UnmodifiableList.<NBCVariableType>of(Arrays.<NBCVariableType>
			asList(__a));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @since 2016/05/12
	 */
	private static List<NBCVariablePush> __stackPush(NBCVariablePush... __a)
	{
		if (__a == null || __a.length <= 0)
			return EmptyList.<NBCVariablePush>empty();
		return UnmodifiableList.<NBCVariablePush>of(Arrays.<NBCVariablePush>
			asList(__a));
	}
}

