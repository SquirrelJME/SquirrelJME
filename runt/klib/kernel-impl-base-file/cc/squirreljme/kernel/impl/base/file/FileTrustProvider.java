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

import cc.squirreljme.kernel.trust.server.TrustProvider;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This contains the trust provider which uses the backed filesystem to
 * store the trust information.
 *
 * @since 2018/01/31
 */
public final class FileTrustProvider
	extends TrustProvider
{
	/** The path where trusts are located. */
	protected final Path trustpath;
	
	/**
	 * Initializes the trust provider using the default set of paths.
	 *
	 * @since 2018/01/31
	 */
	public FileTrustProvider()
	{
		this(StandardPaths.DEFAULT);
	}
	
	/**
	 * Initializes the trust provider using the specified path set.
	 *
	 * @param __sp The paths to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public FileTrustProvider(StandardPaths __sp)
		throws NullPointerException
	{
		if (__sp == null)
			throw new NullPointerException("NARG");
		
		Path trustpath = __sp.trustPath();
		this.trustpath = trustpath;
		
		// Scan the directory for trusts
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(trustpath))
		{
			for (Path p : ds)
			{
				// Only consider files	
				if (!Files.isRegularFile(p))
					continue;
				
				this.register(new FileTrustGroup(p));
			}
		}
		
		// Ignore this as no libraries are actually installed then
		catch (NoSuchFileException e)
		{
		}
		
		// {@squirreljme.error BH02 Could not read installed trusts.}
		catch (IOException e)
		{
			throw new RuntimeException("BH02", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	protected final SystemTrustGroup createTrustGroup(boolean __trusted,
		int __dx, String __name, String __vendor)
		throws NullPointerException
	{
		if (__name == null || __vendor == null)
			throw new NullPointerException("NARG");
		
		// Determine path where the data will be stored
		Path path = this.trustpath.resolve(Integer.toString(__dx));
		
		// Lock
		synchronized (this.lock)
		{
			FileTrustGroup rv = new FileTrustGroup(path);
			
			rv.__set(FileTrustGroup.IS_TRUSTED, Boolean.toString(__trusted));
			rv.__set(FileTrustGroup.NAME, __name);
			rv.__set(FileTrustGroup.VENDOR, __vendor);
			
			return rv;
		}
	}
}

