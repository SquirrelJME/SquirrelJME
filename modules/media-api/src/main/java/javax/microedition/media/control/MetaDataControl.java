// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;

/**
 * This control is used to access metadata which contains any author or
 * other internal data that is not specific to the music.
 *
 * @since 2026/06/10
 */
@Api
public interface MetaDataControl
	extends Control
{
	/** Author. */
	@Api
	String AUTHOR_KEY =
		"author";
	
	/** Copyright. */
	@Api
	String COPYRIGHT_KEY =
		"copyright";
	
	/** Date. */
	@Api
	String DATE_KEY =
		"date";
	
	/** Title. */
	@Api
	String TITLE_KEY =
		"title";
	
	/**
	 * Returns the value of the specified key.
	 *
	 * @param __k The key to obtain.
	 * @return The value of the specified key.
	 * @throws IllegalArgumentException If the key is {@code null} or if the
	 * key is not valid.
	 * @since 2026/06/10
	 */
	@Api
	String getKeyValue(String __k)
		throws IllegalArgumentException;
	
	/**
	 * Returns all the keys which are valid.
	 *
	 * @return All the keys which are valid.
	 * @since 2026/06/10
	 */
	@Api
	String[] getKeys();
}


