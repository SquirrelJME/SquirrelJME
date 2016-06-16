// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
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
public abstract class VM
{
	/** The single virtual machine instance. */
	public static final VM INSTANCE =
		__getInstance();
	
	/** Are fake milliseconds being used? */
	private volatile boolean _fakemillis;
	
	/** The base nano time. */
	private volatile long _basenano;
	
	/**
	 * Returns the unique identifier which identifies this device.
	 *
	 * @return The device unique identifier.
	 * @since 2016/06/16
	 */
	public abstract String deviceUUID();
	
	/**
	 * Obtains the given resource from the JAR file of a given class or the
	 * global namespace.
	 *
	 * @param __base If not {@code null} then the resource is only located in
	 * the JAR that contains the class, otherwise if {@code null} then the
	 * entire class path is searched for the given resource.
	 * @param __abs This should be the fully resolved absolute path of a
	 * resource which does not start with a forward slash.
	 * @return The name of the resource.
	 * @throws NullPointerException If no absolute path was specified.
	 * @since 2016/06/16
	 */
	public abstract String findResource(Class<?> __base,
		String __abs)
		throws NullPointerException;
	
	/**
	 * This goes through the entire class path and locations any resources
	 * that are in named JAR files that match the given absolute name.
	 *
	 * @param __abs This should be the fully resolved absolute path of a
	 * resource which does not start with a forward slash.
	 * @return The array of resource names which pertain to the found
	 * resources, if no resources are found then the array will be blank
	 * however it may return {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public abstract String[] findResources(String __abs)
		throws NullPointerException;
	
	/**
	 * Opens a resource which was previously returned by
	 * {@link #findResource(Class, String)} or {@link #findResources(String)}.
	 *
	 * @param __res The name of the resource.
	 * @return The input stream of the given resource or {@code null} if it
	 * could not be opened for any reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public abstract InputStream openResource(String __res)
		throws NullPointerException;
	
	/**
	 * Returns the approximate number of nanoseconds which have passed since
	 * an unspecified start time.
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2016/06/16
	 */
	public abstract long nanoTime();
	
	/**
	 * Returns the architecture that this virtual machine is running on.
	 *
	 * @return The architecture name.
	 * @since 2016/06/16
	 */
	public abstract String osArch();
	
	/**
	 * Returns the name of the operating system that this virtual machine is
	 * running on.
	 *
	 * @return The operating system name.
	 * @since 2016/06/16
	 */
	public abstract String osName();
	
	/**
	 * Returns the name of the device that SquirrelJME is running on, this may
	 * be the name of the operating system or the name of the actual hardware
	 * running underneath.
	 *
	 * The format for the returned value is:
	 * {@code (Manufacturer)(DeviceModelNumber)[/version[/comments]]}.
	 *
	 * @return The platform SquirrelJME is running on.
	 * @since 2016/06/15
	 */
	public abstract String osPlatform();
	
	/**
	 * Returns the version of the operating system this is running on.
	 *
	 * @return The operating system version.
	 * @since 2016/06/16
	 */
	public abstract String osVersion();
	
	/**
	 * Returns the host name of this system as it appears on the network to
	 * other systems.
	 *
	 * @return The host name of this system.
	 * @since 2016/06/16
	 */
	public String osHostName()
	{
		return "localhost";
	}
	
	/**
	 * Writes a byte to standard error.
	 *
	 * @param __b The byte to write.
	 * @since 2016/06/16
	 */
	public void stdErr(byte __b)
	{
		// The default implementation drops all characters output to the native
		// console since it might not be supported
	}
	
	/**
	 * Writes a byte to standard output.
	 *
	 * @param __b The byte to write.
	 * @since 2016/06/16
	 */
	public void stdOut(byte __b)
	{
		// The default implementation drops all characters output to the native
		// console since it might not be supported
	}
	
	/**
	 * Returns the number of milliseconds that have passed since the UTC Java
	 * epoch.
	 *
	 * The Java epoch is UTC 00:00 (midnight) on January 1, 1970.
	 *
	 * @return The number of passed milliseconds.
	 * @since 2016/06/16
	 */
	public long utcMillis()
	{
		// It is possible that a real time clock is not implemented, so as such
		// virtualize the passage of time using the nanosecond clock from a
		// given starting point.
		long base, now = nanoTime();
		if (!this._fakemillis)
		{
			this._fakemillis = true;
			base = now;
		}
		
		// Use the previous base time
		else
			base = _basenano;
		
		// Determine the number of nanoseconds which have passed and convert
		// that to milliseconds
		long passed = (now - base) / 1_000_000L;
		
		// Use an unspecified epoch
		return 615_729_600_305L + passed;
	}
	
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

