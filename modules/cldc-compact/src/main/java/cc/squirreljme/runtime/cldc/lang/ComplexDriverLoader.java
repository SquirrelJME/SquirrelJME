// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

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
	private static volatile Map<Class<? extends ComplexDriverFactory>,
		ComplexDriverFactory> _DRIVERS;
	
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
	 * @param <F> The type of the driver factory to return.
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	public static <F extends ComplexDriverFactory> F factoryInstance(
		Class<F> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Has this driver already been cached?
		ComplexDriverFactory result =
			ComplexDriverLoader.__cached(__class);
		if (result != null)
			return __class.cast(result);
		
		// If we set a specific driver in a system property use that instead
		// since it is likely a driver override being used.
		result = ComplexDriverLoader.__sysProp(__class);
		if (result == null)
		{
			// Will load up a bunch of drivers, to find the best one
			List<ComplexDriverFactory> factories = new ArrayList<>();
			
			// Use this specific order to try to find drivers
			factories.addAll(
				Arrays.asList(ComplexDriverLoader.__vmDesc(__class)));
			factories.addAll(
				Arrays.asList(ComplexDriverLoader.__service(__class)));
			
			// Find the best driver available for usage
			int bestDriver = -1;
			int bestPriority = ComplexDriverFactory.MIN_PRIORITY;
			for (int i = 0, n = factories.size(); i < n; i++)
			{
				// Is there a valid attempt here?
				ComplexDriverFactory attempt = factories.get(i);
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
		}
		
		// Store the driver instance into the cache
		synchronized (ComplexDriverLoader.class)
		{
			// Need to create this mapping?
			Map<Class<? extends ComplexDriverFactory>, ComplexDriverFactory>
				drivers = ComplexDriverLoader._DRIVERS;
			if (drivers == null)
				ComplexDriverLoader._DRIVERS = (drivers = new HashMap<>());	
			
			// Store the used driver
			drivers.put(__class, result);
		}
		
		// Use this given driver as the expected type
		return __class.cast(result);
	}
	
	/**
	 * Looks for a driver instance that has already been cached and loaded.
	 * 
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none is cached.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static ComplexDriverFactory __cached(
		Class<? extends ComplexDriverFactory> __class) 
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// We need to lock since we are checking a cache
		synchronized (ComplexDriverLoader.class)
		{
			Map<Class<? extends ComplexDriverFactory>, ComplexDriverFactory>
				drivers = ComplexDriverLoader._DRIVERS;
			if (drivers != null)
				return __class.cast(drivers.get(__class));
		}
		
		// Nothing cached
		return null;
	}
	
	/**
	 * Attempts to load the factory class for the driver.
	 * 
	 * @param __name The name of the class to load.
	 * @return The instance of the factory or {@code null} if it could not be
	 * instantiated.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static ComplexDriverFactory __loadFactory(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return (ComplexDriverFactory)
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
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or an empty array if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static ComplexDriverFactory[] __service(
		Class<? extends ComplexDriverFactory> __class)
	{
		List<ComplexDriverFactory> result = new ArrayList<>();
		
		// Is this a matching driver?
		for (ComplexDriverFactory driver : ServiceLoader.load(__class))
			if (driver.getClass().isInstance(__class))
				result.add(driver);
		
		return result.toArray(new ComplexDriverFactory[result.size()]);
	}
	
	/**
	 * Searches system properties for the given driver.
	 * 
	 * @param __class The class of the driver. 
	 * @return The instance of the given driver factory, or {@code null} if
	 * none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static ComplexDriverFactory __sysProp(
		Class<? extends ComplexDriverFactory> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Is there a system property for this driver?
		String targetName = System.getProperty(
			"cc.squirreljme.driver." + __class.getName());
		if (targetName == null)
			return null;
		
		return ComplexDriverLoader.__loadFactory(targetName);
	}
	
	/**
	 * Searches the standard VM descriptor for the given driver.
	 * 
	 * @param __class The class of the driver.  
	 * @return The instance of the given driver factory, or an empty 
	 * array if none exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	private static ComplexDriverFactory[] __vmDesc(
		Class<? extends ComplexDriverFactory> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Load the driver pairs
		ComplexDriverPair[] pairs;
		try
		{
			pairs = ComplexDriverLoader.__vmDescPairs();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return new ComplexDriverFactory[0];
		}
		
		// Resultant drivers
		List<ComplexDriverFactory> drivers = new ArrayList<>();
		
		// Return all matching instances of them
		String wantDriver = __class.getName();
		for (ComplexDriverPair pair : pairs)
		{
			// Is this the driver for the factory we want?
			if (!wantDriver.equals(pair.factory))
				continue;
			
			// Does this driver exist?
			ComplexDriverFactory factory =
				ComplexDriverLoader.__loadFactory(pair.implementation);
			if (factory != null)
				drivers.add(factory);
		}
		
		return drivers.toArray(new ComplexDriverFactory[drivers.size()]);
	}
	
	/**
	 * Returns the description driver list.
	 * 
	 * @return The description driver list.
	 * @throws IllegalArgumentException If the VM description pairs are not
	 * formatted properly.
	 * @since 2021/08/05
	 */
	private static ComplexDriverPair[] __vmDescPairs()
		throws IllegalArgumentException
	{
		// Pre-cached?
		ComplexDriverPair[] result = ComplexDriverLoader._DESC_DRIVERS;
		if (result != null)
			return result;
		
		// Working result
		List<ComplexDriverPair> working = new ArrayList<>();
		
		// Split the raw list accordingly to find services
		String rawList = RuntimeShelf.vmDescription(
			VMDescriptionType.COMPLEX_DRIVER_LIST);
		for (int at = 0, n = rawList.length(); at < n;)
		{
			// Find the end of the string
			int semi = rawList.indexOf(';', at);
			if (semi < 0)
				semi = n;
			
			// {@squirreljme.error ZZ4g VM Description is not formatted
			// properly, no equal sign in grouping.}
			int equal = rawList.indexOf('=', at);
			if (equal < 0 || equal >= semi)
				throw new IllegalArgumentException("ZZ4g");
			
			// Split into driver and implementation pair
			String driver = rawList.substring(at, equal);
			String implementation = rawList.substring(equal + 1, semi);
			
			// {@squirreljme.error ZZ4h VM Description is not formatted
			// properly, driver and/or implementation are blank.}
			if (driver.isEmpty() || implementation.isEmpty())
				throw new IllegalArgumentException("ZZ4h");
			
			// Add this driver
			working.add(new ComplexDriverPair(driver, implementation));
			
			// Go to the next run
			at = semi + 1;
		}
		
		// Cache this for later
		result = working.toArray(new ComplexDriverPair[working.size()]);
		ComplexDriverLoader._DESC_DRIVERS = result;
		
		return result;
	}
}
