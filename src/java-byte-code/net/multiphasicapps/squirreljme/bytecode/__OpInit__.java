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

import java.util.List;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.squirreljme.ci.CIByteBuffer;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.ci.CIClassReference;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodReference;
import net.multiphasicapps.squirreljme.ci.CIPool;

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
		BCStateVerification vin = __id.verificationInput();
		BCStateVerification.Stack stack = vin.stack();
		int top = stack.top();
		
		// {@squirreljme.error AX0t Stack underflow duplicating top-most
		// stack entry.}
		if (top <= 0)
			throw new BCException("AX0t");
		
		// {@squirreljme.error AX0u Cannot duplicate wide types. (The top most
		// entry)}
		BCVariableType vt = stack.get(top - 1);
		if (vt.isWide() || vt == BCVariableType.TOP)
			throw new BCException(String.format("AX0u %s", vt));
		
		// Add it to the pop
		List<BCVariableType> pops = __id.setStackPop(vt);
		
		// Push it also
		BCVariablePush vu = new BCVariablePush(pops, 0);
		__id.setStackPush(vu, vu);
		
		// Rewrite the operation
		__id.rewrite(BCInstructionID.SYNTHETIC_STACK_SHUFFLE);
	}
	
	/**
	 * Invokes a method.
	 *
	 * @param __id Operation info.
	 * @param __it The type of invocation to perform.
	 * @since 2016/05/12
	 */
	public static void invoke(__OpInitData__ __id, BCInvokeType __it)
		throws NullPointerException
	{
		// Check
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Get the byte buffer
		CIByteBuffer bb = __id.byteBuffer();
		int phy = __id.physicalAddress();
		
		// Get the method which was referenced
		CIMethodReference ref = __id.pool().<CIMethodReference>requiredAs(
			bb.readUnsignedShort(phy, 1), CIMethodReference.class);
		__id.setArguments(ref);
		
		// {@squirreljme.error AX0x Invocation of an interface method does not
		// refer to the interface method reference. (The method reference)}
		if (__it == BCInvokeType.INTERFACE && !ref.isInterface())
			throw new BCException(String.format("AX0x %s", ref));
		
		// {@squirreljme.error AX10 The specified method could not be located.
		// (The method reference)}
		throw new Error("TODO");
		/*
		CILookup look = __id.lookup();
		CIMethod m = look.lookupMethod(ref);
		if (m == null)
			throw new BCException(String.format("AX10 %s", ref));
		
		// Get the containing class
		CIClass ncl = m.outerClass();
		
		// {@squirreljme.error AX0z Cannot access the class which was
		// referenced for a method invocation. (The method reference)}
		if (!__id.canAccess(ncl))
			throw new BCException(String.format("AX0z %s", ref));
		
		throw new Error("TODO");*/
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
		CIByteBuffer bb = __id.byteBuffer();
		int phy = __id.physicalAddress();
		
		// Get the type of class to allocate
		CIClassReference ref = __id.pool().<CIClassReference>requiredAs(
			bb.readUnsignedShort(phy, 1), CIClassReference.class);
		__id.setArguments(ref);
		
		// Lookup the class
		CIClass ncl = __id.lookup(ref.get());
		
		// {@squirreljme.error AX0a Byte code refers to a class to be
		// allocated, however it does not exist. (The class name)}
		if (ncl == null)
			throw new BCException(String.format("AX0a %s", ref));
		
		// {@squirreljme.error AX0b The byte code would have attempted to
		// allocate a class which was either abstract or an interface.
		// (The class name; The class flags)}
		CIClassFlags fl = ncl.flags();
		if (fl.isAbstract() || fl.isInterface())
			throw new BCException(String.format("AX0b %s %s", ref, fl));
		
		// {@squirreljme.error AX0f The specified class cannot be accessed
		// and cannot be allocated. (The class to access)}
		if (!__id.canAccess(ncl))
			throw new BCException(String.format("AX0f %s", ncl.thisName()));
		
		// A new object is pushed
		__id.setStackPush(BCVariablePush.newObject());
	}
}

