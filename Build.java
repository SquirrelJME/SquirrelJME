// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.lang.model.SourceVersion;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * This is a simple and basic build system.
 *
 * The system property "project.root" defines the root directory where
 * SquirrelJME's code exists. Projects and such are searched and compiled
 * from the "src" directory from the given root.
 *
 * Each project requires a manifest (META-INF/MANIFEST.MF) file. The manifest
 * must contain the following attributes:
 *
 * X-SquirrelJME-Name -- The name of the project.
 * LIBlet-Title       -- The title of the project (a short nice name).
 * LIBlet-Vendor      -- The creator of the project.
 * LIBlet-Version     -- The version of the project.
 *
 * The following attributes are optional.
 *
 * X-SquirrelJME-Depends -- Other packages (separated by comma) which this
 *                          project depends on for compilation.
 * X-SquirrelJME-Optional -- Optional dependencies which are not always needed
 *                           at runtime.
 * X-SquirrelJME-HostClassPath -- Use the host classpath when building the
 *                                specified package rather than the target one.
 *
 * @since 2016/03/21
 */
public class Build
{
	/** Project root directory. */
	public static final Path PROJECT_ROOT;
	
	/** Path separator. */
	public static final String PATH_SEPARATOR;
	
	/** This is the name of the interpreter that is used. */
	public static final String INTERPRETER_NAME =
		Objects.toString(System.getProperty("net.multiphasicapps.interpreter"),
			"native-compiler-interpreter");
	
	/** Build JARs with no compression? */
	private static volatile boolean _nocompression;
	
	/** This is a cached set of known projects. */
	protected final Map<String, Project> projects =
		new HashMap<>();
	
	/**
	 * Obtain the project root.
	 *
	 * @since 2016/03/21
	 */
	static
	{
		// Get the root and make sure it exists
		PROJECT_ROOT = Paths.get(Objects.<String>requireNonNull(
			System.getProperty("project.root"),
			"The system property `project.root` was not specified."));
		
		// Path separator
		PATH_SEPARATOR = File.pathSeparator;
	}
	
	/**
	 * Initializes the builder.
	 *
	 * @since 2016/03/21
	 */
	public Build()
	{
	}
	
	/**
	 * Obtains the given project.
	 *
	 * @param __n The name of the project to get.
	 * @return The project description.
	 * @throws IllegalStateException If no project exists with that name or
	 * it is an invalid project.
	 * @since 2016/03/21
	 */
	public Project getProject(String __n)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException();
		
		// Already known?
		Project rv = projects.get(__n);
		if (rv != null)
			return rv;
		
		// Setup new project
		try
		{
			projects.put(__n, (rv = new Project(PROJECT_ROOT.resolve("src").
				resolve(__n))));
		}
		
		// Report a bad project
		catch (RuntimeException|Error e)
		{
			// Report it
			System.err.println("Illegal Project: " + __n);
			
			// Toss it
			throw e;
		}
		
		// Change names?
		if (!__n.equals(rv.name))
			throw new IllegalArgumentException(String.format(
				"Project `%s` changed name to `%s`.", __n, rv.name));
		
		// Return it
		return rv;
	}
	
	/**
	 * Invokes the build system.
	 *
	 * @param __args Argument queue.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public void invoke(Deque<String> __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException();
		
		// Get the command to use
		String command = __args.pollFirst();
		if (command == null)
			command = "target";
		
		// Depends on the command
		Project pp;
		int terptarget = 0;
		switch (command)
		{
				// Run tests on the host or interpreter
			case "interpreter-interpreter-tests":
				terptarget++;
			case "interpreter-tests":
				terptarget++;
			case "host-tests":
				__launch(terptarget, getProject("test-all"), __args);
				break;
				
				// Target a specific system (with optional interpreter)
			case "interpreter-interpreter-target":
				terptarget++;
			case "interpreter-target":
				terptarget++;
			case "target":
				// Build hairball
				__build((pp = getProject("hairball")));
				
				// Add output and source directories
				__args.offerFirst(PROJECT_ROOT.resolve("src").toString());
				__args.offerFirst(System.getProperty("user.dir"));
				
				// Launch it
				__launch(terptarget, pp, __args);
				break;
			
				// Launch a program
			case "interpreter-interpreter-launch":
				terptarget++;
			case "interpreter-launch":
				terptarget++;
			case "launch":
				__launch(terptarget, getProject(__args.removeFirst()), __args);
				break;
				
				// Run SquirrelJME launcher
			case "squirreljme":
				__launch(0, getProject("launcher-jvm-javase"), __args);
				break;
				
				// Build a project
			case "build":
				__build(getProject(__args.removeFirst()));
				break;
			
				// Unknown
			default:
				throw new IllegalArgumentException(command);
		}
	}
	
	/**
	 * Builds the given project.
	 *
	 * @param __p The project to build.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __build(final Project __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException();
		
		// Build dependencies first!
		for (Project dep : __p.depends)
			__build(dep);
		
		// If the project is not out of date then do not build it
		if (!__p.isOutOfDate())
			return;
			
		// Use the system's Java compiler
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		if (javac == null)
			throw new NullPointerException("Your run-time does not have a " +
				"Java compiler available.");

		// Must support Java 7
		if (!javac.getSourceVersions().contains(SourceVersion.RELEASE_7))
			throw new IllegalStateException("You have a Java compiler " +
				"available in your run-time, however it does not support " +
				"Java 7.");
          
		// Note it
		System.err.printf("*** Building %s...%n", __p.name);
		
		// File manager
		final StandardJavaFileManager jfm = javac.getStandardFileManager(null,
			null, null);
		
		// Go through the source tree and determine files to compile and ones
		// to add to the JAR
		Path tempdir = null;
		try
		{
			// Create temporary directory where compiled code goes for a bit
			tempdir = Files.createTempDirectory(
				"squirreljme-build-" + __p.name);
			Iterable<File> tdfi = Collections.<File>singleton(
				tempdir.toFile());
			
			// Add source tree to the file root
			jfm.setLocation(StandardLocation.SOURCE_PATH,
				Collections.<File>singleton(__p.root.toFile()));
			
			// Output everything here
			jfm.setLocation(StandardLocation.SOURCE_OUTPUT, tdfi);
			jfm.setLocation(StandardLocation.CLASS_OUTPUT, tdfi);
			
			// The class path is that of the dependencies
			Set<File> ccpath = new HashSet<>();
			__p.classPathFile(ccpath);
			jfm.setLocation(StandardLocation.CLASS_PATH, ccpath);
			
			// Unless the host classpath was specified, use the classpath
			// for the package as the platform one.
			if (!__p.hostclasspath)
				jfm.setLocation(StandardLocation.PLATFORM_CLASS_PATH, ccpath);
			
			// Files to compile
			final Set<JavaFileObject> compile = new HashSet<>();
			
			// Files to go in the ZIP
			final Map<String, Path> zipup = new TreeMap<>();
			
			// Walk the source tree
			walk(__p.root, new Processor<Path>()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/03/21
					 */
					@Override
					public boolean process(Path __q)
					{
						// Ignore directories
						if (Files.isDirectory(__q))
							return true;
						
						// Get the file name
						String fname = __q.getFileName().toString();
						
						// Ignore manifests
						if (fname.equals("MANIFEST.MF"))
							return true;
						
						// If it ends in Java, compile it
						else if (fname.endsWith(".java"))
							for (JavaFileObject f : jfm.getJavaFileObjects(
								__q.toFile()))
								compile.add(f);
						
						// Otherwise add it to the JAR
						else
							zipup.put(zipName(__p.root, __q), __q);
						
						// Keep going
						return true;
					}
				});
			
			// Only compile if there are files
			if (!compile.isEmpty())
			{
				// Setup Java Compiler
				JavaCompiler.CompilationTask task = javac.getTask(null,
					jfm, null, Arrays.<String>asList("-source", "1.7",
						"-target", "1.7", "-g", "-Xlint:deprecation",
						"-Xlint:unchecked"), null,
						compile);
				
				// Execute
				if (!task.call())
					throw new RuntimeException("Compilation failed!");
			}
			
			// Walk the temporary directory to get the compile classes
			final Path tdx = tempdir;
			walk(tdx, new Processor<Path>()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/03/21
					 */
					@Override
					public boolean process(Path __q)
					{
						// Ignore directories
						if (Files.isDirectory(__q))
							return true;
						
						// Add to the JAR
						zipup.put(zipName(tdx, __q), __q);
						
						// Keep going
						return true;
					}
				});
			
			// Create manifest and use the existing attributes
			Manifest jman = new Manifest();
			Attributes jattr = jman.getMainAttributes();
			jattr.putAll(__p.attr);
			
			// Make sure manifest version is specified
			jattr.put(Attributes.Name.MANIFEST_VERSION, "1.0");
			
			// Walk through dependencies
			StringBuilder jcp = new StringBuilder();
			int libnum = 1;
			for (Project dep : __p.depends)
			{
				// Required or optional
				boolean isopt = __p.optional.contains(dep);
				
				// Add liblet dependency
				jattr.putValue(String.format("LIBlet-Dependency-%d", libnum),
					String.format("liblet;%s;%s;%s;%s+", (isopt ? "optional" :
						"required"), dep.libtitle, dep.libvendor,
						dep.libversion));
				
				// Add to the classpath
				jcp.append(' ');
				jcp.append(dep.name + ".jar");
				
				// Increase library number
				libnum++;
			}
			
			// Set class path
			jattr.put(Attributes.Name.CLASS_PATH, jcp.toString());
			
			// If ZIP creation or move fails, then delete it.
			final Path tempzip = Files.createTempFile(
				"squirreljme-build-" + __p.name, ".jar");
			try
			{
				// Calculate the CRC
				CRC32 crc = new CRC32();
				
				// Write JAR file
				boolean nocomp = _nocompression;
				try (JarOutputStream jos = new JarOutputStream(
					new FileOutputStream(tempzip.toFile()), jman))
				{
					// Compression method
					jos.setMethod((nocomp ? JarOutputStream.STORED :
						JarOutputStream.DEFLATED));
				
					// Go through all files to add
					for (Map.Entry<String, Path> e : zipup.entrySet())
					{
						// Make entry for it
						ZipEntry ze = new ZipEntry(e.getKey());
						
						// When using STORED, some details about the file
						// needs to be set
						if (nocomp)
						{
							// Load all file bytes
							byte data[] = Files.readAllBytes(e.getValue());
							
							// Calculate the CRC
							crc.reset();
							crc.update(data);
							
							// Set the sizes
							ze.setSize(data.length);
							ze.setCompressedSize(data.length);
							ze.setCrc(crc.getValue());
							
							// Put it
							jos.putNextEntry(ze);
							
							// Write the data
							jos.write(data, 0, data.length);
						}
						
						// Using compression
						else
						{
							// Put it
							jos.putNextEntry(ze);
					
							// Copy file to the stream
							Files.copy(e.getValue(), jos);
						}
					
						// End entry
						jos.closeEntry();
					}
				
					// Finish
					jos.finish();
					jos.flush();
				}
			
				// Move to the real JAR location
				Files.move(tempzip, __p.jarname,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// Could not write it out
			catch (IOException|RuntimeException|Error e)
			{
				// Delete temporary JAR
				try
				{
					Files.delete(tempzip);
				}
				
				// On read/write errors
				catch (IOException xioe)
				{
					xioe.printStackTrace();
				}
				
				// Toss it again
				throw e;
			}
		}
		
		// Failed read/write
		catch (IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
		
		// Delete temporary directory always
		finally
		{
			// Delete it
			if (tempdir != null)
				try
				{
					// Delete all files
					walk(tempdir, new Processor<Path>()
						{
							/**
							 * {@inheritDoc}
							 * @since 2016/03/21
							 */
							@Override
							public boolean process(Path __q)
							{
								// Delete it
								try
								{
									// Delete it
									Files.delete(__q);
								}
								
								// Failed to delete
								catch (IOException ioe)
								{
									ioe.printStackTrace();
								}
								
								// Keep going
								return true;
							}
						});
					
					// Delete the temporary directory
					Files.delete(tempdir);
				}
				
				// Ignore
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
		}
	}
	
	/**
	 * Launches the given project.
	 *
	 * @param __interp Run this project using the internal interpreter?
	 * @param __p The project to launch.
	 * @param __args The program arguments
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __launch(int __interp, Project __p, String... __args)
		throws NullPointerException
	{
		__launch(__interp, __p,
			new LinkedList<>(Arrays.<String>asList(__args)));
	}
	
	/**
	 * Launches the given project.
	 *
	 * @param __interp Run this project using the internal interpreter?
	 * @param __p The project to launch.
	 * @param __args The program arguments
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __launch(int __interp, Project __p, Deque<String> __args)
		throws NullPointerException
	{
		// Check
		if (__p == null || __args == null)
			throw new NullPointerException("NARG");
		
		// If using the interpreter, build it
		if (__interp >= 1)
			__build(getProject(INTERPRETER_NAME));
		
		// Build the target to be ran
		__build(__p);
		
		// Calculate all dependencies which are needed for execution
		Set<Path> classpath = new LinkedHashSet<>();
		__p.classPath(classpath);
		
		// Determine the main class used for execution
		String mainclass = __p.mainClass();
		if (mainclass == null)
			throw new IllegalArgumentException("Could not find Main-Class " +
				"attribute in the JAR or in any of its dependencies.");
		
		// If interpreted, forward to normal code
		if (__interp >= 1)
		{
			// Build new argument set
			Deque<String> newargs = new LinkedList<>();
			
			// The classpath is special
			StringBuilder sb = new StringBuilder();
			for (Path p : classpath)
			{
				// Append separator?
				if (sb.length() > 0)
					sb.append(PATH_SEPARATOR);
				
				// Add path
				sb.append(p);
			}
			
			// Set the classpath
			newargs.offerLast("-classpath");
			newargs.offerLast(sb.toString());
			
			// Main class
			newargs.offerLast(mainclass);
			
			// Normal arguments
			while (!__args.isEmpty())
				newargs.offerLast(__args.pollFirst());
			
			// Call non-interpreted version
			__launch(__interp - 1, getProject(INTERPRETER_NAME),
				newargs);
		}
		
		// Otherwise, run it native
		else
			try
			{
				// Setup URLs
				URL[] urls = new URL[classpath.size()];
				int u = 0;
				for (Path p : classpath)
					urls[u++] = p.toUri().toURL();
			
				// Setup class loader
				URLClassLoader ucl = new URLClassLoader(urls);
				
				// Set the context class loader which is used by ServiceLoader,
				// otherwise services will not be found
				Thread.currentThread().setContextClassLoader(ucl);
			
				// Find the main class
				Class<?> startclass = Class.forName(mainclass, false, ucl);
				
				// Find the main method
				Method themain = startclass.getDeclaredMethod("main",
					String[].class);
				
				// Execute it
				String[] xargs = __args.<String>toArray(
					new String[__args.size()]);
				themain.invoke(null, (Object)xargs);
			}
			
			// Called code failed to invoke
			catch (InvocationTargetException e)
			{
				Throwable c = e.getCause();
				
				// Unchecked exceptions?
				if (c instanceof RuntimeException)
					throw (RuntimeException)c;
				else if (c instanceof Error)
					throw (Error)c;
				
				// Otherwise wrap the original
				throw new RuntimeException(e);
			}
			
			// Could not execute the main class
			catch (MalformedURLException|ReflectiveOperationException e)
			{
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * Returns the date of the given path.
	 *
	 * @param __p Path to get the date of.
	 * @return The date of the path.
	 * @since 2016/03/21
	 */
	public static long dateOf(Path __p)
	{
		// Get the date of the last change
		try
		{
			FileTime ft = Files.getLastModifiedTime(__p);
			if (ft == null)
				return Long.MIN_VALUE;
			return ft.toMillis();
		}
		
		// Failed to read the date
		catch (IOException ioe)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/03/21
	 */
	public static void main(String... __args)
	{
		// Need commands?
		if (__args == null)
			__args = new String[0];
		
		// Fill in arguments to a queue
		Deque<String> args = new LinkedList<>(Arrays.<String>asList(__args));
		
		// If the first argument is -0, then disable ZIP compression
		if ("-0".equals(args.peekFirst()))
		{
			args.removeFirst();
			_nocompression |= true;
		}
		
		// Setup builder
		Build b = new Build();
		
		// Send arguments to the builder
		b.invoke(args);
	}
	
	/**
	 * Walks a directory and runs a processor on the paths.
	 *
	 * @param __dir The directory to walk over.
	 * @param __proc The processor for path elements.
	 * @return {@code true} if processing continues, otherwise {@code false}
	 * will stop the walk.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public static boolean walk(Path __dir, Processor<Path> __proc)
		throws IOException, NullPointerException
	{
		// Check
		if (__dir == null || __proc == null)
			throw new NullPointerException();
		
		// Open the stream
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__dir))
		{
			// Go through individual files
			for (Path p : ds)
			{
				// Recurse into directories first (depth first)
				if (Files.isDirectory(p))
					if (!walk(p, __proc))
						return false;
				
				// Work on the file
				if (!__proc.process(p))
					return false;
			}
		}
		
		// Done
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
	public static String zipName(Path __root, Path __p)
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
	 * This represents a project which may be built.
	 *
	 * @since 2016/03/21
	 */
	public class Project
	{
		/** The root directory of the project. */
		protected final Path root;
		
		/** Manifest attributes. */
		protected final Attributes attr;
		
		/** Project name. */
		protected final String name;
		
		/** Dependencies. */
		protected final Set<Project> depends;
		
		/** Optional dependencies. */
		protected final Set<Project> optional;
		
		/** The name of the output JAR file. */
		protected final Path jarname;
		
		/** The library title. */
		protected final String libtitle;
		
		/** The library vendor. */
		protected final String libvendor;
		
		/** The library version. */
		protected final String libversion;
		
		/** Use the host class path? */
		protected final boolean hostclasspath;
		
		/** Source code date. */
		private volatile long _sourcedate =
			Long.MIN_VALUE;
		
		/**
		 * Initializes the project.
		 *
		 * @param __root The root directory of the project.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/21
		 */
		private Project(Path __root)
			throws NullPointerException
		{
			// Check
			if (__root == null)
				throw new NullPointerException();
			
			// Set
			root = __root;
			
			// Load manifest
			Manifest man;
			try (InputStream is = new FileInputStream(root.
				resolve("META-INF").resolve("MANIFEST.MF").toFile()))
			{
				man = new Manifest(is);
			}
			
			// Failed to read
			catch (IOException ioe)
			{
				throw new IllegalArgumentException(ioe);
			}
			
			// Get main attributes
			attr = man.getMainAttributes();
			
			// Get project name
			name = Objects.<String>requireNonNull(
				attr.getValue("X-SquirrelJME-Name"), "Missing package name.");
			
			// Required dependencies
			Set<Project> xdeps = new HashSet<>();
			String odeps = attr.getValue("X-SquirrelJME-Depends");
			if (odeps != null)
				for (String s : odeps.split(Pattern.quote(",")))
					xdeps.add(getProject(s.trim()));
			
			// Optional depends
			Set<Project> pdeps = new HashSet<>();
			String qdeps = attr.getValue("X-SquirrelJME-Optional");
			if (qdeps != null)
				for (String s : qdeps.split(Pattern.quote(",")))
					pdeps.add(getProject(s.trim()));
			
			// All force depends
			xdeps.addAll(pdeps);
			
			depends = Collections.<Project>unmodifiableSet(xdeps);
			optional = Collections.<Project>unmodifiableSet(pdeps);
			
			// These must exist for all projects
			libtitle = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Title"), "Missing library title.");
			libvendor = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Vendor"), "Missing library vendor.");
			libversion = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Version"), "Missing library version.");
			
			// Use the host class path?
			String hcps = attr.getValue("X-SquirrelJME-HostClassPath");
			hostclasspath = Boolean.valueOf(hcps);
			
			// Determine JAR name
			jarname = Paths.get(name + ".jar");
		}
		
		/**
		 * Returns the set of all projects along with their dependencies.
		 *
		 * @return The set of all projects and their dependencies.
		 * @since 2016/04/13
		 */
		public Set<Project> allProjects()
		{
			return allProjects(new LinkedHashSet<Project>());
		}
		
		/**
		 * Adds all projects and their dependencies to the given set.
		 *
		 * @param __into The set to place into.
		 * @return {@code __into}.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/13
		 */
		public Set<Project> allProjects(Set<Project> __into)
			throws NullPointerException
		{
			// Check
			if (__into == null)
				throw new NullPointerException("NARG");
				
			// Add self
			__into.add(this);
			
			// Add dependencies
			for (Project dep : depends)
				dep.allProjects(__into);
			
			// Return it
			return __into;
		}
		
		/**
		 * Calculates the classpath used for execution.
		 *
		 * @param __cp The classpath to use for execution.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/21
		 */
		public void classPath(Set<Path> __cp)
			throws NullPointerException
		{
			// Check
			if (__cp == null)
				throw new NullPointerException("NARG");
			
			// Add dependencies
			for (Project dep : depends)
			{
				// Add dependency JAR
				__cp.add(dep.jarname);
				
				// Recurse
				dep.classPath(__cp);
			}
			
			// Add self
			__cp.add(jarname);
		}
		
		/**
		 * Calculates the classpath used for compilation.
		 *
		 * @param __cp The classpath to use for compilation.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/21
		 */
		public void classPathFile(Set<File> __cp)
			throws NullPointerException
		{
			// Check
			if (__cp == null)
				throw new NullPointerException();
			
			// Add dependencies
			for (Project dep : depends)
			{
				// Add dependency JAR
				__cp.add(dep.jarname.toFile());
				
				// Recurse
				dep.classPathFile(__cp);
			}
		}
		
		/**
		 * Checks if this project is out of date.
		 *
		 * @return {@code true} if it is out of date.
		 * @since 2016/03/21
		 */
		public boolean isOutOfDate()
		{
			// Check some things.
			try
			{
				// If the JAR is missing then it is out of date
				if (!Files.exists(jarname))
					return true;
				
				// Get time of the JAR file
				long jartime = dateOf(jarname);
				
				// Check if a dependency is newer than this JAR
				for (Project dep : depends)
					if (dateOf(dep.jarname) > jartime)
						return true;
				
				// Cached source code time?
				long srct = _sourcedate;
				if (srct != Long.MIN_VALUE)
					return (srct > jartime);
				
				// Get the oldest file in the source tree
				walk(root, new Processor<Path>()
					{
						/**
						 * {@inheritDoc}
						 * @since 2016/03/21
						 */
						@Override
						public boolean process(Path __p)
						{
							// Ignore directories
							if (Files.isDirectory(__p))
								return true;
							
							// Get file date
							long ftime = dateOf(__p);
							
							// If newer, use that
							if (ftime > _sourcedate)
								_sourcedate = ftime;
							
							// Keep going
							return true;
						}
					});
				
				// A source is newer than the JAR?
				return (_sourcedate > jartime);
			}
			
			// Failed read
			catch (IOException ioe)
			{
				throw new RuntimeException(ioe);
			}
		}
		
		/**
		 * Returns the class which execution starts at.
		 *
		 * @return The starting class or {@code null} if there is none.
		 * @since 2016/03/21
		 */
		public String mainClass()
		{
			// Does the attribute have it?
			String main = attr.getValue("Main-Class");
			if (main != null)
				return main;
			
			// Otherwise look in depedencides
			for (Project dep : depends)
			{
				// Does it have it?
				main = dep.mainClass();
				if (main != null)
					return main;
			}
			
			// Not found
			return null;
		}
	}
	
	/**
	 * This is a processor callback.
	 *
	 * @since 2016/03/21
	 */
	public static interface Processor<X>
	{
		/**
		 * Processes the given item.
		 *
		 * @param __p The item to process.
		 * @return If {@code true} then processing continues, otherwise it
		 * stops.
		 * @since 2016/03/21
		 */
		public abstract boolean process(X __p);
	}
}

