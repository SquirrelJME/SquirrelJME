// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms.file;

import cc.squirreljme.kernel.lib.SuiteName;
import cc.squirreljme.kernel.lib.SuiteVendor;
import cc.squirreljme.runtime.rms.RecordCluster;
import cc.squirreljme.runtime.rms.RecordStoreOwner;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.microedition.rms.RecordStoreException;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This is a cluster which is backed by the filesystem.
 *
 * Record stores are placed in individual directories with an identifying
 * manifest.
 *
 * @since 2017/02/28
 */
public class FileRecordCluster
	extends RecordCluster
{
	/** The key used for store names. */
	private static final JavaManifestKey _STORE_NAME_KEY =
		new JavaManifestKey("store-name");
	
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
			SuiteVendor vendor = __o.vendor();
			use = __scanPath(use, vendor.hashCode(), "vendor",
				vendor.toString());
			
			// Locate the name directory (or create)
			SuiteName name = __o.name();
			use = __scanPath(use, name.hashCode(), "name",
				name.toString());
			
			// Records are stored here
			this.path = use;
		}
		
		// {@squirreljme.error DC02 Could not initialize the file backed
		// record cluster. (The owner to initialize for)}
		catch (IOException e)
		{
			RecordStoreException t = new RecordStoreException(String.format(
				"DC02 %s", __o));
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
		throws RecordStoreException
	{
		// Lock
		synchronized (this.lock)
		{
			// Iterate through the cluster to find record stores
			try (DirectoryStream<Path> ds =
				Files.newDirectoryStream(this.path))
			{
				Collection<String> output = new HashSet<>();
			
				// Iterate
				for (Path p : ds)
				{
					// Ignore non-directories
					if (!Files.isDirectory(p))
						continue;
					
					// Potential path where the manifest may be
					Path manpath = p.resolve("RECORD.MF");
					try (InputStream is = Channels.newInputStream(
						FileChannel.open(manpath, StandardOpenOption.READ)))
					{
						// Read manifest
						JavaManifest man = new JavaManifest(is);
						JavaManifestAttributes attr = man.getMainAttributes();
						
						// Only add store if it is specified
						String val = attr.get(_STORE_NAME_KEY);
						if (val != null)
							output.add(val);
					}
					
					// Ignore
					catch (NoSuchFileException e)
					{
						continue;
					}
				}
				
				// Use them
				return output.<String>toArray(new String[output.size()]);
			}
		
			// {@squirreljme.error DC03 Could not list record store contents}
			catch (IOException e)
			{
				throw new RecordStoreException("DC03");
			}
		}
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
			Path maybe = __base.resolve(Long.toString(i & 0xFFFFFFFFL, 36));
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

