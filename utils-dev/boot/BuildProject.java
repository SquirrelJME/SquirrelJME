// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This represents a single project which may be built.
 *
 * @since 2016/10/27
 */
public class BuildProject
{
	/** The project base path. */
	protected final Path basepath;
	
	/** The project name. */
	protected final String name;
	
	/** The output path for this JAR. */
	protected final Path jarout;
	
	/** Dependencies of this build project. */
	protected final Set<String> depends;
	
	/** Projects that are available. */
	private final Map<String, BuildProject> _projects;
	
	/** In compilation? */
	private volatile boolean _incompile;
	
	/** Was the source date calculated? */
	private volatile long _sourcedate =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the build project.
	 *
	 * @param __b The project base path.
	 * @param __mp The manifest path.
	 * @param __bjo Build JAR output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/27
	 */
	BuildProject(Path __b, Path __mp, Path __bjo,
		Map<String, BuildProject> __projs)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null || __mp == null || __bjo == null || __projs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.basepath = __b;
		this._projects = __projs;
		
		// Load the manifest
		Manifest man;
		try (InputStream in = Files.newInputStream(
			__mp, StandardOpenOption.READ))
		{
			man = new Manifest(in);
		}
		
		// Handle main attributes
		Attributes attr = man.getMainAttributes();
		
		// The project name depends on the uppermost name of the directory
		String name;
		this.name = (name = __correctProjectName(
			__b.getFileName().toString()));
		
		// Where is this output?
		this.jarout = __bjo.resolve(name + ".jar");
		
		// Determine dependencies
		Set<String> depends = new LinkedHashSet<>();
		String rd = attr.getValue("X-SquirrelJME-Depends");
		if (rd != null)
			for (String s : rd.split("[ \t]"))
				depends.add(__correctProjectName(s.trim()));
		
		// Force a dependency on the CLDC Compact library
		if (!name.equals("cldc-compact"))
			depends.add(__correctProjectName("cldc-compact"));
		
		// Set
		this.depends = depends;
	}
	
	/**
	 * Returns the date of the binary.
	 *
	 * The value here is not cached.
	 *
	 * @return The date of the binary.
	 * @since 2016/10/27
	 */
	public long binaryDate()
	{
		// If it does not exist, the date is not valid
		Path jarout = this.jarout;
		if (!Files.exists(jarout))
			return Long.MIN_VALUE;
		
		// Calculate
		try
		{
			Long[] out = new Long[1];
			out[0] = Long.MIN_VALUE;
			NewBootstrap.<Long[]>__walk(jarout, out, NewBootstrap.DATE);
			return out[0];
		}
		
		// Ignore
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * Compiles this project and any dependencies it may have.
	 *
	 * @return The set of projects which were compiled.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/27
	 */
	public Set<BuildProject> compile()
		throws IOException
	{
		Set<BuildProject> rv = new LinkedHashSet<>();
		
		// Build loop
		try
		{
			// {@squirreljme.error NB02 The specified project eventually
			// depends on itself. (The project name)}
			if (this._incompile)
				throw new IllegalStateException(String.format(
					"NB02 %s", this.name));
			
			// Currently compiling
			this._incompile = true;
			
			// Compile dependencies first
			Map<String, BuildProject> projects = this._projects;
			for (String dep : this.depends)
			{
				// {@squirreljme.error NB06 The dependency of a given
				// project does not exist. (This project; The project it
				// depends on)}
				BuildProject dp = projects.get(dep);
				if (dp == null)
					throw new IllegalStateException(String.format(
						"NB06 %s %s", this.name, dep));
				
				// Compile the dependency and add it to the merge group
				for (BuildProject bp : dp.compile())
					rv.add(bp);
			}
			
			// Other complation state
			__compileStep(rv);
		}
		
		// Clear compile state
		finally
		{
			this._incompile = false;
		}
		
		// Add self to dependency chain
		rv.add(this);
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/10/27
	 */
	public String projectName()
	{
		return this.name;
	}
	
	/**
	 * Returns the date when the project sources were last touched.
	 *
	 * @return The project sources date.
	 * @since 2016/10/27
	 */
	public long sourcesDate()
	{
		// Use cached value
		long rv = this._sourcedate;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Calculate otherwise
		try
		{
			Long[] out = new Long[1];
			out[0] = Long.MIN_VALUE;
			NewBootstrap.<Long[]>__walk(this.basepath, out, NewBootstrap.DATE);
			this._sourcedate = (rv = out[0]);
		}
		
		// Ignore
		catch (IOException e)
		{
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/25
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Secondary compilation step.
	 *
	 * @param __deps Dependencies used.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/27
	 */
	private void __compileStep(Set<BuildProject> __deps)
		throws IOException, NullPointerException
	{
		// Check
		if (__deps == null)
			throw new NullPointerException("NARG");
		
		// Best dependency date
		long depdate = Long.MIN_VALUE;
		for (BuildProject bp : __deps)
			depdate = Math.max(depdate, bp.binaryDate());
		
		// Do not recompile if it is up to date
		long srcdate = sourcesDate(),
			bindate =  binaryDate();
		Path jarout = this.jarout;
		if (Files.exists(jarout) && srcdate <= bindate &&
			bindate > depdate)
			return;
		
		// {@squirreljme.error NB09 Now compiling the specified project.
		// (The name of the project being compiled)}
		String name = this.name;
		System.err.printf("NB09 %s%n", name);
		
		// Try finding the compiler
		BootstrapCompiler javac = BootstrapCompiler.getCompiler();
		
		// Need to clear files
		Path tempdir = null;
		try
		{
			// Create temporary directory
			tempdir = Files.createTempDirectory(
				"squirreljme-build-" + name);
			
			// Source code is just this project
			final Path basepath = this.basepath;
			
			// Get all source code to be compiled
			final Set<Path> fsources = new LinkedHashSet<>();
			NewBootstrap.<Object>__walk(basepath, null,
				new Consumer<Path, Object, IOException>()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/10/27
					 */
					@Override
					public void accept(Path __p, Object __s)
						throws IOException
					{
						// Ignore directories
						if (Files.isDirectory(__p))
							return;
						
						// Add to sources if it is a source file
						if (__p.getFileName().toString().endsWith(".java"))
							fsources.add(__p);
					}
				});
			
			// If there are no sources to compile, do not bother
			if (!fsources.isEmpty())
			{
				// The class path is just the dependencies
				Set<Path> fdeps = new LinkedHashSet<>();
				for (BuildProject bp : __deps)
					fdeps.add(bp.jarout);
				
				// {@squirreljme.error NB08 Compilation of this project
				// failed. (The name of this project)}
				if (!javac.compile(basepath, tempdir, fdeps, fsources,
					Arrays.<String>asList("-source", "1.7",
					"-target", "1.7", "-g", "-Xlint:deprecation",
					"-Xlint:unchecked")))
					throw new RuntimeException(String.format("NB08 %s",
						name));
			}
			
			// Create directory used for output
			Path jpar = jarout.getParent();
			if (jpar != null)
				Files.createDirectories(jpar);
			
			// Generate JAR output
			Path tempjar = Files.createTempFile("buildjar", "jar");
			try (final ZipOutputStream zos = new ZipOutputStream(Files.
				newOutputStream(tempjar,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE)))
			{
				// Write files in the output and input
				final byte[] buf = new byte[4096];
				Consumer<Path, Path, IOException> func =
					new Consumer<Path, Path, IOException>()
					{
						/**
						 * {@inheritDoc}
						 * @since 2016/10/28
						 */
						@Override
						public void accept(Path __p, Path __s)
							throws IOException
						{
							ZipOutputStream out = zos;
							
							// Ignore directories
							if (Files.isDirectory(__p) || __p.
								getFileName().toString().endsWith("java"))
								return;
							
							// Ignore files ending in .java
							Path fn = __p.getFileName();
							String fns = __p.getFileName().toString();
							if (fns.endsWith(".java"))
								return;
							
							// Detect MIME files?
							boolean ismime = false;
							if (fns.endsWith(".__mime"))
							{
								ismime = true;
								fns = fns.substring(0, fns.length() - 7);
							}
							
							// Create new entry
							out.putNextEntry(
								new ZipEntry(NewBootstrap.__zipName(__s,
									__p.resolveSibling(fns))));
							
							// Copy data
							try (InputStream src = Files.newInputStream(__p,
									StandardOpenOption.READ);
								InputStream is = (!ismime ? src :
									new MIMEFileDecoder(src)))
							{
								for (;;)
								{
									int rc = is.read(buf);
									
									// EOF?
									if (rc < 0)
										break;
									
									// Copy
									out.write(buf, 0, rc);
								}
							}
							
							// Close
							out.closeEntry();
						}
					};
				
				// Add sources and classes
				NewBootstrap.<Path>__walk(basepath, basepath, func);
				NewBootstrap.<Path>__walk(tempdir, tempdir, func);
				
				// Finish it
				zos.finish();
				zos.flush();
				
				// Move JAR
				Files.move(tempjar, jarout,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// Failed, delete output JAR
			catch (IOException|RuntimeException|Error e)
			{
				// Delete file
				try
				{
					Files.delete(tempjar);
				}
				catch (IOException f)
				{
					// Ignore
				}
				
				// Rethrow
				throw e;
			}
		}
		
		// Always clear at the end
		finally
		{
			if (tempdir != null)
				try
				{
					NewBootstrap.<Object>__walk(tempdir, null,
						NewBootstrap.DELETE);
				}
				catch (IOException e)
				{
					// Ignore
				}
		}
	}
	
	/**
	 * Lowercases the specified string.
	 *
	 * @param __s The string to check.
	 * @return The string in correct form.
	 * @throws IllegalArgumentException If it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/27
	 */
	private static String __correctProjectName(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check if any characters need lowering first
		int n = __s.length();
		boolean dolower = false;
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase?
			if (c >= 'A' && c <= 'Z')
				dolower = true;
			
			// Legal character?
			else if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||
				c == '-')
				continue;
			
			// {@squirreljme.error NB01 Illegal character used in project
			// name. (The input project name)}
			else
				throw new IllegalArgumentException(String.format("NB01 %s",
					__s));
		}
		
		// Use original string
		if (!dolower)
			return __s;
		
		// Lowercase everything
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase?
			if (c >= 'A' && c <= 'Z')
				sb.append((char)((c - 'A') + 'a'));
			
			// Keep
			else
				sb.append(c);
		}
		
		// Use that
		return sb.toString();
	}
}
