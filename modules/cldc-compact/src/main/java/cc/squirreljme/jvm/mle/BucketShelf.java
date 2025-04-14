// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This shelf is for the long term storage of data which is saved between
 * instances and sessions of SquirrelJME.
 * 
 * This generally is used for RMS data storage and configuration data.
 *
 * @since 2025/04/14
 */
@SquirrelJMEVendorApi
public final class BucketShelf
{
	/**
	 * Deletes the file in the given bucket.
	 *
	 * @param __bucket The bucket to access.
	 * @param __file The file to delete.
	 * @return If there was any file deleted.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static native boolean delete(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError;
	
	/**
	 * Accesses the given bucket.
	 *
	 * @param __type The type of bucket to open.
	 * @return The standard bucket.
	 * @throws MLECallError If the standard bucket type is not valid.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static native BucketBracket bucket(
		@MagicConstant(valuesFromClass = StandardBucketType.class)
			int __type)
		throws MLECallError;
	
	/**
	 * Returns the length of the file within the bucket.
	 *
	 * @param __bucket The bucket to access.
	 * @param __file The file to get the length of.
	 * @return The length of the file or {@code -1} if it does not exist.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static native int length(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError;
	
	/**
	 * Reads data from the given bucket.
	 *
	 * @param __bucket The bucket to read from.
	 * @param __file The file to read.
	 * @param __fileOff The offset within the file to read.
	 * @param __buf The buffer to write into.
	 * @param __off The offset into the buffer.
	 * @param __len The maximum number of bytes to read.
	 * @return The number of bytes actually read or {@code -1} on EOF or if
	 * the file does not exist.
	 * @throws MLECallError On null arguments; or the offset and/or length are
	 * out of bounds or negative.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static native int read(
		@NotNull BucketBracket __bucket,
		@NotNull String __file,
		@Range(from = 0, to = Integer.MAX_VALUE) int __fileOff,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError;
	
	/**
	 * Writes data to the given bucket.
	 *
	 * @param __bucket The bucket to write to.
	 * @param __file The file to write.
	 * @param __fileOff The offset within the file to write at.
	 * @param __buf The buffer to read from.
	 * @param __off The offset into the buffer.
	 * @param __len The number of bytes to write.
	 * @throws MLECallError On null arguments; or the offset and/or length are
	 * out of bounds or negative.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static native void write(
		@NotNull BucketBracket __bucket,
		@NotNull String __file,
		@Range(from = 0, to = Integer.MAX_VALUE) int __fileOff,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError;
}
