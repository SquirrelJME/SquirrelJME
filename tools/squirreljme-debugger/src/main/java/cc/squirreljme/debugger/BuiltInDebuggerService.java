// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.emulator.vm.VMDebuggerService;
import cc.squirreljme.jdwp.JDWPHostFactory;
import net.multiphasicapps.io.BidirectionalPipe;
import net.multiphasicapps.io.BidirectionalPipeSide;

/**
 * Service for this debugger.
 *
 * @since 2024/01/19
 */
public class BuiltInDebuggerService
	implements VMDebuggerService
{
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public JDWPHostFactory jdwpFactory()
	{
		// Setup a bidirectional local pipe
		BidirectionalPipe pipe = new BidirectionalPipe();
		
		// Start debugger with one end of the pipe
		BidirectionalPipeSide a = pipe.side(false);
		Main.start(a.in(), a.out());
		
		// JDWP factory gets the other end
		BidirectionalPipeSide b = pipe.side(true);
		return new JDWPHostFactory(b.in(), b.out());
	}
}
