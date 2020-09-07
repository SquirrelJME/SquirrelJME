// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import org.gradle.workers.WorkAction;

/**
 * This performs the actual work of running the VM tests.
 *
 * @since 2020/09/07
 */
public abstract class VMTestWorkAction
	implements WorkAction<MultiVMTestParameters>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/09/07
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	@Override
	public void execute()
	{
		MultiVMTestParameters parameters = this.getParameters();
		
		// The process might not be able to execute
		try
		{
			// Start the process with the command line that was pre-determined
			Process process = new ProcessBuilder(parameters.getCommandLine()
				.get().toArray(new String[0])).start();
			
			// Wait for the process to terminate, the exit code will contain
			// the result of the test (pass, skip, fail)
			int exitCode = -1;
			for (;;)
				try
				{
					exitCode = process.waitFor();
					break;
				}
				catch (InterruptedException ignored)
				{
				}
			
			// Read all of standard error and output, these will be stored
			// in the log
			byte[] stdOut = MultiVMHelpers.readAll(process.getInputStream());
			byte[] stdErr = MultiVMHelpers.readAll(process.getErrorStream());
			
			System.err.printf("DEBUG -- %s: %d%n",
				parameters.getTestName().get(), exitCode);
			System.out.write(stdOut);
			System.err.write(stdErr);
		}
		
		// Process failed to execute
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
