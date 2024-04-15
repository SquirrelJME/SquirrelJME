// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Checks for entry points.
 *
 * @since 2020/08/07
 */
public class CheckForEntryPoints
	implements Spec<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		// If an entry point is directly specified, then always allow this
		// to be executed
		if (null != System.getProperty(JavaMEMidlet.MIDLET_PROPERTY))
			return true;
		
		// Load the configuration
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
		
		// Only consider actual applications, do not normally allow anything
		// else to run as it is not considered valid.
		if (config.swmType != JavaMEMidletType.APPLICATION)
			return false;
		
		// As long as there is at least one entry point we consider this valid
		// for running
		return config.mainClass != null || !config.midlets.isEmpty();
	}
}
