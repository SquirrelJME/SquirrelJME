// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * The class provides factory access to the WinterCoat functionalities such
 * as a simulaed virtual environment or exported ROM for user environments.
 *
 * It is also used as a base for developing the JIT before targetting real
 * systems, it is intended to make things much easier in the long run.
 *
 * @since 2018/02/21
 */
public class WinterCoatFactory
	implements Runnable
{
	/** The binary manager since they need to be accessed for building. */
	protected final BinaryManager binarymanager;
	
	/** The command to execute. */
	protected final String command;
	
	/** Arguments to the task command. */
	private final String[] _args;
	
	/**
	 * Initializes the factory.
	 *
	 * @param __bl The binary manager.
	 * @param __args factory arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public WinterCoatFactory(BinaryManager __bm, String... __args)
		throws NullPointerException
	{
		if (__bm == null)
			throw new NullPointerException("NARG");
		
		this.binarymanager = __bm;
		
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// {@squirreljme.error AU10 Expected command for WinterCoat operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU10");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public void run()
	{
		// Load arguments into a queue
		Deque<String> args =
			new ArrayDeque<>(Arrays.<String>asList(this._args));
		
		// Depends on the command
		String command = this.command;
		switch (command)
		{
				// Build winter coat ROM
			case "build":
				throw new todo.TODO();
				
				// {@squirreljme.error AU11 The specified wintercoat command is
				// not valid. Valid commands are:
				// build
				// .(The command)}
			case "help":
			default:
				throw new IllegalArgumentException(String.format("AU11 %s",
					command));
		}
	}
}

