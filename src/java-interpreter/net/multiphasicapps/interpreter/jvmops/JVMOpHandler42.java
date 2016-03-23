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
import net.multiphasicapps.interpreter.JVMCodeParser;

/**
 * Handles opcode 42: aload_0.
 *
 * @since 2016/03/23
 */
public class JVMOpHandler42
	implements JVMCodeParser.ByteOpHandler
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public void handle(JVMCodeParser.HandlerBridge __br)
		throws IOException
	{
		throw new Error("TODO");
	}
}

