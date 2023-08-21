// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.AbstractFileSystem;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Set;

@Api
public final class Files
{
	private Files()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Path copy(Path __a, Path __b, CopyOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	/**
	 * Creates directories for the entire specified path with the given
	 * attributes.
	 *
	 * @param __path The path to create directories for.
	 * @param __attribs The attributes to use for the new directories.
	 * @return {@code __path}.
	 * @throws FileAlreadyExistsException If a segment in the path exists
	 * however it is not a directory or directory-like such a non-directory
	 * symlink.
	 * @throws IOException If a directory could not be created.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If creating a directory is not permitted.
	 * @throws UnsupportedOperationException If creating a directory is not
	 * possible on the given filesystem or a file system attribute cannot be
	 * applied atomically.
	 * @since 2023/08/20
	 */
	@Api
	public static Path createDirectories(Path __path,
		FileAttribute<?>... __attribs)
		throws FileAlreadyExistsException, IOException, NullPointerException,
			SecurityException, UnsupportedOperationException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Start at the root directory, if applicable
		Path rover = __path.getRoot();
		
		// Go through and process each directory
		int count = __path.getNameCount();
		for (int at = 0; at < count; at++)
		{
			Path segment = __path.getName(at);
			
			// Determine which directory from the root we are working on
			if (rover == null)
				rover = segment;
			else
				rover = rover.resolve(segment);
			
			// If the file exists, check its type first
			if (Files.exists(rover))
			{
				BasicFileAttributes attrs = Files.readAttributes(
					rover, BasicFileAttributes.class);
				
				/* {@squirreljme.error ZY03 Cannot create directory because
				a non-directory already exists. (The path) */
				if (!attrs.isDirectory())
					throw new FileAlreadyExistsException("ZY03 " + rover);
			}
			
			// Create otherwise
			else
				Files.createDirectory(rover, __attribs);
		}
		
		// Return the originally created path
		return __path;
	}
	
	@Api
	public static Path createDirectory(Path __a, FileAttribute<?>... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path createFile(Path __a, FileAttribute<?>... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path createTempDirectory(Path __a, String __b,
		FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path createTempDirectory(String __a, FileAttribute<?>...
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path createTempFile(Path __a, String __b, String __c,
		FileAttribute<?>... __d)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path createTempFile(String __a, String __b, FileAttribute
		<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static void delete(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean deleteIfExists(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	/**
	 * Checks whether the given path exists.
	 *
	 * @param __path The path to check.
	 * @param __linkOptions The symbolic link follow options.
	 * @return If the path exists or not.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this is not permitted by the current
	 * security policy.
	 * @since 2023/08/20
	 */
	@Api
	public static boolean exists(Path __path, LinkOption... __linkOptions)
		throws NullPointerException, SecurityException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Just try to read the file attributes for this path, it will fail
		// if the file is not found
		try
		{
			Files.readAttributes(__path, BasicFileAttributes.class);
			return true;
		}
		
		// If there are any read errors, then it is just assumed to not exist
		// even though it technically is not correct
		catch (IOException ignored)
		{
			return false;
		}
	}
	
	@Api
	public static Object getAttribute(Path __a, String __b, LinkOption... __c
		)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static FileStore getFileStore(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static FileTime getLastModifiedTime(Path __a, LinkOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isDirectory(Path __path, LinkOption... __linkOptions)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isHidden(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isReadable(Path __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isRegularFile(Path __p)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isSameFile(Path __a, Path __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isWritable(Path __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Path move(Path __a, Path __b, CopyOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static SeekableByteChannel newByteChannel(Path __a, Set<? extends
		OpenOption> __b, FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static SeekableByteChannel newByteChannel(Path __a, OpenOption...
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static DirectoryStream<Path> newDirectoryStream(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static DirectoryStream<Path> newDirectoryStream(Path __a, String
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static DirectoryStream<Path> newDirectoryStream(Path __a,
		DirectoryStream.Filter<? super Path> __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static InputStream newInputStream(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static OutputStream newOutputStream(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean notExists(Path __a, LinkOption... __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Reads file attributes.
	 * 
	 * All implementations are required to implement
	 * {@link BasicFileAttributes}, so this must never fail.
	 *
	 * @param <A> The attribute type to read.
	 * @param __path The path to read the attributes for.
	 * @param __attributeType The attribute type to read.
	 * @param __linkOptions The options for symbolic links.
	 * @return The given attribute
	 * @throws IOException On read errors or if the attributes could not
	 * be read.
	 * @throws SecurityException If the operation is not permitted.
	 * @throws UnsupportedOperationException If the given attribute is not
	 * valid for the given filesystem.
	 * @since 2023/08/20
	 */
	@Api
	public static <A extends BasicFileAttributes> A readAttributes(Path __path,
		Class<A> __attributeType, LinkOption... __linkOptions)
		throws IOException, SecurityException, UnsupportedOperationException
	{
		if (__path == null || __attributeType == null)
			throw new NullPointerException("NARG");
		
		// Get the SquirrelJME filesystem base
		AbstractFileSystem fileSystem =
			(AbstractFileSystem)__path.getFileSystem();
		
		// Read the attributes from it
		return fileSystem.readAttributes(__path, __attributeType,
			__linkOptions);
	}
	
	@Api
	public static Path setAttribute(Path __a, String __b, Object __c,
		LinkOption... __d)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static Path setLastModifiedTime(Path __a, FileTime __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static long size(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}

