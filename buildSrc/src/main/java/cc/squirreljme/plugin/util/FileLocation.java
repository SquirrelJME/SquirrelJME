// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.nio.file.Path;

/**
 * Source file and its input/output.
 *
 * @since 2020/02/28
 */
public final class FileLocation
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
	public FileLocation(Path __absolute, Path __relative)
		throws NullPointerException
	{
		if (__absolute == null || __relative == null)
			throw new NullPointerException();
		
		this.absolute = __absolute;
		this.relative = __relative;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof FileLocation))
			return false;
		
		FileLocation o = (FileLocation)__o;
		return this.relative.equals(o.relative) &&
			this.absolute.equals(o.absolute);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public final int hashCode()
	{
		return this.absolute.hashCode() ^ this.relative.hashCode();
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
