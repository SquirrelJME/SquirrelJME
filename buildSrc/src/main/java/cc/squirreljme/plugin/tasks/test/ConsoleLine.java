// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.tasks.testing.TestOutputEvent;

/**
 * This indicates a line that happened on the consoles.
 *
 * @since 2020/06/22
 */
public final class ConsoleLine
{
	/** Is this to standard error?. */
	public final boolean stdErr;
	
	/** When did this happen? */
	public final long timeMillis;
	
	/** The text that was printed to the console. */
	public final String text;
	
	/**
	 * Initializes the console line.
	 * 
	 * @param __stdErr Is this standard error?
	 * @param __timeMillis When this message appeared.
	 * @param __text The text for the message.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public ConsoleLine(boolean __stdErr, long __timeMillis, String __text)
		throws NullPointerException
	{
		if (__text == null)
			throw new NullPointerException("NARG");
		
		this.stdErr = __stdErr;
		this.timeMillis = __timeMillis;
		this.text = __text;
	}
	
	/**
	 * Reports the line.
	 * 
	 * @param __results The test results output.
	 * @param __method The method to report to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public void report(TestResultProcessor __results,
		EmulatedTestMethodDescriptor __method)
		throws NullPointerException
	{
		// Where is this going?
		TestOutputEvent.Destination destination = (this.stdErr ?
				TestOutputEvent.Destination.StdErr :
				TestOutputEvent.Destination.StdOut);
		
		// Print output
		__results.output(__method.getId(),
			EmulatedTestUtilities.output(destination,
				this.text + System.getProperty("line.separator")));
	}
}
