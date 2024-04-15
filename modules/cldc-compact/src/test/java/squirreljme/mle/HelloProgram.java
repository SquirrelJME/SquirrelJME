// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

/**
 * This is a simple program that just prints hello to the output.
 *
 * @since 2020/07/02
 */
public class HelloProgram
{
	/** The string to print. */
	public static final String STRING =
		"Hello! Squirrels are adorable!";
	
	/**
	 * Main entry point.
	 * 
	 * @param __args Arguments, these are ignored.
	 * @since 2020/07/02
	 */
	public static void main(String... __args)
	{
		// Print something on standard error to at least see if it worked
		System.err.print('x');
		System.err.flush();
		
		// Print it but forgo the newline
		System.out.println(HelloProgram.STRING);
		System.out.flush();
	}
}
