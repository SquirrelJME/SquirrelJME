// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.gdb.GdbUtils;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;

/**
 * Runs the program within the virtual machine.
 *
 * @since 2024/07/28
 */
public class VMRunTaskDetached
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/** The logger for logging messages. */
	private final Logger _logger;
	
	/** The classpath to use. */
	private final Path[] _classPath;
	
	/** The MIDlet to execute, optionally. */
	private final JavaMEMidlet _midlet;
	
	/** Optional main class, required if no MIDlet was specified. */
	private final String _mainClass;
	
	/** The working directory to run under. */
	private final Path _workDir;
	
	/** Context project. */
	private final Project _anyProject;
	
	/** Debug server to use. */
	private final URI _debugServer;
	
	/**
	 * Initializes the detached run task.
	 *
	 * @param __classifier The classifier used.
	 * @param __logger The logger to use.
	 * @param __classPath The classpath to use.
	 * @param __midlet The MIDlet to run.
	 * @param __mainClass The main class to run, if there is no MIDlet.
	 * @param __workDir The working directly to run under.
	 * @param __anyProject Any project.
	 * @param __debugServer The debug server to use.
	 * @since 2024/07/28
	 */
	public VMRunTaskDetached(SourceTargetClassifier __classifier,
		Logger __logger, Path[] __classPath, JavaMEMidlet __midlet,
		String __mainClass, Path __workDir, Project __anyProject,
		URI __debugServer)
	{
		this.classifier = __classifier;
		this._logger = __logger;
		this._classPath = __classPath;
		this._midlet = __midlet;
		this._mainClass = __mainClass;
		this._workDir = __workDir;
		this._anyProject = __anyProject;
		this._debugServer = __debugServer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	public void run()
	{
		// Gather the class path to use for target execution, this is all the
		// SquirrelJME modules this depends on
		VMSpecifier vmType = this.classifier.getVmType();
		Path[] classPath = this._classPath;
		
		// Debug
		if (this._logger != null)
			this._logger.debug("Classpath: {}", Arrays.asList(classPath));
		
		// Determine the main entry class or MIDlet to use
		JavaMEMidlet midlet = this._midlet;
		String mainClass = VMHelpers.mainClass(midlet, this._mainClass);
		
		// Debug
		if (this._logger != null)
			this._logger.debug("MIDlet: {}", midlet);
		if (this._logger != null)
			this._logger.debug("MainClass: {}", mainClass);
		
		// Debugger being used?
		URI debugServer = this._debugServer;
		
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
			// LLDB Server?
			if ("lldb".equals(debugServer.getScheme()))
			{
				procArgs.add(Paths.get(GdbUtils.setScheme(debugServer, 
					"file")).toAbsolutePath().toString());
				procArgs.add("gdbserver");
				procArgs.add("localhost:2345");
				procArgs.add("--");
				
				// Force interpreter to be used
				forceInterpreter = true;
			}
			
			// GDB Server?
			else if ("gdb".equals(debugServer.getScheme()))
			{
				procArgs.add(Paths.get(GdbUtils.setScheme(debugServer, 
					"file")).toAbsolutePath().toString());
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
					this._anyProject).call();
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
				builder.directory(this._workDir.toFile());
				
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
		if (this._logger != null)
			this._logger.debug("Target Working Dir: {}",
				System.getProperty("user.dir"));
		
		// Execute the virtual machine, if the exit status is non-zero then
		// the task execution will be considered as a failure
		JavaExecSpecFiller execSpec = new SimpleJavaExecSpecFiller();
		vmType.spawnJvmArguments(this._anyProject, this.classifier,
			true,
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
		procBuilder.directory(this._anyProject.getBuildDir());
		
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
			if (this._logger != null)
			{
				stdOut = new ForwardInputToOutput(
					proc.getInputStream(), new GradleLoggerOutputStream(
					this._logger, LogLevel.LIFECYCLE,
					-1, -1));
				stdErr = new ForwardInputToOutput(
					proc.getErrorStream(), new GradleLoggerOutputStream(
					this._logger, LogLevel.ERROR,
					-1, -1));
			}
			else
			{
				stdOut = new ForwardInputToOutput(proc.getInputStream(),
					System.out);
				stdErr = new ForwardInputToOutput(proc.getErrorStream(),
					System.err);
			}
			
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
