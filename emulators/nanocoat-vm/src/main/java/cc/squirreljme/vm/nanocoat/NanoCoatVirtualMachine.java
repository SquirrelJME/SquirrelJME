// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the NanoCoat virtual machine interface implementation on the
 * Java side of the emulator.
 *
 * @since 2023/12/05
 */
public class NanoCoatVirtualMachine
	implements VirtualMachine
{
	/** The executable arguments. */
	protected final List<String> execArgs;
	
	/** The executable path. */
	protected final Path execPath;
	
	/**
	 * Handles launching of NanoCoat.
	 *
	 * @param __execPath The NanoCoat executable path.
	 * @param __execArgs The arguments to the NanoCoat executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/07/14
	 */
	public NanoCoatVirtualMachine(Path __execPath, List<String> __execArgs)
		throws NullPointerException
	{
		if (__execPath == null || __execArgs == null)
			throw new NullPointerException("NARG");
		
		this.execPath = __execPath;
		this.execArgs = new ArrayList<>(__execArgs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/05
	 */
	@Override
	public int runVm()
		throws VMException
	{
		// Build final argument set
		List<String> args = new ArrayList<>();
		args.add(this.execPath.toAbsolutePath().normalize()
			.toString());
		args.addAll(this.execArgs);
		
		// Setup new process
		ProcessBuilder builder = new ProcessBuilder(args);
		
		// All pipes are inherited since this container process is not doing
		// much
		builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
		// Always work in the same working directory as the caller
		builder.directory(Paths.get(System.getProperty("user.dir"))
			.toAbsolutePath().normalize().toFile());
		
		// Spawn the process
		Process process = null;
		try
		{
			// Start it
			process = builder.start();
			
			// Wait for it to stop
			return process.waitFor();
		}
		catch (IOException|InterruptedException __e)
		{
			throw new VMException(__e.getMessage(), __e);
		}
		finally
		{
			if (process != null)
				process.destroyForcibly();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setTraceBits(boolean __or, int __bits)
	{
		// Not implemented
	}
}
