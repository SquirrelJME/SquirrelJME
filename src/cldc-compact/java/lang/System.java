// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.OutputStream;
import java.io.PrintStream;
import java.security.Permission;
import net.multiphasicapps.squirreljme.unsafe.VM;

public final class System
{
	/** Standard error stream (stderr). */
	public static final PrintStream err =
		new __CanSetPrintStream__(new PrintStream(new __StandardError__()));
	
	/** Standard output stream (stdout). */
	public static final PrintStream out =
		new __CanSetPrintStream__(new PrintStream(new __StandardOutput__()));
	
	private System()
	{
		throw new Error();
	}
	
	public static void arraycopy(Object __a, int __b, Object __c, int __d,
		int __e)
	{
		throw new Error("TODO");
	}
	
	public static long currentTimeMillis()
	{
		// Returns the current time in UTC, not local time zone.
		return VM.INSTANCE.time.utcMillis();
	}
	
	public static void exit(int __a)
	{
		throw new Error("TODO");
	}
	
	public static void gc()
	{
		throw new Error("TODO");
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
		
		// {@squirreljme.error ZZ0p Cannot request a system property which has
		// a blank key.}
		if (__k.equals(""))
			throw new IllegalArgumentException("ZZ0p");
		
		// Not allowed to do this?
		getSecurityManager().checkPropertyAccess(__k);
		
		// Fixed values
		switch (__k)
		{
				// Java Version
			case "java.version":
				return "1.8";
			
				// The vendor of the VM
			case "java.vendor":
				return "Steven Gawroriski (Multi-Phasic Applications)";
			
				// The URL to the website
			case "java.vendor.url":
				return "http://multiphasicapps.net/";
			
				// The configuration and which classes are available for usage
				// This actually looks up a number of classes which only
				// appear in the full configuration. Note that this is just an
				// approximation and is not fully accurate.
			case "microedition.configuration":
				try
				{
					// Try getting a number of classes
					Class.forName("java.nio.channels.ClosedChannelException");
					Class.forName("java.nio.file.FileStore");
					Class.forName("java.nio.file.FileSystem");
					Class.forName("java.nio.file.Path");
					Class.forName("java.util.logging.Level");
					Class.forName("java.util.logging.Logger");
					
					// At this point, assume the full library is being used
					return "CLDC-1.8";
				}
				
				// Classes not found, assume compact
				catch (ClassNotFoundException e)
				{
					return "CLDC-1.8-Compact";
				}
				
				// Device specific UUID
			case "microedition.deviceid.uuid":
				return VM.INSTANCE.environment.deviceUUID();
			
				// The host name of this device.
			case "microedition.hostname":
				return VM.INSTANCE.environment.osHostName();
				
				// The platform SquirrelJME is running on.
			case "microedition.platform":
				return VM.INSTANCE.environment.osPlatform();
				
				// Returns the version of SquirrelJME that this library
				// pertains to
			case "net.multiphasicapps.squirreljme.version":
				return "1.8.19890706";
				
				// Is the virtual machine running on squirrels?
			case "net.multiphasicapps.squirreljme.squirrels":
				return "true";
				
				// The architecture this VM runs on
			case "os.arch":
				return VM.INSTANCE.environment.osArch();
				
				// The OS this VM runs on
			case "os.name":
				return VM.INSTANCE.environment.osName();
				
				// The version of the OS
			case "os.version":
				return VM.INSTANCE.environment.osVersion();
			
				// Unknown, use default handling
			default:
				break;
		}
		
		throw new Error("TODO");
	}
	
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
	
	public static SecurityManager getSecurityManager()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns the identity hash code of the object. The identity hash
	 * code is randomly given by the virtual machine to an object. There is
	 * no definition on how the value is to be derived. It may be a unique
	 * object ID or it may be a memory address. Two objects may also share the
	 * same identity hash code.
	 *
	 * @param __a The input object to get the hash code for.
	 * @return The hash code which was given by the virtual machine, if the
	 * input is {@code null} then {@code 0} is returned.
	 * @since 2015/11/09
	 */
	public static int identityHashCode(Object __a)
	{
		// If null, this is zero
		if (__a == null)
			return 0;
		
		// Use object hash code data
		return __a.__identityHashCode();
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
		return VM.INSTANCE.time.nanoTime();
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

