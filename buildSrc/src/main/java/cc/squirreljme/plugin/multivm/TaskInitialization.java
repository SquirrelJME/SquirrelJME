// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;

/**
 * This is used to initialize the Gradle tasks for projects accordingly.
 *
 * @since 2020/08/07
 */
public final class TaskInitialization
{
	/** Source sets that are used. */
	private static final String[] _SOURCE_SETS =
		new String[]{SourceSet.MAIN_SOURCE_SET_NAME,
			SourceSet.TEST_SOURCE_SET_NAME};
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private TaskInitialization()
	{
	}
	
	/**
	 * Initializes the project for the tasks and such 
	 * 
	 * @param __project The project to initialize for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		// Initialize or both main classes and such
		for (String sourceSet : TaskInitialization._SOURCE_SETS)
			TaskInitialization.initialize(__project, sourceSet);
	}
	
	/**
	 * Initializes the source set for the given project.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set to be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Initialize for each VM
		for (VirtualMachineType vmType : VirtualMachineType.values())
			TaskInitialization.initialize(__project, __sourceSet, vmType);
	}
	
	/**
	 * Initializes the virtual machine for the given project's sourceset.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Library that needs to be constructed so execution happens properly
		MultiVMLibraryTask libTask = tasks.create(
			TaskInitialization.task("lib", __sourceSet, __vmType),
			MultiVMLibraryTask.class, __sourceSet, __vmType);
		
		// Running the target
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			tasks.create(
				TaskInitialization.task("run", __sourceSet, __vmType),
				MultiVMRunTask.class, __sourceSet, __vmType, libTask);
		
		// Testing the target
		else if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			tasks.create(
				TaskInitialization.task("test", __sourceSet, __vmType),
				MultiVMTestTask.class, __sourceSet, __vmType, libTask);
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public static void romTasks(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
			
		// Initialize or both main classes and such
		for (String sourceSet : TaskInitialization._SOURCE_SETS)
			for (VirtualMachineType vmType : VirtualMachineType.values())
				TaskInitialization.romTasks(__project, sourceSet, vmType);
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @param __sourceSet The source set to use.
	 * @param __vmType The virtual machine type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	private static void romTasks(Project __project, String __sourceSet,
		VirtualMachineType __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
			
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Does the VM utilize ROMs?
		if (__vmType.hasRom())
			tasks.create(
				TaskInitialization.task("rom", __sourceSet, __vmType),
				MultiVMRomTask.class, __sourceSet, __vmType);
	}
	
	/**
	 * Builds a name for a task.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __vmType The type of virtual machine used.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static String task(String __name, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		if (__name == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// If this is the main source set, never include the source set as
		// it becomes implied. Additionally if the name and the source set
		// are the same, reduce the confusion so there is no "testTestHosted".
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME) ||
			__sourceSet.equals(__name))
			return __name + __vmType.vmName(VMNameFormat.PROPER_NOUN);
		
		// Otherwise include it
		return __name +
			Character.toUpperCase(__sourceSet.charAt(0)) +
			__sourceSet.substring(1) +
			__vmType.vmName(VMNameFormat.PROPER_NOUN);
	}
}
