// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.CloseableList;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.UnmodifiableCollection;
import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.DefaultCompiler;
import net.multiphasicapps.javac.FilePathSet;
import net.multiphasicapps.javac.ZipCompilerOutput;
import net.multiphasicapps.javac.ZipPathSet;
import net.multiphasicapps.squirreljme.runtime.midlet.DependencySet;
import net.multiphasicapps.squirreljme.runtime.midlet.ManifestedDependency;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletDependency;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletDependencyLevel;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletDependencyType;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletSuiteID;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletSuiteName;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletVersion;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletVersionRange;
import net.multiphasicapps.strings.StringUtils;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This class is used to manage binaries which are available for running
 * and/or compilation.
 *
 * @since 2017/10/31
 */
public final class BinaryManager
	implements Iterable<Binary>
{
	/** The output directory where built binaries are to be placed. */
	protected final Path output;
	
	/** Projects which may exist that provide access to source code. */
	protected final SourceManager sources;
	
	/** Projects which have been read by the manager. */
	private final Map<SourceName, Binary> _binaries =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the binary manager.
	 *
	 * @param __out The output directory, this directory is scanned for
	 * binaries as requested.
	 * @param __src The source code where projects may be sourced from, may
	 * be {@code null} if there is no source code.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException If no output path was specified.
	 * @since 2017/10/31
	 */
	public BinaryManager(Path __out, SourceManager __src)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.output = __out;
		this.sources = __src;
		
		// Load in binaries from source code first
		Map<SourceName, Binary> binaries = this._binaries;
		for (Source src : __src)
		{
			SourceName name = src.name();
			binaries.put(name,
				new Binary(name, src, __out.resolve(name.toFileName())));
		}
		
		// Go through directory containing binaries and try building binaries
		// that have no source
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__out))
		{
			for (Path p : ds)
			{
				// Ignore directories and non-binary files
				if (Files.isDirectory(p) || !SourceName.isBinaryPath(p))
					continue;
				
				// Only add a binary if it does not exist, this allows one
				// to add external binaries
				SourceName name = SourceName.ofBinaryPath(p);
				Binary bin = binaries.get(name);
				if (bin == null)
					binaries.put(name, new Binary(name, null, p));
			}
		}
		
		// Ignore these
		catch (NoSuchFileException e)
		{
		}
	}
	
	/**
	 * Returns the dependencies which statisfy the given set.
	 *
	 * @param __set The set of dependencies to get.
	 * @param __opt If {@code true} include optional dependencies.
	 * @return Binaries which statisfy the given dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] dependencies(DependencySet __set, boolean __opt)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		// The returning set
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Go through the entire set and search for dependencies
		for (ManifestedDependency md : __set)
		{
			// Not wanting an optional dependency
			boolean mdopt = md.isOptional();
			if (mdopt && !__opt)
				continue;
			
			// {@squirreljme.error AU0c Could not locate the binary which
			// statifies the given dependency. (The dependency to look for)}
			Binary bin = this.findDependency(md);
			if (bin == null)
				if (mdopt)
					continue;
				else
					throw new InvalidBinaryException(
						String.format("AU0c %s", md));
			
			// Recursively obtain the dependencies of that dependency
			for (Binary dep : this.dependencies(bin.dependencies(), false))
				rv.add(dep);
			
			// Add the self dependency at the end
			rv.add(bin);
		}
		
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
	
	/**
	 * Returns the class path for the given binary.
	 *
	 * @param __b The binary to get the classpath for.
	 * @return The binary class path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] classPath(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Return value to use run-time
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Make sure all dependencies are used
		for (Binary dep : this.dependencies(__b.dependencies(), false))
			rv.add(dep);
		
		// Include this in the run-time
		rv.add(__b);
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
	
	/**
	 * Compiles the specified binary and all of their dependencies.
	 *
	 * @param __b The binary to compile.
	 * @return The binaries which were compiled and are part of the class
	 * path.
	 * @throws InvalidBinaryException If the binary is not valid because it
	 * could not be compiled or one of its dependencies could not be compiled.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] compile(Binary __b)
		throws InvalidBinaryException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Return value to use run-time
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Make sure all dependencies are compiled, this will result in the
		// entire class path being determined aslso for compilation
		for (Binary dep : this.dependencies(__b.dependencies(), false))
			for (Binary c : this.compile(dep))
				rv.add(c);
		
		// Always recompile if the source is newer, but go through all
		// dependencies and recompile if any dependency is newer than this
		// binary
		boolean docompile = __b.isSourceNewer();
		if (!docompile)
		{
			// Get the date for this binary
			long mydate = __b.lastModifiedTime();
			
			// If any dependency has a newer binary than this one, compile
			for (Binary dep : rv)
				if (dep.isSourceNewer() || dep.lastModifiedTime() > mydate)
				{
					docompile = true;
					break;
				}
		}
		
		// Compilation needs to be performed
		if (docompile)
		{
			// Temporary output file to be cleaned
			Path temp = null;
			
			// {@squirreljme.error AU0h Cannot compile the specified project
			// because it has no source code. (The name of the project)}
			Source src = __b.source();
			if (src == null)
				throw new InvalidBinaryException(
					String.format("AU0h %s", __b.name()));
			
			// Need to close everything that is opened
			try (CloseableList<Closeable> closing = new CloseableList<>())
			{
				// {@squirreljme.error AU0f Compiling the given project.
				// (The project name)}
				System.err.printf("AU0f %s%n", __b.name());
			
				// Setup compiler instance
				Compiler javac = DefaultCompiler.createInstance();
				
				// Use the source root to lookup source code
				FilePathSet srcps = closing.<FilePathSet>addThis(
					new FilePathSet(src.root()), FilePathSet.class);
				javac.setLocation(CompilerInputLocation.SOURCE, srcps);
				
				// Explicitly compile every source file
				Set<CompilerInput> noninput = new LinkedHashSet<>();
				boolean hasinput = false;
				for (CompilerInput i : srcps)
				{
					String name = i.name();
					
					// Compile any source file
					if (name.endsWith(".java"))
					{
						hasinput = true;
						javac.addInput(i);
					}
					
					// Do not include the manifest ever
					else if (!name.equals("META-INF/MANIFEST.MF"))
						noninput.add(i);
				}
				
				// Go through all binaries and load dependencies into the
				// class path
				int ndeps = rv.size(),
					i = 0;
				CompilerPathSet[] bins = new CompilerPathSet[ndeps];
				for (Binary dep : rv)
					bins[i++] = closing.<ZipPathSet>addThis(
						new ZipPathSet(dep.zipBlock()), ZipPathSet.class);
				javac.setLocation(CompilerInputLocation.CLASS, bins);
				
				// Need temporary file for output
				temp = Files.createTempFile("squirreljme-", ".ja_");
				
				// Output to a temporary ZIP file
				try (ZipCompilerOutput out = new ZipCompilerOutput(
					new ZipStreamWriter(Files.newOutputStream(temp,
						StandardOpenOption.CREATE,
						StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING))))
				{
					// Write the resulting actual manifest
					try (OutputStream mos = out.output("META-INF/MANIFEST.MF"))
					{
						__writeRealManifest(mos, rv, __b);
					}
					
					// Run compilation task, but not if there is no input
					if (hasinput)
						javac.compile(out).run();
					
					// Go through non-input and copy all of the data
					byte[] buf = new byte[512];
					for (CompilerInput j : noninput)
						try (InputStream ei = j.open();
							OutputStream eo = out.output(j.name()))
						{
							for (;;)
							{
								int rc = ei.read(buf);
								
								if (rc < 0)
									break;
								
								eo.write(buf, 0, rc);
							}
						}
					
					// Flush the ZIP before it is closed
					out.close();
				}
				
				// Make sure the output directories exist
				Path outpath = __b.path();
				Files.createDirectories(outpath.getParent());
				
				// Replace output file with temporary one
				Files.move(temp, outpath, StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// {@squirreljme.error AU0g Could not compile the specified
			// project. (The project which failed to compile)}
			catch (CompilerException|IOException e)
			{
				throw new InvalidBinaryException(
					String.format("AU0g %s", __b.name()));
			}
			
			// Clean output temporary file as needed
			finally
			{
				if (temp != null)
					try
					{
						Files.deleteIfExists(temp);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
			}
		}
		
		// Include this in the run-time
		rv.add(__b);
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
	
	/**
	 * Finds the binary project which statifies the given dependency.
	 *
	 * @param __dep The dependency to locate.
	 * @return The binary if one was found, otherwise {@code null} if not.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public final Binary findDependency(ManifestedDependency __dep)
		throws NullPointerException
	{
		if (__dep == null)
			throw new NullPointerException("NARG");
		
		// Look for the first matching dependency
		for (Binary bin : this)
		{
			ManifestedDependency[] check = bin.matchedDependencies(__dep);
			
			if (check.length > 0)
				return bin;
		}
		
		// None found
		return null;
	}
	
	/**
	 * Obtains the binary which uses the given source name.
	 *
	 * @param __n The name of the project to get.
	 * @return The binary for the given name.
	 * @throws NoSuchBinaryException If no binary with the given name exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary get(String __n)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.get(new SourceName(__n));
	}
	
	/**
	 * Obtains the binary which uses the given source name.
	 *
	 * @param __n The name of the project to get.
	 * @return The binary for the given name.
	 * @throws NoSuchBinaryException If no binary with the given name exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Binary get(SourceName __n)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Locate
		Binary rv = this._binaries.get(__n);
		
		// {@squirreljme.error AU0d The specified binary does not exist.
		// (The name of the binary)}
		if (rv == null)
			throw new NoSuchBinaryException(String.format("AU0d %s"));
		return rv;
	}
	
	/**
	 * Creates a virtual binary which is sourced from the given JAR file and
	 * where it has no backing in source code.
	 *
	 * @param __p The path to JAR to be opened as a binary.
	 * @return The binary for the given path.
	 * @throws NoSuchBinaryException If no such binary exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Binary getVirtual(Path __p)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AU01 Cannot open the specified path as a project
		// because it does not exist. (The path to open as a binary)}
		if (!Files.exists(__p))
			throw new NoSuchBinaryException(String.format("AU01 %s", __p));
		
		// Just create the binary
		return new Binary(SourceName.ofBinaryPath(__p), null, __p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/25
	 */
	@Override
	public final Iterator<Binary> iterator()
	{
		return UnmodifiableCollection.<Binary>of(this._binaries.values()).
			iterator();
	}
	
	/**
	 * Writes the real manifest to the output binary with non-proprietary
	 * SquirrelJME dependencies.
	 *
	 * @param __os The stream to write to.
	 * @param __deps The dependencies to use for lookup.
	 * @param __bin The binary to write the manifest for.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	private static void __writeRealManifest(OutputStream __os,
		Set<Binary> __deps, Binary __bin)
		throws IOException, NullPointerException
	{
		if (__os == null || __deps == null || __bin == null)
			throw new NullPointerException("NARG");
		
		// Initialize the output manifest with the initial approximated
		// manifest first
		MutableJavaManifest outman = new MutableJavaManifest(
			__bin.source().approximateBinaryManifest());
		MutableJavaManifestAttributes outattr = outman.getMainAttributes();
		
		// Is this a midlet? Needed for correct dependency placement
		boolean ismidlet = (__bin.type() == ProjectType.MIDLET);
		
		// Read in all dependency values. Since SquirrelJME uses a rather
		// abstract dependency system, project references will be converted to
		// configurations, profiles, standard, or liblets.
		Set<MidletDependency> mdeps = new LinkedHashSet<>();
		for (int i = 1; i >= 1; i++)
		{
			JavaManifestKey key = new JavaManifestKey((ismidlet ?
				"MIDlet-Dependency-" : "LIBlet-Dependency-") + i);
			
			// No more values to parse
			String value = outattr.get(key);
			if (value == null)
				break;
			
			// Read in dependency and remove from original set, will be
			// re-added later after being evaluated
			mdeps.add(new MidletDependency(outattr.remove(key)));
		}
		
		// Go through and remap dependencies
		int next = 1;
		for (MidletDependency dep : mdeps)
		{
			JavaManifestKey key = new JavaManifestKey((ismidlet ?
				"MIDlet-Dependency-" : "LIBlet-Dependency-") + next);
			
			// Debug
			System.err.printf("DEBUG -- Dep: %s%n", dep);
			
			// Non-proprietary dependency
			String name = Objects.toString(dep.name(), "").trim();
			if (dep.type() != MidletDependencyType.PROPRIETARY ||
				!name.startsWith("squirreljme.project"))
			{
				// Just directly copy it since it has an unknown translation
				outattr.put(key, dep.toString());
				
				// Next key
				next++;
				continue;
			}
			
			// {@squirreljme.error AU0k Expected at sign in
			// {@code squirreljme.project} type dependency. (This project)}
			int at = name.indexOf('@');
			if (at < 0)
				throw new InvalidBinaryException(
					String.format("AU0k %s", __bin.name()));
			SourceName sname = new SourceName(name.substring(at + 1).trim());
			
			// Find the dependency using the given project
			Binary found = null;
			for (Binary bin : __deps)
				if (sname.equals(bin.name()))
				{
					found = bin;
					break;
				}
			
			// {@squirreljme.error AU0l Could not locate the project with the
			// specified dependency. (This project; The dependency)}
			if (found == null)
				throw new InvalidBinaryException(
					String.format("AU0l %s %s", __bin.name(), sname));
			
			// Depending on an API, use configuration, standard, or profile
			if (found.type() == ProjectType.API)
			{
				throw new todo.TODO();
			}
			
			// Relying on a liblet
			else
			{
				MidletSuiteID sid = found.suiteId();
				outattr.put(key, new MidletDependency(
					MidletDependencyType.LIBLET,
					dep.level(),
					sid.name(),
					sid.vendor(),
					MidletVersionRange.exactly(sid.version())).toString());
				
				// Use next key
				next++;
			}
		}
		
		// Debug
		System.err.println("DEBUG -- Begin manifest...");
		outman.write(System.err);
		System.err.println("DEBUG -- ...end manifest.");
		
		// Write it
		outman.write(__os);
	}
}

