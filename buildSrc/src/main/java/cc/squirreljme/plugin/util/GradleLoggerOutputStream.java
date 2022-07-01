// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;

/**
 * Outputs whatever text to Gradle's Logger interface
 * 
 * @since 2022/07/01
 */
public final class GradleLoggerOutputStream
	extends OutputStream
{
	/** The log level to output as. */
	private final LogLevel _logLevel;
	
	/** The logger to output to. */
	private final Logger _logger;
	
	/** The working output for logging. */
	private final ByteArrayOutputStream _output =
		new ByteArrayOutputStream();
	
	/** The test ID. */
	private final int _testId;
	
	/** The test total. */
	private final int _testTotal;
	
	/**
	 * Outputs to the given logger.
	 * 
	 * @param __logger The logger to output to.
	 * @param __logLevel The log level to output under.
	 * @param __testId The test ID, is optional.
	 * @param __testTotal The total number of tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/01
	 */
	public GradleLoggerOutputStream(Logger __logger, LogLevel __logLevel,
		int __testId, int __testTotal)
		throws NullPointerException
	{
		if (__logger == null || __logLevel == null)
			throw new NullPointerException("NARG");
		
		this._logger = __logger;
		this._logLevel = __logLevel;
		this._testId = __testId;
		this._testTotal = __testTotal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/01
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		synchronized (this)
		{
			if (__b == '\r' || __b == '\n')
				this.__push((byte)__b);
			else
				this._output.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/01
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			for (int i = 0; i < __l; i++)
			{
				byte b = __b[__o + i];
				
				if (b == '\r' || b == '\n')
					this.__push(b);
				else
					this._output.write(b);
			}
		}
	}
	
	/**
	 * Pushes the byte to the output.
	 * 
	 * @param __b The byte which triggered the push.
	 * @since 2022/07/01
	 */
	private void __push(byte __b)
	{
		// Ignore carriage returns
		if (__b == '\r')
			return;
		
		int testId = this._testId;
		int testTotal = this._testTotal;
		
		// Which output string do we want?
		ByteArrayOutputStream output = this._output;
		String message;
		if (testId < 0 || testTotal < 0)
			message = output.toString();
		else
			message = String.format("[%d/%d]: %s", testId, testTotal, output);
		
		// Output to the logger before it goes away
		this._logger.log(this._logLevel, message);
		
		// Wipe everything for the next round
		output.reset();
	}
}
