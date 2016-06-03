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
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.terp.Interpreter;
import net.multiphasicapps.squirreljme.terp.InterpreterException;
import net.multiphasicapps.squirreljme.terp.InterpreterProcess;
import net.multiphasicapps.squirreljme.terp.InterpreterThread;

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
		new RRDataStream(this);
	
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
	 * @since 2016/06/03
	 */
	@Override
	public ClassPath adjustClassPath(ClassPath __cp)
	{
		// Check
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// Get the data stream
		RRDataStream rds = dataStream();
		synchronized (rds)
		{
			// If playing, change the class path
			if (rds.isPlaying())
				throw new Error("TODO");
			
			// Record the class path
			if (rds.isRecording())
			{
				// Get all the class units
				ClassUnit[] cus = __cp.units();
				int n = cus.length;
				
				// Record it
				try (RRDataPacket pk = rds.createPacket(
					RRDataCommand.ADJUST_CLASS_PATH, n))
				{
					// Store all unit names
					for (int i = 0; i < n; i++)
						pk.set(i, cus[i].toString());
					
					// Record it
					rds.record(pk);
				}
			}
		}
		
		// Return the input, which may have changed
		return __cp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public Object[] adjustMainArguments(Object... __args)
	{
		// Check
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Get the data stream
		RRDataStream rds = dataStream();
		synchronized (rds)
		{
			// If playing, change the arguments
			if (rds.isPlaying())
				throw new Error("TODO");
			
			// Record the arguments
			int n;
			if (rds.isRecording())
				try (RRDataPacket pk = rds.createPacket(
					RRDataCommand.ADJUST_MAIN_ARGUMENTS, (n = __args.length)))
				{
					// Store all arguments
					for (int i = 0; i < n; i++)
						pk.set(i, __args[i]);
					
					// Record it
					rds.record(pk);
				}
		}
		
		// Return the input, which may have changed
		return __args;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public ClassNameSymbol adjustMainClass(ClassNameSymbol __mc)
	{
		// Check
		if (__mc == null)
			throw new NullPointerException("NARG");
		
		// Get the data stream
		RRDataStream rds = dataStream();
		synchronized (rds)
		{
			// If playing, change the class
			if (rds.isPlaying())
				throw new Error("TODO");
			
			// Record the class
			if (rds.isRecording())
				try (RRDataPacket pk = rds.createPacket(
					RRDataCommand.ADJUST_MAIN_CLASS, 1))
				{
					// Store class
					pk.set(0, __mc.toString());
					
					// Record it
					rds.record(pk);
				}
		}
		
		// Return the input, which may have changed
		return __mc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public CIMethodID adjustMainMethod(CIMethodID __mm)
	{
		// Check
		if (__mm == null)
			throw new NullPointerException("NARG");
		
		// Get the data stream
		RRDataStream rds = dataStream();
		synchronized (rds)
		{
			// If playing, change the method
			if (rds.isPlaying())
				throw new Error("TODO");
			
			// Record the method
			if (rds.isRecording())
				try (RRDataPacket pk = rds.createPacket(
					RRDataCommand.ADJUST_MAIN_METHOD, 2))
				{
					// Store name and type
					pk.set(0, __mm.name().toString());
					pk.set(1, __mm.type().toString());
					
					// Record it
					rds.record(pk);
				}
		}
		
		// Return the input, which may have changed
		return __mm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public RRProcess createProcess(ClassPath __cp)
	{
		return new RRProcess(this, __cp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public InterpreterThread createThread(InterpreterProcess __ip,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws InterpreterException, NullPointerException
	{
		return new RRThread(this, (RRProcess)__ip, __mc, __mm, __args);
	}
	
	/**
	 * Returns the data stream of the rerecording interpreter.
	 *
	 * @return The interpreter data stream.
	 * @since 2016/06/03
	 */
	public RRDataStream dataStream()
	{
		return this.datastream;
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
		synchronized (this)
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
		
		// {@squirreljme.cmdline -Xsquirreljme-rerecord-replay=(path) This
		// is file file which should be played back from a previously
		// recorded session.}
		v = __xo.get("squirreljme-rerecord-replay");
		if (v != null)
			datastream.streamInput(Paths.get(v));
		
		// {@squirreljme.cmdline -Xsquirreljme-rerecord-record=(path) This is
		// the file where events should be recorded into. If the file already
		// exists then it is not overwritten and the interpreter throws an
		// exception.}
		v = __xo.get("squirreljme-rerecord-record");
		if (v != null)
			datastream.streamOutput(Paths.get(v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	public void runCycle()
	{
		throw new Error("TODO");
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
		synchronized (this)
		{
			// Set new values
			this._jips = __jips;
			this._nanosperjip = 1_000_000_000L / (long)__jips;
		}
	}
}

