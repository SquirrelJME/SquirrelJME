// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;
import net.multiphasicapps.squirreljme.jit.base.JITConfig;

/**
 * This class is used to write the output which is needed to write ELF binary
 * executable.
 *
 * @since 2016/09/28
 */
public class ELFExecutable
	implements ExecutableOutput
{
	/** The configuration used to determine how ELF data is to be written. */
	protected final JITConfig config;
	
	/**
	 * Initializes the ELF output.
	 *
	 * @param __conf The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/28
	 */
	public ELFExecutable(JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/28
	 */
	@Override
	public JITConfig config()
	{
		return this.config;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/28
	 */
	@Override
	public void writeOutput(OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

