// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Files
{
	private Files()
	{
		super();
		throw new todo.TODO();
	}
	
	public static Path copy(Path __a, Path __b, CopyOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createDirectories(Path __a, FileAttribute<?>... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createDirectory(Path __a, FileAttribute<?>... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createFile(Path __a, FileAttribute<?>... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createTempDirectory(Path __a, String __b,
		FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createTempDirectory(String __a, FileAttribute<?>...
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createTempFile(Path __a, String __b, String __c,
		FileAttribute<?>... __d)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path createTempFile(String __a, String __b, FileAttribute
		<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static void delete(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean deleteIfExists(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean exists(Path __a, LinkOption... __b)
	{
		throw new todo.TODO();
	}
	
	public static Object getAttribute(Path __a, String __b, LinkOption... __c
		)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static FileStore getFileStore(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static FileTime getLastModifiedTime(Path __a, LinkOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean isDirectory(Path __a, LinkOption... __b)
	{
		throw new todo.TODO();
	}
	
	public static boolean isHidden(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean isReadable(Path __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isRegularFile(Path __p)
	{
		throw new todo.TODO();
	}
	
	public static boolean isSameFile(Path __a, Path __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean isWritable(Path __a)
	{
		throw new todo.TODO();
	}
	
	public static Path move(Path __a, Path __b, CopyOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static SeekableByteChannel newByteChannel(Path __a, Set<? extends
		OpenOption> __b, FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static SeekableByteChannel newByteChannel(Path __a, OpenOption...
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static DirectoryStream<Path> newDirectoryStream(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static DirectoryStream<Path> newDirectoryStream(Path __a, String
		__b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static DirectoryStream<Path> newDirectoryStream(Path __a,
		DirectoryStream.Filter<? super Path> __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static InputStream newInputStream(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static OutputStream newOutputStream(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static boolean notExists(Path __a, LinkOption... __b)
	{
		throw new todo.TODO();
	}
	
	public static <A extends BasicFileAttributes> A readAttributes(Path __a,
		Class<A> __b, LinkOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Map<String, Object> readAttributes(Path __a, String __b,
		LinkOption... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path setAttribute(Path __a, String __b, Object __c,
		LinkOption... __d)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static Path setLastModifiedTime(Path __a, FileTime __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static long size(Path __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}

