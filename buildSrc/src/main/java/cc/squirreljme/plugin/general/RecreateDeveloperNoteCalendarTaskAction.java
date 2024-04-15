// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import cc.squirreljme.plugin.util.NoteCalendarGenerator;
import java.io.IOException;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * The action for {@link RecreateDeveloperNoteCalendarTask}.
 *
 * @since 2022/07/10
 */
public class RecreateDeveloperNoteCalendarTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/10
	 */
	@Override
	public void execute(Task __task)
	{
		try
		{
			NoteCalendarGenerator.generateAndStore(FossilExe.instance());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not generate calendar.", e);
		}
	}
}
