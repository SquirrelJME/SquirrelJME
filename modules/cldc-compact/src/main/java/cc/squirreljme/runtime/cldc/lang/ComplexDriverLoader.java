// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is used to provide a complex driver interface, this is used to install
 * and operate drivers that are fixed for a given system.
 * 
 * System properties can be used to override drivers, by prefixing the
 * driver class with {@code cc.squirreljme.driver.}, the value is the
 * implementation
 * 
 * Using {@link VMDescriptionType#COMPLEX_DRIVER_LIST} the format is in the
 * following: {@code factory.a=implementation.a;factory.b=implementation.b}.
 *
 * @since 2021/08/05
 */
public final class ComplexDriverLoader
{
	/** The set of drivers which are already loaded. */
	private static volatile Map<Class<? extends ComplexDriverFactory<?>>,
		ComplexDriverFactory<?>> _DRIVERS;
	
	/** The list of {@link VMDescriptionType#COMPLEX_DRIVER_LIST}. */
	private static volatile ComplexDriverPair[] _DESC_DRIVERS;
	
	/**
	 * Not used.
	 * 
	 * @since 2021/08/05
	 */
	private ComplexDriverLoader()
	{
	}
	
	/**
	 * Returns an instance of the given driver, or {@code null} if no such
	 * driver exists.
	 * 
	 * @param <D> The type of driver factory to obtain.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	public static <D> ComplexDriverFactory<D> driverInstance(
		Class<ComplexDriverFactory<D>> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Has this driver already been cached?
		ComplexDriverFactory<D> result =
			ComplexDriverLoader.<D>__cached(__class);
		if (result != null)
			return result;
		
		// Load up a bunch of drivers, to find the right one
		List<ComplexDriverFactory<D>> factories =
			new ArrayList<>(3);
		
		// Use this specific order to try to find drivers
		factories.add(ComplexDriverLoader.<D>__sysProp(__class));
		factories.add(ComplexDriverLoader.<D>__vmDesc(__class));
		factories.add(ComplexDriverLoader.<D>__serviceLoader(__class));
		
		int bestDriver = -1;
		int bestPriority = ComplexDriverFactory.MIN_PRIORITY;
		for (int i = 0, n = factories.size(); i < n; i++)
		{
			// Is there a valid attempt here?
			ComplexDriverFactory<D> attempt = factories.get(i);
			if (attempt == null)
				continue;
			
			// Use a new available driver?
			if (bestDriver < 0 || attempt.priority() < bestPriority)
			{
				bestDriver = i;
				bestPriority = attempt.priority();
			}
		}
		
		// Choose the best driver with the lowest priority
		result = (bestDriver < 0 ? null : factories.get(bestDriver));
		
		// Store the driver instance into the cache
		synchronized (ComplexDriverLoader.class)
		{
			ComplexDriverLoader._DRIVERS.put(__class, result);
		}
		
		return result;
	}
	
	/**
	 * Looks for a driver instance that has already been cached and loaded.
	 * 
	 * @param <D> The type of driver factory to obtain.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none is cached.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static <D> ComplexDriverFactory<D> __cached(
		Class<ComplexDriverFactory<D>> __class) 
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// We need to lock since we are checking a cache
		synchronized (ComplexDriverLoader.class)
		{
			return __class.cast(ComplexDriverLoader._DRIVERS.get(__class));
		}
	}
	
	/**
	 * Returns the description driver list.
	 * 
	 * @return The description driver list.
	 * @since 2021/08/05
	 */
	private static ComplexDriverPair[] __descDrivers()
	{
		// _DESC_DRIVERS
		throw Debugging.todo();
	}
	
	/**
	 * Attempts to load the factory class for the driver.
	 * 
	 * @param <D> The type of instance to load.
	 * @param __name The name of the class to load.
	 * @return The instance of the factory or {@code null} if it could not be
	 * instantiated.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	@SuppressWarnings("unchecked")
	private static <D> ComplexDriverFactory<D> __loadFactory(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return (ComplexDriverFactory<D>)
				(Class.forName(__name).newInstance());
		}
		catch (ClassNotFoundException|InstantiationException|
			IllegalAccessException __e)
		{
			__e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Searches the service loaders for the given driver.
	 * 
	 * @param <D> The type of driver factory to obtain.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static <D> ComplexDriverFactory<D> __serviceLoader(
		Class<ComplexDriverFactory<D>> __class)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Searches system properties for the given driver.
	 * 
	 * @param <D> The type of driver factory to obtain.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static <D> ComplexDriverFactory<D> __sysProp(
		Class<ComplexDriverFactory<D>> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		String targetName = System.getProperty(
			"cc.squirreljme.driver." + __class.getName());
		if (targetName == null)
			return null;
		
		return ComplexDriverLoader.<D>__loadFactory(targetName);
	}
	
	/**
	 * Searches the standard VM descriptor for the given driver.
	 * 
	 * @param <D> The type of driver factory to obtain.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static <D> ComplexDriverFactory<D> __vmDesc(
		Class<ComplexDriverFactory<D>> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
			
		throw Debugging.todo();
	}
}
