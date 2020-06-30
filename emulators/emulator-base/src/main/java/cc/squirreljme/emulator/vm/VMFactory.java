// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.vm.JarClassLibrary;
import cc.squirreljme.vm.NameOverrideClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Stream;

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
	/** The name of the VM implementation. */
	protected final String name;
	
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
	 * @param __ps The profiler snapshot to write to.
	 * @param __sm The suite manager.
	 * @param __cp The classpath to initialize with.
	 * @param __maincl The main class to start executing.
	 * @param __sprops System properties for the running program.
	 * @param __args Arguments for the running program.
	 * @return An instance of the virtual machine.
	 * @throws IllegalArgumentException If an input argument is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine could not be created.
	 * @since 2018/11/17
	 */
	protected abstract VirtualMachine createVM(ProfilerSnapshot __ps,
		VMSuiteManager __sm, VMClassLibrary[] __cp, String __maincl,
		Map<String, String> __sprops, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException;
	
	/**
	 * Main entry point for general programs.
	 *
	 * @param __args Arguments.
	 * @since 2020/02/29
	 */
	public static void main(String... __args)
	{
		// Poke the VM to initialize some probably important parts of it
		Poking.poke();
		
		// Default settings
		String vmName = "springcoat";
		Path snapshotPath = null;
		Collection<String> libraries = new LinkedList<>();
		Collection<String> suiteClasspath = new LinkedList<>();
		Map<String, String> systemProperties = new LinkedHashMap<>();
		
		// There always is a profiler being run, just differs if we save it
		ProfilerSnapshot profilerSnapshot = new ProfilerSnapshot();
		
		// Command line format is:
		// -Xemulator:(vm)
		// -Xsnapshot:(path-to-nps)
		// -Xlibraries:(class:path:...)
		// -Dsysprop=value
		// -classpath (class:path:...)
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
			
			// Select a VM
			if (item.startsWith("-Xemulator:"))
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
				// Extract path elements
				for (int i = item.indexOf(':') + 1, n = item.length();
					i < n; i++)
				{
					// Get location of the next colon
					int dx = item.indexOf(File.pathSeparatorChar, i);
					if (dx < 0)
						dx = n;
					
					// Add to path
					VMFactory.__addPaths(libraries, item.substring(i, dx));
					
					// Go to next colon
					i = dx;
				}
			}
			
			// JARs to load
			else if (item.equals("-classpath") || item.equals("-cp"))
			{
				// Get argument attached to this
				String strings = queue.pollFirst();
				if (strings == null)
					throw new NullPointerException("Classpath missing.");
				
				// Extract path elements
				for (int i = 0, n = strings.length(); i < n; i++)
				{
					// Get location of the next colon
					int dx = strings.indexOf(File.pathSeparatorChar, i);
					if (dx < 0)
						dx = n;
					
					// Add to path
					VMFactory.__addPaths(suiteClasspath,
						strings.substring(i, dx));
					
					// Go to next colon
					i = dx;
				}
			}
			
			// Unknown
			else
				throw new IllegalArgumentException(String.format(
					"Unknown command line switch: %s", item));
		}
		
		// Main class is here
		String mainClass = Objects.<String>requireNonNull(queue.pollFirst(),
			"No main class specified.");
		
		// Fill in the rest with the main argument calls
		Collection<String> mainArgs = new LinkedList<>();
		while (!queue.isEmpty())
			mainArgs.add(queue.removeFirst());
		
		// Implicitly include all of the classes specified as part of suites
		// to be part of the library path
		libraries.addAll(suiteClasspath);
		
		// Determine any suites that are available in the suite library but
		// are not available to
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
			System.err.printf("Registering %s (%s)%n", normalName, path);
			
			// Place in the class library, but make sure the name matches
			// the normalized name of the JAR
			suites.put(normalName, new NameOverrideClassLibrary(
				JarClassLibrary.of(path), normalName));
		}
		
		// Go through the class path and normalize the names so that it finds
		// the correct JAR files
		Collection<String> classpath = new LinkedList<>();
		for (String classItem : suiteClasspath)
			classpath.add(VMFactory.__normalizeName(
				Paths.get(classItem).getFileName().toString()));
		
		// Run the VM, but always make sure we can
		int exitCode = -1;
		try
		{
			// Debug
			System.err.printf("Starting virtual machine (in %s)...%n",
				mainClass);
			/*System.err.printf(" * Libraries: %s%n",
				VMFactory.__libraryNames(suites.values()));
			System.err.printf(" * Classpath: %s%n",
				classpath);*/
			
			// Run the VM
			VirtualMachine vm = VMFactory.mainVm(vmName,
				profilerSnapshot,
				new ArraySuiteManager(suites.values()),
				classpath.<String>toArray(new String[classpath.size()]),
				mainClass,
				systemProperties,
				mainArgs.<String>toArray(new String[mainArgs.size()]));
			
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
	public static VirtualMachine mainVm(String __vm,
		ProfilerSnapshot __ps, VMSuiteManager __sm, String[] __cp,
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
		
		// {@squirreljme.error AK03 The specified virtual machine does not
		// exist. (The virtual machine name)}
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
		return factory.createVM(__ps, __sm, classpath, __bootcl,
			__sprops, __args);
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
		if (!__path.endsWith("*"))
		{
			__files.add(__path);
			return;
		}
		
		// Try searching for JAR files in a directory
		try
		{
			Path startPath = Paths.get(
				__path.substring(0, __path.length() - 1));
			Files.walkFileTree(startPath, new __JarWalker__(__files));
		}
		catch (IOException e)
		{
			throw new RuntimeException(String.format(
				"Could not load wildcard JARs: %s", __path), e);
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
		
		// Get the base name of the JAR
		if (__name.endsWith(".jar") || __name.endsWith(".JAR"))
			__name = __name.substring(0, __name.length() - ".jar".length());
		
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
		return __name.toLowerCase() + ".jar";
	}
}
