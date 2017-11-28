// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This is input which uses a given file as input.
 *
 * @since 2017/11/28
 */
public final class FileInput
	implements CompilerInput
{
	/** The path to the file. */
	protected final Path path;
	
	/** The name of the file. */
	protected final String name;
	
	/**
	 * Initializes the file input.
	 *
	 * @param __p The path to the file.
	 * @param __n The name of the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public FileInput(Path __p, String __n)
		throws NullPointerException
	{
		if (__p == null || __n == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof FileInput))
			return false;
		
		FileInput o = (FileInput)__o;
		return this.path.equals(o.path) &&
			this.name.equals(o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public final int hashCode()
	{
		return this.path.hashCode() ^
			this.name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public final String name()
		throws CompilerException
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public final InputStream open()
		throws CompilerException, NoSuchInputException
	{
		Path path = this.path;
		try
		{
			return Files.newInputStream(path, StandardOpenOption.READ);
		}
		
		// {@squirreljme.error AQ06 The specified path does not exist.
		// (The path to the file)}
		catch (NoSuchFileException e)
		{
			throw new NoSuchInputException(String.format("AQ06 %s", path), e);
		}
		
		// {@squirreljme.error AQ08 Could not read from the specified path.
		// (The path to the file)}
		catch (IOException e)
		{
			throw new NoSuchInputException(String.format("AQ08 %s", path), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public String toString()
	{
		return this.path.toString();
	}
}

