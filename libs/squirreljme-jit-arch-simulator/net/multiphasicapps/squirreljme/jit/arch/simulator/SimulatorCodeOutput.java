// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.simulator;

import net.multiphasicapps.squirreljme.jit.arch.BasicFuturePositionMarker;
import net.multiphasicapps.squirreljme.jit.arch.FuturePositionMarker;
import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.arch.PositionMarker;
import net.multiphasicapps.squirreljme.jit.arch.Register;
import net.multiphasicapps.squirreljme.jit.arch.ZeroComparisonType;
import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;
import net.multiphasicapps.squirreljme.jit.bin.FragmentDestination;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This outputs simulator machine code.
 *
 * @since 2017/08/11
 */
public class SimulatorCodeOutput
	implements MachineCodeOutput
{
	/** The JIT configuration used. */
	protected final JITConfig config;
	
	/** The destination where the fragment goes when this is closed. */
	protected final FragmentDestination destination;
	
	/** The destination fragment. */
	protected final SimulatorFragmentBuilder fragment =
		new SimulatorFragmentBuilder();
	
	/** The next ID to use for future positions. */
	private volatile int _nextfpid;
	
	/**
	 * Initializes the Simulator code output.
	 *
	 * @param __conf The JIT configuration.
	 * @param __dest The destination for the code output.
	 * @throws JITException If the configuration is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public SimulatorCodeOutput(JITConfig __conf, FragmentDestination __dest)
		throws JITException, NullPointerException
	{
		// Check
		if (__conf == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.destination = __dest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public void compareAndAbsoluteBranchToRegister(
		ZeroComparisonType __t, Register __r, Register __a)
		throws JITException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public void compareAndRelativeBranch(ZeroComparisonType __t,
		Register __r, FuturePositionMarker __a)
		throws JITException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public JITConfig config()
	{
		return this.config;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public FuturePositionMarker createFuturePositionMarker()
	{
		return new BasicFuturePositionMarker(++this._nextfpid);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public void markFuturePosition(FuturePositionMarker __fpm)
		throws JITException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public PositionMarker positionMarker()
	{
		return this.fragment.positionMarker();
	}
}

