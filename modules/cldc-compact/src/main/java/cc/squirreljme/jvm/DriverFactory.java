// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import java.util.ServiceLoader;

/**
 * Interface for creating a specific driver, used with {@link ServiceLoader}.
 *
 * @since 2023/08/20
 */
public interface DriverFactory
{
	/**
	 * The name of this driver.
	 *
	 * @return The driver name
	 * @since 2023/08/20
	 */
	String name();
	
	/**
	 * Creates a new instance of the driver.
	 *
	 * @return The driver instance.
	 * @since 2023/08/20
	 */
	Object newInstance();
	
	/**
	 * The default priority of the driver.
	 *
	 * @return The driver priority, lower is more priority.
	 * @since 2023/08/20
	 */
	int priority();
	
	/**
	 * Which class does this provide?
	 *
	 * @return The providing class.
	 * @since 2023/08/20
	 */
	Class<?> providesClass();
}
