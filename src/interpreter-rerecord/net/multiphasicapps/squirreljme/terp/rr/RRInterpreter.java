// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This contains the interpreter which is used for re-recording.
 *
 * @since 2016/05/29
 */
public class RRInterpreter
	extends Interpreter
{
	/** Java instructions per second. */
	public static final int DEFAULT_JIPS =
		1_000_000;
	
	/** The data stream to source data from and such. */
	protected final RRDataStream datastream =
		new RRDataStream(this, this.lock);
	
	/** The current JIPS. */
	private volatile int _jips;
	
	/** Nanoseconds that pass per JIP. */
	private volatile long _nanosperjip;
	
	/**
	 * initializes the re-recording interpreter.
	 *
	 * @since 2016/05/29
	 */
	public RRInterpreter()
	{
		// Set default timing
		setJIPS(DEFAULT_JIPS);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	public void adjustProgramStart(ClassPath[] __cp, CIMethod[] __mm,
		String[][] __args)
		throws NullPointerException
	{
		// Lock
		synchronized (this.lock)
		{
			// Potential super adjustments
			super.adjustProgramStart(__cp, __mm, __args);
			
			// This either records it or plays the program start back
			this.datastream.adjustProgramStart(__cp, __mm, __args);
		}
	}
	
	/**
	 * Returns the current Java instructions per second.
	 *
	 * @return The Java instructions per second used.
	 * @since 2016/05/30
	 */
	public int getJIPS()
	{
		// Lock
		synchronized (this.lock)
		{
			return _jips;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public void handleXOptions(Map<String, String> __xo)
		throws NullPointerException
	{
		// Check
		if (__xo == null)
			throw new NullPointerException("NARG");
		
		String v;
		
		// {@squirreljme.cmdline -Xsquirreljme-rerecord-jips=(int) This
		// specifies the number of Java instructions which should be executed
		// in a single second.}
		v = __xo.get("squirreljme-rerecord-jips");
		if (v != null)
			try
			{
				setJIPS(Integer.decode(v));
			}
			
			// Ignore
			catch (NumberFormatException e)
			{
			}
	}
	
	/**
	 * Sets the number of Java instructions that are executed in a single
	 * second.
	 *
	 * @param __jips The number of instructions to execute per second.
	 * @throws IllegalArgumentException If the instructions per second is zero
	 * or negative.
	 * @since 2016/05/29
	 */
	public void setJIPS(int __jips)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BC01 The number of Java instructions per
		// second to interpret cannot be zero or negative. (The requested
		// JIPS)}
		if (__jips <= 0)
			throw new IllegalArgumentException(String.format("BC01 %d",
				__jips));
		
		// Lock
		synchronized (this.lock)
		{
			// Set new values
			this._jips = __jips;
			this._nanosperjip = 1_000_000_000L / (long)__jips;
		}
	}
}

