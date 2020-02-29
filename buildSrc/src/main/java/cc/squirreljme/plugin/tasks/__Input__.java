// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import java.nio.file.Path;

/**
 * Source file and its input/output.
 *
 * @since 2020/02/28
 */
final class __Input__
{
	/** The absolute path. */
	public final Path absolute;
	
	/** The relative path. */
	public final Path relative;
	
	/**
	 * Initializes the file information.
	 *
	 * @param __absolute The absolute path.
	 * @param __relative The relative path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	__Input__(Path __absolute, Path __relative)
		throws NullPointerException
	{
		if (__absolute == null || __relative == null)
			throw new NullPointerException();
		
		this.absolute = __absolute;
		this.relative = __relative;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/28
	 */
	@Override
	public final String toString()
	{
		return String.format("{absolute=%s, relative=%s}",
			this.absolute, this.relative);
	}
}
