// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.InputStream;

/**
 * This allows access to the library class path and resources.
 *
 * @since 2020/06/07
 */
@SquirrelJMEVendorApi
public final class JarPackageShelf
{
	/**
	 * Returns the classpath of the current program.
	 *
	 * @return The classpath of the current program.
	 * @since 2020/06/07
	 */
	@SquirrelJMEVendorApi
	public static native JarPackageBracket[] classPath();
	
	/**
	 * Checks if the given brackets refer to the same JAR.
	 * 
	 * @param __a The first JAR.
	 * @param __b The second JAR.
	 * @return If these are equal or not.
	 * @throws MLECallError If either argument is {@code null}.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	public static native boolean equals(
		JarPackageBracket __a, JarPackageBracket __b)
		throws MLECallError;
	
	/**
	 * Returns the libraries which are available to the virtual machine.
	 * 
	 * @return The libraries that are currently available.
	 * @since 2020/10/31
	 */
	@SquirrelJMEVendorApi
	public static native JarPackageBracket[] libraries();
	
	/**
	 * Returns the path to the given JAR.
	 * 
	 * Note that this may or may not be a physical path, it could be a
	 * representation of the JAR file in string form.
	 * 
	 * @param __jar The JAR to get the path of.
	 * @return The path of the given JAR.
	 * @throws MLECallError If the JAR is not valid.
	 * @since 2020/10/31
	 */
	@SquirrelJMEVendorApi
	public static native String libraryPath(JarPackageBracket __jar)
		throws MLECallError;
	
	/**
	 * Opens the resource from the input stream.
	 *
	 * @param __jar The JAR to open.
	 * @param __rc The resource to load from the given JAR.
	 * @return Input stream to the resource, may be {@code null} if it does
	 * not exist.
	 * @throws MLECallError If the JAR is not valid or the resource was not
	 * specified.
	 * @since 2020/06/07
	 */
	@SquirrelJMEVendorApi
	public static native InputStream openResource(JarPackageBracket __jar,
		String __rc)
		throws MLECallError;
	
	/**
	 * Reads a section of a JAR representation, note that the format is not
	 * necessarily in the format of a JAR or ZIP file but may exist in SQC
	 * form.
	 * 
	 * @param __jar The library to read the raw data from.
	 * @param __jarOffset The offset into the raw data.
	 * @param __b The buffer to write into.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @return The number of bytes read from the raw Jar data.
	 * @throws MLECallError On null arguments or if the offset and/or length
	 * exceed the array bounds.
	 * @since 2022/03/04
	 */
	@SquirrelJMEVendorApi
	public static native int rawData(JarPackageBracket __jar,
		int __jarOffset, byte[] __b, int __o, int __l)
		throws MLECallError;
	
	/**
	 * Returns the raw size of a given JAR, note that this may not be
	 * the size of a JAR file but a compiled form such a SQC.
	 * 
	 * @param __jar The JAR to lookup.
	 * @return The raw size of the JAR, this will be a negative value if the
	 * JAR is virtual and its size is not known.
	 * @throws MLECallError If {@code __jar} is null.
	 * @since 2022/03/04
	 */
	@SquirrelJMEVendorApi
	public static native int rawSize(JarPackageBracket __jar)
		throws MLECallError;
}
