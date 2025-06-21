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
}
