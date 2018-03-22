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

import cc.squirreljme.kernel.suiteinfo.InvalidSuiteException;
import cc.squirreljme.kernel.suiteinfo.SuiteInfo;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.javac.ByteArrayCompilerInput;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.MergedPathSet;
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
public abstract class Source
{
	/** The name of this project. */
	protected final SourceName name;
	
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
	 * Initializes the base source.
	 *
	 * @param __n The name of the project.
	 * @param __t The type of project this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public Source(SourceName __n, ProjectType __t)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.type = __t;
	}
	
	/**
	 * Returns the input set which makes up the source project, internally
	 * as the end result will be wrapped to add some special meta-files if
	 * needed.
	 *
	 * @param __root Should only the root be considered?
	 * @return The input set.
	 * @since 2018/03/06
	 */
	protected abstract CompilerPathSet internalPathSet(boolean __root);
	
	/**
	 * The source manifest.
	 *
	 * @return The source manifest.
	 * @since 2017/11/17
	 */
	public abstract JavaManifest sourceManifest();
	
	/**
	 * Returns the time that the source code was last modified.
	 *
	 * @return The last modification date of the source code.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public final long lastModifiedTime()
	{
		// Could be pre-cached
		long rv = this._lastmodtime;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Go through all input and compare the modified times
		try (CompilerPathSet ps = this.internalPathSet(true))
		{
			rv = Long.MIN_VALUE;
			for (CompilerInput ci : ps)
			{
				long now = ci.lastModifiedTime();
				if (now > rv)
					rv = now;
			}
		}
		
		// Cache for next time
		this._lastmodtime = rv;
		return rv;
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
	 * Returns the path set to use for the source code.
	 *
	 * @return The input set.
	 * @since 2018/03/06
	 */
	public final CompilerPathSet pathSet()
	{
		return this.internalPathSet(false);
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

