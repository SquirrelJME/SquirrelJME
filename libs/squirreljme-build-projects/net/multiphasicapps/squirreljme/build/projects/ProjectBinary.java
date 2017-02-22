// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteID;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteName;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteVendor;
import net.multiphasicapps.squirreljme.suiteid.MidletVersion;

/**
 * This represents the base for a binary project which is a compiled form
 * of an existing project.
 *
 * @since 2016/12/14
 */
public abstract class ProjectBinary
	extends ProjectBase
{
	/** The manifest for this binary. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** The suite identifier. */
	private volatile Reference<MidletSuiteID> _suiteid;
	
	/**
	 * Initializes the project binary.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the project's binary.
	 * @throws IOException If the specified path is not valid for a binary.
	 * @since 2016/12/17
	 */
	ProjectBinary(Project __p, Path __fp)
		throws IOException
	{
		super(__p, __fp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/20
	 */
	@Override
	public JavaManifest binaryManifest()
		throws InvalidProjectException
	{
		// The binary manifest is the manifest
		return manifest();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/18
	 */
	@Override
	public final void dependencies(Set<Project> __out)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Need these for parsing
		JavaManifestAttributes attr = manifest().getMainAttributes();
		
		// Add common dependencies
		__commonDependencies(__out);
		
		// Parse Xlet-Dependency-# fields
		boolean midlet = (type() == NamespaceType.MIDLET);
		for (int count = 1; count < Integer.MAX_VALUE; count++)
		{
			// There must be a key, if it is missing then stop
			String val = attr.get(new JavaManifestKey(String.format(
				"%s-Dependency-%d", (midlet ? "MIDlet" : "LIBlet"), count)));
			if (val == null)
				break;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public final JavaManifest manifest()
		throws InvalidProjectException
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		// Needs loading?
		if (ref == null || null == (rv = ref.get()))
			try (FileDirectory fd = openFileDirectory();
				InputStream is = fd.open("META-INF/MANIFEST.MF"))
			{
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(is)));
			}
		
			// {@squirreljme.error AT0d Could not parse the manifest for
			// the given project. (The project name)}
			catch (JavaManifestException|IOException e)
			{
				throw new InvalidProjectException(String.format("AT0d %s",
					name()), e);
			}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/21
	 */
	@Override
	public MidletSuiteID suiteId()
		throws InvalidProjectException
	{
		Reference<MidletSuiteID> ref = this._suiteid;
		MidletSuiteID rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// This a midlet?
			boolean midlet = (type() == NamespaceType.MIDLET);
			
			// {@squirreljme.error AT0u This project is missing the project
			// name key. (The name of this project)}
			String name = attr.get((midlet ? _MIDLET_NAME : _LIBLET_NAME));
			if (name == null)
				throw new InvalidProjectException(String.format("AT0u %s",
					name()));
			
			// {@squirreljme.error AT0v This project is missing the project
			// vendor key. (The name of this project)}
			String vendor = attr.get((midlet ? _MIDLET_VENDOR :
				_LIBLET_VENDOR));
			if (vendor == null)
				throw new InvalidProjectException(String.format("AT0v %s",
					name()));
			
			// {@squirreljme.error AT0w This project is missing the project
			// version key. (The name of this project)}
			String version = attr.get((midlet ? _MIDLET_VERSION :
				_LIBLET_VERSION));
			if (version == null)
				throw new InvalidProjectException(String.format("AT0w %s",
					name()));
			
			// Generate
			this._suiteid = new WeakReference<>((rv = new MidletSuiteID(
				new MidletSuiteName(name), new MidletSuiteVendor(vendor),
				new MidletVersion(version))));
		}
		
		return rv;
	}
}

