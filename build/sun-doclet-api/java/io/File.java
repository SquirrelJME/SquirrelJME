// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This implements a very minimal set of the Java SE {@link File} class so that
 * it may be used by the Doclet API to refer to file names.
 *
 * @since 2016/09/12
 */
public class File
	implements Comparable<File>
{
	private final Path _path;
	
	/**
	 * Initializes the file with the given path string.
	 *
	 * @param __s The string to use for the path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/12
	 */
	public File(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set path
		this._path = Paths.get(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/12
	 */
	@Override
	public int compareTo(File __o)
	{
		return toPath().compareTo(__o.toPath());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof File))
			return false;
		
		// Compare
		return toPath().equals(((File)__o).toPath());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/12
	 */
	@Override
	public int hashCode()
	{
		return toPath().hashCode();
	}
	
	/**
	 * Returns the NIO path of the file.
	 *
	 * @return The path of the file.
	 * @since 2016/0912
	 */
	public Path toPath()
	{
		return this._path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/12
	 */
	@Override
	public String toString()
	{
		return toPath().toString();
	}
}

