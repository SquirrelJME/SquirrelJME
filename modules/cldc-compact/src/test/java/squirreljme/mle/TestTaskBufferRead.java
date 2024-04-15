// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.runtime.cldc.io.TaskInputStream;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests that buffer reads from tasks work properly.
 *
 * @since 2020/07/02
 */
public class TestTaskBufferRead
	extends TestSupplier<String>
{
	/** When to give up, in milliseconds. */
	public static final long GIVE_UP_DELAY =
		20_000L;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/02
	 */
	@Override
	public String test()
		throws Throwable
	{
		// Start the task
		TaskBracket task = TaskShelf.start(
			JarPackageShelf.classPath(),
			"squirreljme.mle.HelloProgram",
			new String[0],
			new String[0],
			TaskPipeRedirectType.BUFFER,
			TaskPipeRedirectType.BUFFER);
		
		// Read in data from the program
		StringBuilder result = new StringBuilder();
		try (TaskInputStream in = new TaskInputStream(task,
			StandardPipeType.STDOUT))
		{
			for (long giveUp = System.currentTimeMillis() +
				TestTaskBufferRead.GIVE_UP_DELAY;;)
			{
				// Giving up?
				if (System.currentTimeMillis() > giveUp)
					throw new RuntimeException("GIVE");
				
				int ch = in.read();
				
				// EOF?
				if (ch < 0)
					break;
				
				// Ignore CR and LF
				if (ch == '\r' || ch == '\n')
					continue;
				
				// Place in the string
				result.append((char)ch); 
			}
		}
		
		// Test if a single byte read from standard error's buffer
		byte[] errBuf = new byte[1];
		this.secondary("stderr", TaskShelf.read(task,
			StandardPipeType.STDERR, errBuf, 0, 1));
		
		// Return the result of standard output
		return result.toString();
	}
}
