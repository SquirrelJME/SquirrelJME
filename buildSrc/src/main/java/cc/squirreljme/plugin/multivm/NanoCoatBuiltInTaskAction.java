// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Copies over the Jar and generates a source file for NanoCoat.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTaskAction
	implements Action<Task>
{
	/** The input Jar. */
	protected final NanoCoatBuiltInTaskInput input;
	
	/** The output Jar. */
	protected final NanoCoatBuiltInTaskOutput outJar;
	
	/** The output source. */
	protected final NanoCoatBuiltInTaskOutput outSrc;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __input The input Jar.
	 * @param __outJar The output Jar.
	 * @param __outSrc The output source.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/12
	 */
	public NanoCoatBuiltInTaskAction(NanoCoatBuiltInTaskInput __input,
		NanoCoatBuiltInTaskOutput __outJar, NanoCoatBuiltInTaskOutput __outSrc)
		throws NullPointerException
	{
		if (__input == null || __outJar == null || __outSrc == null)
			throw new NullPointerException("NARG");
		
		this.input = __input;
		this.outJar = __outJar;
		this.outSrc = __outSrc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public void execute(Task __task)
	{
		NanoCoatBuiltInTask task = (NanoCoatBuiltInTask)__task;
		
		// This could fail to write
		Path sourceTemp = null;
		try
		{
			// Copy the Jar over
			Files.copy(this.input.call(), this.outJar.call(),
				StandardCopyOption.REPLACE_EXISTING);
			
			// We will be writing to a file then moving it over, so we only
			// need a single temporary file for everything
			sourceTemp = Files.createTempFile("source", ".x");
		}
		
		// It did fail to write
		catch (Exception e)
		{
			throw new RuntimeException("Could not copy ROM.", e);
		}
		
		// Cleanup after this
		finally
		{
			if (sourceTemp != null)
				try
				{
					Files.delete(sourceTemp);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
