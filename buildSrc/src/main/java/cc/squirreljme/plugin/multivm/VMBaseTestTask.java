// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;

/**
 * Base task for testing related tasks.
 *
 * @since 2025/04/23
 */
public interface VMBaseTestTask
	extends VMBaseTask
{
	/**
	 * Returns the directory where state exists.
	 *
	 * @return The directory where state exists.
	 * @since 2025/04/23
	 */
	Path statePath();
}
