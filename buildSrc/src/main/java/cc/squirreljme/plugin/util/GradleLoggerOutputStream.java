// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	extends LinePushOutputStream
{
	/** The log level to output as. */
	private final LogLevel _logLevel;
	
	/** The logger to output to. */
	private final Logger _logger;
	
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
	 * @since 2022/09/11
	 */
	@Override
	protected void push(String __string)
	{
		int testId = this._testId;
		int testTotal = this._testTotal;
		
		// Which output string do we want?
		String message;
		if (testId < 0 || testTotal < 0)
			message = __string;
		else
			message = String.format("[%d/%d]: %s", testId, testTotal,
				__string);
		
		// Output to the logger before it goes away
		this._logger.log(this._logLevel, message);
	}
}
