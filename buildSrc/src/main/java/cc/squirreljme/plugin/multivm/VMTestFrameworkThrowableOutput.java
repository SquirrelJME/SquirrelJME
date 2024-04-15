// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Used to print a message and otherwise.
 *
 * @since 2022/09/14
 */
public class VMTestFrameworkThrowableOutput
	extends Throwable
{
	/**
	 * Initializes the throwable.
	 * 
	 * @param __s The string used.
	 * @since 2022/09/14
	 */
	public VMTestFrameworkThrowableOutput(String __s)
	{
		super(__s);
		
		// Wipe the stack trace
		super.setStackTrace(new StackTraceElement[0]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/14
	 */
	@Override
	public void printStackTrace()
	{
		this.printStackTrace(System.err);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/14
	 */
	@Override
	public void printStackTrace(PrintStream __ps)
	{
		this.printStackTrace(new PrintWriter(__ps));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/14
	 */
	@Override
	public void printStackTrace(PrintWriter __pw)
	{
		__pw.println(super.getMessage());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/14
	 */
	@Override
	public String toString()
	{
		return super.getMessage();
	}
}
