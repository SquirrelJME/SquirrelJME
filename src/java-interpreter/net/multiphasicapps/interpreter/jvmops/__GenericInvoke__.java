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
import net.multiphasicapps.interpreter.JVMClassFormatError;
import net.multiphasicapps.interpreter.JVMCodeParser;
import net.multiphasicapps.interpreter.JVMConstantEntry;
import net.multiphasicapps.interpreter.JVMInvokeType;
import net.multiphasicapps.interpreter.JVMProgramAtom;
import net.multiphasicapps.interpreter.JVMProgramSlot;
import net.multiphasicapps.interpreter.JVMProgramState;
import net.multiphasicapps.interpreter.JVMProgramVars;
import net.multiphasicapps.interpreter.JVMVariableType;

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
			throw new JVMClassFormatError(String.format("IN21 %s %s",
				mref, __type));
		
		// If an interface, the `invokeinterface` instruction adds an extra
		// count (which is not needed) and a zero byte.
		if (__type == JVMInvokeType.INTERFACE)
		{
			// Must be non-zero
			byte nonz = dis.readByte();
			if (nonz == 0)
				throw new JVMClassFormatError(String.format("IN22 %d", nonz));
			
			// Must always be zero
			byte zero = dis.readByte();
			if (zero != 0)
				throw new JVMClassFormatError(String.format("IN23 %d", zero));
		}
		
		throw new Error("TODO");
	}
}

