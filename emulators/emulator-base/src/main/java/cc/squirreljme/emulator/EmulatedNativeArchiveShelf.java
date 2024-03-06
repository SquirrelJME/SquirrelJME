// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.NativeArchiveShelf;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Emulated version of {@link NativeArchiveShelf}.
 *
 * @since 2024/03/05
 */
@SuppressWarnings("unused")
public final class EmulatedNativeArchiveShelf
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/05
	 */
	private EmulatedNativeArchiveShelf()
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
	public static void archiveClose(
		@NotNull NativeArchiveBracket __archive)
		throws MLECallError
	{
		if (__archive == null)
			throw new MLECallError("Null arguments.");
		
		try
		{
			((EmulatedNativeArchiveBracket)__archive).close();
		}
		catch (IOException __e)
		{
			throw new MLECallError("Could not close archive.", __e);
		}
	}
	
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
	public static NativeArchiveEntryBracket archiveEntry(
		@NotNull NativeArchiveBracket __archive,
		@NotNull String __name)
		throws MLECallError
	{
		if (__archive == null || __name == null)
			throw new MLECallError("Null arguments.");
		
		return ((EmulatedNativeArchiveBracket)__archive).entry(__name);
	}
	
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
	public static NativeArchiveBracket archiveOpenZip(
		@NotNull byte[] __buf,
		@Range(from=0, to=Integer.MAX_VALUE) int __off,
		@Range(from=0, to=Integer.MAX_VALUE) int __len)
		throws MLECallError
	{
		if (__buf == null)
			throw new MLECallError("Null arguments.");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __buf.length)
			throw new MLECallError("Buffer out of bounds.");
		
		try
		{
			return new EmulatedNativeZipArchiveBracket(
				new ZipBlockReader(__buf, __off, __len));
		}
		catch (IOException __e)
		{
			throw new MLECallError("Could not initialize Zip.", __e);
		}
	}
	
	/**
	 * Returns whether the given entry is a directory.
	 *
	 * @param __entry The entry to check.
	 * @return If it is a directory.
	 * @throws MLECallError If the entry is not valid.
	 * @since 2024/03/05
	 */
	public static boolean entryIsDirectory(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError
	{
		if (__entry == null)
			throw new MLECallError("Null arguments.");
		
		return ((EmulatedNativeArchiveEntryBracket)__entry).isDirectory();
	}
	
	/**
	 * Opens the given entry.
	 *
	 * @param __entry The entry to open.
	 * @return The stream to the entry data.
	 * @throws MLECallError If the entry is not valid, or it is a directory.
	 * @since 2024/03/05
	 */
	public static InputStream entryOpen(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError
	{
		if (__entry == null)
			throw new MLECallError("Null arguments.");
	
		return ((EmulatedNativeArchiveEntryBracket)__entry).open();
	}
	
	/**
	 * Returns the uncompressed size of the entry.
	 *
	 * @param __entry The entry to get from.
	 * @return The uncompressed size of the entry.
	 * @throws MLECallError If the entry is not valid.
	 * @since 2024/03/05
	 */
	public static long entryUncompressedSize(
		@NotNull NativeArchiveEntryBracket __entry)
		throws MLECallError
	{
		if (__entry == null)
			throw new MLECallError("Null arguments.");
	
		return ((EmulatedNativeArchiveEntryBracket)__entry).uncompressedSize();
	}
}
