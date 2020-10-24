// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;

/**
 * Task for running the full-suite of SquirrelJME.
 *
 * @since 2020/10/17
 */
public class VMFullSuite
	extends DefaultTask
{
	/** The virtual machine type. */
	public final VMSpecifier vmType;
	
	/**
	 * Initializes the full suite task.
	 * 
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	@Inject
	public VMFullSuite(VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__vmType == null)
			throw new NullPointerException("NARG");
		
		this.vmType = __vmType;
		
		// Runs the entire API/Library suite of SquirrelJME to run a given
		// application
		this.setGroup("squirreljme");
		this.setDescription("Runs the full suite of SquirrelJME Modules.");
		
		// This always runs, no matter what
		this.onlyIf(new AlwaysTrue());
		this.getOutputs().upToDateWhen(new AlwaysTrue());
		
		// This depends on everything!
		this.dependsOn(new VMFullSuiteDepends(this, __vmType));
		
		// Actual running of everything
		this.doLast(new VMFullSuiteTaskAction(__vmType));
	}
}
