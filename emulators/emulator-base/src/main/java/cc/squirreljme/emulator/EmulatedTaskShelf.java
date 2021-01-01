// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Emulation for {@link TaskShelf}.
 *
 * @since 2020/12/31
 */
@SuppressWarnings("unused")
public final class EmulatedTaskShelf
{
	/**
	 * As {@link TaskShelf#start(JarPackageBracket[], String, String[],
	 * String[], int, int)}. 
	 * 
	 * @param __classPath The class path that the launched application should
	 * use.
	 * @param __mainClass The main class to execute, this must be a class which
	 * contains {@code static void main(String[] __args)}.
	 * @param __args The arguments to the main class.
	 * @param __sysPropPairs System property pairs, even values are keys and
	 * odd values are values. This array must always be a multiple of two.
	 * @param __stdOut The {@link TaskPipeRedirectType} for standard output.
	 * @param __stdErr The {@link TaskPipeRedirectType} for standard error.
	 * @return The bracket that represents the task.
	 * @throws MLECallError If any argument is {@code null}, or an array
	 * contains a {@code null} value, the {@code __sysPropPairs} is not a
	 * multiple of two, {@code __classPath[0]} is not the same Jar package
	 * as our own classpath's first JAR, if the task could not be created,
	 * or if either the {@code __stdOut} or {@code __stdErr} redirect types
	 * are not valid.
	 * @since 2020/07/02
	 */
	public static TaskBracket start(
		JarPackageBracket[] __classPath, String __mainClass, String[] __args,
		String[] __sysPropPairs, int __stdOut, int __stdErr)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
