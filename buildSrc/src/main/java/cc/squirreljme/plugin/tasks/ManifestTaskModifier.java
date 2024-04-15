// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import java.nio.file.Path;
import org.gradle.api.Action;
import org.gradle.api.java.archives.Manifest;

/**
 * The action for pulling in the actual manifest for
 * {@link AdditionalManifestPropertiesTask}.
 *
 * @since 2022/07/10
 */
public class ManifestTaskModifier
	implements Action<Manifest>
{
	/** The task output. */
	final Path _taskOutput;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __taskOutput The task output.
	 * @since 2022/07/10
	 */
	public ManifestTaskModifier(Path __taskOutput)
	{
		this._taskOutput = __taskOutput;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/07/10
	 */
	@Override
	public void execute(Manifest __manifest)
	{
		__manifest.from(this._taskOutput.toFile());
	}
}
