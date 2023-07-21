// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * This provides the interface for references which are used to weakly refer
 * to them, so that they may be collected or act as a cache.
 *
 * @see RefLinkBracket
 * @since 2020/05/30
 */
@SquirrelJMEVendorApi
public final class ReferenceShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/05/30
	 */
	private ReferenceShelf()
	{
	}
	
	/**
	 * Deletes the reference link, freeing any associated memory.
	 *
	 * @param __link The link to delete.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void deleteLink(@NotNull RefLinkBracket __link);
	
	/**
	 * Chains this link into the given object atomically.
	 * 
	 * @param __thisLink The link to chain.
	 * @param __forObject The object to chain into.
	 * @throws MLECallError On null arguments.
	 * @since 2022/09/01
	 */
	@SquirrelJMEVendorApi
	public static native void linkChain(@NotNull RefLinkBracket __thisLink,
		@NotNull Object __forObject)
		throws MLECallError;
	
	/**
	 * Returns the link after the specified one.
	 *
	 * @param __link The link.
	 * @return The next link or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native RefLinkBracket linkGetNext(
		@NotNull RefLinkBracket __link);
	
	/**
	 * Gets the object this points to.
	 *
	 * @param __link The link to get the object of.
	 * @return The object that this points to, or {@code null} if there
	 * is no pointed object.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native Object linkGetObject(@NotNull RefLinkBracket __link);
	
	/**
	 * Returns the link before the specified one.
	 *
	 * @param __link The link.
	 * @return The previous link or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native RefLinkBracket linkGetPrev(
		@NotNull RefLinkBracket __link);
	
	/**
	 * Sets the link that is after this one
	 *
	 * @param __link The link.
	 * @param __next The new link to set, may be {@code null} to clear.
	 * @deprecated Do not use.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	@Deprecated
	public static native void linkSetNext(@NotNull RefLinkBracket __link,
		RefLinkBracket __next);
	
	/**
	 * Sets the object that this points to.
	 *
	 * @param __link The link to be given the object.
	 * @param __v The object to set to, may be {@code null}.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void linkSetObject(@NotNull RefLinkBracket __link,
		Object __v);
	
	/**
	 * Sets the link that is before this one.
	 *
	 * @param __link The link.
	 * @param __prev The new link to set, may be {@code null} to clear.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void linkSetPrev(@NotNull RefLinkBracket __link,
		RefLinkBracket __prev);
	
	/**
	 * Unchains the given link from the previous and next.
	 * 
	 * @param __link The link to unchain.
	 * @throws MLECallError If the links could not be unchained.
	 * @since 2022/09/01
	 */
	@SquirrelJMEVendorApi
	public static native void linkUnchain(@NotNull RefLinkBracket __link)
		throws MLECallError;
	
	/**
	 * Unlinks and clears the links.
	 * 
	 * @param __link The link to clear.
	 * @throws MLECallError If the link is null or could not be unchained.
	 * @since 2022/10/08
	 */
	@SquirrelJMEVendorApi
	public static native void linkUnlinkAndClear(
		@NotNull RefLinkBracket __link)
		throws MLECallError;
	
	/**
	 * Creates a new reference link.
	 *
	 * @return The newly created reference link.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native RefLinkBracket newLink();
	
	/**
	 * Gets the link of an object.
	 *
	 * @param __o The object to get the link of.
	 * @return The link of the object or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native RefLinkBracket objectGet(@NotNull Object __o);
	
	/**
	 * Sets the link of the object.
	 *
	 * @param __o The object to set the link of.
	 * @param __link The link to set to it.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void objectSet(@NotNull Object __o,
		RefLinkBracket __link);
}
