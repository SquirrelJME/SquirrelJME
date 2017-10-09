// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.SourceCompiler;
import net.multiphasicapps.squirreljme.build.base.SourceCompilerProvider;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.java.manifest.mutable.
	MutableJavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.mutable.
	MutableJavaManifestAttributes;
import net.multiphasicapps.squirreljme.midlet.MidletSuiteID;
import net.multiphasicapps.squirreljme.midlet.MidletSuiteName;
import net.multiphasicapps.squirreljme.midlet.MidletSuiteVendor;
import net.multiphasicapps.squirreljme.midlet.MidletVersion;
import net.multiphasicapps.squirreljme.midlet.ServiceSuiteID;
import net.multiphasicapps.util.empty.EmptySet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This represents the base for the class which represents the source code
 * of a given project.
 *
 * @since 2016/12/14
 */
public abstract class ProjectSource
	extends ProjectBase
{
	/** The property used for dependencies. */
	private static final JavaManifestKey _DEPENDS_PROPERTY =
		new JavaManifestKey("X-SquirrelJME-Depends");
	
	/** Name of the project. */
	static final JavaManifestKey _SOURCE_NAME_KEY =
		new JavaManifestKey("X-SquirrelJME-Name");
	
	/** Vendor of the project. */
	static final JavaManifestKey _SOURCE_VENDOR_KEY =
		new JavaManifestKey("X-SquirrelJME-Vendor");
	
	/** Version of the project. */
	static final JavaManifestKey _SOURCE_VERSION_KEY =
		new JavaManifestKey("X-SquirrelJME-Version");
	
	/** Classic main class key. */
	static final JavaManifestKey _MAIN_CLASS_KEY =
		new JavaManifestKey("Main-Class");
	
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/** Dependencies of this project. */
	private volatile Reference<Set<Project>> _depends;
	
	/** The suite identifier. */
	private volatile Reference<MidletSuiteID> _midlet;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __pr The project owning this.
	 * @param __fp The path to the source code.
	 * @throws IOException On read errors.
	 * @since 2016/12/14
	 */
	ProjectSource(Project __pr, Path __fp)
		throws IOException
	{
		super(__pr, __fp);
		
		// Load the manifest
		JavaManifest manifest = ProjectManager.__readManifest(
			this.path.resolve("META-INF").resolve("MANIFEST.MF"));
		this.manifest = manifest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/20
	 */
	@Override
	public final JavaManifest binaryManifest()
		throws InvalidProjectException
	{
		// Need temporary output, does not matter where
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Generate manifest
			__generateManifest(binaryDependencies(true), baos);
			
			// Flush to make sure it gets written
			baos.flush();
			
			// Parse the generated manifest
			try (ByteArrayInputStream bais = new ByteArrayInputStream(
				baos.toByteArray()))
			{
				return new JavaManifest(bais);
			}
		}
		
		// {@squirreljme.error AT0h Could not create the binary manifest.}
		catch (JavaManifestException|IOException e)
		{
			throw new InvalidProjectException("AT0h", e);
		}
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
		
		// Since dependencies are very much fixed, just cache them so that
		// it does not need to be calculated/checked every time
		Reference<Set<Project>> ref = this._depends;
		Set<Project> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			rv = new LinkedHashSet<>();
			
			// Parse configuration/profile to auto-depend on CLDC and/or
			// MIDP required APIs
			__commonDependencies(__out);
			
			// Parse dependency property
			String attr = this.manifest.getMainAttributes().
				get(_DEPENDS_PROPERTY);
			if (attr != null)
			{
				// Decode using spaces
				ProjectManager pm = projectManager();
				NamespaceType nt = type();
				for (int i = 0, n = attr.length(); i < n; i++)
				{
					// Get the net space to find the current name
					int ls = attr.indexOf(' ', i);
					if (ls < 0)
						ls = n;
					
					// Split project name
					ProjectName name = new ProjectName(attr.substring(i, ls));
					
					// {@squirreljme.error AT0l The project depends on another
					// project which does not exist. (The name of this project;
					// The missing project it depends on)}
					Project p = pm.get(name);
					if (p == null)
						throw new InvalidProjectException(String.format(
							"AT0l %s %s", name(), name));
					
					// {@squirreljme.error AT0m This project cannot depend on
					// the other project which is of the specified type. (The
					// name of this project; The type of this project; The
					// name of the project this depends on; The type of the
					// dependended of project)}
					NamespaceType ot = p.type();
					if (!nt.canDependOn(ot))
						throw new InvalidProjectException(String.format(
							"AT0m %s %s %s %s", name(), nt, name, ot));
					
					// Depends on it
					rv.add(p);
					
					// Skip to next dependency
					i = ls;
				}
			}
			
			// Cache it
			this._depends = new WeakReference<>(rv);
		}
		
		// Fill projects
		for (Project p : rv)
			__out.add(p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public final JavaManifest manifest()
		throws InvalidProjectException
	{
		return this.manifest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/21
	 */
	@Override
	public MidletSuiteID suiteId()
		throws InvalidProjectException
	{
		Reference<MidletSuiteID> ref = this._midlet;
		MidletSuiteID rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// {@squirreljme.error AT0r This project is missing the project
			// name key. (The name of this project)}
			String name = attr.get(_SOURCE_NAME_KEY);
			if (name == null)
				throw new InvalidProjectException(String.format("AT0r %s",
					name()));
			
			// {@squirreljme.error AT0s This project is missing the project
			// vendor key. (The name of this project)}
			String vendor = attr.get(_SOURCE_VENDOR_KEY);
			if (vendor == null)
				throw new InvalidProjectException(String.format("AT0s %s",
					name()));
			
			// {@squirreljme.error AT0t This project is missing the project
			// version key. (The name of this project)}
			String version = attr.get(_SOURCE_VERSION_KEY);
			if (version == null)
				throw new InvalidProjectException(String.format("AT0t %s",
					name()));
			
			// Generate
			this._midlet = new WeakReference<>((rv = new MidletSuiteID(
				new MidletSuiteName(name), new MidletSuiteVendor(vendor),
				new MidletVersion(version))));
		}
		
		return rv;
	}
	
	/**
	 * Compiles the source code for this project.
	 *
	 * @param __dest The destination path where the binary should be placed.
	 * @param __deps The binary dependencies needed for it to compile.
	 * @throws InvalidProjectException If the project could not be compiled.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/19
	 */
	final void __compile(Path __dest, Set<ProjectBinary> __deps)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__dest == null || __deps == null)
			throw new NullPointerException("NARG");
		
		// Locate compiler
		SourceCompiler sc;
		try
		{
			sc = SourceCompilerProvider.newInstance();
		}
		
		// {@squirreljme.error AT08 Could not compile the project because
		// no compiler is available.}
		catch (RuntimeException e)
		{
			throw new InvalidProjectException("AT08", e);
		}
		
		// Need to access directories during compilation
		Path tempjar = null;
		try (FileDirectory fd = openFileDirectory();
			__CloseableList__<FileDirectory> bds = new __CloseableList__<>())
		{
			// Need a place to store files and where to write the output JAR
			String sname = name().toString();
			tempjar = Files.createTempFile(sname, "-build.jar");
			
			// Source code is here
			sc.setSourceDirectory(fd);
			
			// Load dependency into directories
			for (ProjectBinary db : __deps)
			{
				// Load
				FileDirectory bfd = db.openFileDirectory();
				bds.add(bfd);
				
				// Set where classes exist
				sc.addClassDirectory(bfd);
			}
			
			// Add files to be compiled
			for (String s : fd)
				if (__isValidSource(s))
					sc.addSource(s);
			
			// Target Java 7 as usual, with debugging as needed and
			// add some other options
			sc.setCompileOptions("-source", "1.7", "-target", "1.7", "-g",
				"-Xlint:deprecation", "-Xlint:unchecked");
			
			// Need to package the binary
			try (final ZipStreamWriter zsw = new ZipStreamWriter(
				Channels.newOutputStream(FileChannel.open(tempjar,
				StandardOpenOption.WRITE))))
			{
				// Write the manifest to the output
				try (OutputStream os = zsw.nextEntry("META-INF/MANIFEST.MF"))
				{
					__generateManifest(__deps, os);
				}
				
				// Set output to write to this ZIP
				sc.setOutput(new __CompilerOutput__(zsw));
				
				// {@squirreljme.error AT0a Failed to compile the given source
				// project. (The name of this project)}
				if (!sc.compile())
					throw new InvalidProjectException(String.format("AT0a %s",
						sname));
				
				// Add non-valid source files from the input source dir
				__copyNonValid(fd, zsw);
				
				// Finish off
				zsw.flush();
			}
			
			// Make sure the destination directory where binaries go exists
			Path par = __dest.getParent();
			if (par != null)
				Files.createDirectories(par);
			
			// Replace the JAR
			Files.move(tempjar, __dest, StandardCopyOption.REPLACE_EXISTING);
		}
		
		// Always clear temporary files
		finally
		{
			// Delete temporary JAR if it exists
			if (tempjar != null)
				try
				{
					Files.delete(tempjar);
				}
				catch (IOException e)
				{
					// Ignore
				}
		}
	}
	
	/**
	 * Generates the manifest used in the output file.
	 *
	 * @param __deps The dependencies for this project.
	 * @param __os Where the manifest is to be written.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/26
	 */
	private final void __generateManifest(Set<ProjectBinary> __deps,
		OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__deps == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Setup output with copies of all the inputs
		MutableJavaManifest man = new MutableJavaManifest(manifest());
		MutableJavaManifestAttributes attr = man.getMainAttributes();
		
		// Write midlet details
		boolean midlet = (type() == NamespaceType.MIDLET);
		if (type() != NamespaceType.API)
		{
			// Just check if it has the given key
			if (midlet)
			{
				// {@squirreljme.error AT0q The MIDlet is missing the required
				// {@code MIDlet-1} property and does not have a fallback
				// {@code Main-Class} property.}
				if (null == attr.get(_FIRST_MIDLET) &&
					null == attr.get(_MAIN_CLASS_KEY))
					throw new IOException("AT0q");
			}
			
			// Write the name, vendor, and version
			MidletSuiteID msid = suiteId();
			attr.put((midlet ? _MIDLET_NAME : _LIBLET_NAME),
				msid.name().toString());
			attr.put((midlet ? _MIDLET_VENDOR : _LIBLET_VENDOR),
				msid.vendor().toString());
			attr.put((midlet ? _MIDLET_VERSION : _LIBLET_VERSION),
				msid.version().toString());
		}
		
		// Go through dependencies and generate liblet/midlet dependencies
		int depcount = 1;
		for (ProjectBinary bp : __deps)
		{
			String value;
			
			// Depends on an API, refer to it by its special API identifier
			// if applicable since it does not exist as a liblet
			if (bp.type() == NamespaceType.API)
			{
				// This might be an API but there might not be any service to
				// implement so ignore it
				ServiceSuiteID ssid = bp.serviceId();
				if (ssid == null)
					continue;
				
				// Format service
				MidletSuiteVendor ven = ssid.vendor();
				MidletVersion ver = ssid.version();
				value = String.format("standard;required;%s;%s;%s%s",
					ssid.name(), (ven != null ? ven : ""),
					(ver != null ? ver : ""), (ver != null ? "+" : ""));
			}
			
			// Depends on a liblet
			else
			{
				MidletSuiteID msid = bp.suiteId();
				value = String.format("liblet;required;%s;%s;%s+",
					msid.name(), msid.vendor(), msid.version());
			}
			
			// Build target key for value placement then write value
			JavaManifestKey key = new JavaManifestKey(
				String.format("%s-Dependency-%d",
				(midlet ? "MIDlet" : "LIBlet"), depcount++));
			attr.put(key, value);
		}
		
		// Write it
		man.write(__os);
	}
	
	/**
	 * Copies non-valid files from the input directory to the output ZIP.
	 *
	 * @param __in The input file source.
	 * @param __zsw The output where files are to be copied.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	private static void __copyNonValid(FileDirectory __in,
		ZipStreamWriter __zsw)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null || __zsw == null)
			throw new NullPointerException("NARG");
		
		// Copy all files
		byte[] buf = new byte[512];
		for (String s : __in)
			if (!__isValidSource(s) && !s.equals("META-INF/MANIFEST.MF"))
				try (InputStream is = __in.open(s);
					OutputStream os = __zsw.nextEntry(s))
				{
					for (;;)
					{
						int rc = is.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						// Write
						os.write(buf, 0, rc);
					}
				}
	}
	
	/**
	 * Is this character valid in a Java identifier?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is valid.
	 * @since 2016/12/26
	 */
	private static boolean __isValidChar(char __c)
	{
		return (__c >= 'A' && __c <= 'Z') || (__c >= 'a' && __c <= 'z') ||
			(__c >= '0' && __c <= '9') || __c == '_' || __c == '$';
	}
	
	/**
	 * Checks whether this file is a valid source code name.
	 *
	 * @param __s The source file name to check.
	 * @return {@code true} if the source code is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	private static boolean __isValidSource(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Must be source code
		if (!__s.endsWith(".java"))
			return false;
		
		// Determine base and directory name
		int ls = __s.lastIndexOf('/');
		String dir = (ls < 0 ? "" : __s.substring(0, ls));
		String fn = (ls < 0 ? __s : __s.substring(ls + 1));
		
		// All directory characters must be valid.
		for (int i = 0, n = dir.length(); i < n; i++)
		{
			char c = dir.charAt(i);
			if (c != '/' && !__isValidChar(c))
				return false;
		}
		
		// Force package info to be valid
		if (fn.equals("package-info.java"))
			return true;
		
		// Check everything except the extension
		for (int i = 0, n = fn.length() - 5; i < n; i++)
			if (!__isValidChar(fn.charAt(i)))
				return false;
		
		// Is valid
		return true;
	}
	
	/**
	 * Calculates the name that a file would appear as inside of a ZIP file.
	 *
	 * @param __root The root path.
	 * @param __p The file to add.
	 * @return The ZIP compatible name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private static String __zipName(Path __root, Path __p)
		throws NullPointerException
	{
		// Check
		if (__root == null || __p == null)
			throw new NullPointerException();
		
		// Calculate relative name
		Path rel = __root.toAbsolutePath().relativize(__p.toAbsolutePath());
		
		// Build name
		StringBuilder sb = new StringBuilder();
		for (Path comp : rel)
		{
			// Prefix slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp);
		}
		
		// Return it
		return sb.toString();
	}
	
	/**
	 * Consumes a value which may throw an exception.
	 *
	 * @since 2016/10/27
	 */
	private static interface __Consumer__
	{
		/**
		 * Accepts a value.
		 *
		 * @param __v The given path.
		 * @throws IOException On read/write errors.
		 * @since 2016/10/27
		 */
		public abstract void accept(Path __v)
			throws IOException;
	}
}

