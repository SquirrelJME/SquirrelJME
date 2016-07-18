// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

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
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is the cache manager for builder blobs that are later compiled into
 * an actual binary.
 *
 * @since 2016/07/18
 */
public class BuilderCache
	implements JITCacheCreator
{
	/** The owning builder. */
	protected final Builder builder;
	
	/** Package information to namespace blobs. */
	protected final Map<PackageInfo, Path> blobmap =
		new HashMap<>();
	
	/**
	 * Initializes the builder cache.
	 *
	 * @param __b The builder which owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public BuilderCache(Builder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.builder = __b;
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
		
		// {@squirreljme.error DW05 The namespace does not end in .jar.
		// (The namespace)}
		if (!__ns.endsWith(".jar"))
			throw new IllegalStateException(String.format("DW05 %s", __ns));
		
		// Remove the JAR
		String jarless = __ns.substring(0, __ns.length() - ".jar".length());
		
		// {@squirreljme.error DW08 The namespace does not have an associated
		// package. (The namespace)}
		PackageInfo pi = this.builder.packageList().get(jarless);
		if (pi == null)
			throw new IllegalStateException(String.format("DW08 %s", __ns));
		
		// Create temporary output file where the stream goes
		Path p = Files.createTempFile(this.builder.temporaryDirectory(),
			"squirreljme-build", __ns);
		
		// Mark it
		Map<PackageInfo, Path> blobmap = this.blobmap;
		try
		{
			// Place
			blobmap.put(pi, p);
			
			// Create output
			return Channels.newOutputStream(FileChannel.open(p,
				StandardOpenOption.WRITE));
		}
		
		// Failed to open
		catch (IOException|Error|RuntimeException e)
		{
			// Remove it
			blobmap.remove(pi);
			
			// Rethrow
			throw e;
		}
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
		
		throw new Error("TODO");
	}
}

