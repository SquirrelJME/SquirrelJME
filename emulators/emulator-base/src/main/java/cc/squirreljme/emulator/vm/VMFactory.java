// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.jvm.launch.Application;
import cc.squirreljme.jvm.launch.AvailableSuites;
import cc.squirreljme.jvm.launch.SuiteScanner;
import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.SystemPathProvider;
import cc.squirreljme.vm.DataContainerLibrary;
import cc.squirreljme.vm.JarClassLibrary;
import cc.squirreljme.vm.NameOverrideClassLibrary;
import cc.squirreljme.vm.SummerCoatJarLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.ServiceLoader;

/**
 * This class is used to initialize virtual machines based on a set of factory
 * classes.
 *
 * This was added because there were many cases where tons of code was
 * duplicated to initialize the virtual machine, which were effectively
 * copy and pasted to each other so this will remove that.
 *
 * This uses the service loader to locate factories which are available.
 *
 * @since 2018/11/17
 */
public abstract class VMFactory
{
	/** Default main class when acting standalone. */
	private static final String STANDALONE_MAIN_CLASS =
		"X-SquirrelJME-Standalone-Main-Class";
	
	/** Default parameter. */
	private static final String STANDALONE_PARAMETER =
		"X-SquirrelJME-Standalone-Parameter";
	
	/** Default class path when acting standalone. */
	private static final String STANDALONE_CLASSPATH =
		"X-SquirrelJME-Standalone-Classpath";
	
	/** Standalone library. */
	private static final String STANDALONE_LIBRARY =
		"X-SquirrelJME-Standalone-Library";
	
	/** Internal JAR directory root. */
	private static final String STANDALONE_DIRECTORY =
		"X-SquirrelJME-Standalone-Internal-Jar-Root";
	
	/** Internal JAR directory root (debug). */
	private static final String STANDALONE_DIRECTORY_DEBUG =
		"X-SquirrelJME-Standalone-Internal-Debug-Jar-Root";
	
	/** The separator character. */
	private static final char SEPARATOR_CHAR;
	
	/** The name of the VM implementation. */
	protected final String name;
	
	static
	{
		// Determine the path separator character
		String sepString = System.getProperty("path.separator");
		SEPARATOR_CHAR = (sepString == null || sepString.isEmpty() ? ':' :
			sepString.charAt(0));
	}
	
	/**
	 * Initializes the factory.
	 *
	 * @param __name The name of the virtual machine.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	public VMFactory(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * Creates the virtual machine using the given parameters.
	 *
	 * @param __profiler The profiler snapshot to write to.
	 * @param __jdwp The debugger to use.
	 * @param __threadModel The threading model to use.
	 * @param __suiteManager The suite manager.
	 * @param __classpath The classpath to initialize with.
	 * @param __mainClass The main class to start executing.
	 * @param __sysProps System properties for the running program.
	 * @param __args Arguments for the running program.
	 * @return An instance of the virtual machine.
	 * @throws IllegalArgumentException If an input argument is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine could not be created.
	 * @since 2018/11/17
	 */
	protected abstract VirtualMachine createVM(ProfilerSnapshot __profiler,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel,
		VMSuiteManager __suiteManager, VMClassLibrary[] __classpath,
		String __mainClass, Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException;
	
	/**
	 * Main entry point for general programs.
	 *
	 * @param __args Arguments.
	 * @since 2020/02/29
	 */
	public static void main(String... __args)
	{
		// Default settings
		String vmName = "springcoat";
		Path snapshotPath = null;
		Collection<String> libraries = new LinkedList<>();
		Collection<String> suiteClasspath = new LinkedList<>();
		Map<String, String> systemProperties = new LinkedHashMap<>();
		
		// There always is a profiler being run, just differs if we save it
		ProfilerSnapshot profilerSnapshot = new ProfilerSnapshot();
		
		// Debugging host and port, if enabled
		String jdwpHost = null; 
		int jdwpPort = -1;
		boolean internalDebug = false;
		boolean internalDebugFork = true;
		
		// Threading model
		VMThreadModel threadModel = VMThreadModel.DEFAULT;
		
		// Load our own META-INF/MANIFEST.MF for some special properties
		JavaManifest metaManifest = null;
		try (InputStream in = VMFactory.class
			.getResourceAsStream("/META-INF/MANIFEST.MF"))
		{
			Debugging.debugNote("GOT MANIFEST: %s", in);
			if (in != null)
				metaManifest = new JavaManifest(in);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Initial trace bits
		int initTraceBits = 0;
		
		// Was the -jar switch used?
		boolean didJar = false;
		String rawJarPath = null;
		String rawJarEntry = null;
		
		// Clutter level of the library
		String clutterLevel = "release";
		
		// Load in standard system properties
		VMFactory.__standardSysProps(systemProperties);
		
		// Load in standard paths
		VMFactory.__standardPaths(libraries);
		
		// Command line format is:
		// -Xemulator:(vm)
		// -Xsnapshot:(path-to-nps)
		// -Xentry:id
		// -Xlibraries:(class:path:...)
		// -Xjdwp:[hostname]:port
		// -Xthread:(single|coop|multi|smt)
		// -Dsysprop=value
		// -classpath (class:path:...)
		// -Xclutter:(release|debug)
		// -Xtrace:(flag|...)
		// -zero
		// -client
		// -server
		// -XstartOnFirstThread
		// -Xscritchui:(ui)
		// Optionally `-jar`
		// Main-class
		// Arguments...
		Deque<String> queue = new LinkedList<>(Arrays.<String>asList(__args));
		while (!queue.isEmpty())
		{
			// End of our items?
			String item = queue.peekFirst();
			if (item == null || item.isEmpty() || item.charAt(0) != '-')
				break;
			
			// Eat it up
			queue.removeFirst();
			
			// Thread model
			if (item.startsWith("-Xthread:"))
				threadModel = VMThreadModel.of(
					item.substring("-Xthread:".length()));
			
			// JDWP Usage
			else if (item.startsWith("-Xjdwp:"))
			{
				String hostPort = item.substring("-Xjdwp:".length());
				
				// Figure the hostname/port split
				int lastCol = hostPort.lastIndexOf(':');
				if (lastCol < 0)
					throw new IllegalArgumentException(String.format(
						"Expected %s to be like -Xjdwp:[hostname]:port.",
						item));
				
				// Split hostname and port
				jdwpHost = hostPort.substring(0, lastCol);
				jdwpPort = Integer.parseInt(
					hostPort.substring(lastCol + 1));
			}
			
			// Direct debugger usage
			else if (item.startsWith("-Xdebug") ||
				item.startsWith("-Xdebug:"))
			{
				// Just set this flag
				internalDebug = true;
				
				// Should the debugger be forked?
				int col = item.indexOf(':');
				if (col >= 0)
				{
					String param = item.substring(col + 1);
					
					if ("fork".equals(param))
						internalDebugFork = true;
					else if ("nofork".equals(param))
						internalDebugFork = false;
				}	
			}
			
			// Select a VM
			else if (item.startsWith("-Xemulator:"))
				vmName = item.substring("-Xemulator:".length());
			
			// VisualVM Snapshot Dump path
			else if (item.startsWith("-Xsnapshot:"))
				snapshotPath = Paths.get(
					item.substring("-Xsnapshot:".length()));
			
			// System property
			else if (item.startsWith("-D"))
			{
				int equalDx = item.indexOf('=');
				if (equalDx < 0)
					systemProperties.put(item.substring(2), "");
				else
					systemProperties.put(item.substring(2, equalDx),
						item.substring(equalDx + 1));
			}
			
			// Libraries to make available to the virtual machine
			else if (item.startsWith("-Xlibraries:"))
			{
				for (String entry : VMFactory.__unSeparateClassPath(
					item.substring(item.indexOf(':') + 1)))
					VMFactory.__addPaths(libraries, entry);
			}
			
			// Jar entry point selection
			else if (item.startsWith("-Xentry:"))
			{
				rawJarEntry = item.substring("-Xentry:".length());
			}
			
			// Initial trace options
			else if (item.startsWith("-Xtrace:"))
			{
				initTraceBits = VMTraceFlagTracker.parseBits(
					item.substring("-Xtrace:".length()));
			}
			
			// Clutter level to use
			else if (item.startsWith("-Xclutter:"))
				clutterLevel = item.substring("-Xclutter:".length());
			
			// JARs to load
			else if (item.equals("-classpath") || item.equals("-cp"))
			{
				// Get argument attached to this
				String strings = queue.pollFirst();
				if (strings == null)
					throw new IllegalArgumentException("Classpath missing.");
				
				// Extract path elements
				for (String entry : VMFactory.__unSeparateClassPath(strings))
					VMFactory.__addPaths(suiteClasspath, entry);
			}
			
			// Direct Jar launch
			else if (item.equals("-jar"))
			{
				// Get Jar attached to this
				String string = queue.pollFirst();
				if (string == null)
					throw new IllegalArgumentException(
						"Jar argument missing.");
				
				// We use this Jar
				rawJarPath = string;
				
				// We stop everything and just parse everything else as a Jar
				// directly...
				didJar = true;
				break;
			}
			
			// Alias for SpringCoat
			else if (item.equals("-zero") || item.equals("-Xint"))
				vmName = "springcoat";
			
			// Ignored
			else if (item.equals("-client") || item.equals("-server") ||
				item.equals("-XstartOnFirstThread"))
			{
				// Ignored
			}
			
			// ScritchUI library
			else if (item.startsWith("-Xscritchui:"))
			{
				systemProperties.put("cc.squirreljme.scritchui",
					item.substring("-Xscritchui:".length()));
			}
			
			// Unknown
			else
				throw new IllegalArgumentException(String.format(
					"Unknown command line switch: %s", item));
		}
		
		// These options are mutually exclusive...
		if (internalDebug && (jdwpHost != null || jdwpPort >= 1))
			throw new IllegalArgumentException(
				"-Xdebug and -Xjdwp are mutually exclusive.");
		
		// Main program arguments
		List<String> mainArgs = new LinkedList<>();
		
		// Default built in libraries, if available?
		if (metaManifest != null)
		{
			String defLib = metaManifest.getMainAttributes().getValue(
				VMFactory.STANDALONE_LIBRARY);
			if (defLib != null && !defLib.isEmpty())
				for (String entry : VMFactory.__unSeparateClassPath(defLib))
					VMFactory.__addPaths(libraries, entry);
		}
		
		// Did not do -jar, so do normal command line parse
		String mainClass;
		if (!didJar)
		{
			// Main class is here
			mainClass = queue.pollFirst();
			if (mainClass == null || mainClass.isEmpty())
			{
				// Try from the manifest
				if (metaManifest != null)
					mainClass = metaManifest.getMainAttributes().getValue(
						VMFactory.STANDALONE_MAIN_CLASS);
				
				// Still failed?
				if (mainClass == null || mainClass.isEmpty())
					throw new IllegalArgumentException(
						"No main class specified.");
				
				// Default class path for launching
				String defCp = metaManifest.getMainAttributes().getValue(
					VMFactory.STANDALONE_CLASSPATH);
				if (defCp != null && !defCp.isEmpty())
					for (String entry : VMFactory.__unSeparateClassPath(defCp))
						VMFactory.__addPaths(suiteClasspath, entry);
				
				// Default parameter?
				String defParam = metaManifest.getMainAttributes().getValue(
					VMFactory.STANDALONE_PARAMETER);
				if (defParam != null && !defParam.isEmpty())
					mainArgs.add(defParam);
			}
		}
		else
		{
			// Make sure this exists in the library path
			if (!libraries.contains(rawJarPath))
				libraries.add(rawJarPath);
			
			mainClass = null;
		}
		
		// Fill in the rest with the main argument calls
		while (!queue.isEmpty())
			mainArgs.add(queue.removeFirst());
		
		// Implicitly include all the classes specified as part of suites
		// to be part of the library path
		libraries.addAll(suiteClasspath);
		
		// Standalone directory, if one is passed through the JAR?
		ResourceBasedSuiteManager standaloneDir = null;
		if (metaManifest != null)
		{
			String prefix;
			if (Objects.equals("debug", clutterLevel))
				prefix = metaManifest.getMainAttributes()
					.getValue(VMFactory.STANDALONE_DIRECTORY_DEBUG);
			else
				prefix = metaManifest.getMainAttributes()
					.getValue(VMFactory.STANDALONE_DIRECTORY);
			
			// If it exists, use it!
			if (prefix != null)
				standaloneDir = new ResourceBasedSuiteManager(
					VMFactory.class, prefix);
		}
		
		// Found Jar library?
		VMClassLibrary jarLib = null;
		
		// Determine any suites that are available in the suite library
		Map<String, VMClassLibrary> suites = new LinkedHashMap<>();
		for (String library : libraries)
		{
			Path path = Paths.get(library);
			
			// Do not add the same libraries multiple times
			String normalName = VMFactory.__normalizeName(
				path.getFileName().toString());
			if (suites.containsKey(normalName))
				continue;
			
			// Note it
			Debugging.debugNote("Registering %s (%s)",
				normalName, path);
			
			// Is there a built-in resource based for this JAR itself?
			VMClassLibrary place;
			if (standaloneDir != null &&
				standaloneDir.loadLibrary(normalName) != null)
				place = standaloneDir.loadLibrary(normalName);
			
			// Treat SQCs special in that they have a specific resource for
			// their ROM data
			else if (SummerCoatJarLibrary.isSqc(path))
				place = new SummerCoatJarLibrary(path);
			else if (JarClassLibrary.isJar(path))
				place = JarClassLibrary.of(path);
			
			// Is just normalized data
			else
				place = new DataContainerLibrary(path);
			
			// Place in the class library, but make sure the name matches
			// the normalized name of the JAR
			VMClassLibrary target =
				new NameOverrideClassLibrary(place, normalName);
			suites.put(normalName, target);
			
			// Is this a Jar we are launching?
			if (rawJarPath != null)
				if (rawJarPath.equals(normalName) ||
					rawJarPath.equals(library))
					jarLib = target;
		}
		
		// Go through the class path and normalize the names so that it finds
		// the correct JAR files
		Collection<String> classpath = new LinkedList<>();
		for (String classItem : suiteClasspath)
			classpath.add(VMFactory.__normalizeName(
				Paths.get(classItem).getFileName().toString()));
		
		// Now that we loaded in all the libraries we can do the resolution
		// for the -jar switch
		if (didJar)
		{
			// Initialize fake shelf
			FakeJarPackageShelf fakeShelf = new FakeJarPackageShelf(suites);
			
			// No original launching Jar found?
			if (jarLib == null)
				throw new IllegalArgumentException(
					"Could not find the original Jar?");
			
			// Map to a fake jar
			JarPackageBracket fakeJar = new FakeJarPackageBracket(jarLib);
			
			// Setup suite scanner to use our fake suite list and combined
			// libraries accordingly, scan all suites to get available
			// applications we can potentially launch
			SuiteScanner scanner = new SuiteScanner(false, fakeShelf);
			AvailableSuites available = scanner.scanSuites();
			
			// Find applications for our Jar
			Application[] apps = available.findApplications(fakeJar);
			if (apps == null || apps.length == 0)
				throw new IllegalArgumentException("Found no applications " +
					"within jar: " + rawJarPath);
			
			// Debug note them
			for (int i = 0, n = apps.length; i < n; i++)
				Debugging.debugNote("Application %d: %s",
					i, apps[i].entryPoint());
			
			// Which index are we launching?
			int launchIndex;
			if (rawJarEntry == null)
				launchIndex = 0;
			else
			{
				// Mappable to integer?
				try
				{
					launchIndex = Integer.parseInt(rawJarEntry);
				}
				catch (NumberFormatException ignored)
				{
					launchIndex = 0;
					for (int i = 0, n = apps.length; i < n; i++)
					{
						Application app = apps[i];
						
						if (rawJarEntry.equals(
							app.entryPoint().name()) || rawJarEntry.equals(
							app.entryPoint().entryPoint()))
						{
							launchIndex = i;
							break;
						}
					}
				}
			}
			
			// Fill in launch information accordingly
			Application app = apps[launchIndex];
			EntryPoint appEntry = app.entryPoint();
			
			// There might need to be a helper for this
			mainClass = app.loaderEntryClass();
			if (mainClass == null)
				mainClass = appEntry.entryPoint();
			
			// Extract any needed system properties
			Map<String, String> wantProps = app.loaderSystemProperties();
			if (wantProps != null)
				systemProperties.putAll(wantProps);
			
			// Do we need special loader arguments to pass before this, so
			// it can correctly launch?
			String[] loaderArgs = app.loaderEntryArgs();
			if (loaderArgs != null && loaderArgs.length > 0)
				mainArgs.addAll(0, Arrays.asList(loaderArgs));
			
			// Need to use the classpath to run the jar with
			classpath.clear();
			for (JarPackageBracket jar : app.classPath())
			{
				// Get the original path
				String path = fakeShelf.libraryPath(jar);
				
				// Debug
				Debugging.debugNote("Adding into classpath: %s",
					path);
				
				// Add it
				classpath.add(path);
			}
		}
		
		// Run the VM, but always make sure we can
		int exitCode = -1;
		try
		{
			// Debug
			Debugging.debugNote("Starting virtual machine (in %s)...",
				mainClass);
			Debugging.debugNote("Args: %s", Arrays.asList(__args));
			
			// Run the VM
			VirtualMachine vm = VMFactory.mainVm(vmName,
				profilerSnapshot,
				(internalDebug ?
					VMFactory.__setupJdwpInternal(internalDebugFork) :
					(jdwpPort >= 1 ? VMFactory.__setupJdwp(jdwpHost,
						jdwpPort) : null)),
				threadModel,
				new ArraySuiteManager(suites.values()),
				classpath.<String>toArray(new String[classpath.size()]),
				mainClass,
				systemProperties,
				mainArgs.<String>toArray(new String[mainArgs.size()]));
			
			// Set global trace bits for the VM
			if (VMTraceFlagTracker.GLOBAL_TRACING_BITS != 0)
				vm.setTraceBits(true,
					VMTraceFlagTracker.GLOBAL_TRACING_BITS);
			
			// Set trace bits for the VM
			if (initTraceBits != 0)
				vm.setTraceBits(true,
					initTraceBits);
			
			// Run the virtual machine until it exits, but do not exit yet
			// because we want the snapshot to be created
			exitCode = vm.runVm();
		}
		
		// Always write the snapshot file
		finally
		{
			if (snapshotPath != null)
			{
				// Create directory where it goes
				try
				{
					Files.createDirectories(snapshotPath.getParent());
				}
				catch (IOException e)
				{
					// Ignore
				}
				
				// Write file
				try (OutputStream out = Files.newOutputStream(snapshotPath,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING))
				{
					profilerSnapshot.writeTo(out);
				}
				catch (IOException e)
				{
					// Ignore
				}
			}
		}
		
		// Exit with the exit code the VM gave us back
		System.exit(exitCode);
	}
	
	/**
	 * Main entry point for the virtual machine using the given properties.
	 *
	 * @param __vm The name of the virtual machine to use, if {@code null}
	 * then this is automatically determined.
	 * @param __ps The profiler snapshot to use.
	 * @param __jdwp The debugger to use.
	 * @param __threadModel The threading model.
	 * @param __sm The suite manager used.
	 * @param __cp The starting class path.
	 * @param __bootcl The booting class, if {@code null} then {@code __bootid}
	 * is used instead.
	 * @param __sprops System properties to pass to the target VM.
	 * @param __args Arguments to the program which is running.
	 * @return The created virtual machine.
	 * @throws IllegalArgumentException If neither a boot class or boot ID
	 * were specified.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine failed to initialize.
	 * @since 2018/11/17
	 */
	public static VirtualMachine mainVm(String __vm, ProfilerSnapshot __ps,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel, VMSuiteManager __sm,
		String[] __cp,
		String __bootcl, Map<String, String> __sprops, String... __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		if (__bootcl == null || __sm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Always exists
		__args = (__args == null ? new String[0] : __args.clone());
		__sprops = (__sprops == null ? new HashMap<String, String>() :
			new HashMap<String, String>(__sprops));
		
		// If not specified, check the system property that specifies the VM
		if (__vm == null)
			try
			{
				// {@squirreljme.property cc.squirreljme.vm.name Specifies the
				// name of the virtual machine to use.}
				__vm = VMFactory.__getProperty(__sprops,
					"cc.squirreljme.vm.name");
			}
			catch (SecurityException e)
			{
			}
		
		// If none specified, default to SpringCoat
		if (__vm == null)
			__vm = "springcoat";
		
		// Determine the virtual machine to use
		VMFactory factory = null;
		for (VMFactory f : ServiceLoader.<VMFactory>load(VMFactory.class))
		{
			// If no name was specified then use the first one, otherwise
			// use the one which matches the name
			if (f.name.equalsIgnoreCase(__vm))
			{
				factory = f;
				break;
			}
		}
		
		/* {@squirreljme.error AK03 The specified virtual machine does not
		exist. (The virtual machine name)} */
		if (factory == null)
			throw new VMException("AK03 " + __vm);
		
		// Always make a profiler snapshot exist
		if (__ps == null)
			__ps = new ProfilerSnapshot();
		
		// Go through the suites and load the classpath that we are using
		int numlibs = __cp.length;
		VMClassLibrary[] classpath = new VMClassLibrary[numlibs];
		for (int i = 0; i < numlibs; i++)
		{
			VMClassLibrary lib = __sm.loadLibrary(__cp[i]);
			if (lib == null)
				throw new IllegalArgumentException(String.format(
					"Library %s not in classpath!", __cp[i]));
			
			classpath[i] = lib;
		}
		
		// Create the virtual machine now that everything is available
		return factory.createVM(__ps, __jdwp, __threadModel, __sm, classpath,
			__bootcl, __sprops, __args);
	}
	
	/**
	 * Adds paths to the collection of files for the classpath usage.
	 *
	 * @param __files The target files.
	 * @param __path The path to evaluate.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/19
	 */
	@SuppressWarnings("SingleCharacterStartsWith")
	private static void __addPaths(Collection<String> __files, String __path)
		throws NullPointerException
	{
		if (__files == null || __path == null)
			throw new NullPointerException("NARG");
		
		// Add directly if not a wildcard
		if (!__path.endsWith("*") && !__path.startsWith("wildcard="))
		{
			__files.add(__path);
			return;
		}
		
		// Try multiple different wildcard types
		String basePath;
		if (__path.startsWith("wildcard="))
			basePath = __path.substring("wildcard=".length());
		else if (__path.endsWith("*.*"))
			basePath = __path.substring(0, __path.length() - 3);
		else if (__path.endsWith("**"))
			basePath = __path.substring(0, __path.length() - 2);
		else
			basePath = __path.substring(0, __path.length() - 1);
		
		// Realize it
		VMFactory.__addPathsWildcard(__files, basePath);
	}
	
	/**
	 * Adds wildcard directory.
	 *
	 * @param __files The files to place into.
	 * @param __basePath The base path.
	 * @since 2024/02/25
	 */
	private static void __addPathsWildcard(Collection<String> __files,
		String __basePath)
		throws NullPointerException
	{
		if (__files == null || __basePath == null)
			throw new NullPointerException("NARG");
		
		VMFactory.__addPathsWildcard(__files, Paths.get(__basePath));
	}
	
	/**
	 * Adds wildcard directory.
	 *
	 * @param __files The files to place into.
	 * @param __basePath The base path.
	 * @since 2024/02/25
	 */
	private static void __addPathsWildcard(Collection<String> __files,
		Path __basePath)
		throws NullPointerException
	{
		if (__files == null || __basePath == null)
			throw new NullPointerException("NARG");
		
		try
		{
			// Ignore if not a directory
			if (!Files.isDirectory(__basePath))
				return;
			
			Files.walkFileTree(__basePath,
				new HashSet<FileVisitOption>(
					Arrays.asList(FileVisitOption.FOLLOW_LINKS)),
				64,
				new __JarWalker__(__files));
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
	}
	
	/**
	 * Tries to get a property from a passed map otherwise reads from the
	 * system properties used.
	 *
	 * @param __props The properties to check first.
	 * @param __key The key to get.
	 * @return The value for the given key, {@code null} means it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	private static String __getProperty(Map<String, String> __props,
		String __key)
		throws NullPointerException
	{
		return VMFactory.__getProperty(__props, __key, null);
	}
	
	/**
	 * Tries to get a property from a passed map otherwise reads from the
	 * system properties used.
	 *
	 * @param __props The properties to check first.
	 * @param __key The key to get.
	 * @param __def Default value.
	 * @return The value for the given key, {@code __def} means it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	private static String __getProperty(Map<String, String> __props,
		String __key, String __def)
		throws NullPointerException
	{
		if (__props == null || __key == null)
			throw new NullPointerException("NARG");
		
		// Grab from these properties first
		String rv = __props.get(__key);
		if (rv != null)
			return rv;
		
		// Otherwise try a property instead
		try
		{
			rv = System.getProperty(__key);
			if (rv != null)
				return rv;
			return __def;
		}
		catch (SecurityException e)
		{
			return __def;
		}
	}
	
	/**
	 * Maps class libraries to names.
	 *
	 * @param __libs The values to map to name.
	 * @return Class libraries to strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/28
	 */
	private static Collection<String> __libraryNames(
		Iterable<VMClassLibrary> __libs)
		throws NullPointerException
	{
		if (__libs == null)
			throw new NullPointerException("NARG");
		
		Collection<String> rv = new LinkedList<>();
		
		for (VMClassLibrary lib : __libs)
			rv.add(lib.name());
		
		return rv;
	}
	
	/**
	 * Normalizes the name of the library.
	 *
	 * @param __name The name of the JAR.
	 * @return The normalized name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/01
	 */
	private static String __normalizeName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Not a known extension or normalized type
		if (!SuiteUtils.isAny(__name))
			return __name;
		
		// Get the base name of the JAR or SQC
		int lastDot = __name.lastIndexOf('.');
		String ext = __name.substring(lastDot + 1);
		__name = __name.substring(0, lastDot);
		
		// Chop down potential foo"-0.4.0" from the end
		for (int n = __name.length(), i = n - 1; i >= 0; i--)
		{
			char c = __name.charAt(i);
			
			// Still potentially a version bit
			if (c == '.' || c == '-' || (c >= '0' && c <= '9'))
				__name = __name.substring(0, i);
			
			// Do not need
			else
				break;
		}
		
		// Use this name
		return __name + "." + ext;
	}
	
	/**
	 * Sets up JDWP stream for connection.
	 *
	 * @param __host The hostname to use, if {@code null} this will be
	 * a server.
	 * @param __port The port to listen on.
	 * @since 2021/03/08
	 */
	private static JDWPHostFactory __setupJdwp(String __host, int __port)
	{
		// Try opening the socket
		Socket socket = null;
		try
		{
			// Create socket
			Debugging.debugNote("Waiting for connection with %s:%d...",
				__host, __port);
			if (__host == null || __host.isEmpty())
				socket = new ServerSocket(__port).accept();
			else
				socket = new Socket(__host, __port);
			
			// Use factory to create it
			return new JDWPHostFactory(socket.getInputStream(),
				socket.getOutputStream(), __port);
		}
		
		// Could not open the socket?
		catch (IOException e)
		{
			// Close the socket or try to
			if (socket != null)
				try
				{
					socket.close();
				}
				catch (IOException f)
				{
					e.addSuppressed(f);
				}
				
			throw new RuntimeException(String.format(
				"Could not open JDWP socket: %s:%d", __host, __port), e);
		}
	}
	
	/**
	 * Sets up an internal JDWP based debugger that is built into SquirrelJME. 
	 *
	 * @param __fork Should the debugger be forked?
	 * @return The factory for creating the buffer.
	 * @since 2024/01/19
	 */
	private static JDWPHostFactory __setupJdwpInternal(boolean __fork)
	{
		// Look for service for it
		for (VMDebuggerService service :
			ServiceLoader.load(VMDebuggerService.class))
		{
			// Running forked VM?
			if (__fork)
			{
				// Choose random port that is not likely to be used
				int port = 32767 +
					new Random(System.currentTimeMillis())
						.nextInt(32767);
				
				// Determine arguments for the debugger
				List<String> args = new ArrayList<>();
				
				// Use this Java command
				args.add(Objects.toString(RuntimeShelf.vmDescription(
					VMDescriptionType.EXECUTABLE_PATH), "java"));
				
				// Use the same classpath as the host
				args.add("-classpath");
				args.add(System.getProperty("java.class.path"));
				
				// Launch into the debugger instead
				args.add("cc.squirreljme.debugger.Main");
				args.add("localhost:" + port);
				
				// Fork process with the debugger
				ProcessBuilder builder = new ProcessBuilder(args);
				
				// Use our terminal and pipes for the output
				builder.inheritIO();
				
				// Use the same working directory as the host
				builder.directory(Paths
					.get(System.getProperty("user.dir")).toFile());
				
				// Start the debugger
				try
				{
					// Start the debugger
					builder.start();
				
					// Start listening for the connection
					return VMFactory.__setupJdwp(null, port);
				}
				
				// It failed, so fallback to internal debugger
				catch (IOException __e)
				{
					new RuntimeException("Could not fork debugger.", __e)
						.printStackTrace(System.err);
				}
			}
			
			// Otherwise use non-forked debugger
			return service.jdwpFactory();
		}
		
		// Not found, does nothing
		return null;
	}
	
	/**
	 * Load in standard paths.
	 *
	 * @param __libraries The libraries to load into.
	 * @since 2024/02/25
	 */
	private static void __standardPaths(Collection<String> __libraries)
	{
		// Class path to the environment?
		String classPath = System.getenv("SQUIRRELJME_CLASSPATH");
		if (classPath != null)
			for (String path : VMFactory.__unSeparateClassPath(classPath))
				VMFactory.__addPathsWildcard(__libraries, path);
		
		// Java Home Directory?
		String rawJavaHome = System.getenv("SQUIRRELJME_JAVA_HOME");
		if (rawJavaHome != null)
		{
			Path javaHome = Paths.get(rawJavaHome);
			
			VMFactory.__addPathsWildcard(__libraries,
				javaHome.resolve("lib"));
			VMFactory.__addPathsWildcard(__libraries,
				javaHome.resolve("jre").resolve("lib"));
		}
		
		// Standard data libraries?
		SystemPathProvider paths = SystemPathProvider.provider();
		Path dataPath = paths.data();
		if (dataPath != null)
			VMFactory.__addPathsWildcard(__libraries,
				dataPath.resolve("lib"));
	}
	
	/**
	 * Loads standard system properties from the environment and
	 * configuration.
	 *
	 * @param __sysProps The system properties to load into.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	private static void __standardSysProps(
		Map<String, String> __sysProps)
		throws NullPointerException
	{
		if (__sysProps == null)
			throw new NullPointerException("NARG");
		
		SystemPathProvider paths = SystemPathProvider.provider();
		
		// Configuration file, if it exists?
		Path configDir = paths.config();
		if (configDir != null)
		{
			Path configFile = configDir.resolve(
				"squirreljme.properties");
			if (Files.exists(configFile))
				try
				{
					for (String line : Files.readAllLines(configFile))
					{
						// Comment?
						line = line.trim();
						if (line.isEmpty() || line.startsWith("#"))
							continue;
						
						// Add property?
						int eq = line.indexOf('=');
						if (eq > 0)
							__sysProps.put(line.substring(0, eq).trim(),
								line.substring(eq + 1).trim());
					}
				}
				catch (IOException __e)
				{
					__e.printStackTrace();
				}
		}
		
		// Extra Java VM options
		String javaOpts = System.getenv("SQUIRRELJME_JAVA_OPTS");
		if (javaOpts != null)
			try
			{
				// Setup tokenizer
				StreamTokenizer tokenizer = new StreamTokenizer(
					new StringReader(javaOpts));
				tokenizer.resetSyntax();
				tokenizer.quoteChar('\"');
				tokenizer.quoteChar('\'');
				tokenizer.wordChars('.', '.');
				tokenizer.wordChars('-', '-');
				tokenizer.wordChars('_', '_');
				tokenizer.wordChars('a', 'z');
				tokenizer.wordChars('A', 'Z');
				tokenizer.wordChars('0', '9');
				
				// Handle all tokens
				String key = null;
				String val = null;
				boolean wantKey = true;
				boolean wantVal = false;
				for (;;)
				{
					// Read in more tokens
					int token = tokenizer.nextToken();
					if (token == StreamTokenizer.TT_EOF)
						break;
					
					// Token string?
					if (tokenizer.sval != null)
					{
						if (wantKey && tokenizer.sval.startsWith("-D"))
							key = tokenizer.sval.substring(2);
						else if (wantVal)
						{
							// Add in key
							val = tokenizer.sval;
							__sysProps.put(key, val);
							
							// Clear
							key = null;
							val = null;
							
							// Reset
							wantKey = true;
							wantVal = false;
						}
					}
					else if (token == '=')
					{
						if (wantKey)
						{
							wantKey = false;
							wantVal = true;
						}
					}
				}
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
	}
	
	/**
	 * Merges path entries for the classpath.
	 * 
	 * @param __in The input string.
	 * @return The un-separated string.
	 * @since 2022/06/13
	 */
	private static String[] __unSeparateClassPath(String __in)
	{
		List<String> result = new ArrayList<>();
		
		// Extract path elements
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			// Get location of the next colon
			int dx = __in.indexOf(VMFactory.SEPARATOR_CHAR, i);
			if (dx < 0)
				dx = n;
			
			// Add
			result.add(__in.substring(i, dx));
			
			// Go to next colon
			i = dx;
		}
		
		return result.<String>toArray(new String[result.size()]);
	}
}
