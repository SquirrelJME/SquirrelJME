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
 * This goes through all of the projects and builds the SDK which is used as
 * a base for programs in SquirrelJME.
 *
 * There is the user SDK which only has specific packages which may be
 * referred to. In general, most programs will always use this.
 *
 * Additionally there is the proprietary SDK which can be used if one wants
 * to access the internal classes that exist.
 *
 * @since 2018/01/27
 */
public class SDKFactory
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
	 * @since 2018/01/27
	 */
	public SDKFactory(BinaryManager __bm, String... __args)
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
		
		// {@squirreljme.error AU0m Expected command for SDK operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0m");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/27
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
				// Build user SDK (publically available classes)
			case "build":
				throw new todo.TODO();
				
				// Build internal SDK (all classes)
			case "internal":
				throw new todo.TODO();
				
				// {@squirreljme.error AU0n The specified sdk command is not
				// valid. Valid commands are:
				// build, internal
				// .(The command)}
			case "help":
			default:
				throw new IllegalArgumentException(String.format("AU0n %s",
					command));
		}
	}
}

