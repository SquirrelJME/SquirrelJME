// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.hosted;

import cc.squirreljme.emulator.EmulatedTaskShelf;
import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.DataContainerLibrary;
import cc.squirreljme.vm.NameOverrideClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Contains the hosted virtual machine state.
 *
 * @since 2023/12/03
 */
public class HostedVirtualMachine
	implements VirtualMachine
{
	/** The process being built. */
	protected final ProcessBuilder builder;
	
	/** Actual system properties. */
	protected final Map<String, String> actualSysProps;
	
	/** The main class. */
	protected final String mainClass;
	
	/** The manager which contains all the library JARs. */
	protected final VMSuiteManager suiteManager;
	
	/** Factory for the host debugger interface. */ 
	protected final JDWPHostFactory jdwpFactory;
	
	/** The classpath to use for execution. */
	private final VMClassLibrary[] _classPath;
	
	/** Main class arguments. */
	private final String[] _mainArgs;
	
	/**
	 * Initializes the hosted virtual machine.
	 *
	 * @param __jdwp The debugger interface to communicate with.
	 * @param __suiteManager The manager for suite entries.
	 * @param __classPath The initial classpath to use.
	 * @param __mainClass The main class to execute.
	 * @param __sysProps The system properties to pass.
	 * @param __args Arguments to the main class.
	 * @since 2023/12/03
	 */
	public HostedVirtualMachine(JDWPHostFactory __jdwp,
		VMSuiteManager __suiteManager,
		VMClassLibrary[] __classPath, String __mainClass,
		Map<String, String> __sysProps, String... __args)
		throws NullPointerException
	{
		if (__suiteManager == null || __classPath == null ||
			__mainClass == null)
			throw new NullPointerException("NARG");
		
		// Setup base builder
		ProcessBuilder builder = new ProcessBuilder();
		this.builder = builder;
		
		// Redirect all inputs
		builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
		// Setup environment variables that the VM needs to function
		Map<String, String> env = builder.environment();
		
		// Setup actual system properties as a copy, for correction and
		// otherwise
		Map<String, String> actualSysProps = new LinkedHashMap<>();
		this.actualSysProps = actualSysProps;
		if (__sysProps != null)
			actualSysProps.putAll(__sysProps);
		
		// Libraries and where they can be found
		actualSysProps.put(EmulatedTaskShelf.AVAILABLE_LIBRARIES, "");
		actualSysProps.put(EmulatedTaskShelf.RUN_CLASSPATH, "");
		actualSysProps.put(EmulatedTaskShelf.HOSTED_VM_CLASSPATH, "");
		actualSysProps.put(EmulatedTaskShelf.HOSTED_VM_SUPPORTPATH, "");
		
		// Store other values
		this.jdwpFactory = __jdwp;
		this.suiteManager = __suiteManager;
		this.mainClass = __mainClass;
		this._classPath = __classPath.clone();
		this._mainArgs = (__args != null ? __args.clone() : new String[0]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/03
	 */
	@Override
	public int runVm()
		throws VMException
	{
		ProcessBuilder builder = this.builder;
		
		// Setup and run the virtual machine
		Path tempJars = null;
		Process process = null;
		try
		{
			// We need to go through and possibly extract JARs depending on
			// how they are stored in the suite manager
			tempJars = Files.createTempDirectory(
				"squirreljme-hosted-jars");
			
			// Setup cleanup on exit
			Runtime.getRuntime().addShutdownHook(
				new Thread(new HostedCleanup(tempJars),
					"hosted-cleanup"));
			
			// Get system properties to use
			Map<String, String> actualSysProps = this.actualSysProps;
			
			// Extract and handle libraries accordingly
			Set<Path> libPaths = new LinkedHashSet<>();
			Map<String, Path> jarToPath = new LinkedHashMap<>();
			for (String libName : this.suiteManager.listLibraryNames())
			{
				// Obtain the library
				VMClassLibrary lib = this.suiteManager.loadLibrary(
					libName);
				
				String actualName;
				Path path;
				
				if (lib instanceof NameOverrideClassLibrary)
				{
					actualName = lib.name();
					lib = ((NameOverrideClassLibrary)lib).originalLibrary();
				}
				else
					actualName = lib.name();
				
				// Implode everything regardless of what the input is, if
				// this is not done then extra libraries specified via
				// -Xlibraries and used with for example i-mode software it
				// will not be able to find the JAM because it technically
				// is in the wrong location
				path = HostedVirtualMachine.implodeJar(tempJars,
					lib, actualName);
				
				// Map between the two
				libPaths.add(path);
				jarToPath.put(actualName, path);
				
				// System property path translation, as needed
				Path wasPath = lib.path();
				if (wasPath != null)
				{
					// What are we coming from?
					String wasRelStr = wasPath.toString();
					String wasAbsStr = wasPath.toAbsolutePath().toString();
						
					// What are we going to?
					String nowAbsStr = path.toAbsolutePath().toString();
					for (Map.Entry<String, String> entry :
						this.actualSysProps.entrySet())
					{
						String value = entry.getValue();
						if (Objects.equals(value, wasRelStr) ||
							Objects.equals(value, wasAbsStr))
							entry.setValue(nowAbsStr);
					}
				}
			}
			
			// Determine the true classpath of what is being run
			Set<Path> classPaths = new LinkedHashSet<>();
			for (VMClassLibrary lib : this._classPath)
			{
				Path target = jarToPath.get(lib.name());
				
				if (target != null)
					classPaths.add(target);
			}
			
			// Actual classpath string
			String classPathString =
				EmulatedTaskShelf.classpathAsString(classPaths);
			
			// Our own classpath is the support path
			String supportPath = System.getProperty("java.class.path");
			
			// Define all the available library and classpath paths
			actualSysProps.put(EmulatedTaskShelf.AVAILABLE_LIBRARIES,
				EmulatedTaskShelf.classpathAsString(libPaths));
			actualSysProps.put(EmulatedTaskShelf.HOSTED_VM_CLASSPATH,
				classPathString);
			actualSysProps.put(EmulatedTaskShelf.HOSTED_VM_SUPPORTPATH,
				supportPath);
			actualSysProps.put(EmulatedTaskShelf.RUN_CLASSPATH,
				classPathString);
			
			// Library path for the native binding, to reduce extraction load
			Path nativeLib = NativeBinding.loadedLibraryPath();
			if (nativeLib != null)
				actualSysProps.put(NativeBinding.LIB_PRELOAD,
					nativeLib.toAbsolutePath().toString());
			
			// Build a command line of how we are executing the hosted virtual
			// machine...
			List<String> args = new ArrayList<>();
			
			// We will be calling the executable directly, fallback to Java if
			// not found
			args.add(Objects.toString(RuntimeShelf.vmDescription(
				VMDescriptionType.EXECUTABLE_PATH), "java"));
			
			// Needed on macOS for the GUI to properly work
			String osName = System.getProperty("os.name");
			if (osName != null &&
				(osName.toLowerCase().contains("mac os") ||
				osName.toLowerCase().contains("mac os x")))
				args.add("-XstartOnFirstThread");
			
			// If we are connecting to a debugger, we need to set up a proxy
			// between the new JVM and this current one through TCP
			JDWPHostFactory jdwpFactory = this.jdwpFactory;
			if (jdwpFactory != null)
				try
				{
					// Setup proxy
					HostedJDWPProxy proxy = new HostedJDWPProxy(jdwpFactory);
					
					// We need to connect to the VM through our own internal
					// proxy
					args.add(String.format("-agentlib:jdwp=" +
						"transport=dt_socket,server=n," +
						"address=localhost:%d,suspend=y",
						proxy.port));
				}
				catch (IOException __e)
				{
					__e.printStackTrace();
				}
			
			// Any system properties
			for (Map.Entry<String, String> prop : actualSysProps.entrySet())
				args.add(String.format("-D%s=%s",
					prop.getKey(), prop.getValue()));
			
			// Classpath accordingly
			args.add("-classpath");
			args.add(supportPath + File.pathSeparator + classPathString);
			
			// NativeBinding is the wrapper which loads our library for us
			args.add("cc.squirreljme.emulator.NativeBinding");
			
			// Main class accordingly
			args.add(this.mainClass);
			
			// Arguments to the main class
			for (String arg : this._mainArgs)
				if (arg != null)
					args.add(arg);
			
			// Use these arguments
			builder.command(args);
			
			// Debug
			Debugging.debugNote("Hosted Args: %s", args);
			
			// Execute the virtual machine, wait for it to complete
			process = builder.start();
			int exitCode;
			for (;;)
				try
				{
					exitCode = process.waitFor();
					break;
				}
				catch(InterruptedException ignored)
				{
				}
			
			// Exit with the exit code of the VM
			return exitCode;
		}
		
		// Oops!
		catch (IOException e)
		{
			throw new VMException("I/O Error", e);
		}
		
		// Cleanup extracted JARs
		finally
		{
			// Kill the process if it is running
			if (process != null)
				if (process.isAlive())
					try
					{
						process.destroyForcibly();
					}
					catch (Throwable __e)
					{
						__e.printStackTrace();
					}
			
			// Cleanup Jars
			new HostedCleanup(tempJars).run();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setTraceBits(boolean __or, int __bits)
	{
		// No effect.
	}
	
	/**
	 * Transforms a resource based class library into an actual Jar that
	 * standard virtual machines can use.
	 *
	 * @param __tempJars The path where the JAR should be created.
	 * @param __lib The library to transform.
	 * @param __libName The library name.
	 * @return The resultant path of the library.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/03
	 */
	public static Path implodeJar(Path __tempJars,
		VMClassLibrary __lib, String __libName)
		throws IOException, NullPointerException
	{
		if (__tempJars == null || __lib == null || __libName == null)
			throw new NullPointerException("NARG");
		
		// The target path of the output Jar, always end in JAR
		Path resultPath = __tempJars.resolve(
			((SuiteUtils.isAny(__libName) ?
				__libName : __libName + ".jar")));
		
		// Buffer to use for copying data
		byte[] tempBuf = new byte[1048576];
		
		// Extract library
		Path temp = null;
		try
		{
			// Setup new file to write to
			temp = Files.createTempFile("implode", ".jar");
			
			// Copy all the Zip entries accordingly
			try (OutputStream out = Files.newOutputStream(temp,
					StandardOpenOption.CREATE, StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING))
			{
				// Just a resource file?
				String[] rcList = __lib.listResources();
				if (rcList.length == 1 &&
					DataContainerLibrary.RESOURCE_NAME.equals(rcList[0]))
				{
					try (InputStream in = __lib.resourceAsStream(rcList[0]))
					{
						StreamUtils.copy(in, out, tempBuf);
					}
				}
				else
				{
					try (ZipOutputStream zip = new ZipOutputStream(out))
					{
						// Copy all resource data
						for (String rcName : rcList)
						{
							// Setup ZIP entry
							ZipEntry entry = new ZipEntry(rcName);
							zip.putNextEntry(entry);
							
							// Write to it all
							try (InputStream in = __lib.resourceAsStream(
								rcName))
							{
								StreamUtils.copy(in, zip, tempBuf);
							}
							
							// Finish it
							zip.closeEntry();
						}
						
						// Finish the Zip
						zip.finish();
						zip.flush();
					}
				}
			}
			
			// Replace the target file
			Files.move(temp, resultPath,
				StandardCopyOption.REPLACE_EXISTING);
			
			// Use this path
			return resultPath;
		}
		finally
		{
			if (temp != null)
				try
				{
					Files.delete(temp);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
