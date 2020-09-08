// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.JavaExecSpec;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VirtualMachineType
	implements VirtualMachineSpecifier
{
	/** Hosted virtual machine. */
	HOSTED("Hosted", "jar",
		":emulators:emulator-base")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			MultiVMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpec __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __classPath, String... __args)
			throws NullPointerException
		{
			if (__task == null || __execSpec == null || __mainClass == null ||
				__sysProps == null || __classPath == null || __args == null)
				throw new NullPointerException("NARG");
			
			// Start with the base emulator class path
			List<Object> classPath = new ArrayList<>();
			classPath.add(MultiVMHelpers.projectRuntimeClasspath(
				__task.getProject().project(this.emulatorProject)));
			
			// Add all of the emulator outputs
			for (File file : __task.getProject().project(this.emulatorProject)
				.getTasks().getByName("jar").getOutputs().getFiles())
				classPath.add(file);
			
			// Append the target class path on top of this, as everything
			// will be running directly
			classPath.addAll(Arrays.asList(__classPath));
			
			// Debug
			__task.getLogger().debug("Hosted ClassPath: {}", classPath);
			
			// Use the classpath we previously determined
			__execSpec.classpath(classPath);
			
			// Main class was the directly specified class, we do not
			// need to handle the standard VM factory launcher
			__execSpec.setMain(__mainClass);
			
			// Use the passed arguments directly
			__execSpec.setArgs(Arrays.asList(__args));
			
			// Any desired system properties
			__execSpec.systemProperties(__sysProps);
		}
	},
	
	/** SpringCoat virtual machine. */
	SPRINGCOAT("SpringCoat", "jar",
		":emulators:springcoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			MultiVMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpec __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__task, __execSpec, __mainClass,
				__sysProps, __classPath, __args);
		}
	},
	
	/** SummerCoat virtual machine. */
	SUMMERCOAT("SummerCoat", "sqc",
		":emulators:summercoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Use the AOT backend for execution
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpec __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__task, __execSpec, __mainClass,
				__sysProps, __classPath, __args);
		}
	},
	
	/* End. */
	;
	
	/** The proper name of the VM. */
	public final String properName;
	
	/** The extension for the VM. */
	public final String extension;
	
	/** The project used for the emulator. */
	public final String emulatorProject;
	
	/**
	 * Returns the proper name of the virtual machine.
	 * 
	 * @param __properName The proper name of the VM.
	 * @param __extension The library extension.
	 * @param __emulatorProject The project used for the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	VirtualMachineType(String __properName, String __extension,
		String __emulatorProject)
		throws NullPointerException
	{
		if (__properName == null || __extension == null ||
			__emulatorProject == null)
			throw new NullPointerException("NARG");
		
		this.properName = __properName;
		this.extension = __extension;
		this.emulatorProject = __emulatorProject;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String outputLibraryName(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// The main library should never show its original source set
		if (SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
			return __project.getName() + "." + this.extension;
		
		// Otherwise include the source sets
		return __project.getName() + "-" + __sourceSet + "." + this.extension;
	}
	
	/**
	 * Spawns a virtual machine using the standard {@code VmFactory} class.
	 * 
	 * @param __task The task being executed, may be used as context.
	 * @param __execSpec The execution specification.
	 * @param __mainClass The main class to execute.
	 * @param __sysProps The system properties to define.
	 * @param __classPath The class path of the execution target.
	 * @param __args Arguments to the started program.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public void spawnVmViaFactory(Task __task, JavaExecSpec __execSpec,
		String __mainClass, Map<String, String> __sysProps, Path[] __classPath,
		String[] __args)
		throws NullPointerException
	{
		if (__task == null || __execSpec == null || __mainClass == null ||
			__sysProps == null || __classPath == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Determine the class-path for the emulator
		List<Path> vmClassPath = new ArrayList<>();
		for (File file : MultiVMHelpers.projectRuntimeClasspath(
			__task.getProject().project(this.emulatorProject)))
			vmClassPath.add(file.toPath());
		
		// Add all of the emulator outputs
		for (File file : __task.getProject().project(this.emulatorProject)
			.getTasks().getByName("jar").getOutputs().getFiles())
			vmClassPath.add(file.toPath());
		
		// Debug
		__task.getLogger().debug("VM ClassPath: {}", vmClassPath);
		
		// Build arguments to the VM
		Collection<String> vmArgs = new LinkedList<>();
		
		// Add emulator to launch
		vmArgs.add("-Xemulator:" + this.vmName(VMNameFormat.LOWERCASE));
		
		// Determine where profiler snapshots are to go, try to use the
		// profiler directory for that
		Path profilerDir = ((__task instanceof MultiVMExecutableTask) ?
			MultiVMHelpers.profilerDir(__task.getProject(), this,
			((MultiVMExecutableTask)__task).getSourceSet()).get() :
			__task.getProject().getBuildDir().toPath());
		
		// Use the main class name unless this is a test, so that they are
		// named better
		String profilerClass = (__mainClass.equals(
			MultiVMHelpers.SINGLE_TEST_RUNNER) && __args.length > 0 ?
			__args[0] : __mainClass);
		vmArgs.add("-Xsnapshot:" + profilerDir.resolve(
			__task.getProject().getName() + "_" +
			profilerClass.replace('.', '-') + ".nps"));
		
		// Class path for the target program to launch
		vmArgs.add("-classpath");
		vmArgs.add(MultiVMHelpers.classpathAsString(__classPath));
		
		// Any system properties
		for (Map.Entry<String, String> sysProp : __sysProps.entrySet())
			vmArgs.add("-D" + sysProp.getKey() + "=" + sysProp.getValue());
		
		// Main class of the target to run
		vmArgs.add(__mainClass);
		
		// Any arguments to the target run
		vmArgs.addAll(Arrays.asList(__args));
		
		// Launching is effectively the same as the hosted run but with the
		// VM here instead
		VirtualMachineType.HOSTED.spawnJvmArguments(__task, __execSpec,
			"cc.squirreljme.emulator.vm.VMFactory", __sysProps,
			vmClassPath.<Path>toArray(new Path[vmClassPath.size()]),
			vmArgs.<String>toArray(new String[vmArgs.size()]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String vmName(VMNameFormat __format)
		throws NullPointerException
	{
		String properName = this.properName;
		switch (__format)
		{
			case LOWERCASE:
				return properName.toLowerCase();
				
			case CAMEL_CASE:
				return Character.toLowerCase(properName.charAt(0)) +
					properName.substring(1);
				
			case PROPER_NOUN:
			default:
				return properName;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/16
	 */
	@Override
	public final String emulatorProject()
	{
		return this.emulatorProject;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public final boolean hasRom()
	{
		return this == VirtualMachineType.SUMMERCOAT;
	}
}
