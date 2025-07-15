// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.ArraySuiteManager;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VMThreadModel;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.DataContainerLibrary;
import cc.squirreljme.vm.JarClassLibrary;
import cc.squirreljme.vm.NameOverrideClassLibrary;
import cc.squirreljme.vm.ResourceBasedClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.standalone.hosted.HostedJDWPProxy;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.AccessMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Instantiates and initializes the NanoCoat VM.
 *
 * @since 2023/12/03
 */
public class NanoCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @since 2023/12/03
	 */
	public NanoCoatFactory()
	{
		super("nanocoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/03
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __profiler,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel,
		VMSuiteManager __suiteManager, VMClassLibrary[] __classpath,
		String __mainClass, Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Is this Windows?
		boolean isWindows = System.getProperty("os.name")
			.toLowerCase(Locale.ROOT).contains("windows");
		
		// Locate the executable path
		Path execPath = NativeBinding.libFromResources(
			(isWindows ? "squirreljme.exe" : "squirreljme"), false);
		if (!Files.isExecutable(execPath))
			try
			{
				// Add the execute bit
				Set<PosixFilePermission> bits =
					new HashSet<>(Files.getPosixFilePermissions(execPath));
				bits.add(PosixFilePermission.OWNER_EXECUTE);
				
				// Set the new bits
				Files.setPosixFilePermissions(execPath, bits);
			}
			catch (IOException|UnsupportedOperationException ignored)
			{
			}
		
		// Determine all the arguments to the VM
		List<String> execArgs = new ArrayList<>();
		
		// JDWP Debugger?
		if (__jdwp != null)
		{
			// Setup proxy
			HostedJDWPProxy jdwp = new HostedJDWPProxy(__jdwp);
			
			// Use this proxy to forward instead
			execArgs.add(String.format("-Xjdwp:localhost:%d",
				jdwp.port));
		}
		
		// Thread model?
		if (__threadModel != null)
			switch (__threadModel)
			{
				case SINGLE_THREAD_COOP:
					execArgs.add("-Xthread:coop");
					break;
					
				case SINGLE_THREAD_PREEMPT:
					execArgs.add("-Xthread:shared");
					break;
					
				case MULTI_THREAD:
					execArgs.add("-Xthread:multi");
					break;
			}
		
		// Recursively process ROMs and libraries
		Set<Path> roms = new LinkedHashSet<>();
		Set<String> libraries = new LinkedHashSet<>();
		if (__suiteManager != null)
			NanoCoatFactory.unblend(roms, libraries, "", __suiteManager);
		
		// Any roms to add?
		if (!roms.isEmpty())
		{
			// Build the classpath
			StringBuilder cp = new StringBuilder("-Xroms");
			for (Path rom : roms)
			{
				cp.append(File.pathSeparator);
				cp.append(rom.toAbsolutePath().normalize());
			}
			
			// Add final path
			execArgs.add(cp.toString());
		}
		
		// Any libraries to add?
		if (!libraries.isEmpty())
		{
			// Build the classpath
			StringBuilder cp = new StringBuilder("-Xlibraries");
			for (String library : libraries)
			{
				cp.append(File.pathSeparator);
				cp.append(library);
			}
			
			// Add final path
			execArgs.add(cp.toString());
		}
		
		// Classpath?
		if (__classpath != null)
		{
			// Build the classpath
			StringBuilder cp = new StringBuilder();
			for (VMClassLibrary lib : __classpath)
			{
				if (cp.length() > 0)
					cp.append(File.pathSeparator);
				
				// Prefer the path, otherwise fallback to the name
				if (lib.path() != null)
					cp.append(lib.path().toAbsolutePath().normalize());
				else
					cp.append(lib.name());
			}
			
			// Add final classpath
			execArgs.add("-classpath");
			execArgs.add(cp.toString());
		}
		
		// System properties
		if (__sysProps != null)
			for (Map.Entry<String, String> sysProp : __sysProps.entrySet())
				execArgs.add(String.format("-D%s=%s",
					sysProp.getKey(), sysProp.getValue()));
		
		// Main class and arguments
		if (__mainClass != null)
		{
			execArgs.add(__mainClass);
			if (__args != null)
				execArgs.addAll(Arrays.asList(__args));
		}
		
		// Debug
		Debugging.debugNote("Exec: %s %s",
			execPath, execArgs);
		
		// Set up final virtual machine launcher
		return new NanoCoatVirtualMachine(execPath, execArgs);
	}
	
	/**
	 * Unblends suites and libraries.
	 *
	 * @param __outRoms The output SpringCoat ROMs.
	 * @param __outLibs The output libraries.
	 * @param __prefix The prefix used for final output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If unblending failed.
	 * @since 2025/07/14
	 */
	public static void unblend(Set<Path> __outRoms, Set<String> __outLibs,
		String __prefix, VMSuiteManager... __in)
		throws NullPointerException, VMException
	{
		if (__outRoms == null || __outLibs == null || __in == null)
			throw new NullPointerException("NARG");
		
		for (VMSuiteManager suite : __in)
		{
			if (suite instanceof ArraySuiteManager)
			{
				ArraySuiteManager arraySuite = (ArraySuiteManager)suite;
				
				// Process each library
				for (VMClassLibrary lib : arraySuite)
					NanoCoatFactory.unblend(__outRoms, __outLibs,
						__prefix, lib);
			}
			
			// Unknown
			else
				throw Debugging.todo(suite.getClass());
		}
	}
	
	/**
	 * Unblends suites and libraries.
	 *
	 * @param __outRoms The output SpringCoat ROMs.
	 * @param __outLibs The output libraries.
	 * @param __prefix The prefixed used for final output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If unblending failed.
	 * @since 2025/07/14
	 */
	public static void unblend(Set<Path> __outRoms, Set<String> __outLibs,
		String __prefix, VMClassLibrary... __in)
		throws NullPointerException, VMException
	{
		if (__outRoms == null || __outLibs == null || __in == null)
			throw new NullPointerException("NARG");
		
		for (VMClassLibrary lib : __in)
		{
			// Renamed library
			if (lib instanceof NameOverrideClassLibrary)
			{
				NameOverrideClassLibrary rename =
					(NameOverrideClassLibrary)lib;
				
				// Process library further
				NanoCoatFactory.unblend(__outRoms, __outLibs,
					String.format("?mv?%s?", rename.name), rename.base);
			}
			
			// Library from a resourced Jar
			else if (lib instanceof ResourceBasedClassLibrary)
			{
				ResourceBasedClassLibrary rcLib =
					(ResourceBasedClassLibrary)lib;
				
				// Determine the actual Jar Path
				Path jarPath;
				try
				{
					jarPath = Paths.get(rcLib.actingclass
						.getProtectionDomain().getCodeSource().getLocation()
						.toURI().getPath());
				}
				catch (URISyntaxException __e)
				{
					throw new VMException(__e.getMessage(), __e);
				}
				
				// Is this really just a ROM in disguise?
				String prefix = rcLib.prefix;
				if (prefix.startsWith("/SQUIRRELJME.SQC/") ||
					prefix.startsWith("/SQUIRRELJME-DEBUG.SQC/"))
					__outRoms.add(jarPath);
				
				// Add actual resource library
				else
					__outLibs.add(String.format("?rc?%s?%s?%s",
						rcLib.prefix, rcLib.name, jarPath.toAbsolutePath()
							.normalize()));
			}
			
			// Actual Jar or data container
			else if (lib instanceof JarClassLibrary ||
				lib instanceof DataContainerLibrary)
				__outLibs.add(lib.path().toAbsolutePath()
					.normalize().toString());
			
			else
				throw Debugging.todo(lib.getClass());
		}
	}
}
