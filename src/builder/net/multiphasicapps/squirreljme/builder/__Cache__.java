// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.squirreljme.projects.PackageInfo;
import net.multiphasicapps.squirreljme.projects.PackageList;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceContent;

/**
 * This is the cache manager for builder blobs that are later compiled into
 * an actual binary.
 *
 * @since 2016/07/18
 */
public class __Cache__
	implements JITCacheCreator, JITNamespaceContent
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.builder.hexdump=(true/false)
	 * Sets whether or not created cached content should be hexdumped to the
	 * console for debugging purposes.}
	 */
	private static final boolean _HEX_DUMP_OUTPUT =
		Boolean.getBoolean("net.multiphasicapps.squirreljme.builder.hexdump");
	
	/** Package information to namespace blobs. */
	protected final Map<PackageInfo, Path> blobmap =
		new HashMap<>();
	
	/** The package list. */
	protected final PackageList plist;
	
	/** The temporary directory. */
	protected final Path tempdir;
	
	/**
	 * Initializes the builder cache.
	 *
	 * @param __pl The package list.
	 * @param __td The temporary directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public __Cache__(PackageList __pl, Path __td)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __td == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __pl;
		this.tempdir = __td;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public String[] cachedNamespaces()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public OutputStream createCache(String __ns)
		throws IOException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Get the package
		PackageInfo pi = __getPackage(__ns);
		
		// Create temporary output file where the stream goes
		Path p = Files.createTempFile(this.tempdir,
			"squirreljme-build", __ns);
		
		// Mark it
		Map<PackageInfo, Path> blobmap = this.blobmap;
		try
		{
			// Place
			synchronized (blobmap)
			{
				blobmap.put(pi, p);
			}
			
			// Create output
			OutputStream rv = Channels.newOutputStream(FileChannel.open(p,
					StandardOpenOption.WRITE));
			if (_HEX_DUMP_OUTPUT)
				return new HexDumpOutputStream(rv, System.err);
			return rv;
		}
		
		// Failed to open
		catch (IOException|Error|RuntimeException e)
		{
			// Remove it
			synchronized (blobmap)
			{
				blobmap.remove(pi);
			}
			
			// Rethrow
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/07
	 */
	@Override
	public JITNamespaceContent.Directory directoryOf(String __ns)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new __Directory__(this.plist, this.tempdir, __ns);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public InputStream openCache(String __ns)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Get the package
		PackageInfo pi = __getPackage(__ns);
		
		// Lock
		Map<PackageInfo, Path> blobmap = this.blobmap;
		Path p;
		synchronized (blobmap)
		{
			p = blobmap.get(pi);
		}
		
		// {@squirreljme.error DW0f The specified namespace was not previously
		// opened. (The namespace)}
		if (p == null)
			throw new JITException(String.format("DW0f %s", __ns));
		
		// Open it
		return Channels.newInputStream(FileChannel.open(p,
			StandardOpenOption.READ));
	}
	
	/**
	 * Returns the package that is associated with the given namespace.
	 *
	 * @param __ns The namespace to get the package for.
	 * @throws JITException If the namespace is not valid.
	 * @throws NullPointerException On null arguments.
	 * @return The associated package or {@code null} if it does not exist.
	 * @since 2016/06/18
	 */
	private PackageInfo __getPackage(String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DW06 The namespace does not end in .jar.
		// (The namespace)}
		if (!__ns.endsWith(".jar"))
			throw new JITException(String.format("DW06 %s", __ns));
		
		// Remove the JAR
		String jarless = __ns.substring(0, __ns.length() - ".jar".length());
		
		// {@squirreljme.error DW0e The namespace does not have an associated
		// package. (The namespace)}
		PackageInfo pi = this.plist.get(jarless);
		if (pi == null)
			throw new JITException(String.format("DW0e %s", __ns));
		
		// Return it
		return pi;
	}
}

