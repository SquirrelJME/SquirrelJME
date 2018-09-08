// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.kernel.suiteinfo.EntryPoint;
import cc.squirreljme.kernel.suiteinfo.EntryPoints;
import cc.squirreljme.springcoat.vm.SpringClass;
import cc.squirreljme.springcoat.vm.SpringClassLoader;
import cc.squirreljme.springcoat.vm.SpringMachine;
import cc.squirreljme.springcoat.vm.SpringMethod;
import cc.squirreljme.springcoat.vm.SpringObject;
import cc.squirreljme.springcoat.vm.SpringThread;
import cc.squirreljme.springcoat.vm.SpringThreadWorker;
import java.util.ArrayDeque;
import java.util.Queue;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Main entry class for the SpringCoat virtual machine, this initializes and
 * runs the virtual machine or runs the compiler.
 *
 * @since 2018/07/29
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments.
	 * @since 2018/07/29
	 */
	public static void main(String... __args)
	{
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Setup project manager
		ProjectManager pm = ProjectManager.fromArguments(args);
		
		// {@squirreljme.error BK01 No project to launch was specified. The
		// format is project-name or project-name:entry-point-id.}
		if (args.isEmpty())
			throw new IllegalArgumentException("BK01");
		
		// Determine the project and launch ID to execute
		String project = args.remove();
		int launchid = 0;
		
		// Check if a launch ID was specified, separate with colon because
		// there could be command line arguments to launch with using the
		// classic entry point. So we cannot really specificy the entry point
		// using a switch as we would need to specially handle it. Not to
		// mention the program may require it.
		int col = project.indexOf(':');
		if (col >= 0)
		{
			launchid = Integer.valueOf(project.substring(col + 1));
			project = project.substring(0, col);
		}
		
		// Get the project and all of its dependencies built which forms
		// the class path
		Binary[] classpath = pm.build(TimeSpaceType.BUILD, project);
		
		// The binary to launch is the final binary specified, get those
		// entry points
		Binary bootbin = classpath[classpath.length - 1];
		EntryPoints entries = new EntryPoints(bootbin.manifest());
		
		// Print entry points out out for debug
		System.err.println("Entry points:");
		int n = entries.size();
		for (int i = 0; i < n; i++)
			System.err.printf("    %d: %s%n", i, entries.get(i));
		
		// Use the first program if the ID is not valid
		if (launchid < 0 || launchid >= n)
			launchid = 0;
		
		// Initialize the class loader to find classes
		SpringClassLoader classloader = new SpringClassLoader(classpath);
		SpringMachine machine = new SpringMachine(classloader);
		
		// Load the entry point class
		EntryPoint entry = entries.get(launchid);
		SpringClass entrycl = classloader.loadClass(new ClassName(
			entry.entryPoint().replace('.', '/')));
		
		// Thread that will be used as the main thread of execution
		SpringThread mainthread = machine.createThread("main");
		
		// Find the method to be entered in
		SpringMethod mainmethod;
		boolean ismidlet;
		if ((ismidlet = entry.isMidlet()))
			mainmethod = entrycl.lookupMethod(false,
				new MethodNameAndType("startApp", "()V"));
		else
			mainmethod = entrycl.lookupMethod(true,
				new MethodNameAndType("main", "(Ljava/lang/String;)V"));
		
		// We will be using the same logic in the thread worker if we need to
		// initialize any objects or arguments
		SpringThreadWorker worker = new SpringThreadWorker(machine,
			mainthread);
		
		// If this is a midlet, we are going to need to initialize a new
		// instance of our MIDlet and then push that to the current frame's
		// stack then call the main method on it
		Object[] entryargs;
		if (ismidlet)
		{
			// Allocate an object for our instance
			SpringObject midinstance = worker.allocateObject(entrycl);
			
			// Initialize the object
			SpringMethod defcon = entrycl.lookupDefaultConstructor();
			mainthread.enterFrame(defcon, midinstance);
			
			// Since the constructor was entered, run all the code needed to
			// actually initialize it and such, this method will return once
			// there are no frames left
			worker.run();
			
			// The arguments to the method we are calling is just the instance
			// of the midlet we created and initialized
			entryargs = new Object[]{midinstance};
		}
		
		// Initialize program arguments from a bunch of string arguments that
		// way they are passed to the main entry point
		else
		{
			if (true)
				throw new todo.TODO();
		}
		
		// Enter the frame for that method using the arguments we passed (in
		// a static fashion)
		mainthread.enterFrame(mainmethod, entryargs);
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		worker.run();
		
		// Wait until all threads have terminated before actually leaving
		throw new todo.TODO();
	}
}

