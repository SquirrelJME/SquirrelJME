// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a file system which is completely nullary.
 *
 * @since 2019/12/22
 */
public final class NullFileSystem
	extends FileSystem
{
	/** The instance of this filesystem. */
	public static final NullFileSystem INSTANCE =
		new NullFileSystem();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Has no effect
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Iterable<FileStore> getFileStores()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Path getPath(String __base, String... __more)
		throws NullPointerException
	{
		if (__base == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZY01 The null filesystem only supports a single
		path element.} */
		if (!__base.equals(NullPath.ONLY_PATH_STRING))
			throw new InvalidPathException(__base, "ZY01");
		
		/* {@squirreljme.error ZY02 Additional null path elements must be
		blank or null.} */
		if (__more != null)
			for (String m : __more)
				if (m != null && !m.isEmpty())
					throw new InvalidPathException(m, "ZY02");
		
		return NullPath.INSTANCE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Iterable<Path> getRootDirectories()
	{
		// There is only a single null root
		return Arrays.<Path>asList(this.getPath("null:$"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final String getSeparator()
	{
		// Null file system uses dollar signs as paths
		return "$";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean isOpen()
	{
		// Always is open
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final boolean isReadOnly()
	{
		// File system is always read-only
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/22
	 */
	@Override
	public final Set<String> supportedFileAttributeViews()
	{
		// No attributes are supported
		return new HashSet<>();
	}
}

