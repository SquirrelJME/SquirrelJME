// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.jvmops;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.interpreter.JVMClassFormatError;
import net.multiphasicapps.interpreter.JVMCodeParser;
import net.multiphasicapps.interpreter.JVMConstantEntry;
import net.multiphasicapps.interpreter.JVMInvokeType;
import net.multiphasicapps.interpreter.JVMOperatorLink;
import net.multiphasicapps.interpreter.JVMProgramAtom;
import net.multiphasicapps.interpreter.JVMProgramSlot;
import net.multiphasicapps.interpreter.JVMProgramState;
import net.multiphasicapps.interpreter.JVMProgramVars;
import net.multiphasicapps.interpreter.JVMVariableType;
import net.multiphasicapps.interpreter.JVMVerifyException;

/**
 * This class is used for handling generic invocations of code.
 *
 * @since 2016/03/29
 */
final class __GenericInvoke__
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/03/29
	 */
	private __GenericInvoke__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Generates code for invoking the virtual machine.
	 *
	 * @param __type The type of method invocation to perform.
	 * @param __br The bridged used for code generation.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	static void __invoke(JVMInvokeType __type,
		JVMCodeParser.HandlerBridge __br)
		throws IOException, NullPointerException
	{
		// Check
		if (__type == null || __br == null)
			throw new NullPointerException("NARG");
		
		// Get the constant pool which references the pool entry containing
		// the reference to invoke
		DataInputStream dis = __br.source();
		JVMConstantEntry.MethodReference mref = __br.constantPool().
			<JVMConstantEntry.MethodReference>getAs(dis.readUnsignedShort(),
				JVMConstantEntry.MethodReference.class);
		
		// Only interfaces uses the interface method reference
		boolean isimef = (mref instanceof
			JVMConstantEntry.InterfaceMethodReference);
		if (isimef != (__type == JVMInvokeType.INTERFACE))
			throw new JVMVerifyException(String.format("IN21 %s %s",
				mref, __type));
		
		// If an interface, the `invokeinterface` instruction adds an extra
		// count (which is not needed) and a zero byte.
		if (__type == JVMInvokeType.INTERFACE)
		{
			// Must be non-zero
			byte nonz = dis.readByte();
			if (nonz == 0)
				throw new JVMVerifyException(String.format("IN22 %d", nonz));
			
			// Must always be zero
			byte zero = dis.readByte();
			if (zero != 0)
				throw new JVMVerifyException(String.format("IN23 %d", zero));
		}
		
		// Get the method descriptor which determines the arguments to remove
		// from the stack and such
		MethodSymbol desc = mref.memberType();
		int nargs = desc.argumentCount();
		
		// Determine the actual number of pass arguments
		int actpass = nargs + (__type.isInstance() ? 1 : 0);
		
		// These are the input arguments for the method which are all uniques
		// which point to the results of other operations.
		long[] uniqs = new long[actpass];
		Arrays.fill(uniqs, -1L);
		int uni = actpass;
		
		// Get the current and derived atom
		JVMProgramAtom cur = __br.currentAtom();
		JVMProgramAtom div = cur.derive();
		
		// Arguments are popped from the last argument to the first
		JVMProgramVars stack = div.stack();
		for (int i = nargs - 1; i >= 0; i--)
		{
			// Get argument field type and determine the desired variable
			// type used
			FieldSymbol farg = desc.get(i);
			JVMVariableType fvt = JVMVariableType.bySymbol(farg);
			
			// Pop from the stack
			JVMProgramSlot sl = stack.pop();
			
			// Get the type
			JVMVariableType slvt = sl.getType(true);
			
			// If the field is wide, this must be TOP
			if (fvt.isWide())
			{
				// Check top
				if (!slvt.equals(JVMVariableType.TOP))
					throw new JVMVerifyException(String.format("IN26 %s",
						slvt));
				
				// Read new type
				sl = stack.pop();
				slvt = sl.getType();
			}
			
			// Must be the same
			if (!fvt.equals(slvt))
				throw new JVMVerifyException(String.format("IN27 %s %s",
					fvt, slvt));
			
			// Set unique input
			uniqs[--uni] = sl.unique();
		}
		
		// If an instance method, then the last entry to be popped must be an
		// object which is not null and acts as the first argument (this) in
		// a call to a method.
		boolean instance = __type.isInstance();
		if (instance)
		{
			// Get slot and type
			JVMProgramSlot sl = stack.pop();
			JVMVariableType slvt = sl.getType(true);
			
			// Must be object
			if (!JVMVariableType.OBJECT.equals(slvt))
				throw new JVMVerifyException(String.format("IN28 %s",
					slvt));
			
			// Set unique input
			uniqs[--uni] = sl.unique();
		}
		
		// Debug
		for (int i = 0; i < actpass; i++)
			System.err.printf("DEBUG -- Unique #%d: %s%n", i,
				JVMOperatorLink.uniqueToString(uniqs[i]));
		
		throw new Error("TODO");
	}
}

