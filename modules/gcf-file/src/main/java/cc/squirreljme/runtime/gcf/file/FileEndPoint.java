// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * This represents an endpoint for file connections, this performs the actual
 * logic for direct file access.
 *
 * @since 2025/12/29
 */
@SquirrelJMEVendorApi
public abstract class FileEndPoint
	implements Closeable
{
	/** The mode this end point is opened in. */
	@SquirrelJMEVendorApi
	@MagicConstant(flagsFromClass = Connector.class)
	protected final int mode;
	
	/** The URI part of this endpoint. */
	@SquirrelJMEVendorApi
	protected final UriGenericPart part;
	
	/**
	 * Initializes the endpoint.
	 *
	 * @param __part The URI part of the endpoint.
	 * @param __mode The mode the endpoint is opened in.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	protected FileEndPoint(@NotNull UriGenericPart __part,
		@MagicConstant(flagsFromClass = Connector.class) int __mode)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		this.part = __part;
		this.mode = __mode;
	}
	
	/**
	 * Returns the attributes which are currently attached.
	 *
	 * @return The attached attributes.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract ExtraFileAttributes attachedAttributes()
		throws SecurityException;
	
	/**
	 * Returns the attached file store.
	 *
	 * @return The attached file store or {@code null} if not available.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract FileStore attachedFileStore()
		throws SecurityException;
	
	/**
	 * Returns the attached filesystem.
	 *
	 * @return The attached filesystem or {@code null} if not available.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract FileSystem attachedFileSystem()
		throws SecurityException;
	
	/**
	 * Lists the content of this directory.
	 *
	 * @param __into The mapping where directory contents are placed, note
	 * that all {@link UriGenericPart} should be in the form
	 * of {@code //auth/path} as there is no {@code file:}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	protected abstract void listDirectory(
		@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException;
	
	/**
	 * Is this a directory?
	 *
	 * @return If this is a directory.
	 * @since 2025/12/30
	 */
	public boolean isDirectory()
	{
		// If the directory bit it set or if it ends with a slash, then this
		// is a directory
		ExtraFileAttributes attrib = this.attachedAttributes();
		return attrib.isDirectory() ||
			this.part.getPath().endsWith("/");
	}
}
