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

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.suiteid.APIConfiguration;
import net.multiphasicapps.squirreljme.suiteid.MidletDependency;
import net.multiphasicapps.squirreljme.suiteid.MidletDependencyType;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteID;
import net.multiphasicapps.squirreljme.suiteid.ServiceSuiteID;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This acts as the common base for binary and source projects.
 *
 * @since 2016/12/17
 */
public abstract class ProjectBase
{
	/** The key used for the configuration. */
	static final JavaManifestKey _CONFIGURATION_KEY =
		new JavaManifestKey("MicroEdition-Configuration");
	
	/** The profile the midlet/liblet requires. */
	static final JavaManifestKey _PROFILE_KEY =
		new JavaManifestKey("MicroEdition-Profile");
	
	/** Configuration support key. */
	static final JavaManifestKey _SUPPORT_CONFIGURATIONS_KEY =
		new JavaManifestKey("X-SquirrelJME-DefinesConfigurations");
	
	/** Does not depend on a configuration? */
	static final JavaManifestKey _NO_DEPENDS_CONFIGURATION_KEY =
		new JavaManifestKey("X-SquirrelJME-NoDependsConfiguration");
	
	/** Standard supported. */
	static final JavaManifestKey _STANDARD_KEY =
		new JavaManifestKey("X-SquirrelJME-Standard");
	
	/** Groups the project si in. */
	static final JavaManifestKey _GROUP_KEY =
		new JavaManifestKey("X-SquirrelJME-Group");
	
	/** The first MIDlet key. */
	static final JavaManifestKey _FIRST_MIDLET =
		new JavaManifestKey("MIDlet-1");
	
	/** Liblet name. */
	static final JavaManifestKey _LIBLET_NAME =
		new JavaManifestKey("LIBlet-Name");
	
	/** Liblet vendor. */
	static final JavaManifestKey _LIBLET_VENDOR =
		new JavaManifestKey("LIBlet-Vendor");
	
	/** Liblet version. */
	static final JavaManifestKey _LIBLET_VERSION =
		new JavaManifestKey("LIBlet-Version");
	
	/** Midlet name. */
	static final JavaManifestKey _MIDLET_NAME =
		new JavaManifestKey("MIDlet-Name");
	
	/** Midlet vendor. */
	static final JavaManifestKey _MIDLET_VENDOR =
		new JavaManifestKey("MIDlet-Vendor");
	
	/** Midlet version. */
	static final JavaManifestKey _MIDLET_VERSION =
		new JavaManifestKey("MIDlet-Version");
	
	/** The earliest date. */
	private static final FileTime _EARLIEST_DATE =
		FileTime.fromMillis(Long.MIN_VALUE);
	
	/** The owning project. */
	protected final Project project;
	
	/** The path to the source or binary. */
	protected final Path path;
	
	/** The current project time. */
	private volatile long _time =
		Long.MIN_VALUE;
	
	/** Supported configurations. */
	private volatile Reference<Collection<APIConfiguration>> _configs;
	
	/** The service ID. */
	private volatile Reference<ServiceSuiteID> _serviceid;
	
	/** Groups the project is in. */
	private volatile Reference<Set<String>> _groups;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	ProjectBase(Project __p, Path __fp)
		throws NullPointerException
	{
		// Check
		if (__p == null || __fp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.project = __p;
		this.path = __fp;
	}
	
	/**
	 * Returns the binary manifest for this project based.
	 *
	 * @return The binary manifest for this project.
	 * @throws InvalidProjectException If the manifest could not be generated.
	 * @since 2017/01/20
	 */
	public abstract JavaManifest binaryManifest()
		throws InvalidProjectException;
	
	/**
	 * Returns the dependencies that this project relies on for running and
	 * compilation.
	 *
	 * @param __out The output set where dependencies are placed.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/18
	 */
	public abstract void dependencies(Set<Project> __out)
		throws InvalidProjectException, NullPointerException;
	
	/**
	 * Returns the manifest that is used for this project.
	 *
	 * @return The manifest for this project.
	 * @throws InvalidProjectException If the manifest could not be read or
	 * is not valid.
	 * @since 2016/12/27
	 */
	public abstract JavaManifest manifest()
		throws InvalidProjectException;
	
	/**
	 * Returns the Suite ID of this project.
	 *
	 * @return The suite ID.
	 * @throws InvalidProjectException If it is not valid.
	 * @since 2017/02/21
	 */
	public abstract MidletSuiteID suiteId()
		throws InvalidProjectException;
	
	/**
	 * Obtains the binary projects which this binary project depends on and
	 * places them into the specified set.
	 *
	 * @param __out The output set where dependencies are placed.
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/18
	 */
	public final void binaryDependencies(Set<ProjectBinary> __out,
		boolean __rec)
		throws InvalidProjectException, NullPointerException
	{
		__internalBinaryDependencies(null, __out, __rec);
	}
		
	/**
	 * Obtains the binary projects which this binary project depends on.
	 *
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @return The binary dependencies.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @since 2016/12/17
	 */
	public final Set<ProjectBinary> binaryDependencies(boolean __rec)
		throws InvalidProjectException
	{
		Set<ProjectBinary> rv = new LinkedHashSet<>();
		binaryDependencies(rv, __rec);
		return rv;
	}
	
	/**
	 * Returns the dependencies of this project.
	 *
	 * @return The project dependencies.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @since 2016/12/18
	 */
	public final Set<Project> dependencies()
		throws InvalidProjectException
	{
		Set<Project> rv = new LinkedHashSet<>();
		dependencies(rv);
		return rv;
	}
	
	/**
	 * Returns the groups the project is in.
	 *
	 * @return The project groups.
	 * @since 2017/03/15
	 */
	public final Set<String> groups()
	{
		Reference<Set<String>> ref = this._groups;
		Set<String> rv;
		
		// Cache
		if (ref == null || null == (rv = ref.get()))
		{
			// Need the manifest
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Setup set
			rv = new LinkedHashSet<>();
			String val = attr.get(_GROUP_KEY);
			if (val != null)
			{
				for (int i = 0, n = val.length(); i < n; i++)
				{
					// Ignore spaces
					if (val.charAt(i) <= ' ')
						continue;
					
					// Find next space
					int nt = val.indexOf(' ', i);
					if (nt < 0)
						nt = n;
					
					// Lowercase
					StringBuilder sb = new StringBuilder();
					for (int j = i; j < nt; j++)
					{
						char c = val.charAt(j);
						
						// Lowercase?
						if (c >= 'A' && c <= 'Z')
							c = (char)((c - 'A') + 'a');
						
						sb.append(c);
					}
					rv.add(sb.toString());
					
					// Space will be skipped
					i = nt;
				}
			}
			
			// Cache
			this._groups = new WeakReference<>(
				(rv = UnmodifiableSet.<String>of(rv)));
		}
		
		return rv;
	}
	
	/**
	 * Checks whether this given project is of the given dependency.
	 *
	 * @param __d The dependency to check.
	 * @return {@code true} if this project is the given dependency.
	 * @throws NullPointerException On null arguments
	 * @since 2017/02/22
	 */
	public final boolean isDependency(MidletDependency __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Nothing may ever depend on a midlet
		NamespaceType nstype = type();
		if (nstype == NamespaceType.MIDLET)
			return false;
		
		// Services may be used by either APIs or LIBlets
		MidletDependencyType deptype = __d.type();
		if (deptype == MidletDependencyType.SERVICE)
		{
			System.err.printf("DEBUG -- Service Check: %s%n", __d);
			throw new todo.TODO();
		}
		
		// Standards are only valid on APIs
		if (nstype == NamespaceType.API)
		{
			// Only depend on standards
			if (deptype != MidletDependencyType.STANDARD)
				return false;
			
			// This might be an API which defines no standards
			ServiceSuiteID s = serviceId();
			if (s == null)
				return false;
			
			// Check
			return __d.isCompatible(s.name(), s.vendor(), s.version());
		}
		
		// Otherwise, everything else can rely on liblets
		else
		{
			// Only depend on liblets
			if (deptype != MidletDependencyType.LIBLET)
				return false;
			
			// Check
			MidletSuiteID s = suiteId();
			return __d.isCompatible(s.name(), s.vendor(), s.version());
		}
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/12/24
	 */
	public final ProjectName name()
	{
		return this.project.name();
	}
	
	/**
	 * Opens the specified project data as a directory tree so that the
	 * compiler and other portions of the code may access files that exist
	 * within projects.
	 *
	 * If the file is a regular file it will be opened as a ZIP file, otherwise
	 * directories will be scanned.
	 *
	 * This method may be overridden if functionality needs to be modified
	 * to provide an alternative directory scheme.
	 *
	 * @return The file directory.
	 * @throws IOException If it could not be opened.
	 * @since 2016/12/21
	 */
	public FileDirectory openFileDirectory()
		throws IOException
	{
		// If a directory open it as one
		Path p = this.path;
		if (Files.isDirectory(p))
			return new __DirectoryFileDirectory__(p);
		
		// Otherwise treat as a ZIP
		FileChannel fc = null;
		try
		{
			// Open the file
			fc = FileChannel.open(p, StandardOpenOption.READ);
			
			// Open as a ZIP
			return new __ZipFileDirectory__(new ZipBlockReader(
				new FileChannelBlockAccessor(fc)));
		}
		
		// Failed to open, make sure the channel gets closed
		catch (RuntimeException|IOException|Error e)
		{
			// Close the file channel
			if (fc != null)
				try
				{
					fc.close();
				}
				catch (Exception f)
				{
					e.addSuppressed(f);
				}
			
			// Rethrow
			throw e;
		}
	}
	
	/**
	 * Returns the path to the file.
	 *
	 * @return The file path.
	 * @since 2016/12/17
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Returns the owning project manager.
	 *
	 * @return The project manager.
	 * @since 2017/01/21
	 */
	public final ProjectManager projectManager()
	{
		return this.project.projectManager();
	}
	
	/**
	 * This returns the service ID of this project, this is optional and is
	 * only valid for APIs.
	 *
	 * @return The service identifier or {@code null} if it is not specified.
	 * @throws InvalidProjectException If the service identifier is not valid.
	 * @since 2017/02/22
	 */
	public final ServiceSuiteID serviceId()
		throws InvalidProjectException
	{
		// Must be an API
		if (type() != NamespaceType.API)
			return null;
		
		Reference<ServiceSuiteID> ref = this._serviceid;
		ServiceSuiteID rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Get the manifest
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Needs to have the standard field
			String val = attr.get(_STANDARD_KEY);
			if (val == null)
				return null;
			
			// Parse it
			try
			{
				this._serviceid = new WeakReference<>(
					(rv = new ServiceSuiteID(val)));
			}
			
			// {@squirreljme.error AT0p Could not parse the supplied service
			// identifier field.}
			catch (IllegalArgumentException e)
			{
				throw new InvalidProjectException("AT0p", e);
			}
		}
		
		return rv;
	}
	
	/**
	 * Returns the configurations which are supported by this project.
	 *
	 * @return The array of supported configurations.
	 * @since 2017/01/21
	 */
	public final APIConfiguration[] supportedConfigurations()
	{
		// Only APIs may support configurations
		if (type() != NamespaceType.API)
			return new APIConfiguration[0];
		
		// Need to parse supported configuration
		Reference<Collection<APIConfiguration>> configs = this._configs;
		Collection<APIConfiguration> rv;
		
		// Cache?
		if (configs == null || null == (rv = configs.get()))
		{
			// Setup
			rv = new LinkedHashSet<>();
			
			// Get vlaue
			String val = manifest().getMainAttributes().
				get(_SUPPORT_CONFIGURATIONS_KEY);
			if (val != null)
				for (int i = 0, n = val.length(); i < n; i++)
				{
					// Ignore spaces
					if (val.charAt(i) == ' ')
						continue;
					
					// Get next space position
					int ns = val.indexOf(' ', i);
					if (ns < 0)
						ns = n;
					
					// Add API
					rv.add(new APIConfiguration(val.substring(i, ns)));
					i = ns;
				}
			
			// Cache
			this._configs = new WeakReference<>(rv);
		}
		
		// Return copy
		return rv.<APIConfiguration>toArray(new APIConfiguration[
			rv.size()]);
	}
	
	/**
	 * Checks whether this project supports the given configuration.
	 *
	 * @param __c The configuration to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	public final boolean supportsConfiguration(APIConfiguration __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Only APIs may support configurations
		if (type() != NamespaceType.API)
			return false;
		
		// Only if it supports it
		for (APIConfiguration a : supportedConfigurations())
			return a.equals(__c);
		
		// Not supported
		return false;
	}
	
	/**
	 * Recursively determines the time and date of the project base.
	 *
	 * @return The time that the binary or source code last changed in
	 * milliseconds.
	 * @since 2016/12/17
	 */
	public final long time()
	{
		// Use precached time
		long rv = this._time;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Otherwise scan
		try
		{
			this._time = (rv = __recursiveDate(this.path).toMillis());
			return rv;
		}
		
		// {@squirreljme.error AT0k Could not determine the project time.}
		catch (IOException e)
		{
			throw new InvalidProjectException("AT0k", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/22
	 */
	@Override
	public String toString()
	{
		return type() + ":" + name();
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2016/12/19
	 */
	public final NamespaceType type()
	{
		return this.project.type();
	}
	
	/**
	 * Decodes the configuration and profile settings to determine which
	 * projects should be used for inclusion.
	 *
	 * @param __to The target set.
	 * @throws InvalidProjectException If the project is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/20
	 */
	final void __commonDependencies(Set<Project> __to)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__to == null)
			throw new NullPointerException("NARG");
		
		// Obtain the manifest, this is needed to get the configuration and
		// profile settings
		JavaManifest man = manifest();
		JavaManifestAttributes attr = man.getMainAttributes();
		
		// Get configuration required to run
		// However, CLDC-Compact needs a special configuration which allows it
		// to not have to depend on itself
		String conf = attr.get(_CONFIGURATION_KEY);
		if (conf == null &&
			!Boolean.valueOf(attr.get(_NO_DEPENDS_CONFIGURATION_KEY)))
		{
			// {@squirreljme.error AT0i The project is missing the required
			// {@code MicroEdition-Configuration} key in its manifest. (The
			// name of the project)}
			throw new InvalidProjectException(String.format("AT0i %s",
				name()));
		}
		
		// Special class is used
		if (conf != null)
		{
			APIConfiguration aconf = new APIConfiguration(conf.trim());
		
			// Go through projects and find a project which implements the
			// given configuration
			for (Project p : projectManager())
			{
				// Only consider APIs
				ProjectBase pb = p.__mostUpToDate();
				if (pb.type() != NamespaceType.API)
					continue;
			
				// If the configuration is supported, use it
				if (pb.supportsConfiguration(aconf))
					__to.add(p);
			}
		}
		
		// Decode profile
		String prof = attr.get(_PROFILE_KEY);
		if (prof != null)
			throw new todo.TODO();
	}
	
	/**
	 * Obtains the binary projects which this binary project depends on and
	 * places them into the specified set.
	 *
	 * @param __did The projects which were processed so that they are not
	 * handled multiple times.
	 * @param __out The output set where dependencies are placed.
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException If no output was specified
	 * @since 2016/12/18
	 */
	private final void __internalBinaryDependencies(Set<Project> __did,
		Set<ProjectBinary> __out, boolean __rec)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Must always exist
		if (__did == null)
			__did = new HashSet<>();
		
		// Go through dependencies
		for (Project p : dependencies())
		{
			// Ignore if already handled
			if (__did.contains(p))
				continue;
			__did.add(p);
			
			// Add binary to output
			ProjectBinary bin = p.binary();
			__out.add(bin);
			
			// Get dependencies of those depends
			if (__rec)
				((ProjectBase)bin).
					__internalBinaryDependencies(__did, __out, true);
		}
	}
	
	/**
	 * Walks the directory tree and returns the highest modification date.
	 *
	 * @param __p The path to search through.
	 * @return The highest modification time.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private static FileTime __recursiveDate(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Start at the earliest date
		FileTime rv = _EARLIEST_DATE;
		
		// If just a normal file get the time for it
		if (!Files.isDirectory(__p))
			return Files.getLastModifiedTime(__p);
		
		// Look at all entries
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
		{
			for (Path p : ds)
			{
				FileTime tt;
				if (Files.isDirectory(p))
					tt = __recursiveDate(p);
				else
					tt = Files.getLastModifiedTime(p);
				
				// Use this date instead?
				if (tt.compareTo(rv) > 0)
					rv = tt;
			}
		}
		
		// Return the latest
		return rv;
	}
}

