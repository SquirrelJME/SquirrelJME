// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * This is the bootstrap which builds the bootstrap system and then launches
 * it. It bridges the bootstrap compiler and launcher system to the host
 * Jave SE VM.
 *
 * @since 2016/09/18
 */
public class Bootstrap
	implements Runnable
{
	/** The project which contains the bootstrap to build. */
	public static final String BOOTSTRAP_PROJECT =
		"bootstrap";
	
	/** The bootstrap JAR file, which is not a project. */
	public static final Path BOOTSTRAP_JAR_PATH =
		Paths.get("sjmeboot.jar");
	
	/** The bootstrap manifest file. */
	public static final Path BOOTSTRAP_MANIFEST;
	
	/** Project root directory. */
	public static final Path PROJECT_ROOT;
	
	/** Source root. */
	public static final Path SOURCE_ROOT;
	
	/** Contrib root. */
	public static final Path CONTRIB_ROOT;
	
	/** Input program arguments. */
	private final String[] _args;
	
	/**
	 * Initializes some details.
	 *
	 * @since 2016/09/18
	 */
	static
	{
		// Get the root and make sure it exists
		PROJECT_ROOT = Paths.get(Objects.<String>requireNonNull(
			System.getProperty("project.root"),
			"The system property `project.root` was not specified."));
		
		// Resolve other paths
		SOURCE_ROOT = PROJECT_ROOT.resolve("src");
		CONTRIB_ROOT = PROJECT_ROOT.resolve("contrib");
		
		// Set manifest
		BOOTSTRAP_MANIFEST = SOURCE_ROOT.resolve(BOOTSTRAP_PROJECT).
			resolve("net").resolve("multiphasicapps").resolve("squirreljme").
			resolve("bootstrap").resolve("BOOT.MF");
	}
	
	/**
	 * Bootstrap starter instance.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public Bootstrap(String... __args)
	{
		// Force
		if (__args == null)
			__args = new String[0];
		
		// Set
		this._args = __args.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public void run()
	{
		// Could fail
		try
		{
			// Locate the directories that contains all of the source code
			// along with dependencies for the bootstrap
			Path[] sources = __locateSources();
			System.err.printf("DEBUG -- Located: %s%n",
				Arrays.asList(sources));
			
			// Is the bootstrap technically out of date? Build it if so
			if (__outOfDate(sources))
				__build(sources);
			
			// Launch it
			__launch();
		}
		
		// Failed
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Builds the bootstrap.
	 *
	 * @param __src Source directories to include in the build.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private void __build(Path[] __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// Will generate a number of temporary files
		Path tempdir = null;
		try
		{
			// Setup temporary directory
			tempdir = Files.createTempDirectory("squirreljme-bootstrap");
			
			// Obtain the Java compiler
			JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
			if (javac == null)
				throw new RuntimeException("No Java compiler exists.");
			
			// Create file manager instance
			StandardJavaFileManager jfm = javac.getStandardFileManager(null,
				null, null);
			
			// Output to the temporary directory
			{
				// Need to wrap
				Set<File> dir = new HashSet<>();
				dir.add(tempdir.toFile());
				
				// Output classes and sources there
				jfm.setLocation(StandardLocation.CLASS_OUTPUT, dir);
				jfm.setLocation(StandardLocation.SOURCE_OUTPUT, dir);
			}
			
			// Set source code locations to the input source paths
			{
				// Add standard files to the source path
				Set<File> dir = new TreeSet<>();
				for (Path p : __src)
					dir.add(p.toFile());
				
				// Set source location
				jfm.setLocation(StandardLocation.SOURCE_PATH, dir);
			}
			
			// Source code to compile
			Set<JavaFileObject> ccthese = new TreeSet<>(
				new Comparator<JavaFileObject>()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/09/18
					 */
					@Override
					public int compare(JavaFileObject __a, JavaFileObject __b)
					{
						return __a.toUri().compareTo(__b.toUri());
					}
				});
			
			// Setup base JAR content to include the bootstrap's manifest
			Map<String, Path> jarthese = new TreeMap<>();
			jarthese.put("META-INF/MANIFEST.MF", BOOTSTRAP_MANIFEST);
			
			// Determine the files to get compiled or placed in the JAR
			{
				// Wall all source directories
				for (Path p : __src)
				{
					// Walk this set
					Set<Path> maybe = new HashSet<>();
					__walk(maybe, p);
					
					// Go through and determine which ones are to be compiled
					// and which ones get JARed
					for (Path s : maybe)
					{
						String fn = s.getFileName().toString();
						
						// Compile?
						if (fn.endsWith(".java"))
						{
							for (JavaFileObject o :
								jfm.getJavaFileObjects(s.toFile()))
								ccthese.add(o);
						}
						
						// JAR it, do not consider manifests however
						else if (!Files.isDirectory(s))
						{
							String zn = __zipName(p, s);
							if (zn.equals("META-INF/MANIFEST.MF"))
								continue;
							jarthese.put(zn, s);
						}
					}
				}
			}
			
			// Debug
			System.err.printf("DEBUG -- CC: %s || JAR: %s%n", ccthese,
				jarthese);
			
			throw new Error("TODO");
		}
		
		// Clear temporary files
		finally
		{
			if (tempdir != null)
			{
				// Deletion queue, files are deleted from last to first
				NavigableSet<Path> delete = new TreeSet<>();
				
				// Walk directories
				__walk(delete, tempdir);
				
				// Delete them all
				Iterator<Path> it = delete.descendingIterator();
				while (it.hasNext())
				{
					Path p = it.next();
					
					// Delete directory
					if (Files.isDirectory(p))
						System.err.printf("DEBUG -- Delete dir %s%n", p);
					
					// Delete file
					else
						System.err.printf("DEBUG -- Delete file %s%n", p);
				}
			}
		}
	}
	
	/**
	 * Launches the bootstrap with the proxy classes which are used to
	 * specify the compiler to access along with the native launch system.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/09/18
	 */
	private void __launch()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Locates all source directories which are associated with the bootstrap
	 * and all dependencies.
	 *
	 * @return An array containing the set of source directories that are
	 * considered part of the bootstrap.
	 * @throws IOException On read errors.
	 * @since 2016/09/18
	 */
	private Path[] __locateSources()
		throws IOException
	{
		// Setup return value
		Set<Path> rv = new TreeSet<>();
		
		// Queue dependencies and parse them
		Set<String> did = new HashSet<>();
		Deque<String> q = new ArrayDeque<>();
		q.push(BOOTSTRAP_PROJECT);
		while (!q.isEmpty())
		{
			// Grab
			String p = q.remove();
			
			// If it was done already or is ignored, do not handle it
			if (did.contains(p) || __ignoreProject(p))
				continue;
			
			// The source directory is included in compilation
			Path srcdir = SOURCE_ROOT.resolve(p);
			rv.add(srcdir);
			
			// Obtain the path to the manifest
			Path manpath = srcdir.resolve("META-INF").resolve("MANIFEST.MF");
			
			// Try to open it
			try (InputStream is = Channels.newInputStream(
				FileChannel.open(manpath, StandardOpenOption.READ)))
			{
				// Decode the manifest
				Manifest man = new Manifest(is);
				
				// Get the dependencies for this, if they exist, if they do
				// then add them to the queue for handling
				String deps = man.getMainAttributes().
					getValue("x-squirreljme-depends");
				if (deps != null)
					for (String d : deps.split(Pattern.quote(" ")))
						q.push(d);
			}
		}
		
		// Return them
		return rv.<Path>toArray(new Path[rv.size()]);
	}
	
	/**
	 * Checks if the bootstrap is out of date.
	 *
	 * @param __src The sources to check.
	 * @return {@code true} if the bootstrap is out of date and needs to be
	 * built.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private boolean __outOfDate(Path[] __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// If the output JAR does not exist then it is always out of date
		if (!Files.exists(BOOTSTRAP_JAR_PATH))
			return true;
		
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point for the bootstrap bootstrapper.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
	{
		// Create and run
		new Bootstrap(__args).run();
	}
	
	/**
	 * Checks if the specified project name is to be ignored so that it is
	 * not included for compilation of the bootstrap.
	 *
	 * @param __s The project to check if it is ignored.
	 * @return {@code true} if the project is to be ignored.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private static boolean __ignoreProject(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s)
		{
				// Uses the host Java SE library, so these do not need to
				// be compiled at all
			case "cldc-compact":
			case "cldc-full":
				return true;
			
				// Do not ignore
			default:
				return false;
		}
	}
	
	/**
	 * Walks the given path and adds all files recursively to the given set.
	 *
	 * @param __dest The destination set.
	 * @param __p The path to walk.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private static void __walk(Set<Path> __dest, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__dest == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Walk through the file tree
		Deque<Path> q = new ArrayDeque<>();
		q.push(__p);
		while (!q.isEmpty())
		{
			// Add to the set always
			Path p = q.remove();
			__dest.add(p);
			
			// If it is a directory then add files inside of it to
			// the queue
			if (Files.isDirectory(p))
				try (DirectoryStream<Path> ds =
					Files.newDirectoryStream(p))
				{
					for (Path s : ds)
						q.push(s);
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
	
	/**
	 * This acts as a proxy for the bootstrap system so it has access to the
	 * system's Java compiler.
	 *
	 * @since 2016/09/18
	 */
	private class __CompilerProxy__
		implements InvocationHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/09/18
		 */
		@Override
		public Object invoke(Object __p, Method __m, Object[] __args)
			throws Throwable
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This acts as a proxy for the bootstrap launcher system so that JARs
	 * can be loaded and execute natively.
	 *
	 * @since 2016/09/18
	 */
	private class __LauncherProxy__
		implements InvocationHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/09/18
		 */
		@Override
		public Object invoke(Object __p, Method __m, Object[] __args)
			throws Throwable
		{
			throw new Error("TODO");
		}
	}
}

