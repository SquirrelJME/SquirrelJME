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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import net.multiphasicapps.squirreljme.projects.PackageInfo;
import net.multiphasicapps.squirreljme.projects.PackageList;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceContent;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This is the build directory used by the namespace builder to go through
 * the contents in a given namespace.
 *
 * @since 2016/07/07
 */
public class BuildDirectory
	implements JITNamespaceContent.Directory
{
	/** The opened file channel. */
	protected final FileChannel channel;
	
	/** The opened ZIP. */
	protected final ZipFile zip;
	
	/** The package list. */
	protected final PackageList plist;
	
	/** The temporary directory. */
	protected final Path tempdir;
	
	/**
	 * Initializes the build directory.
	 *
	 * @param __b The owning builder.
	 * @param __ns The namespace to iterate through.
	 * @throws IOException On open failures.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	BuildDirectory(PackageList __pl, Path __td, String __ns)
		throws IOException, NullPointerException
	{
		// Check
		if (__pl == null || __td == null || __ns == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __pl;
		this.tempdir = __td;
		
		// {@squirreljme.error DW05 The namespace does not end in .jar.
		// (The namespace)}
		if (!__ns.endsWith(".jar"))
			throw new IllegalStateException(String.format("DW05 %s", __ns));
	
		// Remove the JAR
		String jarless = __ns.substring(0, __ns.length() - ".jar".length());
	
		// {@squirreljme.error DW08 The namespace does not have an
		// associated package. (The namespace)}
		PackageInfo pi = this.plist.get(jarless);
		if (pi == null)
			throw new IllegalStateException(String.format("DW08 %s", __ns));
		
		// Open 
		FileChannel fc = null;
		ZipFile zip = null;
		try
		{
			// Open channel
			fc = FileChannel.open(pi.path(),
				StandardOpenOption.READ);
			this.channel = fc;
			
			// Then the zip
			zip = ZipFile.open(fc);
			this.zip = zip;
		}
		
		// Failed
		catch (IOException|RuntimeException|Error e)
		{
			// Close ZIP
			try
			{
				zip.close();
			}
			
			catch (IOException f)
			{
				e.addSuppressed(f);
			}
			
			// Close channel
			try
			{
				fc.close();
			}
			
			// Failed
			catch (IOException f)
			{
				e.addSuppressed(f);
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
	public void close()
		throws IOException
	{
		// Close the channel
		IOException fail = null;
		try
		{
			this.channel.close();
		}
		
		// {@squirreljme.error DW0a Failed to close the channel.}
		catch (IOException e)
		{
			if (fail == null)
				fail = new IOException("DW0a");
			fail.addSuppressed(e);
		}
		
		// Close the ZIP
		try
		{
			this.zip.close();
		}
		
		// {@squirreljme.error DW0b Failed to close the ZIP.}
		catch (IOException e)
		{
			if (fail == null)
				fail = new IOException("DW0b");
			fail.addSuppressed(e);
		}
		
		// Failed?
		if (fail != null)
			throw fail;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/07
	 */
	@Override
	public Iterator<JITNamespaceContent.Entry> iterator()
	{
		return new Iterator<JITNamespaceContent.Entry>()
			{
				/** Base iterator for the ZIP. */
				protected final Iterator<ZipEntry> base =
					BuildDirectory.this.zip.iterator();
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/07
				 */
				@Override
				public boolean hasNext()
				{
					return this.base.hasNext();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/07
				 */
				@Override
				public JITNamespaceContent.Entry next()
				{
					return new BuildEntry(this.base.next());
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/07
				 */
				@Override
				public void remove()
				{
					// {@squirreljme.error DW0c Cannot remove entries.}
					throw new UnsupportedOperationException("DW0c");
				}
			};
	}
}

