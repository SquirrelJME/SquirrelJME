// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This represents a binary project which may be launched.
 *
 * @since 2016/10/20
 */
public final class BinaryProject
	extends ProjectInfo
{
	/**
	 * Returns the binary manifest of this project.
	 *
	 * @return The binary manifest.
	 * @since 2016/10/20
	 */
	public BinaryProjectManifest binaryManifest()
	{
		throw new Error("TODO");
	}
}

