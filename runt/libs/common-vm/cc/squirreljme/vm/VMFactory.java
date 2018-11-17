// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.lang.GuestDepth;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.EntryPoints;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.profiler.ProfilerSnapshot;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

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
	 * @throws IllegalArgumentException If neither a boot class or boot ID
	 * were specified.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine failed to initialize.
	 * @since 2018/11/17
	 */
	public static final VirtualMachine main(String __vm, ProfilerSnapshot __ps,
		VMSuiteManager __sm, String[] __cp, String __bootcl, int __bootid,
		int __gd, Map<String, String> __sprops, String... __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		if (__sm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AK02 Neither the boot class or boot ID was
		// specified, one must be specified.}
		if (__bootcl == null && __bootid < 0)
			throw new IllegalArgumentException("AK02");
		
		// If not specified, check the system property that specifies the VM
		if (__vm == null)
			try
			{
				// {@squirreljme.property cc.squirreljme.vm.name Specifies the
				// name of the virtual machine to use.}
				__vm = System.getProperty("cc.squirreljme.vm.name");
			}
			catch (SecurityException e)
			{
			}
		
		// Determine the virtual machine to use
		VMFactory factory = null;
		for (VMFactory f : ServiceLoader.<VMFactory>load(VMFactory.class))
		{
			// If no name was specified then use the first one, otherwise
			// use the one which matches the name
			if (__vm == null || __vm.equals(f.name))
			{
				factory = f;
				break;
			}
		}
		
		// {@squirreljme.error AK06 The specified virtual machine does not
		// exist. (The virtual machine name)}
		if (factory == null)
			throw new VMException("AK06 " + __vm);
		
		// Always exists
		if (__args == null)
			__args = new String[0];
		if (__sprops == null)
			__sprops = new HashMap<>();
		
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
			classpath[i] = __sm.loadLibrary(__cp[i]);
		
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
			
			// {@squirreljme.error AK03 Could not find the specified main
			// class in the entry point list. (The main class)}
			if (__bootid < 0)
				throw new VMException("AK03 " + __bootcl);
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
	public static final void shadedMain(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// We may be able to grab some properties from the shaded manifest
		// information, if one is even available
		JavaManifest man;
		try (InputStream in = VMFactory.class.getResourceAsStream(
			"/META-INF/SQUIRRELJME-SHADED.MF"))
		{
			man = (in == null ? new JavaManifest() : new JavaManifest(in));
		}
		
		// {@squirreljme.error BK38 Could not read the manifest to load the
		// launcher's classpath.}
		catch (IOException e)
		{
			throw new RuntimeException("BK38", e);
		}
		
		// These are parameters which will be parsed to handle how to start
		// the shaded process
		String useactiveclass = null,
			useprefix = null,
			usecp = null,
			usemain = null;
		int bootid = -1;
		
		// Try loading these from properties first, to take more priority
		try
		{
			// {@squirreljme.property cc.squirreljme.vm.shadeactiveclass=class
			// The class to use to load resources from.}
			useactiveclass = System.getProperty(
				"cc.squirreljme.vm.shadeactiveclass");
			
			// {@squirreljme.property cc.squirreljme.vm.shadeprefix=prefix
			// The resource lookup prefix for the classes.}
			useprefix = System.getProperty(
				"cc.squirreljme.vm.shadeprefix");
			
			// {@squirreljme.property cc.squirreljme.vm.shadeclasspath=[class:]
			// The classes which make up the class path for execution.}
			usecp = System.getProperty(
				"cc.squirreljme.vm.shadeclasspath");
			
			// {@squirreljme.property cc.squirreljme.vm.shademain=class
			// The class to use as the main entry point for the VM.}
			usemain = System.getProperty(
				"cc.squirreljme.vm.shademain");
			
			// {@squirreljme.property cc.squirreljme.vm.shadebootid=id
			// The MIDlet or Main class number to use for entering the JAR.}
			bootid = Integer.getInteger("cc.squirreljme.vm.shadebootid", -1);
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
			useprefix = "/__squirreljme/";
		if (bootid < 0)
			bootid = 0;
		
		// Load the resource based suite manager
		VMSuiteManager sm;
		try
		{
			sm = new ResourceBasedSuiteManager(Class.forName(useactiveclass),
				useprefix);
		}
		
		// {@squirreljme.error AK07 Could not locate the class to use for
		// resource lookup. (The class which was not found)}
		catch (ClassNotFoundException e)
		{
			throw new VMException("AK07 " + useactiveclass, e);
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
		
		// Create the VM
		VirtualMachine vm = VMFactory.main(null, null,
			sm, classpath.<String>toArray(new String[classpath.size()]),
			usemain, bootid, -1, null, __args);
		
		// Run the VM and exit with the code it generates
		System.exit(vm.run());
	}
}

