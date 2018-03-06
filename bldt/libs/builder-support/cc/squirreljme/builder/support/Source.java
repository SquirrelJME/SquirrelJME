// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import cc.squirreljme.kernel.suiteinfo.DependencyInfo;
import cc.squirreljme.kernel.suiteinfo.InvalidSuiteException;
import cc.squirreljme.kernel.suiteinfo.ProvidedInfo;
import cc.squirreljme.kernel.suiteinfo.SuiteInfo;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import net.multiphasicapps.strings.StringUtils;
import net.multiphasicapps.javac.ByteArrayCompilerInput;
import net.multiphasicapps.javac.DistinctPathSet;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.EmptyPathSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This represents a single source project which contains the source code for
 * a single project.
 *
 * @since 2017/10/31
 */
public final class Source
{
	/** The base project this provides tests for. */
	protected final Source testingsource;
	
	/** The name of the source. */
	protected final SourceName name;
	
	/** The path to the source code root. */
	protected final Path root;
	
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/** The type of project this is. */
	protected final ProjectType type;
	
	/** The approximate binary manifest. */
	private volatile Reference<JavaManifest> _approxbm;
	
	/** The suite information. */
	private volatile Reference<SuiteInfo> _suiteinfo;
	
	/** Last modified time of the source code. */
	private volatile long _lastmodtime =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the project source.
	 *
	 * @param __name The name of the source.
	 * @param __p The path to the source code.
	 * @param __t The type of project this is.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public Source(SourceName __name, Path __p, ProjectType __t)
		throws IOException, NullPointerException
	{
		if (__name == null || __p == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.root = __p;
		this.type = __t;
		this.testingsource = null;
		
		// Load manifest
		try (InputStream in = Files.newInputStream(__p.resolve("META-INF").
			resolve("MANIFEST.MF"), StandardOpenOption.READ))
		{
			this.manifest = new JavaManifest(in);
		}
	}
	
	/**
	 * Initializes a virtual source project which is based on the specified
	 * real project.
	 *
	 * @param __root The root for the test project.
	 * @param __base The project to base off.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public Source(Path __root, Source __base)
		throws IOException, NullPointerException
	{
		if (__root == null || __base == null)
			throw new NullPointerException("NARG");
		
		// Test projects are always midlets because they are intended to be
		// ran
		this.type = ProjectType.MIDLET;
		
		// Use the virtual .test ending project
		this.name = new SourceName(__base.name() + ".test");
		
		// Real files are still involved in tests so this is needed for dating
		this.root = __root;
		
		// This specifies the source used for testing.
		this.testingsource = __base;
		
		// Build virtual manifest intended to act as tests for the input
		// library
		MutableJavaManifest wman = new MutableJavaManifest();
		MutableJavaManifestAttributes wattr = wman.getMainAttributes();
		
		SuiteInfo bs = __base.suiteInfo();
		wattr.putValue("x-squirreljme-name",
			"Tests for " + bs.name().toString());
		wattr.putValue("x-squirreljme-vendor", bs.vendor().toString());
		wattr.putValue("x-squirreljme-version", bs.version().toString());
		wattr.putValue("x-squirreljme-depends", __base.name().name());
		wattr.putValue("main-class", "TestMain");
		
		// Finalize
		this.manifest = wman.build();
	}
	
	/**
	 * Returns the extra path set for this source project.
	 *
	 * @return The extra path set for this source project.
	 * @since 2018/03/06
	 */
	public CompilerPathSet extraCompilerPathSet()
	{
		// If this is not a test project then include nothing
		Source testingsource = this.testingsource;
		if (testingsource == null)
			return EmptyPathSet.instance();
		
		// Generate input
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos, true))
		{
			// Begin
			ps.println("public class TestMain");
			ps.println("{");
			
			// Main entry
			ps.println("public static void main(String... __args)");
			ps.println("{");
			
			// End main entry
			ps.println("}");
			
			// End
			ps.println("}");
			
			// Build input
			ps.flush();
			return new DistinctPathSet(new ByteArrayCompilerInput(
				"TestMain.java", baos.toByteArray()));
		}
		
		// {@squirreljme.error AU17 Could not generate the virtual test
		// source project.}
		catch (IOException e)
		{
			throw new RuntimeException("AU17", e);
		}
	}
	
	/**
	 * Returns the time that the source code was last modified.
	 *
	 * @return The last modification date of the source code.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public long lastModifiedTime()
	{
		// Could be pre-cached
		long rv = this._lastmodtime;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Need to go through every single file in every directory, the date of
		// the newest file is used
		rv = Long.MIN_VALUE;
		try
		{
			// Start at the root directory
			Deque<Path> queue = new ArrayDeque<>();
			queue.add(this.root);
			
			// Process every directory
			while (!queue.isEmpty())
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(
					queue.removeFirst()))
				{
					for (Path p : ds)
					{
						// Handle directories later
						if (Files.isDirectory(p))
						{
							queue.addLast(p);
							continue;
						}
						
						// Use the newer file time
						FileTime ft = Files.getLastModifiedTime(p);
						if (ft != null)
						{
							long now = ft.toMillis();
							if (now > rv)
								rv = now;
						}
					}
				}
			
			// Cache for next time
			this._lastmodtime = rv;
			return rv;
		}
		
		// Error so the time cannot be known
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * The approximated manifest which would be used for binary output.
	 *
	 * @return The approximated manifest.
	 * @since 2017/11/17
	 */
	public final JavaManifest manifest()
	{
		Reference<JavaManifest> ref = this._approxbm;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Need the input manifest
			JavaManifest rman = this.sourceManifest();
			JavaManifestAttributes rattr = rman.getMainAttributes();
			MutableJavaManifest wman = new MutableJavaManifest();
			MutableJavaManifestAttributes wattr = wman.getMainAttributes();
			
			// Determine the prefix for the type keys
			ProjectType type = this.type;
			boolean isapi = type == ProjectType.API;
			String prefix = (type == ProjectType.MIDLET ?
				"MIDlet" : "LIBlet");
			
			// Projects might have dependencies, so find the actual base
			// before putting dependencies on top
			int depdx = 1;
			for (; depdx >= 0; depdx++)
				if (!rattr.containsKey(
					new JavaManifestKey(prefix + "-Dependency-" + depdx)))
					break;
			
			// Handle fields for the main attributes
			boolean apidetected = false;
			for (Map.Entry<JavaManifestKey, String> e : rattr.entrySet())
			{
				JavaManifestKey k = e.getKey();
				String v = e.getValue();
				
				// Depends on the key, these are lowercase
				switch (k.toString())
				{
						// Project name
					case "x-squirreljme-name":
						wattr.putValue(prefix + "-Name", v);
						break;
						
						// Project vendor
					case "x-squirreljme-vendor":
						wattr.putValue(prefix + "-Vendor", v);
						break;
						
						// Project version
					case "x-squirreljme-version":
						wattr.putValue(prefix + "-Version", v);
						break;
						
						// Project Description
					case "x-squirreljme-description":
						wattr.putValue(prefix + "-Description", v);
						break;
					
						// Dependencies, these are whitespace separated
					case "x-squirreljme-depends":
						for (String split : StringUtils.basicSplit("\0 \t\r\n",
							v))
							wattr.putValue(
								prefix + "-Dependency-" + (depdx++),
								"proprietary;required;squirreljme.project@" +
									split + ";Stephanie Gawroriski;*");
						break;
						
						// Never copy this flag
					case "x-squirreljme-isapi":
						break;
						
						// Only put API definitions in if these are actual APIs
					case "x-squirreljme-definesconfigurations":
					case "x-squirreljme-definesprofiles":
					case "x-squirreljme-definedstandards":
						if (isapi)
						{
							wattr.put(k, v);
							apidetected = true;
						}
						break;
					
						// Unhandled
					default:
						wattr.put(k, v);
						break;
				}
			}
			
			// Write the project name as it appears in the builder
			wattr.putValue("X-SquirrelJME-InternalProjectName",
				name().toString());
			
			// If an API was detected then flag it
			if (apidetected)
				wattr.putValue("X-SquirrelJME-IsAPI", "true");
			
			// Copy other attributes that may exist
			for (Map.Entry<String, JavaManifestAttributes> e : rman.entrySet())
			{
				// Do not copy the main manifest because that is specially
				// handled before
				String k;
				if (!"".equals((k = e.getKey())))
					wman.put(k,
						new MutableJavaManifestAttributes(e.getValue()));
			}
			
			// Build
			this._approxbm = new WeakReference<>((rv = wman.build()));
		}
		
		return rv;
	}
	
	/**
	 * Returns the name of the source.
	 *
	 * @return The source name.
	 * @since 2017/11/23
	 */
	public final SourceName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the root directory of the project source code.
	 *
	 * @return The project source code root.
	 * @since 2017/11/28
	 */
	public final Path root()
	{
		return this.root;
	}
	
	/**
	 * The source manifest.
	 *
	 * @return The source manifest.
	 * @since 2017/11/17
	 */
	public final JavaManifest sourceManifest()
	{
		return this.manifest;
	}
	
	/**
	 * Returns the suite information for this source project.
	 *
	 * @return The suite information to use.
	 * @throws InvalidSourceException If the source project is not valid.
	 * @since 2017/12/04
	 */
	public final SuiteInfo suiteInfo()
		throws InvalidSourceException
	{
		Reference<SuiteInfo> ref = this._suiteinfo;
		SuiteInfo rv;
		
		if (ref == null || null == (rv = ref.get()))
			try
			{
				this._suiteinfo = new WeakReference<>(
					(rv = new SuiteInfo(this.manifest())));
			}
			
			// {@squirreljme.error AU0j Could not approximate the binary
			// suite information. (The name of the project)}
			catch (InvalidSuiteException e)
			{
				throw new InvalidSourceException(
					String.format("AU0j %s", this.name), e);
			}
		
		return rv;
	}
}

