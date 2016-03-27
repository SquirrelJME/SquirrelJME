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
import net.multiphasicapps.interpreter.JVMProgramState;
import net.multiphasicapps.interpreter.JVMVariableType;

/**
 * Handles opcodes 16 to 31.
 *
 * @since 2016/03/26
 */
public class JVMOpHandler16To31
	implements JVMByteOpHandler
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public void handle(int __op, JVMCodeParser.HandlerBridge __br)
		throws IOException
	{
		// Depends on the operation
		switch (__op)
		{
				// iload
			case 21:
				__GenericLocalLoad__.__load(JVMVariableType.INTEGER,
					__br.source().readUnsignedByte(), __br);
				break;
				
				// lload
			case 22:
				__GenericLocalLoad__.__load(JVMVariableType.LONG,
					__br.source().readUnsignedByte(), __br);
				break;
				
				// fload
			case 23:
				__GenericLocalLoad__.__load(JVMVariableType.FLOAT,
					__br.source().readUnsignedByte(), __br);
				break;
				
				// dload
			case 24:
				__GenericLocalLoad__.__load(JVMVariableType.DOUBLE,
					__br.source().readUnsignedByte(), __br);
				break;
				
				// aload
			case 25:
				__GenericLocalLoad__.__load(JVMVariableType.OBJECT,
					__br.source().readUnsignedByte(), __br);
				break;
				
				// iload_0 to iload_4
			case 26:
			case 27:
			case 28:
			case 29:
				__GenericLocalLoad__.__load(JVMVariableType.INTEGER,
					(__op - 26), __br);
				break;
			
				// lload_0 to lload_1
			case 30:
			case 31:
				__GenericLocalLoad__.__load(JVMVariableType.LONG,
					(__op - 30), __br);
				break;
			
				// Unknown
			default:
				throw new JVMClassFormatError(
					String.format("IN1h %d", __op));
		}
	}
}

