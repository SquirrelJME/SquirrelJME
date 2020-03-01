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
import cc.squirreljme.runtime.cldc.asm.SystemProperties;
import cc.squirreljme.runtime.cldc.lang.GuestDepth;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.EntryPoints;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

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
	 * @param __ismid Is the main class a MIDlet?
	 * @param __gd The guest depth of the virtual machine.
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
		boolean __ismid, int __gd, Map<String, String> __sprops,
		String[] __args)
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
		Collection<String> suiteClasspath = new LinkedList<>();
		
		// There always is a profiler being run, just differs if we save it
		ProfilerSnapshot profilerSnapshot = new ProfilerSnapshot();
		
		// Command line format is:
		// -Xemulator:(vm)
		// -Xsnapshot:(path-to-nps)
		// -classpath (class:path:...)
		// Main-class
		// Arguments...
		Deque<String> queue = new ArrayDeque<>(Arrays.<String>asList(__args));
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
					suiteClasspath.add(strings.substring(i, dx));
					
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
		
		// Collect all the suites together
		Collection<String> classpath = new LinkedList<>();
		Collection<VMClassLibrary> suites = new LinkedList<>();
		for (String classitem : suiteClasspath)
		{
			Path path = Paths.get(classitem);
			
			// Load it into memory
			try (InputStream in = Files.newInputStream(path,
					StandardOpenOption.READ);
				ZipStreamReader zip = new ZipStreamReader(in))
			{
				String normalName = VMFactory.__normalizeName(
					path.getFileName().toString());
				
				System.err.printf("Loading %s (%s)...%n", normalName, path);
				suites.add(InMemoryClassLibrary.loadZip(
					normalName, zip));
				
				// Use the true name here
				classpath.add(normalName);
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not load library.", e);
			}
		}
		
		// Run the VM, but always make sure we can
		int exitCode = -1;
		try
		{
			// Debug
			System.err.println("Starting virtual machine...");
			
			// Run the VM
			VirtualMachine vm = VMFactory.mainVm(vmName,
				profilerSnapshot,
				new ArraySuiteManager(suites),
				classpath.<String>toArray(new String[classpath.size()]),
				mainClass,
				0, 0, null,
				mainArgs.<String>toArray(new String[mainArgs.size()]));
			
			// Run the virtual machine until it exits
			exitCode = vm.runVm();
			System.exit(vm.runVm());
		}
		
		// Always write the snapshot file
		finally
		{
			if (snapshotPath != null)
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
	 * @param __bootid The booting index, if negative then {@code __bootcl}
	 * is used instead. This value takes priority.
	 * @param __gd The guest depth of the virtual machine, if negative this
	 * is automatically determined.
	 * @param __sprops System properties to pass to the target VM.
	 * @param __args Arguments to the program which is running.
	 * @return The created virtual machine.
	 * @throws IllegalArgumentException If neither a boot class or boot ID
	 * were specified.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine failed to initialize.
	 * @since 2018/11/17
	 */
	public static final VirtualMachine mainVm(String __vm,
		ProfilerSnapshot __ps, VMSuiteManager __sm, String[] __cp,
		String __bootcl, int __bootid, int __gd, Map<String, String> __sprops,
		String... __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		if (__sm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AK02 Neither the boot class or boot ID was
		// specified, one must be specified.}
		if (__bootcl == null && __bootid < 0)
			throw new IllegalArgumentException("AK02");
		
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
			if (__vm == null || __vm.equalsIgnoreCase(f.name))
			{
				factory = f;
				break;
			}
		}
		
		// {@squirreljme.error AK03 The specified virtual machine does not
		// exist. (The virtual machine name)}
		if (factory == null)
			throw new VMException("AK03 " + __vm);
		
		// Automatically determined guest depth? This is always deeper!
		if (__gd < 0)
			__gd = GuestDepth.guestDepth() + 1;
		
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
		
		// Need to load the manifest where the entry points will be
		VMClassLibrary bl = classpath[numlibs - 1];
		EntryPoints entries;
		try (InputStream in = bl.resourceAsStream("META-INF/MANIFEST.MF"))
		{
			// {@squirreljme.error AK04 Entry point JAR has no manifest.}
			if (in == null)
				throw new VMException("AK04");
			
			entries = new EntryPoints(new JavaManifest(in));
		}
		
		// {@squirreljme.error AK05 Failed to read the manifest.}
		catch (IOException e)
		{
			throw new VMException("AK05", e);
		}
		
		// Print them out for debug
		System.err.println("Entry points:");
		for (int i = 0, n = entries.size(); i < n; i++)
			System.err.printf("    %d: %s%n", i, entries.get(i));
		
		// If a class was specified and not a boot ID, we must search through
		// the boot JAR's (the last one) entry points for a match
		if (__bootid < 0)
		{
			// Determine the entry point used
			for (int i = 0, n = entries.size(); i < n; i++)
				if (__bootcl.equals(entries.get(i).entryPoint()))
				{
					__bootid = i;
					break;
				}
			
			// {@squirreljme.error AK06 Could not find the specified main
			// class in the entry point list. (The main class)}
			if (__bootid < 0)
				throw new VMException("AK06 " + __bootcl);
		}
		
		// Do not use an entry point which is outside of the bounds
		__bootid = Math.max(0, Math.min(entries.size(), __bootid));
		
		// Is this entry point a MIDlet? Used as a hint
		EntryPoint entry = entries.get(__bootid);
		boolean ismidlet = entry.isMidlet();
		
		// Create the virtual machine now that everything is available
		return factory.createVM(__ps, __sm, classpath, entry.entryPoint(),
			ismidlet, __gd, __sprops, __args);
	}
	
	/**
	 * Shaded main entry point.
	 *
	 * @param __args Arguments to the program.
	 * @since 2018/11/17
	 */
	@Deprecated
	public static final void shadedMain(String... __args)
	{
		VMFactory.shadedMain((Map<String, String>)null, __args);
	}
	
	/**
	 * Shaded main entry point, with extra properties map.
	 *
	 * @param __sprops Properties map to use.
	 * @param __args Arguments to the program.
	 * @since 2018/11/17
	 */
	@Deprecated
	public static final void shadedMain(Map<String, String> __sprops,
		String... __args)
	{
		// Defensive copy and force to exist
		__args = (__args == null ? new String[0] : __args.clone());
		__sprops = (__sprops == null ? new HashMap<String, String>() :
			new HashMap<String, String>(__sprops));
		
		// We may be able to grab some properties from the shaded manifest
		// information, if one is even available
		JavaManifest man;
		try (InputStream in = VMFactory.class.getResourceAsStream(
			"/META-INF/SQUIRRELJME-SHADED.MF"))
		{
			man = (in == null ? new JavaManifest() : new JavaManifest(in));
		}
		
		// {@squirreljme.error AK07 Could not read the manifest to load the
		// launcher's classpath.}
		catch (IOException e)
		{
			throw new RuntimeException("AK07", e);
		}
		
		// These are parameters which will be parsed to handle how to start
		// the shaded process
		String useactiveclass = null,
			useprefix = null,
			usecp = null,
			usemain = null;
		int bootid = -1;
		
		// Profiler snapshot to generate to
		ProfilerSnapshot psnap = null;
		
		// Try loading these from properties first, to take more priority
		try
		{
			// {@squirreljme.property cc.squirreljme.vm.shadeactiveclass=class
			// The class to use to load resources from.}
			useactiveclass = VMFactory.__getProperty(__sprops,
				"cc.squirreljme.vm.shadeactiveclass");
			
			// {@squirreljme.property cc.squirreljme.vm.shadeprefix=prefix
			// The resource lookup prefix for the classes.}
			useprefix = VMFactory.__getProperty(__sprops,
				"cc.squirreljme.vm.shadeprefix");
			
			// {@squirreljme.property cc.squirreljme.vm.shadeclasspath=[class:]
			// The classes which make up the class path for execution.}
			usecp = VMFactory.__getProperty(__sprops,
				"cc.squirreljme.vm.shadeclasspath");
			
			// {@squirreljme.property cc.squirreljme.vm.shademain=class
			// The class to use as the main entry point for the VM.}
			usemain = VMFactory.__getProperty(__sprops,
				"cc.squirreljme.vm.shademain");
			
			// {@squirreljme.property cc.squirreljme.vm.shadebootid=id
			// The MIDlet or Main class number to use for entering the JAR.}
			bootid = Integer.valueOf(VMFactory.__getProperty(__sprops,
				"cc.squirreljme.vm.shadebootid", "-1"));
		}
		catch (SecurityException e)
		{
			// Ignore
		}
		
		// If properties were not defined in the system, then they should be
		// in the manifest
		JavaManifestAttributes attr = man.getMainAttributes();
		if (useactiveclass == null)
			useactiveclass = attr.getValue("ActiveClass");
		if (useprefix == null)
			useprefix = attr.getValue("Prefix");
		if (usecp == null)
			usecp = attr.getValue("ClassPath");
		if (usemain == null)
			usemain = attr.getValue("Main-Class");
		
		// Otherwise, if anything is missing use defaults
		if (useactiveclass == null)
			useactiveclass = VMFactory.class.getName();
		if (useprefix == null)
			useprefix = "/__-squirreljme/";
		if (bootid < 0)
			bootid = 0;
		
		// Load the resource based suite manager
		VMSuiteManager sm;
		try
		{
			sm = new ResourceBasedSuiteManager(Class.forName(useactiveclass),
				useprefix);
		}
		
		// {@squirreljme.error AK08 Could not locate the class to use for
		// resource lookup. (The class which was not found)}
		catch (ClassNotFoundException e)
		{
			throw new VMException("AK08 " + useactiveclass, e);
		}
		
		// Split the classpath accordingly using ' ', ';', and ':', allow
		// for multiple forms due to manifests and classpaths used in Windows
		// and UNIX
		List<String> classpath = new ArrayList<>();
		for (int i = 0, n = usecp.length(); i < n;)
		{
			// Find end clip position
			int sp;
			if ((sp = usecp.indexOf(' ', i)) < 0)
				if ((sp = usecp.indexOf(';', i)) < 0)
					if ((sp = usecp.indexOf(':', i)) < 0)
						sp = n;
			
			// Clip string
			String clip = usecp.substring(i, sp).trim();
			if (!clip.isEmpty())
				classpath.add(clip);
			
			// Skip the split character
			i = sp + 1;
		}
		
		// Since we are shaded, we might want to load some extra suites from
		// some directory
		List<VMSuiteManager> mergesm = new ArrayList<>();
		mergesm.add(sm);
		
		// Check our working directory
		String workdir = VMFactory.__getProperty(__sprops, "user.dir");
		if (workdir != null)
			try
			{
				mergesm.add(new PathSuiteManager(
					Paths.get(workdir).resolve("lib")));
			}
			catch (InvalidPathException e)
			{
			}
		
		// Check our executable path
		String execpath = SystemProperties.executablePath();
		if (execpath != null)
			try
			{
				mergesm.add(new PathSuiteManager(
					Paths.get(execpath).getParent().resolve("lib")));
			}
			catch (InvalidPathException|NullPointerException e)
			{
			}
		
		// Do the merge?
		if (mergesm.size() > 1)
			sm = new MergedSuiteManager(mergesm.<VMSuiteManager>toArray(
				new VMSuiteManager[mergesm.size()]));
		
		// Create the VM
		VirtualMachine vm = VMFactory.mainVm(null, null,
			sm, classpath.<String>toArray(new String[classpath.size()]),
			usemain, bootid, -1, null, __args);
		
		// Run the VM and exit with the code it generates
		System.exit(vm.runVm());
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
	private static final String __getProperty(Map<String, String> __props,
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
	private static final String __getProperty(Map<String, String> __props,
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
		if (__name.endsWith(".jar"))
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
		return __name + ".jar";
	}
}

