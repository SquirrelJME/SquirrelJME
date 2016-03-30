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
import net.multiphasicapps.interpreter.JVMByteOpHandler;
import net.multiphasicapps.interpreter.JVMClassFormatError;
import net.multiphasicapps.interpreter.JVMCodeParser;
import net.multiphasicapps.interpreter.JVMInvokeType;
import net.multiphasicapps.interpreter.JVMProgramState;
import net.multiphasicapps.interpreter.JVMVariableType;

/**
 * Handles operations 176 to 191.
 *
 * @since 2016/03/27
 */
@Deprecated
public class JVMOpHandler176To191
	implements JVMByteOpHandler
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/27
	 */
	@Override
	public void handle(int __op, JVMCodeParser.HandlerBridge __br)
		throws IOException
	{
		// Depends on the operation
		switch (__op)
		{
				// invokevirtual
			case 182:
				__GenericInvoke__.__invoke(JVMInvokeType.VIRTUAL, __br);
				break;
				
				// invokespecial
			case 183:
				__GenericInvoke__.__invoke(JVMInvokeType.SPECIAL, __br);
				break;
				
				// invokestatic
			case 184:
				__GenericInvoke__.__invoke(JVMInvokeType.STATIC, __br);
				break;
				
				// invokeinterface
			case 185:
				__GenericInvoke__.__invoke(JVMInvokeType.INTERFACE, __br);
				break;
			
			default:
				throw new JVMClassFormatError(
					String.format("IN1h %d", __op));
		}
	}
}

