// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;

/**
 * This takes the output of a task and provides a {@link Callable} so that
 * it produces that single file.
 *
 * @since 2020/09/06
 */
public class SingleTaskOutputFile
	implements Callable<Path>
{
	/** The task to get. */
	protected final Task task;
	
	/**
	 * Initializes the utility.
	 * 
	 * @param __task The task to extract from.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public SingleTaskOutputFile(Task __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public Path call()
	{
		FileCollection files = this.task.getOutputs().getFiles();
		
		if (files.getFiles().isEmpty())
			return null;
		
		return files.getSingleFile().toPath();
	}
}
