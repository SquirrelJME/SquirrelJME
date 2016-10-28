// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * New bootstrap build system.
 *
 * @since 2016/10/26
 */
public class NewBootstrap
	implements Runnable
{
	/** Deletes files. */
	public static final Consumer<Path, Object, IOException> DELETE =
		new Consumer<Path, Object, IOException>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/10/27
			 */
			@Override
			public void accept(Path __v, Object[] __s)
				throws IOException
			{
				System.err.printf("DEBUG -- DELETE %s%n", __v);
				
				Files.delete(__v);
			}
		};
	
	/** The binary path. */
	protected final Path binarypath;
	
	/** The source path. */
	protected final Path sourcepath;
	
	/** The input launch arguments. */
	protected final String[] launchargs;
	
	/** Projects available for usage. */
	protected final Map<String, BuildProject> projects;
	
	/** The output bootstrap binary. */
	protected final Path bootstrapout;
	
	/**
	 * Initializes the bootstrap base.
	 *
	 * @param __bin The binary output directory.
	 * @param __src The source input namespace directories.
	 * @param __args Arguments to the bootstrap.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/26
	 */
	public NewBootstrap(Path __bin, Path __src, String[] __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.binarypath = __bin;
		this.sourcepath = __src;
		this.launchargs = __args.clone();
		this.bootstrapout = __bin.resolve("sjmeboot.jar");
		
		// Load all projects in the build directory
		Map<String, BuildProject> projects = new LinkedHashMap<>();
		try (DirectoryStream<Path> ds =
			Files.newDirectoryStream(__src.resolve("build")))
		{
			// Go through all directories
			for (Path p : ds)
			{
				// Must be a directory
				if (!Files.isDirectory(p))
					continue;
			
				// See if the manifest exists
				Path man = p.resolve("META-INF").resolve("MANIFEST.MF");
				if (!Files.exists(man))
					continue;
			
				// Load project
				BuildProject bp = new BuildProject(p, man);
				projects.put(bp.projectName(), bp);
			}
		}
		this.projects = projects;
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/26
	 */
	@Override
	public void run()
	{
		// {@squirreljme.error NB03 The entry point project does not exist.}
		Map<String, BuildProject> projects = this.projects;
		BuildProject bp = projects.get("host-javase");
		if (bp == null)
			throw new IllegalStateException("NB03");
		
		// Could fail
		try
		{
			// Compile JARs to be merged together as one
			Set<BuildProject> mergethese = bp.compile();
			
			throw new Error("TODO");
		}
		
		// {@squirreljme.error NB04 Failed to compile the bootstrap due to
		// a read/write error.}
		catch (IOException e)
		{
			throw new RuntimeException("NB04", e);
		}
	}
	
	/**
	 * Main entry point for the new bootstrap system.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On any read/write errors.
	 * @since 2016/10/26
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Get directories for input and output
		Path bin = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.binary")),
			src = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.source"));
		
		// Only build?
		NewBootstrap nb = new NewBootstrap(bin, src, __args);
		
		// Run it
		nb.run();
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
	
	/**
	 * Walks the given path and calls the given consumer for every file and
	 * directory.
	 *
	 * @param <S> The secondary value to pass.
	 * @param __p The path to walk.
	 * @param __s The secondary value.
	 * @param __c The function to call for paths.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments, except for the secondary
	 * value.
	 * @since 2016/09/18
	 */
	private static <S> void __walk(Path __p, S[] __s,
		Consumer<Path, S, IOException> __c)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null || __c == null)
			throw new NullPointerException("NARG");
		
		// If a directory, walk through all the files
		if (Files.isDirectory(__p))
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
			{
				for (Path s : ds)
					__walk(s, __s, __c);
			}
		
		// Always accept, directories are accepted last since directories
		// cannot be deleted if they are not empty
		__c.accept(__p, __s);
	}
	
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
		
		/** In compilation? */
		private volatile boolean _incompile;
		
		/**
		 * Initializes the build project.
		 *
		 * @param __b The project base path.
		 * @param __mp The manifest path.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/27
		 */
		BuildProject(Path __b, Path __mp)
			throws IOException, NullPointerException
		{
			// Check
			if (__b == null || __mp == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.basepath = __b;
			
			// Load the manifest
			Manifest man;
			try (InputStream in = Channels.newInputStream(
				FileChannel.open(__mp, StandardOpenOption.READ)))
			{
				man = new Manifest(in);
			}
			
			// Handle main attributes
			Attributes attr = man.getMainAttributes();
			
			// {@squirreljme.error NB02 No project name was specified for
			// the given manifest. (The manifest path)}
			String rn = attr.getValue("X-SquirrelJME-BuildHostName");
			if (rn == null)
				throw new IllegalArgumentException(String.format("NB02 %s",
					__mp));
			String name;
			this.name = (name = __correctProjectName(rn.trim()));
			
			// Where is this output?
			this.jarout = NewBootstrap.this.binarypath.resolve("bootsjme").
				resolve(name);
			
			// Determine dependencies
			Set<String> depends = new LinkedHashSet<>();
			String rd = attr.getValue("X-SquirrelJME-Depends");
			if (rd != null)
				for (String s : rd.split("[ \t]"))
					depends.add(__correctProjectName(s.trim()));
			this.depends = depends;
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
				// {@squirreljme.error NB05 The specified project eventually
				// depends on itself. (The name of this project)}
				if (this._incompile)
					throw new IllegalStateException(String.format("NB05 %s",
						this.name));
				this._incompile = true;
				
				// Compile dependencies first
				Map<String, BuildProject> projects =
					NewBootstrap.this.projects;
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
				
				// {@squirreljme.error NB07 No system Java compiler is
				// available.}
				JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
				if (javac == null)
					throw new IllegalStateException("NB07");
				
				// Need to access the disk
				StandardJavaFileManager jfm = javac.getStandardFileManager(
					null, null, null);
				
				// Need to clear files
				Path tempdir = null;
				try
				{
					// Create temporary directory
					tempdir = Files.createTempDirectory(
						"squirreljme-build-" + this.name);
					
					// Create task
					if (true)
						throw new Error("TODO");
				}
				
				// Always clear at the end
				finally
				{
					if (tempdir != null)
						try
						{
							NewBootstrap.<Object>__walk(tempdir, null, DELETE);
						}
						catch (IOException e)
						{
							// Ignore
						}
				}
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
	}
	
	/**
	 * Consumes a value which may throw an exception.
	 *
	 * @param <V> The value to consume.
	 * @param <S> A secondary value that may be passed.
	 * @param <E> The exception to potentially throw.
	 * @since 2016/10/27
	 */
	public static interface Consumer<V, S, E extends Exception>
	{
		/**
		 * Accepts a value.
		 *
		 * @param __v The value to access.
		 * @param __s An optional secondary value.
		 * @throws E If this given exception type is thrown.
		 * @since 2016/10/27
		 */
		public abstract void accept(V __v, S[] __s)
			throws E;
	}
}

