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
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
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
	/** Cached buckets. */
	private static final Map<Integer, EmulatedBucketBracket> _buckets =
		new HashMap<>();
	
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
		if (__bucket == null || __file == null)
			throw new MLECallError("NARG");
		
		// Which target path
		Path target = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		
		// Delete it
		try
		{
			return Files.deleteIfExists(target);
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}
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
		if (__bucket == null || __file == null)
			throw new MLECallError("NARG");
		
		// Which target path
		Path target = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		
		// Get the time of the file
		try
		{
			return Files.getLastModifiedTime(target).toMillis();
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}
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
		Map<Integer, EmulatedBucketBracket> buckets =
			EmulatedBucketShelf._buckets;
		Integer type = __type;
		synchronized (EmulatedBucketShelf.class)
		{
			// Already known?
			if (buckets.containsKey(type))
				return buckets.get(type);
			
			// Otherwise setup new bucket
			Path path;
			if (__type == StandardBucketType.DATA_BUCKET)
				path = SystemPathProvider.provider().bucketData();
			else if (__type == StandardBucketType.LIBRARIES_BUCKET)
				path = SystemPathProvider.provider().libraries();
			else if (__type == StandardBucketType.EXTRA_BUCKET)
				path = SystemPathProvider.provider().bucketExtra();
			else
				throw new MLECallError("Unknown bucket type: " + __type);
			
			// No path?
			if (path == null)
				throw new MLECallError("Bucket is unmapped: " + __type);
			
			// Cache and use it
			EmulatedBucketBracket result = new EmulatedBucketBracket(path);
			buckets.put(type, result);
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
		return EmulatedBucketShelf.list(__bucket,
			false, null, null, null);
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
		if (__bucket == null)
			throw new MLECallError("NARG");
		
		// Obtain base path to scan through
		Path root = ((EmulatedBucketBracket)__bucket).root;
		
		// Performing any kind of matching?
		int matching = (__prefix != null ? 1 : 0) |
			(__contains != null ? 2 : 0) |
			(__suffix != null ? 4 : 0);
		
		// List directory contents, no recursion ever
		List<String> result = new ArrayList<>();
		try (Stream<Path> stream = Files.list(root))
		{
			for (Path path : stream.toArray(Path[]::new))
			{
				// Only consider regular files
				if (!Files.isRegularFile(path))
					continue;
				
				// Operate on the base name of the file
				String baseName = path.getFileName().toString();
				
				// Limiting matches?
				if (matching != 0)
				{
					// Find actual sub-match candidates
					int found = ((__prefix != null &&
							baseName.startsWith(__prefix)) ? 1 : 0) |
						((__contains != null &&
							baseName.contains(__contains)) ? 2 : 0) |
						((__suffix != null &&
							baseName.endsWith(__suffix)) ? 4 : 0);
					
					// Is this a valid full match? And also matches
					// the not condition for flipping?
					if (((found & matching) == matching) != __not)
						result.add(baseName);
				}
				
				// Not doing so, just add it
				else
					result.add(baseName);
			}
		}
		catch (FileNotFoundException|NoSuchFileException ignored)
		{
			// RMS directory does not exist
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}	
		
		// Return resultant list
		return result.toArray(new String[result.size()]);
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
		if (__bucket == null || __file == null)
			throw new MLECallError("NARG");
		
		Path path = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		try
		{
			if (!Files.exists(path))
				return -1;
			return Files.size(path);
		}
		catch (FileNotFoundException __e)
		{
			return -1;
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}
	}
	
	/**
	 * Returns the path to the bucket on the disk, if known.
	 *
	 * @param __bucket The bucket to get the path of.
	 * @return The path to the bucket on the local disk or {@code null} if it
	 * is not known.
	 * @throws MLECallError On null arguments.
	 * @since 2025/04/29
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static String path(@NotNull BucketBracket __bucket)
		throws MLECallError
	{
		if (__bucket == null)
			throw new MLECallError("No bucket specified.");
		
		return ((EmulatedBucketBracket)__bucket).root
			.normalize().toAbsolutePath().toString();
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
		if (__bucket == null || __file == null || __buf == null)
			throw new MLECallError("NARG");
		
		if (__fileOff < 0 || __off < 0 || __len < 0 ||
			(__off + __len) < 0 || (__off + __len) > __buf.length)
			throw new MLECallError("IOOB");
		
		// Open the file to read a chunk of it
		Path path = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		try (FileChannel channel = FileChannel.open(path,
			StandardOpenOption.READ))
		{
			return channel.read(ByteBuffer.wrap(__buf, __off, __len),
				__fileOff);
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}
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
		if (__bucket == null || __file == null || __buf == null)
			throw new MLECallError("NARG");
		
		if (__fileOff < 0 || __off < 0 || __len < 0 || (__off + __len) < 0)
			throw new MLECallError("IOOB");
		
		// Which target path
		Path target = ((EmulatedBucketBracket)__bucket).__resolve(__file);
		
		// Depends on the write mode
		Path tempFile = null;
		try (InputStream in = new ByteArrayInputStream(__buf, __off, __len))
		{
			// Create temporary file
			tempFile = Files.createTempFile("squirreljme", ".rms");
			
			// Depends on the mode
			switch (__mode)
			{
				case BucketWriteMode.TRUNCATE:
					if (__fileOff != 0)
						throw new MLECallError(
							"File offset must be zero.");
					
					Files.write(tempFile, StreamUtils.readAll(in));
					break;
				
				default:
					throw Debugging.todo(__mode);
			}
			
			// Replace the original file
			Files.createDirectories(target.getParent());
			Files.move(tempFile, target,
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e.getMessage(), __e);
		}
		finally
		{
			// Make sure the temporary is gone
			if (tempFile != null)
				try
				{
					Files.deleteIfExists(tempFile);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
