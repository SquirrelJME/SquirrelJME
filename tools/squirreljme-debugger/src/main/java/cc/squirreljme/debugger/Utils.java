// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utilities.
 *
 * @since 2024/01/22
 */
public final class Utils
{
	/** Standard timeout value. */
	public static final int TIMEOUT =
		3000;
	
	/** A timeout for a very important item. */
	public static final int IMPORTANT_TIMEOUT =
		1000 * 30;
	
	/**
	 * Not used.
	 * 
	 * @since 2024/01/22
	 */
	private Utils()
	{
	}
	
	/**
	 * Gets the stack trace of the given exception.
	 *
	 * @param __e The exception to get the stack trace of.
	 * @return The string containing the stack trace.
	 * @since 2024/01/22
	 */
	public static String throwableTrace(Throwable __e)
	{
		String message;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 PrintStream ps = new PrintStream(baos, true,
				"utf-8"))
		{
			// Print to the output
			__e.printStackTrace(ps);
			
			// Make sure it is flushed
			ps.flush();
			
			message = baos.toString("utf-8");
		}
		catch (IOException __f)
		{
			// Ignore
			message = "Could not emit trace, check stderr.";
		}
		return message;
	}
	
	/**
	 * Pops up a dialog showing the given trace.
	 *
	 * @param __parent The parent dialog.
	 * @param __title The dialog title.
	 * @param __e The exception to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public static void throwableTraceDialog(JFrame __parent,
		String __title, Throwable __e)
		throws NullPointerException
	{
		JOptionPane.showMessageDialog(__parent,
			Utils.throwableTrace(__e),
			__title,
			JOptionPane.ERROR_MESSAGE);
	}
}
