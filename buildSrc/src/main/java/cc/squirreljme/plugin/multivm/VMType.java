// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import cc.squirreljme.plugin.util.GradleJavaExecSpecFiller;
import cc.squirreljme.plugin.util.GuardedOutputStream;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.ExecResult;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VMType
	implements VMSpecifier
{
	/** Hosted virtual machine. */
	HOSTED("Hosted", "jar",
		":emulators:emulator-base")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/10
		 */
		@Override
		public boolean allowOnlyDebug()
		{
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/05/16
		 */
		@Override
		public void dumpLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			throw new RuntimeException(this.name() + " cannot be dumped.");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2022/10/01
		 */
		@Override
		public boolean isGoldTest()
		{
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			VMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/11/21
		 */
		@Override
		public Iterable<Task> processLibraryDependencies(VMBaseTask __task,
			BangletVariant __variant)
			throws NullPointerException
		{
			return Collections.emptyList();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/11/27
		 */
		@Override
		public void processRom(VMBaseTask __task, BangletVariant __variant,
			OutputStream __out, RomBuildParameters __build, List<Path> __libs)
			throws IOException, NullPointerException
		{
			throw new RuntimeException(this.name() + " is not ROM capable.");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@SuppressWarnings("CallToSystemGetenv")
		@Override
		public void spawnJvmArguments(Project __anyProject,
			SourceTargetClassifier __classifier,
			boolean __debugEligible,
			JavaExecSpecFiller __execSpec, String __mainClass,
			String __commonName, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			if (__anyProject == null || __execSpec == null ||
				__mainClass == null ||
				__sysProps == null || __classPath == null || __args == null)
				throw new NullPointerException("NARG");
				
			// Add our selection of libraries into the hosted environment so in
			// the event the active libraries are needed, they are available.
			Map<String, String> sysProps = new LinkedHashMap<>(__sysProps);
			sysProps.put("squirreljme.hosted.libraries",
				VMHelpers.classpathAsString(__libPath));
			sysProps.put("squirreljme.hosted.classpath",
				VMHelpers.classpathAsString(__classPath));
			
			// For Linux, if this variable is specified we can set the UI
			// scale for Swing so that things are a bit bigger and not
			// ultra-small...
			String gdkScale = System.getenv("GDK_SCALE");
			if (gdkScale != null)
			{
				sysProps.put("sun.java2d.uiScale.enabled", "true");
				sysProps.put("sun.java2d.uiScale", gdkScale);
				sysProps.put("sun.java2d.win.uiScaleX", gdkScale);
				sysProps.put("sun.java2d.win.uiScaleY", gdkScale);
			}
			
			// Can we directly refer to the emulator library already?
			// Only if it has not already been given, doing it here will enable
			// every sub-process quick access to the library
			if (!sysProps.containsKey("squirreljme.emulator.libpath"))
			{
				Path emuLib = VMHelpers.findEmulatorLib(__anyProject);
				if (emuLib != null && Files.exists(emuLib))
					sysProps.put("squirreljme.emulator.libpath",
						emuLib.toString());
			}
			
			// Bring in any system defined properties we want to truly set?
			VMType.__copySysProps(sysProps);
			
			// Make sure the base emulator is available as well
			List<Object> classPath = new ArrayList<>();
			Set<Path> vmSupportPath = new LinkedHashSet<>();
			for (File file : VMHelpers.projectRuntimeClasspath(
				__anyProject.findProject(":emulators:emulator-base")))
				vmSupportPath.add(file.toPath());
			
			// Add all the emulator outputs
			for (String emulatorProject : this.emulatorProjects)
				for (File file : __anyProject.project(emulatorProject)
					.getTasks().getByName("jar").getOutputs().getFiles())
					vmSupportPath.add(file.toPath());
			
			// Use all the supporting path
			classPath.addAll(vmSupportPath);
			
			// Append the target class path on top of this, as everything
			// will be running directly
			classPath.addAll(Arrays.asList(__classPath));
			
			// Add the VM classpath, so it can be recreated if we need to spawn
			// additional tasks such as by the launcher
			sysProps.put("squirreljme.hosted.vm.supportpath",
				VMHelpers.classpathAsString(vmSupportPath));
			sysProps.put("squirreljme.hosted.vm.classpath",
				VMHelpers.classpathAsString(VMHelpers.resolvePath(classPath)));
			
			// Define Java ME Configuration
			sysProps.put("microedition.configuration",
				"CLDC-1.8");
			sysProps.put("squirreljme.orig.microedition.configuration",
				"CLDC-1.8");
			
			// Define Java ME Encoding
			sysProps.put("microedition.encoding", "utf-8");
			sysProps.put("squirreljme.orig.microedition.encoding",
				"utf-8");
			
			// Define Java ME Locale
			sysProps.put("microedition.locale", "en-US");
			sysProps.put("squirreljme.orig.microedition.locale",
				"en-US");
			
			// Define Java ME Platform
			sysProps.put("microedition.platform", "SquirrelJME/0.3.0");
			sysProps.put("squirreljme.orig.microedition.platform",
				"SquirrelJME/0.3.0");
			
			// VM Tracing?
			String tracing = System.getProperty("cc.squirreljme.vm.trace");
			if (tracing != null)
				sysProps.put("cc.squirreljme.vm.trace", tracing);
			
			// Declare system properties that are all the originally defined
			// system properties
			for (Map.Entry<String, String> e : __sysProps.entrySet())
				sysProps.put("squirreljme.orig." + e.getKey(), e.getValue());
			
			// Debug
			__anyProject.getLogger().debug("Hosted SupportPath: {}",
				vmSupportPath);
			__anyProject.getLogger().debug("Hosted ClassPath: {}",
				classPath);
			
			// Arguments for the JVM
			List<String> jvmArgs = new ArrayList<>();
			
			// Copy any agent libraries which are not JDWP based ones, for
			// example if IntelliJ is profiling
			for (String mxArg : ManagementFactory.getRuntimeMXBean()
				.getInputArguments())
				if (mxArg.startsWith("-agentlib:") &&
					!mxArg.startsWith("-agentlib:jdwp="))
					jvmArgs.add(mxArg);
			
			// Is this eligible to be run under a debugger?
			if (__debugEligible)
			{
				// Does this run have a debugger specified already?
				// Prevent double debugger claim which would cause one to fail
				// to listen and thus break debugging
				boolean hasDebug = false;
				for (String arg : __args)
					if (arg.startsWith("-Xjdwp:"))
					{
						hasDebug = true;
						break;
					}
				
				// Enable debugging for the spawned hosted environment
				// Use an alternative variable to allow for VMFactory to be
				// debugged rather than just the emulated environment.
				String xjdwpProp = System.getProperty("squirreljme.xjdwp");
				String jdwpProp = (xjdwpProp != null ? xjdwpProp :
					Objects.toString(System.getProperty("squirreljme.jdwp"),
						__sysProps.get("squirreljme.jdwp")));
				if ((xjdwpProp != null && !xjdwpProp.isEmpty()) ||
					(!hasDebug && jdwpProp != null && !jdwpProp.isEmpty()))
				{
					// Figure the hostname/port split
					int lastCol = jdwpProp.lastIndexOf(':');
					if (lastCol >= 0)
					{
						// Split hostname and port
						String host = jdwpProp.substring(0, lastCol);
						int port = Integer.parseInt(
							jdwpProp.substring(lastCol + 1));
						
						// Listen on a given port?
						if (host.isEmpty())
							jvmArgs.add(String.format(
								"-agentlib:jdwp=transport=dt_socket," +
								"server=y,suspend=y,address=%d", port));
						
						// Connect to remote VM
						else
							jvmArgs.add(String.format(
								"-agentlib:jdwp=transport=dt_socket," +
								"server=n," +
								"address=%s:%d,suspend=y",
								host, port));
					}
				}
			}
			
			// Add in the arguments
			if (!jvmArgs.isEmpty())
				__execSpec.setJvmArgs(jvmArgs);
			
			// Determine true arguments to use
			List<String> trueArgs = new ArrayList<>(
				1 + __args.length);
			trueArgs.add(__mainClass);
			trueArgs.addAll(Arrays.asList(__args));
			
			// Use the classpath we previously determined
			__execSpec.classpath(classPath);
			
			// Use special main handler which handles loading the required
			// methods for the hosted environment to work correctly with
			// SquirrelJME
			__execSpec.setMain("cc.squirreljme.emulator.NativeBinding");
			__execSpec.setArgs(trueArgs);
			
			// Any desired system properties
			__execSpec.systemProperties(sysProps);
		}
	},
	
	/** SpringCoat virtual machine. */
	SPRINGCOAT("SpringCoat", "jar",
		":emulators:springcoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/05/16
		 */
		@Override
		public void dumpLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			throw new RuntimeException(this.name() + " cannot be dumped.");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public NativePortSupport[] hasNativePortSupport()
		{
			// Can be run in NanoCoat
			return new NativePortSupport[]{NativePortSupport.NANOCOAT};
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2022/10/01
		 */
		@Override
		public boolean isGoldTest()
		{
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			VMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Project __anyProject,
			SourceTargetClassifier __classifier,
			boolean __debugEligible,
			JavaExecSpecFiller __execSpec, String __mainClass,
			String __commonName, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__anyProject, __classifier,
				__debugEligible, __execSpec,
				__mainClass, __commonName, __sysProps, __libPath,
				__classPath, __args);
		}
	},
	
	/** Nanocoat, a smaller simpler runtime. */
	NANOCOAT("NanoCoat", "jar",
		":emulators:nanocoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public void dumpLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			throw new RuntimeException(this.name() + " cannot be dumped.");
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public boolean hasEmulator()
		{
			// This can run on the emulator platform
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public NativePortSupport[] hasNativePortSupport()
		{
			// Currently no native port support
			return new NativePortSupport[]{};
			
			// Can be run in NanoCoat
			/*return new NativePortSupport[]{NativePortSupport.NANOCOAT};*/
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public boolean isGoldTest()
		{
			return false;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		public boolean isSingleSourceSetRom(BangletVariant __variant)
		{
			// NanoCoat is this special case
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public void processLibrary(VMBaseTask __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			VMHelpers.copy(__in, __out);
			
			/*
			// Determine checksum sum of the library, used to detect changes
			// in the ROM for example with checkpointing/save states
			// This is not used for security purposes, just to make sure
			// that a resume does not completely break the VM
			byte[] data = VMHelpers.readAll(__in);
			String hex;
			try
			{
				hex = Base64.getEncoder().encodeToString(
					MessageDigest.getInstance("sha-1").digest(data));
			}
			catch (NoSuchAlgorithmException ignored)
			{
				hex = Integer.toHexString(Arrays.hashCode(data));
			}
			
			// Run compilation task
			try (InputStream in = new ByteArrayInputStream(data))
			{
				this.__aotCommand(__task, in, __out,
					Arrays.asList("-XoriginalLibHash:" + hex),
					"compile", Collections.emptyList());
			}
			*/
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2023/12/04
		 */
		@Override
		public void processRom(VMBaseTask __task, BangletVariant __variant,
			OutputStream __out, RomBuildParameters __build, List<Path> __libs)
			throws IOException, NullPointerException
		{
			/* Just do what SpringCoat does... */
			VMType.SPRINGCOAT.processRom(__task, __variant, __out,
				__build, __libs);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/05/28
		 */
		@Override
		public void spawnJvmArguments(Project __anyProject,
			SourceTargetClassifier __classifier,
			boolean __debugEligible,
			JavaExecSpecFiller __execSpec, String __mainClass,
			String __commonName, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__anyProject, __classifier,
				__debugEligible, __execSpec,
				__mainClass, __commonName, __sysProps, __libPath,
				__classPath, __args);
		}
	},
	
	/* End. */
	;
	
	/** Prefix for system properties to appear in the VM. */
	private static final String _JVM_KEY_PREFIX =
		"squirreljme.sysprop.";
	
	/** The proper name of the VM. */
	public final String properName;
	
	/** The extension for the VM. */
	public final String extension;
	
	/** The project used for the emulator. */
	public final List<String> emulatorProjects;
	
	/**
	 * Initializes the virtual machine type handler.
	 * 
	 * @param __properName The proper name of the VM.
	 * @param __extension The library extension.
	 * @param __emulatorProject The project used for the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	VMType(String __properName, String __extension,
		String __emulatorProject)
		throws NullPointerException
	{
		this(__properName, __extension, (__emulatorProject == null ?
			new String[0] : new String[]{__emulatorProject}));
	}
	
	/**
	 * Initializes the virtual machine type handler.
	 * 
	 * @param __properName The proper name of the VM.
	 * @param __extension The library extension.
	 * @param __emulatorProject The project used for the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/10
	 */
	VMType(String __properName, String __extension,
		String[] __emulatorProject)
		throws NullPointerException
	{
		if (__properName == null || __extension == null ||
			__emulatorProject == null)
			throw new NullPointerException("NARG");
		
		this.properName = __properName;
		this.extension = __extension;
		this.emulatorProjects = Collections.unmodifiableList(
			Arrays.asList(__emulatorProject.clone()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/10
	 */
	@Override
	public boolean allowOnlyDebug()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/30
	 */
	@Override
	public Set<BangletVariant> banglets()
	{
		// Defaults to none
		return Collections.unmodifiableSet(EnumSet.of(BangletVariant.NONE));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/16
	 */
	@Override
	public final List<String> emulatorProjects(BangletVariant __variant)
	{
		return this.emulatorProjects;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/16
	 */
	@Override
	public boolean hasDumping()
	{
		return false;/*this == VMType.SUMMERCOAT*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public boolean hasEmulator()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/12/23
	 */
	@Override
	public boolean hasEmulatorJit()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/05/28
	 */
	@Override
	public NativePortSupport[] hasNativePortSupport()
	{
		// Not supported by default
		return new NativePortSupport[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public final boolean hasRom(BangletVariant __variant)
	{
		return this != VMType.HOSTED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public boolean isSingleSourceSetRom(BangletVariant __variant)
	{
		// False by default
		return false;
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
		
		// Otherwise, include the source sets
		return __project.getName() + "-" + __sourceSet + "." + this.extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/27
	 */
	@Override
	public String outputRomName(String __sourceSet, BangletVariant __variant)
		throws NullPointerException
	{
		// If this is the main source set, then just do not include "main"
		// as it is pointless
		String variantNoun = __variant.properNoun;
		if (SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
		{
			if (variantNoun.isEmpty())
				return "squirreljme." + this.extension;
			return "squirreljme-" + variantNoun + "." + this.extension;
		}
		
		// If there is no variant include it
		if (variantNoun.isEmpty())
			return "squirreljme-" + __sourceSet + "." + this.extension;
		
		return "squirreljme-" + __sourceSet + "-" + variantNoun + "." +
			this.extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public Iterable<Task> processLibraryDependencies(VMBaseTask __task,
		BangletVariant __variant)
		throws NullPointerException
	{
		Collection<Task> rv = new LinkedList<>();
		
		Project project = __task.getProject().getRootProject()
			.findProject(":modules:aot-" +
				this.vmName(VMNameFormat.LOWERCASE));
		
		// If there is no AOT, then fallback to SpringCoat
		if (project == null)
			project = __task.getProject().getRootProject()
				.findProject(":modules:aot-springcoat");
		
		// Make sure the AOT compiler is always up-to-date when this is
		// ran, otherwise things can be very weird if it is not updated
		// which would not be a good thing at all
		Project rootProject = project.getRootProject();
		for (ProjectAndTaskName task : VMHelpers.runClassTasks(project,
			new SourceTargetClassifier(SourceSet.MAIN_SOURCE_SET_NAME,
				VMType.HOSTED, BangletVariant.NONE, ClutterLevel.DEBUG)))
		{
			Project taskProject = rootProject.project(task.project);
			
			// Depends on all of the classes, not just the libraries, for
			// anything the AOT compiler uses. If the compiler changes we
			// need to make sure the updated compiler is used!
			rv.add(taskProject.getTasks().getByName("classes"));
			rv.add(taskProject.getTasks().getByName("jar"));
			
			// The library that makes up the task is important
			rv.add(taskProject.getTasks().getByName(task.task));
		}
		
		// Make sure the hosted environment is working since it needs to
		// be kept up to date as well
		for (Task task : new VMEmulatorDependencies(__task,
			new TargetClassifier(VMType.HOSTED, BangletVariant.NONE,
				ClutterLevel.DEBUG)).call())
			rv.add(task);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/27
	 */
	@Override
	public void processRom(VMBaseTask __task, BangletVariant __variant,
		OutputStream __out, RomBuildParameters __build, List<Path> __libs)
		throws IOException, NullPointerException
	{
		if (__task == null || __out == null || __build == null ||
			__libs == null)
			throw new NullPointerException("NARG");
		
		// Build index mapping of the libraries accordingly
		Map<Path, Integer> libIndex = new LinkedHashMap<>();
		for (int i = 0, n = __libs.size(); i < n; i++)
			libIndex.put(__libs.get(i), i);
		
		// Is this a single source set ROM?
		boolean isSingleSourceSet = this.isSingleSourceSetRom(__variant);
		boolean bootLoaderEnabled = !isSingleSourceSet ||
			(isSingleSourceSet &&
				SourceSet.MAIN_SOURCE_SET_NAME.equals(__task.getSourceSet()));
		
		// Setup arguments for packing the ROM
		List<String> args = new ArrayList<>();
		
		// Boot loader
		if (__build.bootLoaderMainClass != null)
			args.add("-XbootLoaderMainClass:" + __build.bootLoaderMainClass);
		
		// Boot loader class path
		if (bootLoaderEnabled)
		{
			if (__build.bootLoaderClassPath != null)
				args.add("-XbootLoaderClassPath:" +
					VMType.__pathIndexList(libIndex,
					__build.bootLoaderClassPath));
			
			// Launcher main class
			if (__build.launcherMainClass != null)
				args.add("-XlauncherMainClass:" + __build.launcherMainClass);
			
			// Launcher arguments, these are a bit special
			if (__build.launcherArgs != null)
			{
				String[] launcherArgs = __build.launcherArgs;
				for (int i = 0, n = launcherArgs.length; i < n; i++)
					args.add(String.format("-XlauncherArgs:%d:%s", i,
						launcherArgs[i]));
			}
			
			// Launcher class path
			if (__build.launcherClassPath != null)
				args.add("-XlauncherClassPath:" +
					VMType.__pathIndexList(libIndex,
					__build.launcherClassPath));
		}
		
		// Put down paths to libraries to link together
		for (Path path : __libs)
			args.add(path.toString());
			
		// Run the specified command
		this.__aotCommand(__task, null, __out,
			null, "rom", args);
	}
	
	/**
	 * Spawns a virtual machine using the standard {@code VmFactory} class.
	 * 
	 * @param __anyProject Any project.
	 * @param __classifier The classifier to use.
	 * @param __debugEligible Is this eligible to be ran under the debugger?
	 * @param __execSpec The execution specification.
	 * @param __mainClass The main class to execute.
	 * @param __commonName The common name for the application, may be
	 * {@code null}.
	 * @param __sysProps The system properties to define.
	 * @param __libPath The library path to use.
	 * @param __classPath The class path of the execution target.
	 * @param __args Arguments to the started program.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public void spawnVmViaFactory(Project __anyProject,
		SourceTargetClassifier __classifier,
		boolean __debugEligible,
		JavaExecSpecFiller __execSpec, String __mainClass,
		String __commonName, Map<String, String> __sysProps, Path[] __libPath,
		Path[] __classPath, String[] __args)
		throws NullPointerException
	{ 
		if (__anyProject == null || __execSpec == null ||
			__mainClass == null ||
			__sysProps == null || __libPath == null || __classPath == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		// Copy system properties
		Map<String, String> sysProps = new LinkedHashMap<>(__sysProps);
		
		// Bring in any system defined properties we want to truly set?
		VMType.__copySysProps(sysProps);
		
		// Determine the class-path for the emulator
		Set<Path> vmClassPath = new LinkedHashSet<>();
		for (String emulatorProject : this.emulatorProjects)
			for (File file : VMHelpers.projectRuntimeClasspath(
				__anyProject.project(emulatorProject)))
				vmClassPath.add(file.toPath());
		
		// Make sure the base emulator is available as well
		for (File file : VMHelpers.projectRuntimeClasspath(
			__anyProject.findProject(":emulators:emulator-base")))
			vmClassPath.add(file.toPath());
		
		// Add all the emulator outputs
		for (String emulatorProject : this.emulatorProjects)
			for (File file : __anyProject.project(emulatorProject)
				.getTasks().getByName("jar").getOutputs().getFiles())
				vmClassPath.add(file.toPath());
		
		// Debug
		__anyProject.getLogger().debug("VM ClassPath: {}", vmClassPath);
		
		// Build arguments to the VM
		Collection<String> vmArgs = new LinkedList<>();
		
		// Add emulator to launch
		vmArgs.add("-Xemulator:" + this.vmName(VMNameFormat.LOWERCASE));
		
		// Add library paths, suites that are available for consumption
		vmArgs.add("-Xlibraries:" + VMHelpers.classpathAsString(__libPath));
		
		// Enable JDWP debugging?
		if (__debugEligible)
		{
			String jdwpProp = System.getProperty("squirreljme.jdwp");
			if (jdwpProp != null)
				vmArgs.add("-Xjdwp:" + jdwpProp);
		}
		
		// Change threading model?
		String threadModel = System.getProperty("squirreljme.thread");
		if (threadModel != null)
			vmArgs.add("-Xthread:" + threadModel);
		
		// Determine where profiler snapshots are to go, try to use the
		// profiler directory for that
		Path profilerDir = (__classifier != null ?
			VMHelpers.profilerDir(__anyProject, __classifier)
			.get() : __anyProject.getProject().getBuildDir().toPath());
		
		// Use the main class name unless this is a test, so that they are
		// named better
		String profilerClass = (__mainClass.equals(
			VMHelpers.SINGLE_TEST_RUNNER) && __args.length > 0 ?
			__args[0] : (__commonName != null ? __commonName : __mainClass));
		vmArgs.add("-Xsnapshot:" + profilerDir.resolve(
			__anyProject.getName() + "_" +
			profilerClass.replace('.', '-') + ".nps"));
		
		// Class path for the target program to launch
		vmArgs.add("-classpath");
		vmArgs.add(VMHelpers.classpathAsString(__classPath));
		
		// Any system properties
		for (Map.Entry<String, String> sysProp : sysProps.entrySet())
			vmArgs.add("-D" + sysProp.getKey() + "=" + sysProp.getValue());
		
		// Main class of the target to run
		vmArgs.add(__mainClass);
		
		// Any arguments to the target run
		vmArgs.addAll(Arrays.asList(__args));
		
		// Classpath used for execution
		Path[] classPath = vmClassPath.<Path>toArray(
			new Path[vmClassPath.size()]);
		
		// Launching is effectively the same as the hosted run but with the
		// VM here instead. System properties are passed through so that the
		// holding VM and the sub-VM share the same properties.
		VMType.HOSTED.spawnJvmArguments(__anyProject, __classifier,
			__debugEligible,
			__execSpec,
			"cc.squirreljme.emulator.vm.VMFactory",
			__commonName, __sysProps, __libPath, classPath,
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
				return Character.toUpperCase(properName.charAt(0)) +
					properName.substring(1);
		}
	}
	
	/**
	 * Returns the command that is used to execute the ahead of time
	 * compiler interface.
	 * 
	 * @param __task The task being run for.
	 * @param __in The input source (optional).
	 * @param __out The output source (optional).
	 * @param __preArgs Pre command arguments.
	 * @param __command The name of the ROM.
	 * @param __args The arguments for the AOT command.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/16
	 */
	void __aotCommand(VMBaseTask __task, InputStream __in, OutputStream __out,
		Iterable<String> __preArgs, String __command, Iterable<String> __args)
		throws NullPointerException
	{
		if (__task == null || __command == null)
			throw new NullPointerException("NARG");
		
		// Class path is of the compiler target, it does not matter
		Path[] classPath = VMHelpers.runClassPath(__task.getProject()
			.getRootProject().project(":modules:aot-" +
				this.vmName(VMNameFormat.LOWERCASE)),
			new SourceTargetClassifier(
				SourceSet.MAIN_SOURCE_SET_NAME, VMType.HOSTED,
				BangletVariant.NONE, ClutterLevel.DEBUG));
		
		// Setup arguments for compilation
		Collection<String> args = new ArrayList<>();
		
		// The engine to use
		args.add("-Xcompiler:" + this.vmName(VMNameFormat.LOWERCASE));
		
		// The name of this JAR
		args.add("-Xname:" + __task.getProject().getName());
		
		// The current clutter level
		args.add("-XclutterLevel:" +
			__task.getClassifier().getTargetClassifier()
				.getClutterLevel());
		
		// Add source set
		args.add("-XsourceSet:" +
			__task.getSourceSet());
		
		// Arguments before the command
		if (__preArgs != null)
			for (String arg : __preArgs)
				args.add(arg);
		
		// Our run command and any additional arguments
		args.add(__command);
		if (__args != null)
			for (String arg : __args)
				args.add(arg);
		
		// Call the AOT backend
		ExecResult exitResult = __task.getProject().javaexec(__spec ->
			{
				// Figure out the arguments to the JVM, it does not matter
				// what the classpath is
				VMType.HOSTED.spawnJvmArguments(__task.getProject(),
					__task.getClassifier(), false,
					new GradleJavaExecSpecFiller(__spec),
					"cc.squirreljme.jvm.aot.Main",
					null, Collections.emptyMap(),
					classPath,
					classPath,
					args.toArray(new String[args.size()]));
				
				// Use the error stream directly
				__spec.setErrorOutput(new GuardedOutputStream(System.err));
				
				// Use the given input
				if (__in != null)
					__spec.setStandardInput(__in);
				
				// Use output if specified
				if (__out != null)
					__spec.setStandardOutput(__out);
				
				// Ignore error states, let us handle it instead of Gradle
				// so we could handle multiple different exit codes.
				__spec.setIgnoreExitValue(true);
			});
		
		// Processing the library did not work?
		int code;
		if ((code = exitResult.getExitValue()) != 0)
			throw new RuntimeException(String.format(
				"Failed to run the AOT processor for %s. " +
				 "(exit code %d, %s %s).",
				__task.getName(), code, __command,
				(__args == null ? "" : __args)));
	}
	
	/**
	 * Copies system properties with the given prefix into the system
	 * properties for the VM.
	 * 
	 * @param __sysProps The system properties to copy into.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	static void __copySysProps(Map<String, String> __sysProps)
		throws NullPointerException
	{
		if (__sysProps == null)
			throw new NullPointerException("NARG");
		
		// Copy any that are set
		for (Map.Entry<Object, Object> prop :
			System.getProperties().entrySet())
		{
			// Only match certain keys
			String baseKey = Objects.toString(prop.getKey());
			
			// Forward prefixed
			if (baseKey.startsWith(VMType._JVM_KEY_PREFIX))
				__sysProps.put(
					baseKey.substring(VMType._JVM_KEY_PREFIX.length()),
					Objects.toString(prop.getValue()));
			
			// IntelliJ Coverage
			else if (baseKey.startsWith("coverage."))
				__sysProps.put(Objects.toString(prop.getKey()),
					Objects.toString(prop.getValue()));
		}
	}
	
	/**
	 * Maps paths to indexes, for flatification and simplification of
	 * processes that take paths and refer to them multiple times.
	 * 
	 * @param __libIndex The library index.
	 * @param __paths The paths to map.
	 * @return A string which maps to the given indexes.
	 * @since 2021/08/22
	 */
	private static String __pathIndexList(Map<Path, Integer> __libIndex,
		Path... __paths)
	{
		StringBuilder sb = new StringBuilder();
		
		for (Path p : __paths)
		{
			// This is an error if this occurs
			Integer index = __libIndex.get(p);
			if (p == null || index == null)
				throw new IllegalStateException(
					"Missing path or index for " + p);
			
			// Comma for the index?
			if (sb.length() > 0)
				sb.append(',');
			
			sb.append(index);
		}
		
		return sb.toString();
	}
}
