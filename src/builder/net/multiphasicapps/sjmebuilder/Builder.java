// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.HashMap;
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
import net.multiphasicapps.squirreljme.ssjit.SSJIT;
import net.multiphasicapps.squirreljme.ssjit.SSJITProducerFactory;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zips.ZipEntry;
import net.multiphasicapps.zips.ZipFile;

/**
 * This is the builder for native binaries.
 *
 * @since 2016/06/24
 */
public class Builder
{
	/** The size of the resource buffer. */
	public static final int RESOURCE_BUFFER_SIZE =
		4096;
	
	/** The package list to use. */
	protected final PackageList plist;
	
	/** The target architecture. */
	protected final String arch;
	
	/** The target variant of the architecture. */
	protected final String archvariant;
	
	/** The target operating system. */
	protected final String os;
	
	/** The target variant. */
	protected final String variant;
	
	/** The triplet. */
	protected final String triplet;
	
	/** The target OS triplet. */
	protected final String targettriplet;
	
	/** The package that implements the JVM for the target triplet. */
	protected final PackageInfo toppackage;
	
	/** All the packages that are dependencies of the top level package. */
	protected final Set<PackageInfo> topdepends;
	
	/** The globbed JARs which are available. */
	protected final Map<PackageInfo, GlobbedJar> globjars =
		new HashMap<>();
	
	/** The code producer factory to use during generation. */
	protected final SSJITProducerFactory factory;
	
	/** The variant to be used during generation. */
	protected final SSJITProducerFactory.Variant factoryvariant;
	
	/**
	 * Initializes the builder for a native target.
	 *
	 * @param __pl The package list to use.
	 * @param __arch The target architecture.
	 * @param __os The target operating system.
	 * @param __var The target variant.
	 * @throws IllegalArgumentException If no package exists for the given
	 * triplet.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public Builder(PackageList __pl, String __arch, String __os, String __var)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__pl == null || __arch == null || __os == null || __var == null)
			throw new NullPointerException("NARG");
		
		// Does the architecture have a variant?
		int hplu = __arch.indexOf('+');
		String archvar;
		if (hplu >= 0)
		{
			this.archvariant = (archvar = __arch.substring(hplu + 1));
			this.arch = (__arch = __arch.substring(0, hplu));
		}
		
		// Does not, assume generic support
		else
		{
			this.arch = __arch;
			this.archvariant = (archvar = "generic");
		}
		
		// Set
		this.plist = __pl;
		this.os = __os;
		this.variant = __var;
		
		// Build triplet
		String triplet = __arch + "+" + archvar + "." + __os + "." + __var;
		String targettriplet = __arch + "." + __os + "." + __var;
		this.triplet = triplet;
		this.targettriplet = targettriplet;
		
		System.err.printf("DEBUG -- Target: %s%n", triplet);
		
		// Go through all of the packages to find the one that specifies that
		// it is the JVM for the given triplet
		PackageInfo tpk = null;
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
			String pott = attr.get("X-SquirrelJME-Target-OS");
			
			// Matches?
			if (targettriplet.equals(pott))
			{
				tpk = pi;
				break;
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
		
		// Find the service that generates for this given system
		ServiceLoader<SSJITProducerFactory> sl =
			ServiceLoader.<SSJITProducerFactory>load(
				SSJITProducerFactory.class);
		SSJITProducerFactory fallback = null;
		SSJITProducerFactory hit = null;
		SSJITProducerFactory.Variant fbvar = null;
		for (SSJITProducerFactory pf : sl)
		{
			// Matches the architecture? and variant?
			if (arch.equals(pf.architecture()))
				if (null != (fbvar = pf.getVariant(archvar)))
				{
					// Only accept a direct hit if a factory provides support
					// for the deisred operating system.
					String pfos = pf.operatingSystem();
					if (pfos != null && os.equals(pfos))
					{
						fallback = hit = pf;
						break;
					}
					
					// Otherwise if no operating system was specified (this
					// is a generic generator) fallback on it. However do not
					// fallback onto a generator that is for a specific OS
					// since it might not work.
					else if (pfos == null)
						fallback = pf;
				}
		}
		
		// If not hit, fallback
		if (hit == null)
			hit = fallback;
		
		// {@squirreljme.error DW08 Could not find a code generator which is
		// capable of targetting the given triplet. (The target triplet)}
		if (hit == null)
			throw new RuntimeException(String.format("DW08 %s", triplet));
		
		// Set
		this.factory = hit;
		this.factoryvariant = fbvar;
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
			
			// Go through all dependencies and dynamically compile every class
			// file in them.
			for (PackageInfo pi : this.topdepends)
				__buildPackage(tempdir, pi);
		
			throw new Error("TODO");
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
		
		// Setup globbed JAR for this package
		GlobbedJar gj = new GlobbedJar(__td, __pi);
		this.globjars.put(__pi, gj);
		
		// Open ZIP
		try (FileChannel fc = FileChannel.open(__pi.path(),
			StandardOpenOption.READ);
			ZipFile zip = ZipFile.open(fc))
		{
			// Go through all entries
			for (ZipEntry e : zip)
			{
				// Ignore directories
				if (e.isDirectory())
					continue;
				
				// If a class file, recompile it
				String name = e.name();
				if (name.endsWith(".class"))
					__handleClass(gj, e);
				
				// A JAR resource, output the data
				else
					__handleResource(gj, e);
			}
		}
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
	 * Handles compilation of a class.
	 *
	 * @param __gj The output globbed JAR.
	 * @param __e The entry in the package.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	private void __handleClass(GlobbedJar __gj, ZipEntry __e)
		throws IOException, NullPointerException
	{
		// Check
		if (__gj == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Determine the name of the class
		String ename = __e.name();
		String classname = ename.substring(0,
			ename.length() - ".class".length());
		
		// Open a resource to be placed in the globbed jar
		try (InputStream is = __e.open();
			OutputStream os = __gj.createClass(classname))
		{
			// Setup JIT
			SSJIT jit = new SSJIT(is, os);
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Handles a JAR resource.
	 *
	 * @param __gj The output globbed JAR.
	 * @param __e The entry in the package.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	private void __handleResource(GlobbedJar __gj, ZipEntry __e)
		throws IOException, NullPointerException
	{
		// Check
		if (__gj == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Open a resource to be placed in the globbed jar
		try (InputStream is = __e.open();
			OutputStream os = __gj.createResource(__e.name()))
		{
			// Copy all the data
			int sz = RESOURCE_BUFFER_SIZE;
			byte[] buf = new byte[sz];
			for (;;)
			{
				// Read in data
				int rc = is.read(buf, 0, sz);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write
				os.write(buf, 0, rc);
			}
		}
	}
}

