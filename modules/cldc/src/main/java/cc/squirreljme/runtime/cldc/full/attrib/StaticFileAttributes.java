// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full.attrib;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.attribute.FileTime;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

/**
 * Static file attributes.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public class StaticFileAttributes
	extends AbstractFileAttributes
{
	/** File flags. */
	@SquirrelJMEVendorApi
	@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
	protected final int flags;
	
	/** The creation time. */
	@SquirrelJMEVendorApi
	protected final FileTime creationTime;
	
	/** The last access time. */
	@SquirrelJMEVendorApi
	protected final FileTime lastAccessTime;
	
	/** The last modified time. */
	@SquirrelJMEVendorApi
	protected final FileTime lastModifiedTime;
	
	/** The POSIX group id. */
	@SquirrelJMEVendorApi
	protected final int posixGroupId;
	
	/** The POSIX user id. */
	@SquirrelJMEVendorApi
	protected final int posixUserId;
	
	/** The size on disk. */
	@SquirrelJMEVendorApi
	protected final long size;
	
	/**
	 * Initialize the static file attributes.
	 *
	 * @param __flags The attribute flags.
	 * @param __size The file size.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public StaticFileAttributes(
		@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
			int __flags, long __size)
	{
		this(__flags, __size,
			null, null, null,
			0, 0);
	}
	
	/**
	 * Initialize the static file attributes.
	 *
	 * @param __flags The attribute flags.
	 * @param __size The file size.
	 * @param __posixGroupId The POSIX group ID.
	 * @param __posixUserId The POSIX user ID.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public StaticFileAttributes(
		@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
			int __flags, long __size,
		int __posixGroupId, int __posixUserId)
	{
		this(__flags, __size,
			null, null, null,
			__posixGroupId, __posixUserId);
	}
	
	/**
	 * Initialize the static file attributes.
	 *
	 * @param __flags The attribute flags.
	 * @param __size The file size.
	 * @param __creationTime The creation time.
	 * @param __lastAccessTime The last access time.
	 * @param __lastModifiedTime The last modified time.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public StaticFileAttributes(
		@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
			int __flags, long __size,
		@Nullable FileTime __creationTime,
		@Nullable FileTime __lastAccessTime,
		@Nullable FileTime __lastModifiedTime)
	{
		this(__flags, __size,
			__creationTime, __lastAccessTime, __lastModifiedTime,
			0, 0);
	}
	
	/**
	 * Initialize the static file attributes.
	 *
	 * @param __flags The attribute flags.
	 * @param __size The file size.
	 * @param __creationTime The creation time.
	 * @param __lastAccessTime The last access time.
	 * @param __lastModifiedTime The last modified time.
	 * @param __posixGroupId The POSIX group ID.
	 * @param __posixUserId The POSIX user ID.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public StaticFileAttributes(
		@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
			int __flags, long __size,
		@Nullable FileTime __creationTime,
		@Nullable FileTime __lastAccessTime,
		@Nullable FileTime __lastModifiedTime,
		int __posixGroupId, int __posixUserId)
	{
		this.flags = __flags;
		this.posixGroupId = Math.max(0, __posixGroupId);
		this.posixUserId = Math.max(0, __posixUserId);
		this.size = Math.max(0, __size);
		
		// These fallback to the epoch
		this.creationTime = (__creationTime != null ?
			__creationTime : AbstractFileAttributes.EPOCH_TIME);
		this.lastAccessTime = (__lastAccessTime != null ? 
			__lastAccessTime : AbstractFileAttributes.EPOCH_TIME);
		this.lastModifiedTime = (__lastModifiedTime != null ? 
			__lastModifiedTime : AbstractFileAttributes.EPOCH_TIME);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected int flags()
	{
		return this.flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime creationTime()
	{
		return this.creationTime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public int getPosixGroupId()
	{
		return this.posixGroupId;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public int getPosixUserId()
	{
		return this.posixUserId;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime lastModifiedTime()
	{
		return this.lastModifiedTime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime lastAccessTime()
	{
		return this.lastAccessTime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public long size()
	{
		return this.size;
	}
}
