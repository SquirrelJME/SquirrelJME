// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.Project;
import org.gradle.api.internal.tasks.testing.DefaultTestSuiteDescriptor;

/**
 * This represents a test suite which is just the project module being ran.
 *
 * @since 2020/03/06
 */
public class EmulatedTestSuiteDescriptor
	extends DefaultTestSuiteDescriptor
{
	/** The project running for. */
	protected final Project project;
	
	/**
	 * Initializes the test suite descriptor.
	 *
	 * @param __project The project to be a module for.
	 * @since 2020/03/06
	 */
	public EmulatedTestSuiteDescriptor(Project __project)
	{
		super(new EmulatedTestId(), __project.getName());
		
		this.project = __project;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
    @Override
	public Object getOwnerBuildOperationId()
	{
		// This must always be set otherwise Gradle will crash
		return this.getId();
	}
}
