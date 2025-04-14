// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;

/**
 * CI/CD bundling task.
 *
 * @since 2024/03/04
 */
public class BundleCiCdTask
	extends DefaultTask
{
	@Inject
	public BundleCiCdTask()
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Bundles from CI/CD.");
		
		// Only run this task if we are on CI/CD
		this.onlyIf(__task -> null != BundlePlatform.instance());
		
		// Action to perform
		this.doLast(new BundleCiCdTaskAction());
	}
}
