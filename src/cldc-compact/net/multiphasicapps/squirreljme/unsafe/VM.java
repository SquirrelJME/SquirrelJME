// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.io.InputStream;

/**
 * This contains the interface to the host virtual machine which handles
 * operating system specific details.
 *
 * Operating systems in general will implement this interface which would then
 * be used to provide the required native functionality.
 *
 * @since 2016/06/15
 */
@Deprecated
public abstract class VM
{
	/** The single virtual machine instance. */
	public static final VM INSTANCE =
		__getInstance();
	
	/** Application level control. */
	public final VMApplication application =
		initializeVMApplication();
	
	/** The environment. */
	public final VMEnvironment environment =
		initializeVMEnvironment();
	
	/** JAR controller. */
	public final VMJar jar =
		initializeVMJar();
	
	/** The standard I/O interfaces. */
	public final VMStandardIO standardio =
		initializeVMStandardIO();
	
	/** Time based control. */
	public final VMTime time =
		initializeVMTime();
	
	/**
	 * Initializes the virtual machine application interface.
	 *
	 * @return The virtual machine application interface.
	 * @since 2016/06/16
	 */
	protected abstract VMApplication initializeVMApplication();
	
	/**
	 * Initializes the virtual machine environment.
	 *
	 * @return The virtual machine environment.
	 * @since 2016/06/16
	 */
	protected abstract VMEnvironment initializeVMEnvironment();
	
	/**
	 * Initializes the JAR controlling interfaces.
	 *
	 * @return The JAR controller interface.
	 * @since 2016/06/16
	 */
	protected abstract VMJar initializeVMJar();
	
	/**
	 * Initializes the standard I/O interfaces.
	 *
	 * @return The standard I/O interface.
	 * @since 2016/06/16
	 */
	protected abstract VMStandardIO initializeVMStandardIO();
	
	/**
	 * Initializes the virtual machine timing interface.
	 *
	 * @return The virtual machine time interface.
	 * @since 2016/06/16
	 */
	protected abstract VMTime initializeVMTime();
	
	/**
	 * The OS interface is magically pre-initialized to a given value, so to
	 * make it Java compilation friendly the value of the field is returned.
	 *
	 * @return {@link #INSTANCE}.
	 * @since 2016/06/15
	 */
	private static VM __getInstance()
	{
		return INSTANCE;
	}
}

