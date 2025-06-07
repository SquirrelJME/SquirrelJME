// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.nttdocomo.io.ConnectionException;

@Api
public interface MediaResource
{
	/**
	 * Disposes of the resource so that it no longer can be used.
	 *
	 * @throws UIException If the resource has already been disposed.
	 * @since 2022/02/14
	 */
	@Api
	void dispose()
		throws UIException;
	
	/**
	 * Retrieves a property associated with the given key.
	 *
	 * @param __key The key to obtain the property of.
	 * @return The value of the given key, returns {@code null} if there is
	 * no value set or the key is unknown.
	 * @throws IllegalArgumentException If the key is empty.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the resource is not currently in use.
	 * @since 2025/05/05
	 */
	@Api
	String getProperty(String __key)
		throws IllegalArgumentException, NullPointerException, UIException;
	
	/**
	 * Returns whether the media can be sent to external applications.
	 *
	 * @return If the media can be sent externally.
	 * @throws UIException If the media is not in use, or if the data is
	 * streamed and the data is incomplete.
	 * @since 2025/05/05
	 */
	@Api
	boolean isRedistributable()
		throws UIException;
	
	/**
	 * Sets the key to the given value.
	 * 
	 * If a key is not supported, then this will have no effect.
	 *
	 * @param __key The key to set.
	 * @param __value The value to set, {@code null} will delete the key.
	 * @throws IllegalArgumentException If the key is blank.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the media is not in the use state.
	 * @since 2025/05/05
	 */
	@Api
	void setProperty(String __key, String __value)
		throws IllegalArgumentException, NullPointerException, UIException;
	
	/**
	 * Sets whether the media can be sent to external applications.
	 *
	 * @param __canRedistribute If the media can be sent externally.
	 * @return If redistribution was successfully set or no change was
	 * needed.
	 * @throws UIException If the media is not in use, or if the data is
	 * streamed and the data is incomplete.
	 * @since 2025/05/05
	 */
	@Api
	boolean setRedistributable(boolean __canRedistribute)
		throws UIException;
	
	/**
	 * Specifies that the media should stop being used and frees up
	 * resources for it. Note that the actual resource in its original
	 * location
	 * is retained for the next {@link #use()} of which this becomes
	 * available.
	 *
	 * @throws UIException If the resource could not be freed.
	 * @since 2022/02/14
	 */
	@Api
	@SuppressWarnings("SpellCheckingInspection")
	void unuse()
		throws UIException;
	
	/**
	 * Specifies that the resource is to be used now, it will be loaded from
	 * the backing resource and will hold a representation of the data. This
	 * in a sense opens the data.
	 *
	 * @throws ConnectionException If the connection could not be made.
	 * @throws SecurityException If the operation is not supported due to
	 * a security mechanism.
	 * @throws UIException If the resource could not be opened.
	 * @since 2022/02/14
	 */
	@Api
	void use()
		throws ConnectionException, SecurityException, UIException;
	
	/**
	 * Specifies that the resource is to be used now, it will be loaded from
	 * the backing resource and will hold a representation of the data. This
	 * in a sense opens the data.
	 *
	 * @param __replaceWith The resource to replace this with, must be of the
	 * same type or {@code null}.
	 * @param __onlyOnce Can this only be used once if replaced?
	 * @throws ConnectionException If the connection could not be made.
	 * @throws SecurityException If the operation is not supported due to
	 * a security mechanism.
	 * @throws UIException If the resource could not be opened.
	 * @since 2022/02/14
	 */
	@Api
	void use(MediaResource __replaceWith, boolean __onlyOnce)
		throws ConnectionException, SecurityException, UIException;
}
