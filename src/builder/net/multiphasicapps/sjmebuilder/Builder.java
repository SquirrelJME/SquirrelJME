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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.sjmepackages.PackageName;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JIT;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceContent;
import net.multiphasicapps.squirreljme.jit.JITNamespaceProcessor;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;
import net.multiphasicapps.squirreljme.jit.JITTriplet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This is the builder for native binaries.
 *
 * @since 2016/06/24
 */
public class Builder
	implements JITNamespaceContent
{
	/** The size of the resource buffer. */
	public static final int RESOURCE_BUFFER_SIZE =
		4096;
	
	/** Target operating system key. */
	public static final String TARGET_OS_KEY =
		"X-SquirrelJME-Target-OS";
	
	/** The package list to use. */
	protected final PackageList plist;
	
	/** The requested triplet. */
	protected final JITTriplet triplet;
	
	/** The package that implements the JVM for the target triplet. */
	protected final PackageInfo toppackage;
	
	/** All the packages that are dependencies of the top level package. */
	protected final Set<PackageInfo> topdepends;
	
	/** JIT options. */
	protected final JITOutputConfig.Immutable jitconfig;
	
	/** The namespace processor. */
	protected final JITNamespaceProcessor processor;
	
	/** The cache builder. */
	protected final BuilderCache buildercache;
	
	/** The temporary output directory. */
	private volatile Path _tempdir;
	
	/**
	 * Initializes the builder for a native target.
	 *
	 * @param __pl The package list to use.
	 * @param __trip The target triplet.
	 * @throws IllegalArgumentException If no package exists for the given
	 * triplet.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public Builder(PackageList __pl, JITTriplet __trip)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__pl == null || __trip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.triplet = __trip;
		String normaltriplet = __trip.toString();
		
		// Setup configuration
		JITOutputConfig jitconfig = new JITOutputConfig();
		jitconfig.setTriplet(__trip);
		String packagetrip = __trip.toPackageTarget();
		
		System.err.printf("DEBUG -- Target: %s %s%n", normaltriplet,
			packagetrip);
		
		// Go through all of the packages to find the one that specifies that
		// it is the JVM for the given triplet
		PackageInfo tpk = null;
		this.plist = __pl;
		for (Map.Entry<PackageName, PackageInfo> e : __pl.entrySet())
		{
			// Get the manifest
			PackageInfo pi = e.getValue();
			JavaManifest man = pi.manifest();
			
			// If it does not exist, ignore
			if (man == null)
				continue;
			
			// Get the main attributes
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// See if the triplet properly exists
			String pott = attr.get(TARGET_OS_KEY);
			if (pott != null)
			{
				// The targets are split by space
				boolean matched = false;
				int n = pott.length();
				for (int i = 0; i < n; i++)
				{
					// Ignore whitespace
					char c = pott.charAt(i);
					if (c <= ' ')
						continue;
					
					// Find the next space or end
					int j;
					for (j = i + 1; j < n; j++)
						if (pott.charAt(j) <= ' ')
							break;
					
					// Matches?
					String sub = pott.substring(i, j);
					if (packagetrip.equals(sub) ||
						normaltriplet.equals(sub))
					{
						matched = true;
						break;
					}
					
					// Skip to next
					i = j;
				}
				
				// Matched? use it
				if (matched)
				{
					tpk = pi;
					break;
				}
			}
		}
		
		// {@squirreljme.error DW06 No package (The used triplet)}
		if (tpk == null)
			throw new IllegalArgumentException(String.format("DW06 %s",
				triplet));
		
		// Set
		this.toppackage = tpk;
		
		// Go through all of the dependencies of the package and include them
		// for compilation
		Set<PackageInfo> pis = new LinkedHashSet<>();
		__getDependencies(pis, tpk);
		this.topdepends = UnmodifiableSet.<PackageInfo>of(pis);
		
		// Setup cache creator for output, writes into the globbed jar
		BuilderCache buildercache = new BuilderCache(this);
		this.buildercache = buildercache;
		jitconfig.setCacheCreator(buildercache);
		
		// Finish
		JITOutputConfig.Immutable immut = jitconfig.immutable();
		this.jitconfig = immut;
		
		// Setup processor
		this.processor = new JITNamespaceProcessor(immut, this);
	}
	
	/**
	 * Performs the actual build step.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/06/24
	 */
	public void build()
		throws IOException
	{
		// Need temporary directory
		Path tempdir = null;
		try
		{
			// Create temporary directory
			tempdir = Files.createTempDirectory("squirreljme-native-build");
			this._tempdir = tempdir;
			
			// Go through all dependencies and dynamically compile every class
			// file in them.
			for (PackageInfo pi : this.topdepends)
				__buildPackage(tempdir, pi);
			
			// Generate an output binary which is linked from the input sources
			JITNamespaceProcessor nsproc = this.processor;
			Path exename = Paths.get(nsproc.executableName());
			try (OutputStream os = Channels.newOutputStream(
				FileChannel.open(exename, StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE)))
			{
				nsproc.linkBinary(os);
			}
			
			// Failed to write the output
			catch (IOException|RuntimeException|Error e)
			{
				// Delete it
				try
				{
					Files.delete(exename);
				}
				
				// Suppressed
				catch (IOException f)
				{
					e.addSuppressed(f);
				}
				
				// Rethrow
				throw e;
			}
		}
		
		// Delete temporary directory
		finally
		{
			// Delete if it exists
			if (tempdir != null)
				try
				{
					// Delete all files in the directory
					try (DirectoryStream<Path> ds = Files.
						newDirectoryStream(tempdir))
					{
						for (Path p : ds)
							try
							{
								System.err.printf("DEBUG -- Delete `%s`%n",
									p);
								Files.delete(p);
							}
							
							// Ignore
							catch (IOException e)
							{
							}
					}
					
					// Delete the directory
					Files.delete(tempdir);
				}
				
				// Ignore
				catch (IOException e)
				{
				}
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
		return new BuildDirectory(__ns);
	}
	
	/**
	 * Returns the current package list being used.
	 *
	 * @return The current package list.
	 * @since 2016/07/18
	 */
	public PackageList packageList()
	{
		return this.plist;
	}
	
	/**
	 * Returns the temporary directory.
	 *
	 * @return The temporary directory.
	 * @since 2016/07/18
	 */
	public Path temporaryDirectory()
	{
		return this._tempdir;
	}
	
	/**
	 * Builds the specified package.
	 *
	 * @param __td The temporary directory.
	 * @param __pi The package to build.
	 * @throws IOException On read or write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	private void __buildPackage(Path __td, PackageInfo __pi)
		throws IOException, NullPointerException
	{
		// Check
		if (__td == null || __pi == null)
			throw new NullPointerException("NARG");
		
		// Process this namespace
		this.processor.processNamespace(__pi.name() + ".jar");
	}
	
	/**
	 * Gets the dependencies of all packages and places them in the given set.
	 *
	 * @param __pis The target set for packages.
	 * @param __pi The top-level package to start at.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	private void __getDependencies(Set<PackageInfo> __pis, PackageInfo __pi)
		throws NullPointerException
	{
		// Check
		if (__pis == null || __pi == null)
			throw new NullPointerException("NARG");
		
		// Setup queue
		Deque<PackageInfo> q = new LinkedList<>();
		q.offerLast(__pi);
		
		// Drain the queue
		while (!q.isEmpty())
		{
			// Remove
			PackageInfo i = q.removeFirst();
			
			// Add any dependencies of the package if it was not added
			if (__pis.add(i))
				for (PackageInfo p : i.dependencies())
					q.offerLast(p);
		}
	}
	
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
		
		/**
		 * Initializes the build directory.
		 *
		 * @param __ns The namespace to iterate through.
		 * @throws IOException On open failures.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/07
		 */
		private BuildDirectory(String __ns)
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
		
			// {@squirreljme.error DW08 The namespace does not have an
			// associated package. (The namespace)}
			PackageInfo pi = Builder.this.plist.get(jarless);
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
	
	/**
	 * This represents an entry which exists within a directory.
	 *
	 * @since 2016/07/07
	 */
	public class BuildEntry
		implements JITNamespaceContent.Entry
	{
		/** The entry to use. */
		protected final ZipEntry entry;
		
		/**
		 * Initializes the build entry.
		 *
		 * @param __ze The entry to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/07
		 */
		private BuildEntry(ZipEntry __ze)
			throws NullPointerException
		{
			// Check
			if (__ze == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.entry = __ze;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/07
		 */
		@Override
		public String name()
			throws JITException
		{
			try
			{
				return this.entry.name();
			}
			
			// {@squirreljme.error DW0d Could not read the entry name.}
			catch (IOException e)
			{
				throw new JITException("DW0d", e);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/07
		 */
		@Override
		public InputStream open()
			throws IOException
		{
			return this.entry.open();
		}
	}
}

