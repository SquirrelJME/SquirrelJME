// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

/**
 * Base file system to add additional methods needed by SquirrelJME.
 *
 * @since 2023/08/20
 */
@SquirrelJMEVendorApi
public abstract class AbstractFileSystem
	extends FileSystem
{
	/** Cached separator for this filesystem. */
	private volatile String _separator;
	
	/**
	 * Compares two path components.
	 *
	 * @param __a The first path.
	 * @param __b The second path.
	 * @return The comparison of these.
	 * @since 2023/08/21
	 */
	@SquirrelJMEVendorApi
	protected abstract int compare(Path __a, Path __b);
	
	/**
	 * Returns a path which is associated with the current filesystem.
	 *
	 * @param __path The first part of the path.
	 * @return The path.
	 * @throws InvalidPathException If the path is not valid for this
	 * file system.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	protected abstract Path getPath(String __path)
		throws InvalidPathException, NullPointerException;
	
	/**
	 * Returns the path separator.
	 *
	 * @return The path separator.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	protected abstract String getSeparatorInternal();
	
	/**
	 * Opens a channel to the file's data.
	 *
	 * @param __path The path of the file to open.
	 * @param __options The open options.
	 * @param __attribs The file attributes to open with.
	 * @return The channel to the open file.
	 * @throws IllegalArgumentException If the open options are not valid.
	 * @throws IOException On read/write errors.
	 * @throws SecurityException If opening the file is not permitted.
	 * @throws UnsupportedOperationException If the file system does not
	 * support opening the specific file.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	public abstract FileChannel open(Path __path,
		Set<? extends OpenOption> __options, FileAttribute<?>... __attribs)
		throws IllegalArgumentException, IOException, SecurityException,
			UnsupportedOperationException;
	
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
	@SquirrelJMEVendorApi
	public abstract <A extends BasicFileAttributes> A readAttributes(
		Path __path, Class<A> __attributeType, LinkOption... __linkOptions)
		throws IOException, SecurityException, UnsupportedOperationException;
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public String getSeparator()
	{
		String result = this._separator;
		if (result == null)
		{
			result = this.getSeparatorInternal();
			this._separator = result;
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path getPath(String __path, String... __more)
	{
		if (__path == null || __more == null)
			throw new NullPointerException("NARG");
		
		// Separator which is used to build a full path
		String separator = this.getSeparator();
		
		// Build full target string with separators
		StringBuilder sb = new StringBuilder(__path);
		for (String segment : __more)
		{
			if (segment == null)
				throw new NullPointerException("NARG");
			
			// Does this start with the path separator?
			boolean startSep = segment.startsWith(separator);
			
			// Only add separator if the first added segment was not blank or
			// the current segment starts with the separator already
			if (sb.length() > 0 && !startSep)
				sb.append(separator);
			
			// Append segment otherwise
			sb.append(segment);
		}
		
		// Forward to internal handler
		return this.getPath(sb.toString());
	}
}
