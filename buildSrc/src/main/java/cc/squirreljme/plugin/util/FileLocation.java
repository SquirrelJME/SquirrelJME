// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Source file and its input/output.
 *
 * @since 2020/02/28
 */
public final class FileLocation
	implements Serializable
{
	/** Serialization ID. */
	private static final long serialVersionUID =
		12893674381423984L;
	
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
	
	/**
	 * Reads the serialization object.
	 * 
	 * @param __in The input object.
	 * @throws NoSuchFieldException If the field does not exist.
	 * @throws IOException On read errors.
	 * @throws ClassNotFoundException If our class is not valid?
	 * @throws IllegalAccessException If we cannot write a final field.
	 * @since 2022/09/11
	 */
	private void readObject(ObjectInputStream __in)
		throws NoSuchFieldException, IOException, ClassNotFoundException,
			IllegalAccessException
	{
		Field absolute = this.getClass().getDeclaredField("absolute");
		absolute.setAccessible(true);
		absolute.set(this, Paths.get(__in.readObject().toString()));
		
		Field relative = this.getClass().getDeclaredField("relative");
		relative.setAccessible(true);
		relative.set(this, Paths.get(__in.readObject().toString()));
	}
	
	/**
	 * Serializes the paths.
	 * 
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @since 2022/09/11
	 */
	private void writeObject(ObjectOutputStream __out)
		throws IOException
	{
		__out.writeObject(this.absolute.toString());
		__out.writeObject(this.relative.toString());
	}
}
