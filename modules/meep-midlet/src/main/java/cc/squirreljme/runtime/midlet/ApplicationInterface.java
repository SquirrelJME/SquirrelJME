// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Handles differences in various types of applications so that they all
 * have a uniform start and ending cycle.
 *
 * @param <A> The application controlling instance.
 * @see ApplicationHandler
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public interface ApplicationInterface<A>
{
	/**
	 * Destroys the instance of the application.
	 * 
	 * @param __instance The instance that was previously created.
	 * @param __thrown The thrown exception if any, for possible special
	 * handle or failure handling.
	 * @throws NullPointerException On null arguments.
	 * @throws Throwable On any exception.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	void destroy(A __instance, Throwable __thrown)
		throws NullPointerException, Throwable;
	
	/**
	 * Creates a new instance of the application.
	 * 
	 * @return The created instance.
	 * @throws Throwable On any exception.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	A newInstance()
		throws Throwable;
	
	/**
	 * Starts the application instance.
	 * 
	 * @param __instance The previously created instance.
	 * @throws NullPointerException On null arguments.
	 * @throws Throwable On any exception.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	void startApp(A __instance)
		throws NullPointerException, Throwable;
	
	/**
	 * Returns the application type.
	 * 
	 * @return The application type.
	 * @since 2022/07/21
	 */
	@SquirrelJMEVendorApi
	ApplicationType type();
}
