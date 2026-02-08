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
import java.nio.file.attribute.FileTime;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Base abstract file attributes.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public abstract class AbstractFileAttributes
	implements ExtraFileAttributes
{
	/** Is a directory. */
	@SquirrelJMEVendorApi
	public static final byte IS_DIRECTORY =
		1;
	
	/** Is some other type of file. */
	@SquirrelJMEVendorApi
	public static final byte IS_OTHER =
		2;
	
	/** Is a regular file. */
	@SquirrelJMEVendorApi
	public static final byte IS_REGULAR_FILE =
		4;
	
	/** Is a symbolic link. */
	@SquirrelJMEVendorApi
	public static final byte IS_SYMBOLIC_LINK =
		8;
	
	/** Is DOS archivable. */
	@SquirrelJMEVendorApi
	public static final byte IS_DOS_ARCHIVABLE = 
		16;
	
	/** Is DOS hidden. */
	@SquirrelJMEVendorApi
	public static final byte IS_DOS_HIDDEN = 
		32;
	
	/** Is DOS read-only. */
	@SquirrelJMEVendorApi
	public static final byte IS_DOS_READ_ONLY = 
		64;
	
	/** Is DOS system file. */
	@SquirrelJMEVendorApi
	public static final short IS_DOS_SYSTEM = 
		128;
	
	/** Is POSIX group executable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_GROUP_EXECUTE = 
		256;
	
	/** Is POSIX group readable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_GROUP_READ = 
		512;
	
	/** Is POSIX set group ID. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_GROUP_SUID = 
		1024;
	
	/** Is POSIX group writable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_GROUP_WRITE = 
		2048;
	
	/** Is POSIX other executable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_OTHER_EXECUTE = 
		4096;
	
	/** Is POSIX other readable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_OTHER_READ =
		8192;
	
	/** Is POSIX other writable. */
	@SquirrelJMEVendorApi
	public static final short IS_POSIX_OTHER_WRITE = 
		16384;
	
	/** Is POSIX restricted deletion. */
	@SquirrelJMEVendorApi
	public static final int IS_POSIX_RESTRICTED_DELETE = 
		32768;
	
	/** Is POSIX user executable. */
	@SquirrelJMEVendorApi
	public static final int IS_POSIX_USER_EXECUTE = 
		65536;
	
	/** Is POSIX user readable. */
	@SquirrelJMEVendorApi
	public static final int IS_POSIX_USER_READ = 
		131072;
	
	/** Is POSIX is user set ID. */
	@SquirrelJMEVendorApi
	public static final int IS_POSIX_USER_SUID = 
		262144;
	
	/** Is POSIX is user writable. */
	@SquirrelJMEVendorApi
	public static final int IS_POSIX_USER_WRITE = 
		524288;
	
	/** Epoch based file time. */
	@SquirrelJMEVendorApi
	public static FileTime EPOCH_TIME =
		FileTime.fromMillis(0);
	
	/**
	 * Returns the file flags.
	 *
	 * @return The file flags.
	 * @since 2025/12/30
	 */
	@MagicConstant(flagsFromClass = AbstractFileAttributes.class)
	protected abstract int flags();
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime creationTime()
	{
		// Default to epoch time
		return AbstractFileAttributes.EPOCH_TIME;
	}
	
	/**
	 * Always returns {@code null}, this is for compatibility with Java SE.
	 *
	 * @return Always {@code null}.
	 * @since 2025/12/30
	 */
	@SuppressWarnings("override")
	@SquirrelJMEVendorApi
	public final Object fileKey()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public int getPosixGroupId()
	{
		// No group (root)
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public int getPosixUserId()
	{
		// No user (root)
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isDirectory()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_DIRECTORY) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isDosArchivable()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_DOS_ARCHIVABLE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isDosHidden()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_DOS_HIDDEN) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isDosReadOnly()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_DOS_READ_ONLY) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isDosSystem()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_DOS_SYSTEM) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isOther()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_OTHER) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixGroupExecute()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_GROUP_EXECUTE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixGroupRead()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_GROUP_READ) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixGroupSUID()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_GROUP_SUID) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixGroupWrite()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_GROUP_WRITE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixOtherExecute()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_OTHER_EXECUTE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixOtherRead()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_OTHER_READ) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixOtherWrite()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_OTHER_WRITE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixRestrictedDelete()
	{
		int flags = this.flags();
		return (flags &
			AbstractFileAttributes.IS_POSIX_RESTRICTED_DELETE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixUserExecute()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_USER_EXECUTE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixUserRead()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_USER_READ) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixUserSUID()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_USER_SUID) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isPosixUserWrite()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_POSIX_USER_WRITE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isRegularFile()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_REGULAR_FILE) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean isSymbolicLink()
	{
		int flags = this.flags();
		return (flags & AbstractFileAttributes.IS_SYMBOLIC_LINK) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime lastAccessTime()
	{
		// Default to epoch time
		return AbstractFileAttributes.EPOCH_TIME;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileTime lastModifiedTime()
	{
		// Default to epoch time
		return AbstractFileAttributes.EPOCH_TIME;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public long size()
	{
		// Default to no size
		return 0;
	}
}
