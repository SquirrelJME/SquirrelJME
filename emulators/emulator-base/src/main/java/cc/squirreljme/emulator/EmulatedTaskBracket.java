// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.TaskBracket;

/**
 * Represents a native task.
 *
 * @since 2020/12/31
 */
public class EmulatedTaskBracket
	implements TaskBracket
{
	/** The process to check and access. */
	protected final Process process;
	
	/**
	 * Initializes the task holder.
	 * 
	 * @param __proc The process this is running on.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/31
	 */
	public EmulatedTaskBracket(Process __proc)
		throws NullPointerException
	{
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		this.process = __proc;
	}
}
