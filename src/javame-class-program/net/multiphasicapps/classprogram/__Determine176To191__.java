// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import net.multiphasicapps.classfile.CFFieldReference;
import net.multiphasicapps.classfile.CFMethodReference;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * Determines the stack operations for opcodes 176 to 191.
 *
 * @since 2016/04/18
 */
class __Determine176To191__
	extends __VMWorkers__.__Determiner__
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/18
	 */
	@Override
	public void determine(__DetermineTypes__ __dt, CPOp __op)
	{
		// Depends on the opcode
		int opcode = __op.instructionCode();
		switch (opcode)
		{
				// Return object
			case 176:
				__return(__dt, __op, CPVariableType.OBJECT);
				break;
				
				// Plain return, does nothing
			case 177:
				break;
			
				// Get from static field
			case 178:
				__getstatic(__dt, __op);
				break;
				
				// Put to static field
			case 179:
				__putstatic(__dt, __op);
				break;
				
				// invokevirtual
			case 182:
				__invoke(__dt, __op, CPInvokeType.VIRTUAL);
				break;
			
				// invokespecial
			case 183:
				__invoke(__dt, __op, CPInvokeType.SPECIAL);
				break;
				
				// invokestatic
			case 184:
				__invoke(__dt, __op, CPInvokeType.STATIC);
				break;
				
				// invokeinterface
			case 185:
				__invoke(__dt, __op, CPInvokeType.INTERFACE);
				break;
				
				// New
			case 187:
				__new(__dt, __op);
				break;
				
				// Throw exception
			case 191:
				__athrow(__dt, __op);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Throws an exception.
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @since 2016/04/16
	 */
	static void __athrow(__DetermineTypes__ __dt, CPOp __op)
	{
		// Just pop an object
		__dt.operate(__op, null, CPVariableType.OBJECT, null);
	}
	
	/**
	 * Gets a static variable.
	 *
	 * @param __dt The determiner.
	 * @param __op The current operation.
	 * @since 2016/04/12
	 */
	static void __getstatic(__DetermineTypes__ __dt, CPOp __op)
	{
		__dt.operate(__op, null, null, CPVariableType.bySymbol(
			((CFFieldReference)__op.arguments().get(0)).
			nameAndType().getValue().asField()));
	}
	
	/**
	 * Invokes a method.
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __it The type of invocation being performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/13
	 */
	static void __invoke(__DetermineTypes__ __dt, CPOp __op, CPInvokeType __it)
		throws NullPointerException
	{
		// Check
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Read the method to invoke
		CFMethodReference ref = (CFMethodReference)__op.arguments().get(0);
		MethodSymbol desc = ref.nameAndType().getValue().asMethod();
		
		// Get the method name
		IdentifierSymbol name = ref.memberName();
		
		// {@squirreljme.error CP1m Cannot invoke a method with the specified
		// name. (The method name)}
		if (!name.isValidMethod())
			throw new CPProgramException(String.format("CP1m %s", name));
		
		// {@squirreljme.error CP1o Cannot invoke a method which is called the
		// static initializer. (The method name)}
		if (name.isStaticInitializer())
			throw new CPProgramException(String.format("CP1o %s", name));
		
		// {@squirreljme.error CP1p Only {@code invokespecial} may invoke a
		// method which is called the constructor. (The method name)}
		if (name.isConstructor() && __it != CPInvokeType.SPECIAL)
			throw new CPProgramException(String.format("CP1p %s", name));
		
		// Get argument count and any instance variables
		int argc = desc.argumentCount();
		int ivc = (__it.isInstance() ? 1 : 0);
		FieldSymbol rv;
		int rvc = (((rv = desc.returnValue()) != null) ? 1 : 0);
		
		// Setup operation array
		Object[] ops = new Object[2 + argc + ivc + rvc];
		int wp = 0;
		
		// There are no locals
		ops[wp++] = null;
		
		// Pop method arguments from last to first
		for (int i = argc - 1; i >= 0; i--)
			ops[wp++] = CPVariableType.bySymbol(desc.get(i));
		
		// If an instance, pop an extra object
		if (__it.isInstance())
			ops[wp++] = CPVariableType.OBJECT;
		
		// Spacer null
		ops[wp++] = null;
		
		// Push the return type
		if (rv != null)
			ops[wp++] = CPVariableType.bySymbol(rv);
		
		// Perform operation
		__dt.operate(__op, ops);
	}
	
	/**
	 * Calculates the new operation.
	 *
	 * @param __dt The owning determiner
	 * @param __op The current operation.
	 * @since 2016/04/11
	 */
	static void __new(__DetermineTypes__ __dt, CPOp __op)
	{
		// Just add an element to the stack
		__dt.operate(__op, null, null, CPVariableType.OBJECT);
	}
	
	/**
	 * Stores a value into a static field.
	 *
	 * @param __dt The determiner.
	 * @param __op The operation.
	 * @since 2016/04/15
	 */
	static void __putstatic(__DetermineTypes__ __dt, CPOp __op)
	{
		__dt.operate(__op, null, CPVariableType.bySymbol(
			((CFFieldReference)__op.arguments().get(0)).
			nameAndType().getValue().asField()), null);
	}
	
	/**
	 * Returns a value.
	 *
	 * @param __dt The determiner.
	 * @param __op The operation.
	 * @param __rt The type of value to return.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/04/16
	 */
	static void __return(__DetermineTypes__ __dt, CPOp __op,
		CPVariableType __rt)
		throws NullPointerException
	{
		// Check
		if (__rt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CP15 Incompatible method return value. (The
		// value to be returned; The type that the method returns)}
		CPVariableType real = CPVariableType.bySymbol(
			__op.program().returnSymbol());
		if (__rt != real)
			throw new CPProgramException(String.format("CP15 %s %s", __rt,
				real));
		
		// Operate on it
		__dt.operate(__op, null, __rt, null);
	}
}

