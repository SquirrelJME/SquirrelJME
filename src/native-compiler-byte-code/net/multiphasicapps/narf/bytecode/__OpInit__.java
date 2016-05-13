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

import java.util.List;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIClassReference;
import net.multiphasicapps.narf.classinterface.NCIPool;

/**
 * This initializes operation data.
 *
 * @since 2016/05/13
 */
class __OpInit__
{
	/**
	 * Not used.
	 *
	 * @since 2016/05/13
	 */
	private __OpInit__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Duplicates the top most stack entry.
	 *
	 * @param __id Operation data.
	 * @since 2016/05/13
	 */
	public static void dup(__OpInitData__ __id)
	{
		// Get input and the stack
		NBCStateVerification vin = __id.verificationInput();
		NBCStateVerification.Stack stack = vin.stack();
		int top = stack.top();
		
		// {@squirreljme.error AX0t Stack underflow duplicating top-most
		// stack entry.}
		if (top <= 0)
			throw new NBCException(NBCException.Issue.STACK_UNDERFLOW,
				"AX0t");
		
		// {@squirreljme.error AX0u Cannot duplicate wide types. (The top most
		// entry)}
		NBCVariableType vt = stack.get(top - 1);
		if (vt.isWide() || vt == NBCVariableType.TOP)
			throw new NBCException(NBCException.Issue.INCORRECT_STACK,
				String.format("AX0u %s", vt));
		
		// Add it to the pop
		List<NBCVariableType> pops = __id.setStackPop(vt);
		
		// Push it also
		__id.setStackPush(new NBCVariablePush(pops, 0));
		
		// Rewrite the operation
		__id.rewrite(NBCInstructionID.SYNTHETIC_STACK_SHUFFLE);
	}
	
	/**
	 * Allocates an object.
	 *
	 * @param __id Operation data.
	 * @since 2016/05/13
	 */
	public static void new_(__OpInitData__ __id)
	{
		// Get the byte buffer
		NCIByteBuffer bb = __id.byteBuffer();
		int phy = __id.physicalAddress();
		
		// Get the type of class to allocate
		NCIClassReference ref = __id.pool().<NCIClassReference>requiredAs(
			bb.readUnsignedShort(phy, 1), NCIClassReference.class);
		__id.setArguments(ref);
		
		// Lookup the class
		NCIClass ncl = __id.lookup(ref.get());
		
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
		if (!__id.canAccess(ncl))
			throw new NBCException(NBCException.Issue.CANNOT_ACCESS_CLASS,
				String.format("AX0f %s", ncl.thisName()));
		
		// A new object is pushed
		__id.setStackPush(NBCVariablePush.newObject());
	}
}

