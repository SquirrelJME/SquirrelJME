// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Access to native archives.
 *
 * @since 2024/03/05
 */
@SquirrelJMEVendorApi
public final class NativeArchiveShelf
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/05
	 */
	private NativeArchiveShelf()
	{
	}
	
	/**
	 * Closes the given archive.
	 *
	 * @param __archive The archive to close.
	 * @throws MLECallError If the archive could not be closed, or it
	 * was {@code null}.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	public static native void archiveClose(
		@NotNull NativeArchiveBracket __archive)
		throws MLECallError;
	
	/**
	 * Obtains the given entry in the archive, assuming it exists.
	 *
	 * @param __archive The archive to get from.
	 * @param __name The name of the entry.
	 * @return The entry for the archive or {@code null} if not found.
	 * @throws MLECallError If the archive is not valid.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native NativeArchiveEntryBracket archiveEntry(
		@NotNull NativeArchiveBracket __archive,
		@NotNull String __name)
		throws MLECallError;
	
	/**
	 * Maps the given byte array to a Zip file in memory.
	 *
	 * @param __buf The buffer to access.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer.
	 * @return The resultant memory mapped Zip.
	 * @throws MLECallError If it could not be mapped, the Zip is not valid,
	 * on null arguments, or the offset and/or length are negative or exceed
	 * the array bounds.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static native NativeArchiveBracket archiveOpenZip(
		@NotNull byte[] __buf,
		@Range(from=0, to=Integer.MAX_VALUE) int __off,
		@Range(from=0, to=Integer.MAX_VALUE) int __len)
		throws MLECallError;
	
	/**
	 * Returns whether the given entry is a directory.
	 *
	 * @param __entry The entry to check.
	 * @return If it is a directory.
	 * @throws MLECallError If the entry is not valid.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	public static native boolean entryIsDirectory(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError;
	
	/**
	 * Opens the given entry.
	 *
	 * @param __entry The entry to open.
	 * @return The stream to the entry data.
	 * @throws MLECallError If the entry is not valid, or it is a directory.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	public static native InputStream entryOpen(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError;
	
	/**
	 * Returns the uncompressed size of the entry.
	 *
	 * @param __entry The entry to get from.
	 * @return The uncompressed size of the entry.
	 * @throws MLECallError If the entry is not valid.
	 * @since 2024/03/05
	 */
	@SquirrelJMEVendorApi
	public static native long entryUncompressedSize(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError;
}
