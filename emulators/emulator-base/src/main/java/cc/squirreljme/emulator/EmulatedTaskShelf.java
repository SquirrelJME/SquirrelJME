// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Emulation for {@link TaskShelf}.
 *
 * @since 2020/12/31
 */
@SuppressWarnings("unused")
public final class EmulatedTaskShelf
{
	/** Property for the libraries that are available. */
	public static final String AVAILABLE_LIBRARIES =
		"squirreljme.hosted.libraries";
	
	/** Property for the classpath of the currently running task. */
	public static final String RUN_CLASSPATH =
		"squirreljme.hosted.classpath";
	
	/** Property for the classpath of the currently running VM. */
	public static final String HOSTED_VM_CLASSPATH =
		"squirreljme.hosted.vm.classpath";
	
	/** The virtual machine support path, the minimal needed to run a VM. */
	public static final String HOSTED_VM_SUPPORTPATH =
		"squirreljme.hosted.vm.supportpath";
	
	/** Original property prefix. */
	public static final String ORIGINAL_PROP_PREFIX =
		"squirreljme.orig.";
	
	/** Are we on Windows? */
	private static final boolean _ON_WINDOWS;
	
	static
	{
		String osName = System.getProperty("os.name");
		_ON_WINDOWS = osName.toLowerCase().contains("windows");
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/21
	 */
	public static String classpathAsString(Path... __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		return EmulatedTaskShelf.classpathAsString(Arrays.asList(__paths));
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	public static String classpathAsString(Iterable<Path> __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		for (Path path : __paths)
		{
			if (sb.length() > 0)
				sb.append(File.pathSeparatorChar);
			sb.append(path);
		}
		
		return sb.toString();
	}
	
	/**
	 * As {@link TaskShelf#start(JarPackageBracket[], String, String[],
	 * String[], int, int)}. 
	 * 
	 * @param __classPath The class path that the launched application should
	 * use.
	 * @param __mainClass The main class to execute, this must be a class which
	 * contains {@code static void main(String[] __args)}.
	 * @param __args The arguments to the main class.
	 * @param __sysPropPairs System property pairs, even values are keys and
	 * odd values are values. This array must always be a multiple of two.
	 * @param __stdOut The {@link TaskPipeRedirectType} for standard output.
	 * @param __stdErr The {@link TaskPipeRedirectType} for standard error.
	 * @return The bracket that represents the task.
	 * @throws MLECallError If any argument is {@code null}, or an array
	 * contains a {@code null} value, the {@code __sysPropPairs} is not a
	 * multiple of two, {@code __classPath[0]} is not the same Jar package
	 * as our own classpath's first JAR, if the task could not be created,
	 * or if either the {@code __stdOut} or {@code __stdErr} redirect types
	 * are not valid.
	 * @since 2020/07/02
	 */
	public static TaskBracket start(
		JarPackageBracket[] __classPath, String __mainClass, String[] __args,
		String[] __sysPropPairs, int __stdOut, int __stdErr)
		throws MLECallError
	{
		if (__classPath == null || __mainClass == null || __args == null ||
			__sysPropPairs == null)
			throw new MLECallError("Null start() arguments.");
		
		// Start building the process
		@SuppressWarnings("UseOfProcessBuilder")
		ProcessBuilder builder = new ProcessBuilder();
		List<String> args = new LinkedList<>();
		
		// We will be calling the Java executable, if we cannot find one then
		// assume it is just "java"
		args.add(Objects.toString(RuntimeShelf.vmDescription(
			VMDescriptionType.EXECUTABLE_PATH), "java"));
		
		// Determine which system properties we inherit from
		Map<String, String> sysProps = new LinkedHashMap<>();
		for (Map.Entry<Object, Object> e : System.getProperties().entrySet())
		{
			// System properties 
			String key = e.getKey().toString();
			if (key.startsWith(EmulatedTaskShelf.ORIGINAL_PROP_PREFIX))
			{
				// Extract the original property so we use it directly
				sysProps.put(key.substring(
					EmulatedTaskShelf.ORIGINAL_PROP_PREFIX.length()),
					Objects.toString(e.getValue(), ""));
				
				// And add the property we based this off, so that we if the
				// task we are starting wants to launch another task it gets
				// all of them as well
				sysProps.put(key,
					Objects.toString(e.getValue(), ""));
			}
			
			// Otherwise only initially inherit these specific properties
			else
			{
				switch (key)
				{
					case EmulatedTaskShelf.AVAILABLE_LIBRARIES:
					case EmulatedTaskShelf.RUN_CLASSPATH:
					case EmulatedTaskShelf.HOSTED_VM_CLASSPATH:
					case EmulatedTaskShelf.HOSTED_VM_SUPPORTPATH:
					case NativeBinding.LIB_PRELOAD:
						sysProps.put(key,
							Objects.toString(e.getValue(), ""));
						break;
				}
			}
		}
		
		// Library preload, if applicable?
		String preloadLib = sysProps.get(NativeBinding.LIB_PRELOAD);
		if (preloadLib == null || preloadLib.isEmpty())
		{
			Path emulatorLib = NativeBinding.loadedLibraryPath();
			if (emulatorLib != null)
				sysProps.put(NativeBinding.LIB_PRELOAD,
					emulatorLib.toAbsolutePath().toString());
		}
		
		// Load system property pairs
		for (int i = 0, n = __sysPropPairs.length & (~1); i < n; i += 2)
			sysProps.put(__sysPropPairs[i], __sysPropPairs[i + 1]);
		
		// We need the support path to determine how we are launching this
		Path[] vmSupportPath = EmulatedTaskShelf.__classpathDecode(
			System.getProperty(EmulatedTaskShelf.HOSTED_VM_SUPPORTPATH));
		
		// Determine the classpath as if one were just running the application
		// itself, this is the virtual classpath to run the process
		Path[] runClassPath = new Path[__classPath.length];
		for (int i = 0, n = __classPath.length; i < n; i++)
			runClassPath[i] = ((EmulatedJarPackageBracket)__classPath[i])
				.vmLib.path();
		
		// We need to tell it what our own classpath is, logically
		sysProps.put(EmulatedTaskShelf.RUN_CLASSPATH,
			EmulatedTaskShelf.classpathAsString(runClassPath));
		
		// Combine these two to make the running classpath, this includes the
		// emulator as well
		Set<Path> allLibs = new LinkedHashSet<>();
		allLibs.addAll(Arrays.<Path>asList(vmSupportPath));
		allLibs.addAll(Arrays.<Path>asList(runClassPath));
		
		// Tell the target what classpath we are running under
		String allLibsStr = EmulatedTaskShelf.classpathAsString(allLibs);
		sysProps.put(EmulatedTaskShelf.HOSTED_VM_CLASSPATH,
			allLibsStr);
		
		// Place in the actual target classpath which is the combination of
		// the base VM one and the target one
		args.add("-classpath");
		args.add(allLibsStr);
		
		// Use all declared system properties to ensure that they are properly
		// inherited from the host virtual machine
		for (Map.Entry<String, String> e : sysProps.entrySet())
			args.add(EmulatedTaskShelf.__escape(String.format("-D%s=%s",
				e.getKey(), e.getValue())));
		
		
		// Use special main handler which handles loading the required
		// methods for the hosted environment to work correctly with
		// SquirrelJME... our sub-tasks need to have this in order to properly
		// work
		args.add("cc.squirreljme.emulator.NativeBinding");
		
		// The main class is our direct main class, we do not need special
		// handling for it at all
		args.add(__mainClass);
		
		// Add all of the calling arguments we desire to be forwarded to the
		// sub-process
		args.addAll(Arrays.<String>asList(__args));
		
		// Use these arguments
		builder.command(args);
		
		// Debug
		Debugging.debugNote("Forked CmdLine: %s", args);
		
		// Alternative piping for standard output
		switch (__stdOut)
		{
			case TaskPipeRedirectType.TERMINAL:
				builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
				break;
			
			case TaskPipeRedirectType.BUFFER:
			case TaskPipeRedirectType.DISCARD:
				throw Debugging.todo();
			
			default:
				throw new MLECallError("Unknown stdOut: " + __stdOut);
		}
		
		// Alternative piping for standard output
		switch (__stdErr)
		{
			case TaskPipeRedirectType.TERMINAL:
				builder.redirectError(ProcessBuilder.Redirect.INHERIT);
				break;
			
			case TaskPipeRedirectType.BUFFER:
			case TaskPipeRedirectType.DISCARD:
				throw Debugging.todo();
			
			default:
				throw new MLECallError("Unknown stdOut: " + __stdOut);
		}
		
		// Start and track the process
		try
		{
			// Start the process and ensure that when this VM terminates that
			// the sub-process is terminated as well.
			Process process = builder.start();
			Runtime.getRuntime()
				.addShutdownHook(new Thread(new ProcessKiller(process),
					String.format("processKiller@%08x",
						System.identityHashCode(process))));
			
			return new EmulatedTaskBracket(process);
		}
		catch (IOException e)
		{
			throw new MLECallError("Could not spawn task.", e);
		}
	}
	
	/**
	 * As {@link TaskShelf#status(TaskBracket)}. 
	 * 
	 * @param __task The task to request the status of.
	 * @return One of {@link TaskStatusType}.
	 * @throws MLECallError If the task is not valid.
	 * @since 2020/12/31
	 */
	public static int status(TaskBracket __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null task.");
		
		Process process = ((EmulatedTaskBracket)__task).process;
		if (process.isAlive())
			return TaskStatusType.ALIVE;
		return TaskStatusType.EXITED;
	}
	
	/**
	 * Decodes the classpath string.
	 * 
	 * @param __string The string to decode.
	 * @return The decoded path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	static Path[] __classpathDecode(String __string)
		throws NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		Collection<Path> result = new LinkedList<>();
		for (String split : __string.split(Pattern.quote(File.pathSeparator)))
			result.add(Paths.get(split));
		
		return result.<Path>toArray(new Path[result.size()]);
	}
	
	/**
	 * Escapes the given string.
	 * 
	 * @param __s The string to escape.
	 * @return The escaped string.
	 * @since 2023/04/13
	 */
	static String __escape(String __s)
	{
		// Do not escape outside of Windows
		if (!EmulatedTaskShelf._ON_WINDOWS)
			return __s;
		
		// No quotes to escape?
		if (__s.indexOf('"') < 0)
			return __s;
		
		// Process each character and look for quotes
		StringBuilder result = new StringBuilder();
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Escape quote if found
			if (c == '"')
				result.append('\\');
			result.append(c);
		}
		
		return result.toString();
	}
}
