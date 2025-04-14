// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.emulator.vm.VMDebuggerService;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
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
		
		try
		{
			// Get the built-in debugger's start method
			Class<?> debuggerMain = Class.forName(
				"cc.squirreljme.debugger.Main");
			Method start = debuggerMain.getMethod("start",
				InputStream.class, OutputStream.class,
				Class.forName("cc.squirreljme.debugger.Preferences"));
			
			// Call it with the streams
			start.invoke(null, a.in(), a.out(), null);
		}
		catch (ReflectiveOperationException __e)
		{
			return null;
		}
		
		// JDWP factory gets the other end
		BidirectionalPipeSide b = pipe.side(true);
		return new JDWPHostFactory(b.in(), b.out(), 0);
	}
}
