// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import java.util.ServiceLoader;

/**
 * Interface used to represent complex drivers, and the factories to create
 * them. This is expected to be used with {@link ServiceLoader} as one
 * implementation, so the request for drivers is not by the actual
 * implementation class itself but one of the drivers instead.
 *
 * @since 2021/08/05
 */
public interface ComplexDriverFactory
{
	/** Minimum driver priority. */
	int MIN_PRIORITY =
		Integer.MAX_VALUE;
	
	/** Maximum driver priority. */
	int MAX_PRIORITY =
		Integer.MIN_VALUE;
	
	/**
	 * Returns an instance of the given driver. The returned instances should
	 * be only single instances that can work together if in the event multiple
	 * ones are created.
	 * 
	 * @param <I> The type of driver to create.
	 * @param __class The type to cast to for this driver.
	 * @return An instance of the given driver.
	 * @since 2021/08/05
	 */
	<I> I instance(Class<I> __class);
	
	/**
	 * Returns the name of this driver.
	 * 
	 * @return The name of this driver.
	 * @since 2021/08/05
	 */
	String name();
	
	/**
	 * Returns the priority of this driver.
	 * 
	 * @return The priority of this driver, lower values are higher priority.
	 * @since 2021/08/05
	 */
	int priority();
}
