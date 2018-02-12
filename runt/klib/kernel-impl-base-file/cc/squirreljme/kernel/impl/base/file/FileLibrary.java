// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.kernel.lib.Library;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This is the base class for libraries which are backed by the filesystem.
 *
 * @since 2018/02/11
 */
public abstract class FileLibrary
	extends Library
{
	/** Lock to prevent mismashed read/writes. */
	protected final Object lock =
		new Object();
	
	/** The path to the library binary. */
	protected final Path binpath;
	
	/** The path to the control manifest. */
	protected final Path controlpath;
	
	/**
	 * Initializes the file library.
	 *
	 * @param __p The path to the library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/11
	 */
	public FileLibrary(Path __p)
		throws NullPointerException
	{
		super(__extractIndex(__p));
		
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.binpath = __p;
		this.controlpath = __p.resolveSibling("control").resolve(
			__p.getFileName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	public final String controlGet(String __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return __Utils__.__get(this.lock, this.controlpath,
			new JavaManifestKey(__k));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	public final void controlSet(String __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		__Utils__.__set(this.lock, this.controlpath, new JavaManifestKey(__k),
			__v);
	}
	
	/**
	 * Extracts the index from the specified path.
	 *
	 * @param __p The path to extract the index from.
	 * @return The index to the file path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/11
	 */
	private static final int __extractIndex(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return Integer.parseInt(__p.getFileName().toString(), 10);
	}
}

