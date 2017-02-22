// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.kernel.ContextClass;
import net.multiphasicapps.squirreljme.kernel.ContextLoadException;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParametersBuilder;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.kernel.ProcessCreationException;
import net.multiphasicapps.squirreljme.kernel.SuiteDataAccessor;
import net.multiphasicapps.squirreljme.kernel.ThreadCreationException;

/**
 * This is a deterministic kernel manager which is capable of recording and
 * playing back previously recorded sessions.
 *
 * Try to use this for debugging and not cheating in video games.
 *
 * @since 2016/11/02
 */
public class DeterministicKernelManager
	extends AbstractKernelManager
	implements Closeable
{
	/** Is a replay being recorded? */
	protected final boolean isrecording;
	
	/** Is a replay being played back? */
	protected final boolean isreplaying;
	
	/** The real launch parameters to use. */
	protected final KernelLaunchParameters launchparms;
	
	/**
	 * Initializes the deterministic kernel manager.
	 *
	 * @param __ai The owning interpreter.
	 * @param __lp Kernel launch parameters.
	 * @param __in The input playback stream.
	 * @param __out The output recording stream.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException If no launch parameters were specified.
	 * @since 2016/11/02
	 */
	public DeterministicKernelManager(AutoInterpreter __ai,
		KernelLaunchParameters __lp, Path __in, Path __out)
		throws IOException, NullPointerException
	{
		super(__ai);
		
		// Check
		if (__lp == null)
			throw new NullPointerException("NARG");
		
		if (true)
			throw new Error("TODO");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/02
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public KernelProcess createProcess(Kernel __k, SuiteDataAccessor[] __cp)
		throws NullPointerException, ProcessCreationException
	{
		// Check
		if (__k == null || __cp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/16
	 */
	@Override
	public KernelThread createThread(KernelProcess __kp, String __mc,
		String __m)
		throws NullPointerException, ThreadCreationException
	{
		// Check
		if (__kp == null || __mc == null || __m == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/07
	 */
	@Override
	protected KernelLaunchParameters internalLaunchParameters()
	{
		return this.launchparms;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/03
	 */
	@Override
	public void runThreads()
		throws InterruptedException
	{
		throw new Error("TODO");
	}
}

