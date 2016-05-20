// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive.fs;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.squirreljme.kernel.archive.Archive;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;

/**
 * This represents an archive which is mapped to a JAR on the filesystem.
 *
 * @since 2016/05/20
 */
public class FSArchive
	extends Archive
{
	/** The path to the archive. */
	protected final Path path;
	
	/** The string representation of this archive. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the archive.
	 *
	 * @param __p The path to the ZIP file.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public FSArchive(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
	
	/*
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	protected NCIClass internalLocateClass(ClassNameSymbol __n)
	{
		throw new Error("TODO");
	}
	
	/*
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	protected InputStream internalLocateResource(String __n)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the path that this filesystem archive points to.
	 *
	 * @return The path to the archive.
	 * @since 2016/05/20
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Need caching?
		if (ref == null || null == (rv = ref.get()))
		{
			// Build
			StringBuilder sb = new StringBuilder();
			
			// Add the basename
			Path p = this.path;
			sb.append(p.getFileName());
			
			// And the directory it is in
			sb.append(" (");
			sb.append(p.getParent());
			sb.append(')');
			
			// Finish
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return
		return rv;
	}
}

