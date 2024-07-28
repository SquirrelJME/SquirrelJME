// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.util.ForwardInputToOutput;
import cc.squirreljme.plugin.util.GradleLoggerOutputStream;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import cc.squirreljme.plugin.util.SimpleJavaExecSpecFiller;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;

/**
 * Runs the program within the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMRunTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/16
	 */
	public VMRunTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// The task owning this
		VMRunTask runTask = (VMRunTask)__task;
		
		// Need this to get the program details
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
			
		// Gather the class path to use for target execution, this is all the
		// SquirrelJME modules this depends on
		VMSpecifier vmType = this.classifier.getVmType();
		Path[] classPath = VMHelpers.runClassPath(__task,
			this.classifier, true);
		
		// Debug
		__task.getLogger().debug("Classpath: {}", Arrays.asList(classPath));
		
		// Determine the main entry class or MIDlet to use
		JavaMEMidlet midlet = runTask.midlet;
		String mainClass = VMHelpers.mainClass(config, midlet);
		
		// Debug
		__task.getLogger().debug("MIDlet: {}", midlet);
		__task.getLogger().debug("MainClass: {}", mainClass);
		
		// Debugger being used?
		URI debugServer = runTask.debugServer;
		
		// Standard SquirrelJME command line arguments to use
		List<String> args = new ArrayList<>();
		
		// Process for the debugger, to kill if the main task exits
		Process debuggerProc = null;
		
		// Force interpreter?
		boolean forceInterpreter = false;
		
		// Which command line is used?
		List<String> procArgs = new ArrayList<>();
		Map<String, String> sysProps = new LinkedHashMap<>();
		if (debugServer != null)
		{
			// Debug server?
			if ("file".equals(debugServer.getScheme()))
			{
				procArgs.add(Paths.get(debugServer).toAbsolutePath()
					.toString());
				procArgs.add("localhost:2345");
				
				// Force interpreter to be used
				forceInterpreter = true;
			}
			
			// JDWP?
			else if ("jdwp".equals(debugServer.getScheme()))
				sysProps.put("squirreljme.jdwp",
					debugServer.getSchemeSpecificPart());
			
			// Internal debugger?
			else if ("internal".equals(debugServer.getScheme()))
			{
				// Choose random port that is not likely to be used
				int port = 32767 +
					new Random(System.currentTimeMillis())
						.nextInt(32767);
				
				// Listen on this port
				sysProps.put("squirreljme.jdwp",
					":" + port);
				
				// Use this task as the base and find its output Jar
				Task debuggerTask = new __FindInternalDebugger__(
					runTask.getProject()).call();
				Path debuggerJar = debuggerTask.getOutputs().getFiles()
					.getSingleFile().toPath();
				
				// Use this Java command
				List<String> debuggerArgs = new ArrayList<>();
				Path javaExe = SimpleJavaExecSpecFiller.findJavaExe();
				debuggerArgs.add((javaExe == null ? "java" :
					javaExe.toAbsolutePath().toString()));
				
				// Use the same classpath as the host
				debuggerArgs.add("-classpath");
				debuggerArgs.add(debuggerJar.toAbsolutePath().toString());
				
				// Launch into the debugger instead
				debuggerArgs.add("cc.squirreljme.debugger.Main");
				debuggerArgs.add("localhost:" + port);
				
				// Fork process with the debugger
				ProcessBuilder builder = new ProcessBuilder(debuggerArgs);
				
				// Use our terminal and pipes for the output
				builder.inheritIO();
				
				// Working directory in the build directory
				builder.directory(runTask.getProject().getBuildDir());
				
				// Start the debugger
				try
				{
					// Start the debugger
					debuggerProc = builder.start();
				}
				
				// It failed, so cannot debug
				catch (IOException __e)
				{
					throw new RuntimeException("Could not launch debugger.",
						__e);
				}
			}
		}
		
		// If executing a MIDlet, then the single main argument is the actual
		// name of the MIDlet to execute
		if (midlet != null)
			args.add(midlet.mainClass);
		
		// Debug
		__task.getLogger().debug("Target Working Dir: {}",
			System.getProperty("user.dir"));
		
		// Execute the virtual machine, if the exit status is non-zero then
		// the task execution will be considered as a failure
		JavaExecSpecFiller execSpec = new SimpleJavaExecSpecFiller();
		vmType.spawnJvmArguments(__task.getProject(),
			this.classifier, true,
			execSpec, mainClass,
			(midlet != null ? midlet.mainClass : mainClass),
			sysProps,
			classPath, classPath,
			args.<String>toArray(new String[args.size()]));
		
		// Get command line to use
		List<String> cmdLine = new ArrayList<>();
		for (String arg : execSpec.getCommandLine())
			cmdLine.add(arg);
		
		// Pull in command line
		for (int i = 0, n = cmdLine.size(); i < n; i++)
		{
			// Normally add the command line
			String arg = cmdLine.get(i);
			procArgs.add(arg);
			
			// If this is the executable, we want to possibly inject
			// -zero to prevent compilation from being performed if doing
			// GDB
			if (i == 0 && forceInterpreter)
				procArgs.add("-zero");
		}
		
		// Setup process
		ProcessBuilder procBuilder = new ProcessBuilder(procArgs);
		
		// Working directory in the build directory, for any logs or
		// otherwise
		procBuilder.directory(runTask.getProject().getBuildDir());
		
		// Pipe output
		procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
		procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
		
		// Run task
		Process proc = null;
		ForwardInputToOutput stdOut = null;
		ForwardInputToOutput stdErr = null;
		try
		{
			// Start it
			proc = procBuilder.start();
			
			// Forward streams
			stdOut = new ForwardInputToOutput(
				proc.getInputStream(), new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.LIFECYCLE,
					-1, -1));
			stdErr = new ForwardInputToOutput(
				proc.getErrorStream(), new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.ERROR,
					-1, -1));
			
			// Run them
			stdOut.runThread("stdOutPipe");
			stdErr.runThread("stdErrPipe");
			
			// Did the task fail?
			int exitValue = proc.waitFor();
			if (exitValue != 0)
				throw new RuntimeException("Task exited with: " + exitValue);
		}
		catch (IOException|InterruptedException __e)
		{
			// Make sure it really dies
			if (proc != null)
				proc.destroyForcibly();
			
			// Always kill the debugger process
			if (debuggerProc != null)
				debuggerProc.destroyForcibly();
			
			// Now fail
			throw new RuntimeException("Task run failed or was interrupted.",
				__e);
		}
		finally
		{
			// Always kill the debugger process
			if (debuggerProc != null)
				debuggerProc.destroyForcibly();
			
			if (stdOut != null)
				try
				{
					stdOut.close();
				}
				catch (IOException __ignored)
				{
				}
			
			if (stdErr != null)
				try
				{
					stdErr.close();
				}
				catch (IOException __ignored)
				{
				}
		}
	}
}
