// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

/**
 * This is a single record which stores multiple tracks of data.
 *
 * Vinyls have a single lock on them.
 *
 * @since 2018/12/13
 */
public interface VinylRecord
{
	/** No memory is available. */
	int ERROR_NO_MEMORY =
		-1;
	
	/** No such volume. */
	int ERROR_NO_VOLUME =
		-2;
	
	/** No such page. */
	int ERROR_NO_PAGE =
		-3;
	
	/**
	 * Locks this record so only a single set of actions can be performed on
	 * them, even for the same thread.
	 *
	 * @return The lock used to eventually unlock, to be used with
	 * try-with-resources.
	 * @since 2018/12/14
	 */
	VinylLock lock();
	
	/**
	 * Adds a page to the given volume.
	 *
	 * @param __vid The volume ID.
	 * @param __b The data to store.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @param __tag The tag to identify the given record with.
	 * @return The ID of the newly created page.
	 * @throws IndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	int pageAdd(int __vid, byte[] __b, int __o, int __l, int __tag)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Deletes the given page.
	 *
	 * @param __vid The volume ID.
	 * @param __pid The page ID.
	 * @return The page that was deleted or an error.
	 * @since 2019/06/09
	 */
	int pageDelete(int __vid, int __pid);
	
	/**
	 * Returns the list of pages in the volume.
	 *
	 * @param __vid The volume ID.
	 * @return The list of records, if the volume is not valid then
	 * the first entry will be the error code.
	 * @since 2019/04/15
	 */
	int[] pageList(int __vid);
	
	/**
	 * Returns the ID of the next page ID that might be used.
	 *
	 * @param __vid The volume ID.
	 * @return The next page ID or an error.
	 * @since 2019/06/09
	 */
	int pageNextId(int __vid);
	
	/**
	 * Returns the data of the given page.
	 *
	 * @param __vid The volume ID.
	 * @param __pid The page ID.
	 * @param __b The output buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The number of bytes read or an error otherwise.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	int pageRead(int __vid, int __pid, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Sets a page to the given value.
	 *
	 * @param __vid The volume ID.
	 * @param __pid The page ID.
	 * @param __b The data to store.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @param __tag The tag to identify the given record with.
	 * @return Should be the ID of the same page, otherwise an error.
	 * @throws IndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/09
	 */
	int pageSet(int __vid, int __pid, byte[] __b, int __o, int __l, int __tag)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Returns the size of the given page.
	 *
	 * @param __vid The volume ID.
	 * @param __pid The page ID.
	 * @return The size of the page or an error otherwise.
	 * @since 2019/05/01
	 */
	int pageSize(int __vid, int __pid);
	
	/**
	 * Returns the tag of the page.
	 *
	 * @param __vid The volume ID.
	 * @param __pid The page ID.
	 * @return The tag identifier.
	 * @since 2019/05/13
	 */
	int pageTag(int __vid, int __pid);
	
	/**
	 * Returns the amount of space available for this record.
	 *
	 * @return The available space count.
	 * @since 2019/05/13
	 */
	int vinylSizeAvailable();
	
	/**
	 * Creates a new record.
	 *
	 * @param __sid The suite identifier.
	 * @param __n The name of the suite.
	 * @param __wo Allow write other?
	 * @return The identifier of the suite.
	 * @since 2019/04/14
	 */
	int volumeCreate(long __sid, String __n, boolean __wo);
	
	/**
	 * Returns the list of all available stores.
	 *
	 * @return The list of available stores.
	 * @since 2019/04/14
	 */
	int[] volumeList();
	
	/**
	 * Returns the modification count of the volume.
	 *
	 * @param __vid The volume ID.
	 * @return The modification count or an error.
	 * @since 2019/05/13
	 */
	int volumeModCount(int __vid);
	
	/**
	 * Returns the modification time of the volume.
	 *
	 * @param __vid The volume ID.
	 * @param __time The output time.
	 * @return Zero or an error.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	int volumeModTime(int __vid, long[] __time)
		throws NullPointerException;
	
	/**
	 * Returns the name of the given record.
	 *
	 * @param __vid Volume ID.
	 * @return The name of the record or {@code null} if there is no name.
	 * @since 2019/04/14
	 */
	String volumeName(int __vid);
	
	/**
	 * Returns the suite identifier for the given record.
	 *
	 * @param __vid Volume ID.
	 * @return The suite identifier or {@code 0} if it is not valid.
	 * @since 2019/04/14
	 */
	long volumeSuiteIdentifier(int __vid);
	
	/**
	 * Returns if this volume is other writable.
	 *
	 * @param __vid The volume ID.
	 * @return If it is writable by others.
	 * @since 2019/04/15
	 */
	boolean volumeOtherWritable(int __vid);
}

