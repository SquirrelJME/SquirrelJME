// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.RawTypeIsDefined;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.StreamConnection;
import org.jetbrains.annotations.NotNull;

@Api
public interface FileConnection
	extends StreamConnection
{
	/**
	 * Returns the available free space for the file system that the
	 * file/directory exists within.
	 *
	 * @return The available number of bytes, or {@code -1} if there is no
	 * supported filesystem.
	 * @throws ConnectionClosedException If the connection is closed.
	 * @throws IllegalModeException If the connection is not opened with
	 * read access.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@Api
	long availableSize()
		throws ConnectionClosedException, IllegalModeException,
			SecurityException;
	
	@Api
	boolean canRead();
	
	@Api
	boolean canWrite();
	
	@Api
	void create()
		throws IOException;
	
	@Api
	void delete()
		throws IOException;
	
	@Api
	long directorySize(boolean __a)
		throws IOException;
	
	@Api
	boolean exists();
	
	@Api
	long fileSize()
		throws IOException;
	
	@Api
	String getName();
	
	@Api
	String getPath();
	
	@Api
	String getURL();
	
	/**
	 * Is this an accessible directory?
	 *
	 * @return If this is a directory that can be accessed.
	 * @throws ConnectionClosedException If the connection is closed.
	 * @throws IllegalModeException If the connection is not opened with
	 * read access.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@Api
	boolean isDirectory()
		throws ConnectionClosedException, IllegalModeException,
			SecurityException;
	
	@Api
	boolean isHidden();
	
	@Api
	boolean isOpen();
	
	@Api
	long lastModified();
	
	/**
	 * As {@code list("*", false)}.
	 *
	 * @return As {@code list("*", false)}.
	 * @throws ConnectionClosedException If the connection is closed.
	 * @throws IllegalModeException If the connection is not opened with
	 * read access.
	 * @throws IOException If the directory is not accessible, this is not
	 * a directory, or some other read error occurs.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@Api
	@SuppressWarnings("rawtypes")
	@RawTypeIsDefined(String.class)
	Enumeration list()
		throws ConnectionClosedException, IllegalModeException, IOException,
			SecurityException;
	
	/**
	 * Iterates over the directory contents, with the given filter applied,
	 * note that the returned list will be in a format that matches URIs and
	 * not the native filesystem. That is, for example on DOS/Windows, the
	 * slashes will always be forward slashes and never backslashes.
	 * 
	 * Any directories will end in a {@link /}.
	 * 
	 * Any files which become part of relative paths such as {@code .} and
	 * {@code ..} are not included in the result.
	 *
	 * @param __filter The filter to apply. If an asterisk ('{@code *}') is
	 * specified it means to match zero or more characters
	 * @param __includeHidden Should any files that are hidden be included?
	 * @return An iteration of the directory contents with the applied filter.
	 * @throws ConnectionClosedException If the connection is closed.
	 * @throws IllegalModeException If the connection is not opened with
	 * read access.
	 * @throws IOException If the directory is not accessible, this is not
	 * a directory, or some other read error occurs.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@Api
	@SuppressWarnings("rawtypes")
	@RawTypeIsDefined(String.class)
	Enumeration list(@NotNull String __filter, boolean __includeHidden)
		throws ConnectionClosedException, IllegalModeException, IOException,
			SecurityException;
	
	@Api
	void mkdir()
		throws IOException;
	
	@Api
	OutputStream openOutputStream(long __a)
		throws IOException;
	
	@Api
	void rename(String __a)
		throws IOException;
	
	/**
	 * Sets this to refer to a different filesystem path in the current
	 * directory, or the parent directory if {@code ..}. Only a single path
	 * may be specified at any one time, and any paths which are specified
	 * must be paths which are returned by {@code list("*", true)}.
	 * 
	 * If {@code ..} is passed, then navigation returns to the parent
	 * directory.
	 *
	 * @param __fileName The file name to set the connection to.
	 * @throws ConnectionClosedException If the connection has already been
	 * closed.
	 * @throws IllegalArgumentException If the path is not valid or does not
	 * exist within the directory.
	 * @throws IOException If this is currently opened on a file and not
	 * a directory; the target file is not accessible; the file name is not
	 * valid for this system; or on any other read error.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/30
	 */
	@Api
	void setFileConnection(String __fileName)
		throws ConnectionClosedException, IllegalArgumentException,
			IOException, NullPointerException, SecurityException;
	
	@Api
	void setHidden(boolean __a)
		throws IOException;
	
	@Api
	void setReadable(boolean __a)
		throws IOException;
	
	@Api
	void setWritable(boolean __a)
		throws IOException;
	
	@Api
	long totalSize();
	
	@Api
	void truncate(long __a)
		throws IOException;
	
	@Api
	long usedSize();
}


