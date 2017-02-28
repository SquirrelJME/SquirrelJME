// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.rms.file;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.microedition.rms.RecordStoreException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.java.manifest.mutable.
	MutableJavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.mutable.
	MutableJavaManifestAttributes;
import net.multiphasicapps.squirreljme.rms.RecordCluster;
import net.multiphasicapps.squirreljme.rms.RecordStoreOwner;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteName;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteVendor;

/**
 * This is a cluster which is backed by the filesystem.
 *
 * @since 2017/02/28
 */
public class FileRecordCluster
	extends RecordCluster
{
	/** The root where record stores exist. */
	protected final Path path;
	
	/**
	 * Initializes the file record cluster.
	 *
	 * @param __o The owner of the cluster.
	 * @param __p The base cluster path.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If there is a problem with the record
	 * system.
	 * @since 2017/02/26
	 */
	public FileRecordCluster(RecordStoreOwner __o, Path __p)
		throws RecordStoreException, NullPointerException
	{
		super(__o);
		
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Could fail to read or write
		try
		{
			// Start here
			Path use = __p;
			
			// Locate the vendor directory (or create)
			MidletSuiteVendor vendor = __o.vendor();
			use = __scanPath(use, vendor.hashCode(), "vendor",
				vendor.toString());
			
			// Locate the name directory (or create)
			MidletSuiteName name = __o.name();
			use = __scanPath(use, name.hashCode(), "name",
				name.toString());
			
			// Records are stored here
			this.path = use;
		}
		
		// {@squirreljme.error AW01 Could not initialize the file backed
		// record cluster. (The owner to initialize for)}
		catch (IOException e)
		{
			RecordStoreException t = new RecordStoreException(String.format(
				"AW01 %s", __o));
			t.initCause(e);
			throw t;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/27
	 */
	@Override
	public String[] listRecordStores()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Scans the path for the associated directory.
	 *
	 * @param __base The base directory.
	 * @param __hc The hashcode to locate.
	 * @param __key The key in the manifest.
	 * @param __val The value expected to that key.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	private static Path __scanPath(Path __base, int __hc, String __key,
		String __val)
		throws IOException, NullPointerException
	{
		// Check
		if (__base == null || __key == null || __val == null)
			throw new NullPointerException("NARG");
		
		// Manifest used for the key
		JavaManifestKey mankey = new JavaManifestKey(__key);
		
		// Locate the vendor directory (or create)
		for (int i = __hc;; i++)
		{
			// Try this path
			Path maybe = __base.resolve(Integer.toString(i, 36));
			Path mpath = maybe.resolve("RECORD.MF");
			
			// Open the manifest if it exists
			try (InputStream is = Channels.newInputStream(FileChannel.open(
				mpath, StandardOpenOption.READ)))
			{
				JavaManifest man = new JavaManifest(is);
				JavaManifestAttributes attr = man.getMainAttributes();
				
				// Only use the same key
				String existval = attr.get(mankey);
				if (!__val.equals(existval))
					continue;
				
				// Use this path
				return maybe;
			}
			
			// No manifest exists, so create directory and place here
			catch (NoSuchFileException e)
			{
				// Create directories
				Files.createDirectories(maybe);
				
				// Setup manifest
				MutableJavaManifest mman = new MutableJavaManifest();
				MutableJavaManifestAttributes mattr = mman.getMainAttributes();
				
				// Fill in key
				mattr.put(mankey, __val);
				
				// Write it
				try (OutputStream os = Channels.newOutputStream(
					FileChannel.open(mpath, StandardOpenOption.WRITE,
					StandardOpenOption.CREATE_NEW)))
				{
					mman.write(os);
				}
				
				// Use this
				return maybe;
			}
		}
	}
}

