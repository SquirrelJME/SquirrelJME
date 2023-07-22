// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;

/**
 * This represents the inputs for the test, which files will get the actual
 * testing performed on them.
 *
 * @since 2020/09/06
 */
public class VMTestInputs
	implements Callable<Iterable<Path>>
{
	/** The task executing under. */
	protected final VMExecutableTask task;
	
	/** The source set working under. */
	protected final String sourceSet;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task testing under.
	 * @param __sourceSet The source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public VMTestInputs(VMExecutableTask __task, String __sourceSet)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public Iterable<Path> call()
	{
		Collection<Path> result = new LinkedHashSet<>();
		
		// The source and result of the test make up the input
		for (CandidateTestFiles file : VMHelpers.runningTests(
			this.task.getProject(), this.sourceSet).tests.values())
		{
			result.add(file.sourceCode.getAbsolute());
			
			// The expected results are optional
			if (file.expectedResult != null)
				result.add(file.expectedResult.getAbsolute());
		}
		
		return result;
	}
}
