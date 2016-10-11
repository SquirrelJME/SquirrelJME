// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This contains information about a single binary or source project.
 *
 * @since 2016/06/15
 */
public class ProjectInfo
	implements Comparable<ProjectInfo>
{
	/** The earliest date. */
	private static final FileTime _EARLIEST_DATE =
		FileTime.fromMillis(Long.MIN_VALUE);
	
	/** The owning project list. */
	protected final ProjectList plist;
	
	/** The project manifest. */
	protected final JavaManifest manifest;
	
	/** The path to the project. */
	protected final Path path;
	
	/** Is this a ZIP file? */
	protected final boolean iszip;
	
	/** The name of this project. */
	protected final ProjectName name;
	
	/** Is this a binary project? */
	protected final boolean isbinary;
	
	/** Package groups this project is in. */
	private volatile Reference<Set<String>> _groups;
	
	/** The dependencies of this project. */
	private volatile Reference<Set<ProjectName>> _depends;
	
	/** Optional dependencies of this project. */
	private volatile Reference<Set<ProjectName>> _odepends;
	
	/** Cached date. */
	private volatile Reference<FileTime> _date;
	
	/** Cached contents. */
	private volatile Reference<Collection<String>> _contents;
	
	/** Opened ZIP file. */
	private volatile __OpenZip__ _ozip;
	
	/**
	 * Initializes the project information from the given ZIP.
	 *
	 * @param __l The project list which contains this project.
	 * @param __p The path to the project.
	 * @param __zip The ZIP file for the project data.
	 * @param __bin Is this a binary project?
	 * @throws InvalidProjectException If this is not a valid project.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	ProjectInfo(ProjectList __l, Path __p, ZipFile __zip, boolean __bin)
		throws InvalidProjectException, IOException, NullPointerException
	{
		this(__l, __p, true, __loadManifestFromZIP(__zip), __bin);
	}
	
	/**
	 * Initializes the project information using the given manifest.
	 *
	 * @param __l The project list which contains this project.
	 * @param __p The path to the project.
	 * @param __zz Is this a ZIP file?
	 * @param __man The manifest data.
	 * @param __bin Is this a binary project?
	 * @throws InvalidProjectException If this is not a valid project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	ProjectInfo(ProjectList __l, Path __p, boolean __zz,
		JavaManifest __man, boolean __bin)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__l == null || __p == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __l;
		this.path = __p;
		this.iszip = __zz;
		this.manifest = __man;
		this.isbinary = __bin;
		
		// Get main attributes
		JavaManifestAttributes main = __man.getMainAttributes();
		
		// {@squirreljme.error CI04 The project manifest does not specify the
		// project name, it is likely not a project. (The path to the project)}
		String rname = main.get("X-SquirrelJME-Name");
		if (rname == null)
			throw new InvalidProjectException(String.format("CI04 %s", __p));
		
		// Set name
		this.name = new ProjectName(rname);
	}
	
	/**
	 * Returns the contents of this project.
	 *
	 * @return The project contents.
	 * @throws IOException On read errors.
	 * @since 2016/09/19
	 */
	public final Collection<String> contents()
		throws IOException
	{
		// Get
		Reference<Collection<String>> ref = this._contents;
		Collection<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Resulting set
			Set<String> into = new LinkedHashSet<>();
			
			// Get contents from the ZIP?
			Path path = this.path;
			if (this.iszip)
				try (ZipFile zip = ZipFile.open(FileChannel.open(path,
						StandardOpenOption.READ)))
				{
					for (ZipEntry e : zip)
						into.add(e.name());
				}
			
			// Get contents from the directory tree
			else
				__recursiveWalk(into, path, path);
			
			// Lock
			rv = UnmodifiableSet.<String>of(into);
			this._contents = new WeakReference<>(rv);
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public final int compareTo(ProjectInfo __pi)
		throws NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// Compare by name
		return this.name.compareTo(__pi.name);
	}
	
	/**
	 * Returns the date of the project's binary file or source tree.
	 *
	 * @return The date of the project.
	 * @throws InvalidProjectException If the date could not be read.
	 */
	public final FileTime date()
		throws InvalidProjectException
	{
		try
		{
			// Get
			Reference<FileTime> ref = this._date;
			FileTime rv;
			
			// Cache?
			if (ref == null || null == (rv = ref.get()))
			{
				// If not a directory, get the time of the file
				Path root = this.path;
				if (!Files.isDirectory(root))
					rv = Files.getLastModifiedTime(root);
				
				// Otherwise browse through all of them
				else
					rv = __recursiveDate(root);
				
				// Cache it
				this._date = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
		
		// {@squirreljme.error CI08 Could not get the latest modification
		// date of the project.}
		catch (IOException e)
		{
			throw new InvalidProjectException("CI08", e);
		}
	}
	
	/**
	 * Returns all of the projects that this project depends on, only
	 * required dependencies are selected.
	 *
	 * @return The set of projects this project depends on.
	 * @throws MissingDependencyException If a dependency is missing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	public final Set<ProjectName> dependencies(DependencyLookupType __lt)
		throws MissingDependencyException, NullPointerException
	{
		return dependencies(__lt, false);
	}
	
	/**
	 * Returns all of the projects that this project depends on.
	 *
	 * @param __opt Select optional dependencies instead.
	 * @return The set of projects this project depends on. If optional
	 * dependencies are specified, they are only included if they exist.
	 * @throws MissingDependencyException If a dependency is missing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public final Set<ProjectName> dependencies(DependencyLookupType __lt,
		boolean __opt)
		throws MissingDependencyException, NullPointerException
	{
		// Check
		if (__lt == null)
			throw new NullPointerException("NARG");
		
		// Get
		Reference<Set<ProjectName>> ref = (__opt ? this._odepends :
			this._depends);
		Set<ProjectName> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			Set<ProjectName> deps = new SortedTreeSet<>();
			
			// Read the manifest
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Get the dependency field
			String pids = attr.get((__opt ? "X-SquirrelJME-Optional" :
				"X-SquirrelJME-Depends"));
			if (pids != null)
			{
				int n = pids.length();
				for (int i = 0; i < n; i++)
				{
					char c = pids.charAt(i);
					
					// Ignore whitespace
					if (c <= ' ')
						continue;
					
					// Find the next space
					int j;
					for (j = i + 1; j < n; j++)
					{
						char d = pids.charAt(j);
						
						if (d == ' ')
							break;
					}
					
					// Split string
					String spl = pids.substring(i, j).trim();
					
					// Add it
					deps.add(new ProjectName(spl));
					
					// Skip
					i = j;
				}
			}
			
			// Lock
			rv = UnmodifiableSet.<ProjectName>of(deps);
			ref = new WeakReference<>(rv);
			if (__opt)
				this._odepends = ref;
			else
				this._depends = ref;
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another project
		if (!(__o instanceof ProjectInfo))
			return false;
		
		// Compare the name
		return this.name.equals(((ProjectInfo)__o).name);
	}
	
	/**
	 * Returns the project groups that this project is a part of, this
	 * information is used by the target build system to include extra projects
	 * that may be needed on a target system.
	 *
	 * @return The set of project groups.
	 * @since 2016/09/02
	 */
	public final Set<String> groups()
	{
		// Get
		Reference<Set<String>> ref = this._groups;
		Set<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target set
			Set<String> target = new SortedTreeSet<>();
			
			// Fill properties
			String prop = this.manifest.getMainAttributes().get(
				"X-SquirrelJME-Group");
			if (prop != null)
			{
				int n = prop.length();
				for (int i = 0; i < n; i++)
				{
					// Ignore whitespace
					char c = prop.charAt(i);
					if (c <= ' ')
						continue;
					
					// Find end
					int j = i + 1;
					for (; j < n; j++)
						if (prop.charAt(j) <= ' ')
							break;
					
					// Add split
					target.add(prop.substring(i, j));
					i = j + 1;
				}
			}
			
			// Store
			rv = UnmodifiableSet.<String>of(target);
			this._groups = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Is this project an executable MIDlet?
	 *
	 * @return {@code true} if it is a MIDlet.
	 * @since 2016/09/19
	 */
	public final boolean isMIDlet()
	{
		// Just check to see if the first MIDlet information is defined
		return (null != manifest().getMainAttributes().get(
			new JavaManifestKey("MIDlet-1")));
	}
	
	/**
	 * Returns the main entry class for a general Java program.
	 *
	 * @return The main class to enter the program at or {@code null} if not
	 * found.
	 * @since 2016/06/20
	 */
	public final String mainClass()
	{
		return this.manifest.getMainAttributes().get("Main-Class");
	}
	
	/**
	 * Returns the manifest of this project.
	 *
	 * @return The project manifest.
	 * @since 2016/06/15
	 */
	public final JavaManifest manifest()
	{
		return this.manifest;
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/06/15
	 */
	public final ProjectName name()
	{
		return this.name;
	}
	
	/**
	 * This opens the specified file that exists within this project and
	 * returns the stream of bytes to its data.
	 *
	 * @param __n The name of the file to open, this matches the format used
	 * in ZIP files.
	 * @return The data stream for the file data.
	 * @throws IOException On read errors.
	 * @throws NoSuchFileException If the specified file does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	public final InputStream open(String __n)
		throws IOException, NoSuchFileException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// If this refers to a ZIP file then open it as one
		Path path = this.path;
		if (this.iszip)
		{
			// If ZIP not opened, then open it
			__OpenZip__ ozip = this._ozip;
			if (ozip == null)
				this._ozip = (ozip = new __OpenZip__(this, path));
			
			// Forward open
			return ozip.open(__n);
		}
		
		// Otherwise
		else
		{
			// Resolve path
			int n = __n.length();
			for (int i = 0; i < n; i++)
			{
				// Find next slash
				int j;
				for (j = i + 1; j < n; j++)
					if (__n.charAt(i) == '/')
						break;
				
				// Split resolve
				path = path.resolve(__n.substring(i, j));
				i = j;
			}
			
			// Open the file
			return Channels.newInputStream(FileChannel.open(path,
				StandardOpenOption.READ));
		}
	}
	
	/**
	 * Returns the path to this project.
	 *
	 * @return The project path.
	 * @since 2016/06/19
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Returns the owning project list.
	 *
	 * @return The project list.
	 * @since 2016/09/20
	 */
	public final ProjectList projectList()
	{
		return this.plist;
	}
	
	/**
	 * Returns the title of this project.
	 *
	 * @return The project title or {@code null} if it was not
	 * specified.
	 * @since 2016/09/19
	 */
	public final String title()
	{
		return manifest().getMainAttributes().get(
			new JavaManifestKey("LIBlet-Title"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2016/09/18
	 */
	public final ProjectType type()
	{
		if (this.isbinary)
			return ProjectType.BINARY;
		return ProjectType.SOURCE;
	}
	
	/**
	 * Returns the vendor of this project.
	 *
	 * @return The project vendor or {@code null} if it was not
	 * specified.
	 * @since 2016/09/19
	 */
	public final String vendor()
	{
		return manifest().getMainAttributes().get(
			new JavaManifestKey("LIBlet-Vendor"));
	}
	
	/**
	 * Returns the version number of this project.
	 *
	 * @return The project version number or {@code null} if it was not
	 * specified.
	 * @since 2016/09/19
	 */
	public final String version()
	{
		return manifest().getMainAttributes().get(
			new JavaManifestKey("LIBlet-Version"));
	}
	
	/**
	 * Loads a manifest from the given ZIP file.
	 *
	 * @parma __zip The ZIP to load the manifest from.
	 * @return The parsed manifest data.
	 * @throws InvalidProjectException If the project is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private static JavaManifest __loadManifestFromZIP(ZipFile __zip)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Find manifest file
		ZipEntry ent = __zip.get("META-INF/MANIFEST.MF");
		
		// {@squirreljme.error CI02 No manifest exists in the JAR.}
		if (ent == null)
			throw new InvalidProjectException("CI02");
		
		// Open input stream
		try (InputStream is = ent.open())
		{
			return new JavaManifest(is);
		}
		
		// {@squirreljme.error CI03 The manifest is not correctly formed.}
		catch (JavaManifestException e)
		{
			throw new InvalidProjectException("CI03", e);
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
	
	/**
	 * Recursively walks the directory tree to obtain project content file
	 * names.
	 *
	 * @param __into The set to write into.
	 * @param __root The root directory.
	 * @param __at The current scan position.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	private static void __recursiveWalk(Set<String> __into, Path __root,
		Path __at)
		throws IOException, NullPointerException
	{
		// Check
		if (__into == null || __root == null || __at == null)
			throw new NullPointerException("NARG");
		
		// Look at all entries
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__at))
		{
			for (Path p : ds)
			{
				// Go deeper
				if (Files.isDirectory(p))
					__recursiveWalk(__into, __root, p);
				
				// Add file
				else
					__into.add(__zipName(__root, p));
			}
		}
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
}

