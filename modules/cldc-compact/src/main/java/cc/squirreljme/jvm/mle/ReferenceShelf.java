// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;

/**
 * This provides the interface for references which are used to weakly refer
 * to them, so that they may be collected or act as a cache.
 *
 * @see RefLinkBracket
 * @since 2020/05/30
 */
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
	 * Gets the object this points to.
	 *
	 * @param __link The link to get the object of.
	 * @return The object that this points to, or {@code null} if there
	 * is no pointed object.
	 * @since 2020/05/30
	 */
	public static native Object linkGet(RefLinkBracket __link);
	
	/**
	 * Returns the link after the specified one.
	 *
	 * @param __link The link.
	 * @return The next link or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	public static native RefLinkBracket linkNext(RefLinkBracket __link);
	
	/**
	 * Returns the link before the specified one.
	 *
	 * @param __link The link.
	 * @return The previous link or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	public static native RefLinkBracket linkPrev(RefLinkBracket __link);
	
	/**
	 * Sets the object that this points to.
	 *
	 * @param __link The link to be given the object.
	 * @param __v The object to set to, may be {@code null}.
	 * @since 2020/05/30
	 */
	public static native void linkSet(RefLinkBracket __link, Object __v);
	
	/**
	 * Gets the link of an object.
	 *
	 * @param __o The object to get the link of.
	 * @return The link of the object or {@code null} if there is none.
	 * @since 2020/05/30
	 */
	public static native RefLinkBracket objectGet(Object __o);
	
	/**
	 * Sets the link of the object.
	 *
	 * @param __o The object to set the link of.
	 * @param __link The link to set to it.
	 * @since 2020/05/30
	 */
	public static native void objectSet(Object __o, RefLinkBracket __link);
}
