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
 * This always evaluates to true.
 *
 * @since 2020/10/07
 */
public class AlwaysTrue
	implements Spec<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/07
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		return true;
	}
}
