// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Checks that there are tests.
 *
 * @since 2020/08/07
 */
public class CheckForTests
	implements Spec<Task>
{
	/** The source set to check. */
	protected final String sourceSet;
	
	/**
	 * Initializes the checker.
	 * 
	 * @param __sourceSet The source set to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public CheckForTests(String __sourceSet)
		throws NullPointerException
	{
		if (__sourceSet == null)
			throw new NullPointerException("NARG");
		
		this.sourceSet = __sourceSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		return !VMHelpers.runningTests(__task.getProject(),
			this.sourceSet).tests.isEmpty();
	}
}
