// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.IOException;

/**
 * This class contains the base for a byte operation handler, when an
 * operation needs to be handled, it is searched for in a lookup table.
 * If it is not there, then it is created and cached.
 *
 * @since 2016/03/23
 */
public interface JVMByteOpHandler
{
	/**
	 * Handles the given operation.
	 *
	 * @param __op The opcode which is requested to be handled.
	 * @param __br The bridge which interfaces with this code parser.
	 * @throws IOException On read errors.
	 * @since 2016/03/23
	 */
	public abstract void handle(int __op, JVMCodeParser.HandlerBridge __br)
		throws IOException;
}

