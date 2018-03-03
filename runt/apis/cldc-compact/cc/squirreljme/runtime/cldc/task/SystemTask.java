// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.task;

import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;
import java.io.InputStream;

/**
 * This interface is used to represent generic access to a task which exists
 * and may be running within the kernel.
 *
 * Since the kernel always uses tasks for permission checking, the instance
 * of this class should pass that information to the kernel as needed.
 *
 * Instances of this class will be used as keys so it must implement
 * {@link #equals(Object)} and {@link #hashCode()} to where even two
 * instances of this class which point to the same program refer to that
 * instance. As such the comparison is performed with the task index.
 *
 * @since 2017/12/10
 */
public interface SystemTask
{
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/09
	 */
	public abstract void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * Returns the task flags.
	 *
	 * @return The flags for the task.
	 * @since 2017/12/27
	 */
	public abstract int flags();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the index of the task.
	 *
	 * @return The task index.
	 * @since 2018/01/03
	 */
	public abstract int index();
	
	/**
	 * Returns the classpath of this task.
	 *
	 * @return The libraries which make up the classpath for the task.
	 * @throws SecurityException If the libraries could not be obtained.
	 * @since 2018/03/03
	 */
	public abstract Library[] libraryClassPath()
		throws SecurityException;
	
	/**
	 * Returns the main entry point of this task.
	 *
	 * @return The task main entry point.
	 * @since 2017/12/10
	 */
	public abstract String mainClass();
	
	/**
	 * Returns the value of the given metric for this task.
	 *
	 * @param __m The metric to get.
	 * @return The value for this metric.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	public abstract long metric(SystemTaskMetric __m)
		throws NullPointerException;
	
	/**
	 * Attempts to restart the specified task, if permitted to.
	 *
	 * This should perform the same operation as destroying and starting it
	 * but re-using the ID of the task.
	 *
	 * @since 2017/12/10
	 */
	public abstract void restart();
	
	/**
	 * Returns the status of the task.
	 *
	 * @return The task status.
	 * @since 2018/03/02
	 */
	public abstract SystemTaskStatus status();
	
	/**
	 * Returns the trust group which this task runs under.
	 *
	 * @return The trust group of the task.
	 * @since 2018/01/09
	 */
	public abstract SystemTrustGroup trustGroup();
}

