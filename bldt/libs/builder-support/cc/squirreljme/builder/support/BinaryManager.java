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

import cc.squirreljme.runtime.swm.Configuration;
import cc.squirreljme.runtime.swm.DependencyInfo;
import cc.squirreljme.runtime.swm.MarkedProvided;
import cc.squirreljme.runtime.swm.MatchResult;
import cc.squirreljme.runtime.swm.Profile;
import cc.squirreljme.runtime.swm.ProvidedInfo;
import cc.squirreljme.runtime.swm.Standard;
import cc.squirreljme.runtime.swm.SuiteDependency;
import cc.squirreljme.runtime.swm.SuiteDependencyLevel;
import cc.squirreljme.runtime.swm.SuiteDependencyType;
import cc.squirreljme.runtime.swm.SuiteInfo;
import cc.squirreljme.runtime.swm.SuiteName;
import cc.squirreljme.runtime.swm.SuiteVersion;
import cc.squirreljme.runtime.swm.SuiteVersionRange;
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
import java.util.ArrayList;
import java.util.Collection;
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
import net.multiphasicapps.javac.MergedPathSet;
import net.multiphasicapps.javac.ZipCompilerOutput;
import net.multiphasicapps.javac.ZipPathSet;
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
		for (Binary dep : this.matchDependencies(
			__b.suiteInfo().dependencies(), false))
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
		for (Binary dep : this.matchDependencies(
			__b.suiteInfo().dependencies(), false))
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
			
			// {@squirreljme.error AU05 Cannot compile the specified project
			// because it has no source code. (The name of the project)}
			Source src = __b.source();
			if (src == null)
				throw new InvalidBinaryException(
					String.format("AU05 %s", __b.name()));
			
			// Need to close everything that is opened
			try (CloseableList<Closeable> closing = new CloseableList<>())
			{
				// {@squirreljme.error AU06 Compiling the given project.
				// (The project name)}
				System.err.printf("AU06 %s%n", __b.name());
			
				// Setup compiler instance
				Compiler javac = DefaultCompiler.createInstance();
				
				// Use the source root to lookup source code
				CompilerPathSet srcps = closing.<CompilerPathSet>addThis(
					src.pathSet(SourcePathSetType.COMPILED),
					CompilerPathSet.class);
				javac.setLocation(CompilerInputLocation.SOURCE, srcps);
				
				// Explicitly compile every source file
				Set<CompilerInput> noninput = new LinkedHashSet<>();
				boolean hasinput = false;
				for (CompilerInput i : srcps)
				{
					String name = i.fileName();
					
					// Is there a dash in this?
					int ls = name.lastIndexOf('/');
					boolean dashname = (ls >= 0 && name.substring(0, ls).
						indexOf('-') >= 0);
					
					// Compile any source file
					if (!dashname && name.endsWith(".java"))
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
							OutputStream eo = out.output(j.fileName()))
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
			
			// {@squirreljme.error AU07 Could not compile the specified
			// project. (The project which failed to compile)}
			catch (CompilerException|IOException e)
			{
				throw new InvalidBinaryException(
					String.format("AU07 %s", __b.name()));
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
		
		// {@squirreljme.error AU08 The specified binary does not exist.
		// (The name of the binary)}
		if (rv == null)
			throw new NoSuchBinaryException(String.format("AU08 %s", __n));
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
		
		// {@squirreljme.error AU09 Cannot open the specified path as a project
		// because it does not exist. (The path to open as a binary)}
		if (!Files.exists(__p))
			throw new NoSuchBinaryException(String.format("AU09 %s", __p));
		
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
	 * Returns the binaries which statisfy the given set.
	 *
	 * @param __set The set of dependencies to get.
	 * @param __opt If {@code true} include optional dependencies.
	 * @return Binaries which statisfy the given dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] matchDependencies(DependencyInfo __set,
		boolean __opt)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		// Clear all optionals if they are not included
		if (!__opt)
			__set = __set.noOptionals();
		
		// No dependencies to search for
		if (__set.isEmpty())
			return new Binary[0];
		
		// Remember the original set for recursive dependency checks
		DependencyInfo original = __set;
		
		// The returning set
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Go through all binaries and attempt to match
		for (Binary bin : this)
		{
			// Only consider matches
			MatchResult result = bin.matchedDependencies(__set);
			if (!result.hasMatches())
				continue;
			
			// Use this as a dependency
			rv.add(bin);
			
			// Recursively go down
			for (Binary sub : this.matchDependencies(
				bin.suiteInfo().dependencies(), false))
				rv.add(sub);
			
			// Use remaining unmatched set
			__set = result.unmatched();
			
			// If the set was emptied then it will never have any more matches
			if (__set.isEmpty())
				break;
		}
		
		// {@squirreljme.error AU0a Could not locate the binary which
		// statifies the given dependency. (The dependency to look for)}
		if (rv.isEmpty())
			throw new InvalidBinaryException(
				String.format("AU0a %s", __set));
		
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
	
	/**
	 * Returns the dependency key to use for the manifest.
	 *
	 * @param __ismidlet Is this a midlet?
	 * @param __i The dependency index.
	 * @return The key to use in the manifest for the dependency.
	 * @since 2018/02/28
	 */
	private static JavaManifestKey __dependencyKey(boolean __ismidlet, int __i)
	{
		return new JavaManifestKey((__ismidlet ?
			"MIDlet-Dependency-" : "LIBlet-Dependency-") + __i);
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
			__bin.source().manifest());
		MutableJavaManifestAttributes outattr = outman.getMainAttributes();
		
		// Is this a midlet? Needed for correct dependency placement
		boolean ismidlet = (__bin.type() == ProjectType.MIDLET);
		
		// Read in all dependency values. Since SquirrelJME uses a rather
		// abstract dependency system, project references will be converted to
		// configurations, profiles, standard, or liblets.
		Set<SuiteDependency> mdeps = new LinkedHashSet<>();
		for (int i = 1; i >= 1; i++)
		{
			JavaManifestKey key = __dependencyKey(ismidlet, i);
			
			// No more values to parse
			String value = outattr.get(key);
			if (value == null)
				break;
			
			// Read in dependency and remove from original set, will be
			// re-added later after being evaluated
			mdeps.add(new SuiteDependency(outattr.remove(key)));
		}
		
		// Go through and remap dependencies
		int next = 1;
		Map<Binary, SuiteDependency> apideps = new LinkedHashMap<>();
		for (SuiteDependency dep : mdeps)
		{
			JavaManifestKey key = __dependencyKey(ismidlet, next);
			
			// Debug
			System.err.printf("DEBUG -- Dep: %s%n", dep);
			
			// Non-proprietary dependency
			String name = Objects.toString(dep.name(), "").trim();
			if (dep.type() != SuiteDependencyType.PROPRIETARY ||
				!name.startsWith("squirreljme.project"))
			{
				// Just directly copy it since it has an unknown translation
				outattr.put(key, dep.toString());
				
				// Next key
				next++;
				continue;
			}
			
			// {@squirreljme.error AU0b Expected at sign in
			// {@code squirreljme.project} type dependency. (This project)}
			int at = name.indexOf('@');
			if (at < 0)
				throw new InvalidBinaryException(
					String.format("AU0b %s", __bin.name()));
			SourceName sname = new SourceName(name.substring(at + 1).trim());
			
			// Find the dependency using the given project
			Binary found = null;
			for (Binary bin : __deps)
				if (sname.equals(bin.name()))
				{
					found = bin;
					break;
				}
			
			// {@squirreljme.error AU0c Could not locate the project with the
			// specified dependency. (This project; The dependency)}
			if (found == null)
				throw new InvalidBinaryException(
					String.format("AU0c %s %s", __bin.name(), sname));
			
			// Depending on an API, use configuration, standard, or profile
			SuiteInfo foundinfo = found.suiteInfo();
			if (found.type() == ProjectType.API)
				apideps.put(found, dep);
			
			// Relying on a liblet
			else
			{
				outattr.put(key, new SuiteDependency(
					SuiteDependencyType.LIBLET,
					dep.level(),
					foundinfo.name(),
					foundinfo.vendor(),
					SuiteVersionRange.exactly(foundinfo.version())).
					toString());
				
				// Use next key
				next++;
			}
		}
		
		// Handle API dependencies since they require a more complex setup
		System.err.printf("DEBUG -- API Depends: %s%n", apideps);
		if (!apideps.isEmpty())
			for (Map.Entry<Binary, SuiteDependency> e : apideps.entrySet())
			{
				Binary bin = e.getKey();
				SuiteDependency dep = e.getValue();
				
				// Go through provided dependencies and try to handle them
				for (MarkedProvided mp : bin.suiteInfo().provided().provided())
				{
					// Standard
					if (mp instanceof Standard)
					{
						Standard s = (Standard)mp;
						SuiteVersion v = s.version();
						outattr.put(__dependencyKey(ismidlet, next++),
							new SuiteDependency(
								SuiteDependencyType.STANDARD,
								dep.level(),
								s.name(),
								s.vendor(),
								(v == null ? null :
									SuiteVersionRange.exactly(v))).
							toString());
					}
					
					// Configuration
					else if (mp instanceof Configuration)
						throw new todo.TODO();
					
					// Profile
					else if (mp instanceof Profile)
						throw new todo.TODO();
					
					// Unknown, ignore
					else
						continue;
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

