// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.interpreter;

import java.io.IOException;
import net.multiphasicapps.squirreljme.emulator.EmulatorException;
import net.multiphasicapps.squirreljme.emulator.FileAccessMode;
import net.multiphasicapps.squirreljme.emulator.OpenFile;
import net.multiphasicapps.squirreljme.emulator.Process;

/**
 * This is a process which is used by the interpreter emulator.
 *
 * @since 2016/07/30
 */
public class InterpreterProcess
	implements Process
{
	/** The process executable. */
	protected final OpenFile executable;
	
	/** Has this been closed. */
	private volatile boolean _closed;
	
	/**
	 * Initializes the interpreter process.
	 *
	 * @param __emu The owning emulator.
	 * @param __env The environment to use.
	 * @param __args The arguments to the process.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public InterpreterProcess(InterpreterEmulator __emu, String[] __env,
		String[] __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__emu == null || __env == null || __args == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK01 No arguments to the process specified.}
		if (__args.length <= 0)
			throw new EmulatorException("BK01");
		
		// Open the executable
		OpenFile executable = __emu.openFile(__args[0], FileAccessMode.READ);
		this.executable = executable;
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void close()
		throws IOException
	{
		if (!this._closed)
		{
			// Mark closed
			this._closed = true;
			
			// Close executable
			this.executable.close();
		}
	}
}

