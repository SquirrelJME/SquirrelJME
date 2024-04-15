// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Source file and its input/output.
 *
 * @since 2020/02/28
 */
public final class FileLocation
	implements Serializable
{
	/** The absolute path. */
	private final SerializedPath absolute;
	
	/** The relative path. */
	private final SerializedPath relative;
	
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
		
		this.absolute = new SerializedPath(__absolute);
		this.relative = new SerializedPath(__relative);
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
		return this.getRelative().equals(o.getRelative()) &&
			this.getAbsolute().equals(o.getAbsolute());
	}
	
	/**
	 * Returns the absolute path.
	 * 
	 * @return The absolute path.
	 * @since 2022/09/11
	 */
	public Path getAbsolute()
	{
		return this.absolute.path;
	}
	
	/**
	 * Returns the relative path.
	 * 
	 * @return The relative path.
	 * @since 2022/09/11
	 */
	public Path getRelative()
	{
		return this.relative.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public final int hashCode()
	{
		return this.getAbsolute().hashCode() ^ this.getRelative().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/28
	 */
	@Override
	public final String toString()
	{
		return String.format("{absolute=%s, relative=%s}", this.getAbsolute(),
			this.getRelative());
	}
}
