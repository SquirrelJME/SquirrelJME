// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.constants.PhoneModelType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.i18n.DefaultLocale;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.ConsoleOutputStream;
import cc.squirreljme.runtime.cldc.lang.LineEndingUtils;
import java.io.PrintStream;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This class contains methods which are used to interact with the system and
 * the environment.
 *
 * @since 2018/10/14
 */
@Api
public final class System
{
	/** Standard error stream (stderr). */
	@Api
	public static final PrintStream err =
		new __CanSetPrintStream__(new PrintStream(
			new ConsoleOutputStream(StandardPipeType.STDERR,
				true), true));
	
	/** Standard output stream (stdout). */
	@Api
	public static final PrintStream out =
		new __CanSetPrintStream__(new PrintStream(
			new ConsoleOutputStream(StandardPipeType.STDOUT,
				false), true));
	
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
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __copyLen The number of elements to copy.
	 * @throws ArrayStoreException If the destination array cannot contain
	 * the given data.
	 * @throws IndexOutOfBoundsException If the offset and or/lengths are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/27
	 */
	@Api
	public static void arraycopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull Object __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull Object __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __copyLen)
		throws ArrayStoreException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ1w Negative offsets and/or length cannot be
		specified. (The source offset; The destination offset; The copy
		length)} */
		if (__srcOff < 0 || __destOff < 0 || __copyLen < 0)
			throw new IndexOutOfBoundsException(
				String.format("ZZ1w %d %d %d",
					__srcOff, __destOff, __copyLen));
		
		/* {@squirreljme.error ZZ1x Copy operation would exceed the bounds of
		the array. (Source offset; Source length; Destination offset;
		Destination length; The copy length)} */
		int srcLen = ObjectShelf.arrayLength(__src);
		int destLen = ObjectShelf.arrayLength(__dest);
		if (__srcOff + __copyLen < 0 || __srcOff + __copyLen > srcLen ||
			__destOff + __copyLen < 0 || __destOff + __copyLen > destLen)
			throw new IndexOutOfBoundsException(String.format(
				"ZZ1x %d %d %d %d %d", __srcOff, srcLen,
					__destOff, destLen, __copyLen));
		
		// Get both respective classes
		Class<?> srcClass = __src.getClass();
		Class<?> destClass = __dest.getClass();
		
		/* {@squirreljme.error ZZ1y The source array type is not compatible
		with destination array. (The source array; The destination array)} */
		if (srcClass != destClass && !destClass.isAssignableFrom(srcClass))
			throw new ArrayStoreException(String.format(
				"ZZ1y %s %s", __src, __dest));
		
		// If we are copying nothing then we need not even bother with anything
		// else, and we do not have to check the array types as well.
		if (__copyLen == 0)
			return;
		
		// Also as well, if the source and destination are the same and the
		// offsets are the same then nothing will happen at all.
		if (__src == __dest && __srcOff == __destOff)
			return;
		
		// We can use the native type system within MLE to knock off a few
		// branch possibilities
		TypeBracket srcType = TypeShelf.classToType(srcClass);
		TypeBracket component = TypeShelf.component(srcType);
		
		// Primitive types can be copied at full speed as they do not require
		// any references are otherwise to be counted or garbage collection to
		// be managed
		if (TypeShelf.isPrimitive(component))
		{
			// More common primitives
			if (srcClass == byte[].class)
				ObjectShelf.arrayCopy((byte[])__src, __srcOff,
					(byte[])__dest, __destOff, __copyLen);
			else if (srcClass == char[].class)
				ObjectShelf.arrayCopy((char[])__src, __srcOff,
					(char[])__dest, __destOff, __copyLen);
			else if (srcClass == int[].class)
				ObjectShelf.arrayCopy((int[])__src, __srcOff,
					(int[])__dest, __destOff, __copyLen);
			else if (srcClass == short[].class)
				ObjectShelf.arrayCopy((short[])__src, __srcOff,
					(short[])__dest, __destOff, __copyLen);
			
			// Less common types
			else if (srcClass == boolean[].class)
				ObjectShelf.arrayCopy((boolean[])__src, __srcOff,
					(boolean[])__dest, __destOff, __copyLen);
			else if (srcClass == long[].class)
				ObjectShelf.arrayCopy((long[])__src, __srcOff,
					(long[])__dest, __destOff, __copyLen);
			else if (srcClass == float[].class)
				ObjectShelf.arrayCopy((float[])__src, __srcOff,
					(float[])__dest, __destOff, __copyLen);
			else if (srcClass == double[].class)
				ObjectShelf.arrayCopy((double[])__src, __srcOff,
					(double[])__dest, __destOff, __copyLen);
			
			/* {@squirreljme.error ZZ1h Not a primitive array type.} */
			else
				throw new Error("ZZ1h");
		}
		
		// There is no native handler for manual object array copies due to
		// references and otherwise
		else
		{
			Object[] src = (Object[])__src;
			Object[] dest = (Object[])__dest; 
			
			// Right-to-left copy when going from left to right would overwrite
			// the source
			if (__destOff > __srcOff)
			{
				int i = (__srcOff + __copyLen) - 1,
					o = (__destOff + __copyLen) - 1;
				
				for (; i >= __srcOff; i--, o--)
					dest[o] = src[i];
			}
			
			// Left-to-right copy
			else
			{
				// These offsets for the loops are the same
				int end = __srcOff + __copyLen;
				for (; __srcOff < end; __srcOff++, __destOff++)
					dest[__destOff] = src[__srcOff];
			}
		}
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
	@Api
	public static long currentTimeMillis()
	{
		// Returns the current time in UTC, not local time zone.
		return RuntimeShelf.currentTimeMillis();
	}
	
	/**
	 * Indicates that the application exits with the given code.
	 *
	 * @param __e The exit code, the value of this code may change according
	 * to the host operating system and the resulting process might not exit
	 * with the given code.
	 * @since 2017/02/08
	 */
	@Api
	@Contract("_ -> fail")
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
	@Api
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
	@Api
	public static String getProperty(String __k)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ1z Cannot request a system property which has
		a blank key.} */
		if (__k.equals(""))
			throw new IllegalArgumentException("ZZ1z");
		
		// Short circuit for run-time detection
		if (__k.equals("cc.squirreljme.isruntime"))
			return "true";
		
		// Not allowed to do this?
		System.getSecurityManager().checkPropertyAccess(__k);
		
		// Depends on the property
		switch (__k)
		{
				// SquirrelJME VM executable path
			case "cc.squirreljme.vm.execpath":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.EXECUTABLE_PATH);
				
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
				return RuntimeShelf.vmDescription(
					VMDescriptionType.VM_VERSION);
				
				// The name of the JVM
			case "java.vm.name":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.VM_NAME);
				
				// The vendor of the JVM
			case "java.vm.vendor":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.VM_VENDOR);
			
				// The e-mail of the JVM
			case "java.vm.vendor.email":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.VM_EMAIL);
			
				// The URL of the JVM
			case "java.vm.vendor.url":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.VM_URL);
				
				// The vendor of the class libraries
			case "java.vendor":
				return "Stephanie Gawroriski";
				
				// Non-standard e-mail address
			case "java.vendor.email":
				return "xerthesquirrel@gmail.com";
				
				// The URL to the virtual machine's site
			case "java.vendor.url":
				return "http://squirreljme.cc/";
				
				// The name of the runtime library
			case "java.runtime.name":
				return "SquirrelJME";
				
				// The version of the run-time
			case "java.runtime.version":
				return SquirrelJME.RUNTIME_VERSION;
				
				// End of line character
			case "line.separator":
				return LineEndingUtils.toString(RuntimeShelf.lineEnding());
				
				// The current configuration, must be set!
			case "microedition.configuration":
				try
				{
					Class<?> file = Class.forName(
						"java.nio.FileSystem");
					if (file == null)
						return "CLDC-1.8-Compact";
					return "CLDC-1.8";
				}
				catch (ClassNotFoundException e)
				{
					return "CLDC-1.8-Compact";
				}
				
				// The current encoding
			case "microedition.encoding":
				return CodecFactory.toString(RuntimeShelf.encoding());
				
				// The current locale, must be set!
			case "microedition.locale":
				return DefaultLocale.toString(RuntimeShelf.locale());
				
				// The current platform
			case "microedition.platform":
				// Allow this to be overridden by the user
				String platformOverride = RuntimeShelf.systemProperty(__k);
				if (platformOverride != null)
					return platformOverride;
			
				// Try to use a specific platform
				int phoneModel = RuntimeShelf.phoneModel();
				if (phoneModel != PhoneModelType.GENERIC)
					return SquirrelJME.platform(phoneModel);
				return SquirrelJME.MICROEDITION_PLATFORM;
				
				// The operating system architecture
			case "os.arch":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.OS_ARCH);
				
				// The operating system name
			case "os.name":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.OS_NAME);
				
				// The operating system name
			case "os.version":
				return RuntimeShelf.vmDescription(
					VMDescriptionType.OS_VERSION);
				
				// Unknown, use system call
			default:
				return RuntimeShelf.systemProperty(__k);
		}
	}
	
	/**
	 * Obtains the specified system property and if it has not been set then
	 * the default value will be returned instead.
	 *
	 * @param __k The system property to get.
	 * @param __d If the system property is not set (returns {@code null}
	 * then this value will be returned instead.
	 * @return The system property.
	 * @throws IllegalArgumentException If the requested system property is
	 * not valid (it is blank).
	 * @throws NullPointerException If no key was specified.
	 * @throws SecurityException If obtaining the given system property is
	 * not permitted.
	 * @since 2017/08/13
	 */
	@Api
	public static String getProperty(String __k, String __d)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		// Get it
		String rv = System.getProperty(__k);
		
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
	@Api
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
	 * object ID, or it may be a memory address. Two objects may also share the
	 * same identity hash code.
	 *
	 * @param __o The input object to get the hash code for.
	 * @return The hash code which was given by the virtual machine, if the
	 * input is {@code null} then {@code 0} is returned.
	 * @since 2015/11/09
	 */
	@Api
	public static int identityHashCode(Object __o)
	{
		// If null, this is zero
		if (__o == null)
			return 0;
		
		return ObjectShelf.identityHashCode(__o);
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
	@Api
	public static long nanoTime()
	{
		// Returns the current monotonic clock time
		return RuntimeShelf.nanoTime();
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
	@Api
	public static void setErr(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Not allowed to do this?
		System.getSecurityManager().checkPermission(new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)System.err).__set(__a);
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
	@Api
	public static void setOut(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Not allowed to do this?
		System.getSecurityManager().checkPermission(
			new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)System.out).__set(__a);
	}
}

