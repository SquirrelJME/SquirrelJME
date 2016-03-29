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

import java.io.IOException;
import net.multiphasicapps.interpreter.JVMClassFormatError;
import net.multiphasicapps.interpreter.JVMCodeParser;
import net.multiphasicapps.interpreter.JVMProgramAtom;
import net.multiphasicapps.interpreter.JVMProgramSlot;
import net.multiphasicapps.interpreter.JVMProgramState;
import net.multiphasicapps.interpreter.JVMProgramVars;
import net.multiphasicapps.interpreter.JVMVariableType;

/**
 * Many load operations from local variables are essentially the same, this
 * reduces the required duplicated code to handle all of these cases.
 *
 * @since 2016/03/26
 */
final class __GenericLocalLoad__
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/03/26
	 */
	private __GenericLocalLoad__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Loads a local variable from the local variable table and places it
	 * onto the stack.
	 *
	 * @param __local The local variable to read.
	 * @param __br The handler bridge.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/26
	 */
	static void __load(JVMVariableType __type, int __local,
		JVMCodeParser.HandlerBridge __br)
		throws NullPointerException
	{
		// Check
		if (__br == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Get the current and next atom
		JVMProgramAtom cur = __br.currentAtom();
		JVMProgramAtom div = cur.derive();
		
		// Get the current local type and check the type
		try
		{
			JVMProgramSlot cls = cur.locals().get(__local);
			JVMVariableType clt = cls.getType();
			if (!__type.equals(clt))
				throw new JVMClassFormatError(String.format("IN1y %d %s %s %s",
					__local, __type, clt, cur));
		
			// If wide, make sure the next local is TOP
			boolean iswide = __type.isWide();
			if (iswide && !JVMVariableType.TOP.equals(
				cls.nextSlot(true).getType()))
				throw new JVMClassFormatError(String.format("IN1x %d %s %s",
					__local, __type, cur));
			
			// Get the target stack
			JVMProgramVars stack = div.stack();
			
			// Push to it
			JVMProgramSlot pu = stack.push();
			
			// Copy the type and link from the local, this propogates the
			// value in the link without performing any operation with it.
			pu.setType(clt);
			pu.setLink(cls.getLink());
			
			// If wide, set the next stack element to TOP
			if (iswide)
				stack.push().setType(JVMVariableType.TOP);
			
			// Merge the derived state into the program
			__br.merge(div);
		}
		
		// Out of bounds
		catch (IndexOutOfBoundsException e)
		{
			throw new JVMClassFormatError("IN20", e);
		}
	}
}

