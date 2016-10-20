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
 * This is a project which provides source code that may be compiled by a
 * compiler into a binary project.
 *
 * @since 2016/10/20
 */
public final class SourceProject
	extends ProjectInfo
{
	/**
	 * Generates a binary manifest from this source project.
	 *
	 * This is used by the compiler. This may also be used to determine what
	 * kind of project a source project compiles into before compiling it.
	 *
	 * @return The binary manifest.
	 * @since 2016/10/20
	 */
	public BinaryProjectManifest generateBinaryManifest()
	{
		throw new Error("TODO");
	}
}

