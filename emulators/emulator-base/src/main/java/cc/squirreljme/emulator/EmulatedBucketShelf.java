// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.BucketWriteMode;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.SystemPathProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Emulates {@link BucketShelf}.
 *
 * @since 2025/04/18
 */
public class EmulatedBucketShelf
{
	private static volatile EmulatedBucketBracket _dataBucket;
	
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
	public static boolean delete(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the last modified time of the given bucket file.
	 *
	 * @param __bucket The bucket to access.
	 * @param __file The file to get the last modified time from.
	 * @return The last modified time or {@link Long#MIN_VALUE} if it cannot
	 * be calculated.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/17
	 */
	@SquirrelJMEVendorApi
	public static long lastModifiedTime(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the given bucket file exists.
	 *
	 * @param __bucket The bucket to access.
	 * @param __file The file to check if it exists.
	 * @return If the file exists or not.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/17
	 */
	@SquirrelJMEVendorApi
	public static boolean exists(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError
	{
		if (__bucket == null || __file == null)
			throw new MLECallError("NARG");
		
		Path path = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		return Files.exists(path);
	}
	
	/**
	 * Accesses the given bucket.
	 *
	 * @param __type The type of bucket to open.
	 * @return The standard bucket.
	 * @throws MLECallError If the standard bucket type is not valid or not
	 * supported.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static BucketBracket bucket(
		@MagicConstant(valuesFromClass = StandardBucketType.class)
			int __type)
		throws MLECallError
	{
		if (__type != StandardBucketType.DATA_BUCKET)
			throw new MLECallError("Unknown bucket type: " + __type);
		
		synchronized (EmulatedBucketShelf.class)
		{
			EmulatedBucketBracket result = EmulatedBucketShelf._dataBucket;
			if (result != null)
				return result;
			
			result = new EmulatedBucketBracket(SystemPathProvider.provider()
				.state().resolve("data"));
			EmulatedBucketShelf._dataBucket = result;
			return result;
		}
	}
	
	/**
	 * Lists all files in the bucket.
	 *
	 * @param __bucket The bucket to the get the list of file from.
	 * @return The list of files in the bucket.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/16
	 */
	@SquirrelJMEVendorApi
	public static String[] list(
		@NotNull BucketBracket __bucket)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * Lists all files in the bucket with a conditional filter. Note that
	 * all conditional filters if specified must be matched, there also is an
	 * option of inverting the filter to match anything otherwise.
	 *
	 * @param __bucket The bucket to the get the list of file from.
	 * @param __not Inverts the condition if {@code __prefix},
	 * {@code __contains}, and/or {@code __suffix} are set.
	 * @param __prefix An optional prefix to filter file names with.
	 * @param __contains An optional string to check if the file name contains
	 * the given name.
	 * @param __suffix An optional suffix to filter file names with.
	 * @return The list of files in the bucket.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/16
	 */
	@SquirrelJMEVendorApi
	public static String[] list(
		@NotNull BucketBracket __bucket,
		boolean __not,
		@Nullable String __prefix,
		@Nullable String __contains,
		@Nullable String __suffix)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
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
	public static long length(
		@NotNull BucketBracket __bucket,
		@NotNull String __file)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
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
	public static int read(
		@NotNull BucketBracket __bucket,
		@NotNull String __file,
		@Range(from = 0, to = Integer.MAX_VALUE) int __fileOff,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes data to the given bucket.
	 *
	 * @param __bucket The bucket to write to.
	 * @param __file The file to write.
	 * @param __fileOff The offset within the file to write at.
	 * @param __buf The buffer to read from.
	 * @param __off The offset into the buffer.
	 * @param __len The number of bytes to write.
	 * @param __mode The {@link BucketWriteMode} of the bucket.
	 * @throws MLECallError On null arguments; or the offset and/or length are
	 * out of bounds or negative.
	 * @since 2025/04/14
	 */
	@SquirrelJMEVendorApi
	public static void write(
		@NotNull BucketBracket __bucket,
		@NotNull String __file,
		@Range(from = 0, to = Integer.MAX_VALUE) int __fileOff,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len,
		@MagicConstant(valuesFromClass = BucketWriteMode.class) int __mode)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
