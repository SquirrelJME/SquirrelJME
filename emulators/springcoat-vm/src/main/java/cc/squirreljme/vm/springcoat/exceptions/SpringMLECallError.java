// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.SpringConvertableThrowable;
import cc.squirreljme.vm.springcoat.SpringException;

/**
 * This is SpringCoat's variant of {@link MLECallError}.
 *
 * @since 2020/06/22
 */
public class SpringMLECallError
	extends SpringException
	implements SpringConvertableThrowable
{
	/** The distinction used. */
	public final int distinction;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2020/06/22
	 */
	public SpringMLECallError()
	{
		this.distinction = 0;
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(String __m)
	{
		super(__m);
		
		this.distinction = 0;
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(String __m, Throwable __c)
	{
		super(__m, __c);
		
		if (__c instanceof MLECallError)
			this.distinction = ((MLECallError)__c).distinction;
		else
			this.distinction = 0;
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(Throwable __c)
	{
		super(__c);
		
		if (__c instanceof MLECallError)
			this.distinction = ((MLECallError)__c).distinction;
		else
			this.distinction = 0;
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	public SpringMLECallError(int __dist)
	{
		this.distinction = __dist;
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	public SpringMLECallError(String __m, int __dist)
	{
		super(__m);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	public SpringMLECallError(String __m, Throwable __c, int __dist)
	{
		super(__m, __c);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	public SpringMLECallError(Throwable __c, int __dist)
	{
		super(__c);
		
		this.distinction = __dist;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public String targetClass()
	{
		return "cc/squirreljme/jvm/mle/exceptions/MLECallError";
	}
}
