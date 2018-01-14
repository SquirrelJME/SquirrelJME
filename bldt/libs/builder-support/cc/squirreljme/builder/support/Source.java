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

import cc.squirreljme.kernel.lib.client.DependencyInfo;
import cc.squirreljme.kernel.lib.client.InvalidSuiteException;
import cc.squirreljme.kernel.lib.client.ProvidedInfo;
import cc.squirreljme.kernel.lib.client.SuiteInfo;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import net.multiphasicapps.strings.StringUtils;
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
		
		// Load manifest
		try (InputStream in = Files.newInputStream(__p.resolve("META-INF").
			resolve("MANIFEST.MF"), StandardOpenOption.READ))
		{
			this.manifest = new JavaManifest(in);
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

