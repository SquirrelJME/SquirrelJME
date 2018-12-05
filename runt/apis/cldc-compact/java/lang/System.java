// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import cc.squirreljme.runtime.cldc.asm.SystemAccess;
import cc.squirreljme.runtime.cldc.asm.SystemProperties;
import cc.squirreljme.runtime.cldc.asm.TimeAccess;
import cc.squirreljme.runtime.cldc.io.StandardError;
import cc.squirreljme.runtime.cldc.io.StandardOutput;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.Permission;

/**
 * This class contains methods which are used to interact with the system and
 * the environment.
 *
 * @since 2018/10/14
 */
public final class System
{
	/** Standard error stream (stderr). */
	public static final PrintStream err =
		new __CanSetPrintStream__(new PrintStream(new StandardError(), true));
	
	/** Standard output stream (stdout). */
	public static final PrintStream out =
		new __CanSetPrintStream__(new PrintStream(new StandardOutput(), true));
	
	/**
	 * Not used.
	 *
	 * @since 2018/03/01
	 */
	private System()
	{
	}
	
	/**
	 * Copies from the source array to the destination.
	 *
	 * @param __src The source array.
	 * @param __srcoff The source offset.
	 * @param __dest The destination array.
	 * @param __destoff The destination offset.
	 * @param __copylen The number of elements to copy.
	 * @throws ArrayStoreException If the destination array cannot contain
	 * the given data.
	 * @throws IndexOutOfBoundsException If the offset and or/lengths are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/27
	 */
	public static void arraycopy(Object __src, int __srcoff,
		Object __dest, int __destoff, int __copylen)
		throws ArrayStoreException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ18 Negative offsets and/or length cannot be
		// specified. (The source offset; The destination offset; The copy
		// length)}
		if (__srcoff < 0 || __destoff < 0 || __copylen < 0)
			throw new IndexOutOfBoundsException(String.format("ZZ18 %d %d %d",
				__srcoff, __destoff, __copylen));
		
		// {@squirreljme.error ZZ19 Copy operation would exceed the bounds of
		// the array. (Source offset; Source length; Destination offset;
		// Destination length; The copy length)}
		int srclen = ObjectAccess.arrayLength(__src),
			destlen = ObjectAccess.arrayLength(__dest);
		if (__srcoff + __copylen > srclen ||
			__destoff + __copylen > srclen)
			throw new IndexOutOfBoundsException(String.format(
				"ZZ19 %d %d %d %d %d", __srcoff, srclen, __destoff, destlen,
				__copylen));
		
		// {@squirreljme.error ZZ1a The source array type is not compatible
		// with destination array. (The source array; The destination array)}
		if (!__dest.getClass().isAssignableFrom(__src.getClass()))
			throw new ArrayStoreException(String.format(
				"ZZ1a %s %s", __src, __dest));
		
		// These offsets for the loops are the same
		int i = __srcoff,
			o = __destoff,
			end = __srcoff + __copylen;
		
		// Copy depending on the type
		if (__src instanceof boolean[])
			for (boolean[] s = (boolean[])__src, d = (boolean[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof byte[])
			for (byte[] s = (byte[])__src, d = (byte[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof short[])
			for (short[] s = (short[])__src, d = (short[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof char[])
			for (char[] s = (char[])__src, d = (char[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof int[])
			for (int[] s = (int[])__src, d = (int[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof long[])
			for (long[] s = (long[])__src, d = (long[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof float[])
			for (float[] s = (float[])__src, d = (float[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else if (__src instanceof double[])
			for (double[] s = (double[])__src, d = (double[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
		else
			for (Object[] s = (Object[])__src, d = (Object[])__dest;
				i < end; i++, o++)
			d[o] = s[i];
	}
	
	/**
	 * Returns the current time on the system's clock in UTC since the epoch
	 * (January 1, 1970 UTC).
	 *
	 * Note that this clock is not monotonic in that if a system adjusts the
	 * system clock this method may return values lower than previous calls
	 * which are made.
	 *
	 * Depending on the host hardware and operating system, the granularity of
	 * this clock may or may not be accurate.
	 *
	 * @return The number of milliseconds since the epoch.
	 * @since 2017/11/10
	 */
	public static long currentTimeMillis()
	{
		// Returns the current time in UTC, not local time zone.
		return TimeAccess.currentTimeMillis();
	}
	
	/**
	 * Indicates that the application exits with the given code.
	 *
	 * @param __e The exit code, the value of this code may change according
	 * to the host operating system and the resulting process might not exit
	 * with the given code.
	 * @since 2017/02/08
	 */
	public static void exit(int __e)
	{
		Runtime.getRuntime().exit(__e);
	}
	
	/**
	 * Indicates that the application should have garbage collection be
	 * performed. It is unspecified when garbage collection occurs.
	 *
	 * @since 2017/02/08
	 */
	public static void gc()
	{
		Runtime.getRuntime().gc();
	}
	
	/**
	 * This obtains the value of a system property (if one is set) and returns
	 * its value. System properties are declared by the system and are used
	 * by applications to potentially modify their behavior.
	 *
	 * {@squirreljme.property java.io.tmpdir This is the temporary directory
	 * which indicates where temporary files (those that are deleted after
	 * an unspecified duration) are to be placed. If there is no filesystem
	 * on the device then a blank string will always be returned.}
	 * {@squirreljme.property java.version This is the version of the virtual
	 * machine which the environment runs under.}
	 * {@squirreljme.property java.vendor This is the vendor of the virtual
	 * machine and specifies who wrote it.}
	 * {@squirreljme.property java.vendor.url This is a URL which usually
	 * points to the website of the vendor.}
	 * {@squirreljme.property line.separator This represents the line
	 * separation sequence that the host operating system uses for its native
	 * files. Generally it would either be {@code '\n'}, {@code '\r'}, or
	 * {@code "\r\n"), however retro-systems might use a different line ending
	 * sequence.}}
	 * {@squirreljme.property microedition.configuration This is the current
	 * configuration of CLDC library which indicates which primary classes
	 * are available to it. The values will either be {@code "CLDC-1.8"} for
	 * the complete set of APIs and {@code "CLDC-1.8-Compact"} for the compact
	 * set of APIs.}
	 * {@squirreljme.property microedition.deviceid.uuid This is the unique
	 * identifier to the current device that regardless of the number of
	 * reboots and reinitializations that occur, this should return the same
	 * value.}
	 * {@squirreljme.property microedition.encoding This is the character
	 * encoding that is used by default for specific methods when one has not
	 * been specified. On modern systems this is likely to be {@code "UTF-8},
	 * while on retro-devices and operating systems this will likely be an
	 * encoding starting with {@code "x-squirreljme"}. Please note that the
	 * default encoding might not be compatible with UTF-8 (and may possibly
	 * well be EBCDIC).}
	 * {@squirreljme.property microedition.hostname The host name of the
	 * current system that the virtual machine is running on as it appears
	 * to other machines on the network.}
	 * {@squirreljme.property microedition.locale The current locale that the
	 * library will use.}
	 * {@squirreljme.property microedition.platform This is the device that
	 * SquirrelJME is running on. It is in the format of
	 * {@code (Manufacturer)(DeviceModelNumber)[/version[/comments]]}. The
	 * manufacturer and device model number are concatenated with no spaces.}
	 * {@squirreljme.property microedition.profiles This is a space separated
	 * list of profiles which are supported by the run-time, an example
	 * value that may be returned is MIDP-3.0 representing that that specified
	 * standard is implemented.}
	 * {@squirreljme.property os.arch This is the architecture of the hardware
	 * that SquirrelJME is running on, the value is dependent on the platform
	 * itself. Note that architecture names use standard SquirrelJME
	 * architecture names.}
	 * {@squirreljme.property os.name This is the name of the operating system
	 * that SquirrelJME is running on, if SquirrelJME is the operating itself
	 * then this value will be {@code "squirreljme"}.}
	 * {@squirreljme.property os.version This is the version number of the
	 * host operating system. The returned value might not be a number and may
	 * be a string representing the host.}
	 * {@squirreljme.property user.dir This is the current working directory
	 * which indicates the location where non-absolute file paths are derived
	 * from. If there is no filesystem on the device then a blank string will
	 * always be returned.}
	 *
	 * @param __k The system property value to obtain.
	 * @return The value of the system property or {@code null} if it is not
	 * does not exist.
	 * @throws IllegalArgumentException If the key is empty.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current process is not permitted to
	 * access system properties or obtain the value of a specific property.
	 * @since 2016/05/21
	 */
	public static String getProperty(String __k)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ1b Cannot request a system property which has
		// a blank key.}
		if (__k.equals(""))
			throw new IllegalArgumentException("ZZ1b");
		
		// Short circuit for run-time detection
		if (__k.equals("cc.squirreljme.isruntime"))
			return "true";
		
		// Not allowed to do this?
		getSecurityManager().checkPropertyAccess(__k);
		
		// Depends on the property
		switch (__k)
		{
				// API level of SquirrelJME
			case "cc.squirreljme.apilevel":
				return ApiLevel.levelToString(ApiLevel.CURRENT_LEVEL);
			
				// SquirrelJME guest depth
			case "cc.squirreljme.guests":
				return Integer.toString(SystemProperties.guestDepth());
				
				// SquirrelJME free memory
			case "cc.squirreljme.vm.freemem":
				return Long.toString(Runtime.getRuntime().freeMemory());
				
				// SquirrelJME total memory
			case "cc.squirreljme.vm.totalmem":
				return Long.toString(Runtime.getRuntime().totalMemory());
				
				// SquirrelJME free memory
			case "cc.squirreljme.vm.maxmem":
				return Long.toString(Runtime.getRuntime().maxMemory());
			
				// The version of the Java virtual machine (fixed value)
			case "java.version":
				return "1.8.0";
				
				// The version of the JVM (full)
			case "java.vm.version":
				return SystemProperties.javaVMVersion();
				
				// The name of the JVM
			case "java.vm.name":
				return SystemProperties.javaVMName();
				
				// The vendor of the JVM
			case "java.vm.vendor":
				return SystemProperties.javaVMVendor();
			
				// The e-mail of the JVM
			case "java.vm.vendor.email":
				return SystemProperties.javaVMEmail();
			
				// The URL of the JVM
			case "java.vm.vendor.url":
				return SystemProperties.javaVMURL();
				
				// The vendor of the class libraries
			case "java.vendor":
				return "Stephanie Gawroriski";
				
				// Non-standard e-mail address
			case "java.vendor.email":
				return "xer@multiphasicapps.net";
				
				// The URL to the virtual machine's site
			case "java.vendor.url":
				return "http://multiphasicapps.net/";
				
				// The name of the runtime library
			case "java.runtime.name":
				return "SquirrelJME";
				
				// The version of the run-time
			case "java.runtime.version":
				return SystemProperties.javaRuntimeVersion();
				
				// Unknown, use system call
			default:
				return SystemProperties.systemProperty(__k);
		}
	}
	
	/**
	 * Obtains the specified system property and if it has not been set then
	 * the default value will be returned instead.
	 *
	 * @param __k The system property to get.
	 * @param __d If the system property is not set (returns {@code null}
	 * then this value will be returned instead.
	 * @throws IllegalArgumentException If the requested system property is
	 * not valid (it is blank).
	 * @throws NullPointerException If no key was specified.
	 * @throws SecurityException If obtaining the given system property is
	 * not permitted.
	 * @since 2017/08/13
	 */
	public static String getProperty(String __k, String __d)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		// Get it
		String rv = getProperty(__k);
		
		// If not set, return the default, otherwise the read value
		if (rv == null)
			return __d;
		return rv;
	}
	
	/**
	 * Returns the current security manager that is in use.
	 *
	 * @return The current security manager in use.
	 * @since 2018/09/18
	 */
	public static SecurityManager getSecurityManager()
	{
		// Lock because it is managed by that class for checking
		synchronized (SecurityManager.class)
		{
			return SecurityManager._CURRENT_MANAGER;
		}
	}
	
	/**
	 * This returns the identity hash code of the object. The identity hash
	 * code is randomly given by the virtual machine to an object. There is
	 * no definition on how the value is to be derived. It may be a unique
	 * object ID or it may be a memory address. Two objects may also share the
	 * same identity hash code.
	 *
	 * @param __o The input object to get the hash code for.
	 * @return The hash code which was given by the virtual machine, if the
	 * input is {@code null} then {@code 0} is returned.
	 * @since 2015/11/09
	 */
	public static int identityHashCode(Object __o)
	{
		// If null, this is zero
		if (__o == null)
			return 0;
		
		return ObjectAccess.identityHashCode(__o);
	}
	
	/**
	 * Returns the number of nanoseconds which have passed from a previously
	 * unspecified time. The returned value might not be accurate to the
	 * nanosecond. This clock is monotonic and does not suffer from time
	 * shifts caused by clock adjustments.
	 *
	 * The value returned here is specific to the current virtual machine and
	 * cannot be used elsewhere. Even two virtual machines running on the
	 * same system can use completely different values.
	 *
	 * After about 292 years (2 to the 63rd power nanoseconds) using signed
	 * comparison to calculate the amount of time that has passed will no
	 * longer function properly. For extremely long running processes it is
	 * recommended to treat the values as unsigned to extend past this limit
	 * or handle the overflow of the time value to represent any time
	 * quantity, this of course requires that time be checked every 292 or
	 * 584 years).
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2016/06/16
	 */
	public static long nanoTime()
	{
		return TimeAccess.nanoTime();
	}
	
	/**
	 * Sets the new destination for standard error.
	 *
	 * Note that the {@link System#err} field is not changed, a wrapper class
	 * is used to prevent reflective abuse.
	 *
	 * @param __a The new stream to use when outputting values.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current program lacks the given
	 * permission to set the stream.
	 * @since 2016/03/17
	 */
	public static void setErr(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Not allowed to do this?
		getSecurityManager().checkPermission(new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)err).__set(__a);
	}
	
	/**
	 * Sets the new destination for standard output.
	 *
	 * Note that the {@link System#out} field is not changed, a wrapper class
	 * is used to prevent reflective abuse.
	 *
	 * @param __a The new stream to use when outputting values.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current program lacks the given
	 * permission to set the stream.
	 * @since 2016/03/17
	 */
	public static void setOut(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Not allowed to do this?
		getSecurityManager().checkPermission(new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)out).__set(__a);
	}
}

