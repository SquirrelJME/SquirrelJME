// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * This represents the result of a test.
 *
 * @since 2020/09/07
 */
public enum VMTestResult
{
	/** Pass. */
	PASS(0),
	
	/** Fail. */
	FAIL(1),
	
	/** Skip. */
	SKIP(2),
	
	/* End. */
	;
	
	/** The mapped exit code. */
	public final int exitCode;
	
	/**
	 * Initializes the result with the exit code.
	 * 
	 * @param __exitCode The exit code.
	 * @since 2021/07/18
	 */
	VMTestResult(int __exitCode)
	{
		this.exitCode = __exitCode;
	}
	
	/**
	 * Decodes the exit value and returns the test result.
	 * 
	 * @param __exitValue The exit value to decode.
	 * @return The test result.
	 * @since 2020/09/07
	 */
	public static VMTestResult valueOf(int __exitValue)
	{
		if (__exitValue == VMTestResult.PASS.exitCode)
			return VMTestResult.PASS;
		else if (__exitValue == VMTestResult.SKIP.exitCode)
			return VMTestResult.SKIP;
		return VMTestResult.FAIL;
	} 
}
