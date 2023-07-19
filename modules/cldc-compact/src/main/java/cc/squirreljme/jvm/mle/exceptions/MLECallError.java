// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.exceptions;

import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is thrown when there was an error made during a MLE call.
 *
 * @since 2020/06/22
 */
@SquirrelJMEVendorApi
public class MLECallError
	extends VirtualMachineError
{
	/** the distinctive error code. */
	public final int distinction;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError()
	{
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m)
	{
		super(__m);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, Throwable __t)
	{
		super(__m);
		
		this.initCause(__t);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(Throwable __t)
	{
		this.initCause(__t);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(int __dist)
	{
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, int __dist)
	{
		super(__m);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, Throwable __t, int __dist)
	{
		super(__m);
		
		this.initCause(__t);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(Throwable __t, int __dist)
	{
		this.initCause(__t);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes system call exception.
	 * 
	 * @param __callId The call ID.
	 * @param __code The {@link SystemCallError}.
	 * @since 2020/11/29
	 */
	@SquirrelJMEVendorApi
	public MLECallError(int __callId, int __code)
	{
		/* {@squirreljme.error ZZ4k Failed system call. (The ID; The Error)} */
		super("ZZ4k " + __callId + " " + __code);
		
		this.distinction = 0;
	}
}
