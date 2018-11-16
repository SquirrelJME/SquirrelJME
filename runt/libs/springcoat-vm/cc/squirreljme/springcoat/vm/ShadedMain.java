// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.cldc.lang.GuestDepth;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.EntryPoints;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This class is used to setup and initialize the shaded entry point for
 * execution.
 *
 * @since 2018/11/15
 */
public class ShadedMain
{
	/**
	 * Main entry point which runs off the given class.
	 *
	 * @param __acl The class to use resources from, if {@code null} then
	 * the current class is used which is likely not intended.
	 * @param __pfx The prefix for library lookup.
	 * @param __cp The classpath for execution.
	 * @param __bootcl the boot class, may be {@code null}.
	 * @param __pid The MIDlet to execute, if a negative value this is
	 * ignored.
	 * @param __args Arguments to the VM.
	 * @throws IllegalArgumentException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	public static final void main(String __acl, String __pfx,
		String[] __cp, String __bootcl, int __pid, String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__acl == null)
			throw new NullPointerException("NARG");
		
		// Locate the active class, used to pivot resources and such
		Class<?> activeclass;
		if (__acl == null)
			activeclass = null;
		else
			try
			{
				activeclass = Class.forName(__acl);
			}
			
			// {@squirreljme.error BK34 Could not locate the active class.}
			catch (ClassNotFoundException e)
			{
				throw new IllegalArgumentException("BK34 " + __acl, e);
			}
		
		// Forward
		ShadedMain.main(activeclass, __pfx, __cp, __bootcl, __pid, __args);
	}
	
	/**
	 * Main entry point which runs off the given class.
	 *
	 * @param __acl The class to use resources from, if {@code null} then
	 * the current class is used which is likely not intended.
	 * @param __pfx The prefix for library lookup.
	 * @param __cp The classpath for execution.
	 * @param __bootcl the boot class, may be {@code null}.
	 * @param __pid The MIDlet to execute, if a negative value this is
	 * ignored.
	 * @param __args Arguments to the VM.
	 * @throws IllegalArgumentException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/15
	 */
	public static final void main(Class<?> __acl, String __pfx,
		String[] __cp, String __bootcl, int __pid, String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__pfx == null || __cp == null || __args == null)
			throw new NullPointerException("NARG");
		
		// If no boot class or program was specified, just default to the ID
		if (__bootcl == null && __pid < 0)
			__pid = 0;
		
		// Just use this class as the base for lookup, which is likely not
		// intended
		if (__acl == null)
			__acl = ShadedMain.class;
		
		// Load the suite manager
		ResourceBasedSuiteManager sm = new ResourceBasedSuiteManager(
			__acl, __pfx);
		
		// Load the classpath
		int numcp = __cp.length;
		SpringClassLibrary[] classpath = new SpringClassLibrary[numcp];
		for (int i = 0; i < numcp; i++)
			classpath[i] = sm.loadLibrary(__cp[i]);
		
		// Initialize the class loader
		SpringClassLoader classloader = new SpringClassLoader(classpath);
		
		// Lookup ID by class name?
		if (__pid < 0)
		{
			SpringClassLibrary bl = classloader.bootLibrary();
			
			// Need to load the manifest where the entry points will be
			EntryPoints entries;
			try (InputStream in = bl.resourceAsStream("META-INF/MANIFEST.MF"))
			{
				// {@squirreljme.error BK35 Entry point JAR has no manifest.}
				if (in == null)
					throw new SpringVirtualMachineException("BK35");
				
				entries = new EntryPoints(new JavaManifest(in));
			}
			
			// {@squirreljme.error BK36 Failed to read the manifest.}
			catch (IOException e)
			{
				throw new SpringVirtualMachineException("BK36", e);
			}
			
			// Determine the entry point used
			for (int i = 0, n = entries.size(); i < n; i++)
				if (__bootcl.equals(entries.get(i).entryPoint()))
				{
					__pid = i;
					break;
				}
			
			// {@squirreljme.error BK37 Could not locate the entry point for
			// the given class. (The class where the entry point could not
			// be found)}
			if (__pid < 0)
				throw new IllegalArgumentException(
					"BK37 " + __bootcl);
		}
		
		// Profiled class information
		ProfilerSnapshot profiler = new ProfilerSnapshot();
		
		// Initialize the virtual machine with our launch ID
		SpringMachine machine = new SpringMachine(sm,
			classloader, new SpringTaskManager(sm, profiler), __pid,
			GuestDepth.guestDepth() + 1, profiler, __args);
		
		// Run the VM until it terminates
		int exitcode = -1;
		try
		{
			machine.run();
			exitcode = 0;
		}
		
		// Exiting with some given code
		catch (SpringMachineExitException e)
		{
			exitcode = e.code();
		}
		
		// Ignore these exceptions, just fatal exit
		catch (SpringFatalException e)
		{
			exitcode = TaskAccess.EXIT_CODE_FATAL_EXCEPTION;
		}
		
		// Dump the profiler snapshot somewhere
		finally
		{
			// Dump to file
			try
			{
				// Create temporary file
				Calendar now = Calendar.getInstance();
				Path temp = Files.createTempFile(String.format(
					"springcoat-%TF_%TH-%TM-%TS-", now, now, now, now),
					".nps");
				
				// Write snapshot to this file
				try (OutputStream os = Files.newOutputStream(temp,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE))
				{
					profiler.writeTo(os);
				}
			}
			
			// Ignore
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		// Exit with our given code
		System.exit(exitcode);
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/11/15
	 */
	public static final void main(String... __args)
	{
		throw new todo.TODO();
	}
	
	/**
	 * The default shaded main entry point.
	 *
	 * @param __args Program entry point.
	 * @since 2018/11/16
	 */
	public static final void shadedMain(String... __args)
	{
		// Make this always exist
		if (__args == null)
			__args = new String[0];
		
		// Load our own main manifest
		JavaManifest man;
		try (InputStream in = ShadedMain.class.getResourceAsStream(
			"/META-INF/SQUIRRELJME-SHADED.MF"))
		{
			man = new JavaManifest(in);
		}
		
		// {@squirreljme.error BK38 Could not read the manifest to load the
		// launcher's classpath.}
		catch (IOException e)
		{
			throw new RuntimeException("BK38", e);
		}
		
		// Read the class path for the launcher from the property
		List<String> classpath = new ArrayList<>();
		JavaManifestAttributes attr = man.getMainAttributes();
		String rawlcp = attr.getValue("X-SquirrelJME-LauncherClassPath");
		if (rawlcp != null)
		{
			// Clip in the path
			for (int i = 0, n = rawlcp.length(); i < n;)
			{
				// Find end clip position
				int sp = rawlcp.indexOf(' ', i);
				if (sp < 0)
					sp = n;
				
				// Clip string
				String clip = rawlcp.substring(i, sp).trim();
				if (!clip.isEmpty())
					classpath.add(clip);
				
				// Skip this space
				i = sp + 1;
			}
		}
		
		// Perform the call to the launcher!
		ShadedMain.main(ShadedMain.class, "__squirreljme/",
			classpath.<String>toArray(new String[classpath.size()]), null, 0,
			__args);
	}
}

