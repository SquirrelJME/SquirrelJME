// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import java.nio.file.Path;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Can be used by tasks to determine if CMake is available.
 *
 * @since 2024/03/15
 */
public class CMakeOnlyIf
	implements Spec<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		return CMakeUtils.cmakeExePath() != null;
	}
}
