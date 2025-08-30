// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface MediaPresenter
{
	/**
	 * Returns the currently playing media.
	 *
	 * @return The currently playing media.
	 * @since 2025/06/07
	 */
	@Api
	MediaResource getMediaResource();
	
	@Api
	void play();
	
	@Api
	void setAttribute(int __attribute, int __value);
	
	/**
	 * Sets the media listener to use when the state of media changes or
	 * another media action occurs.
	 * 
	 * Only a single listener may be set for a given presenter.
	 *
	 * @param __listener The listener to use, {@code null} clears it.
	 * @since 2025/05/04
	 */
	@Api
	void setMediaListener(MediaListener __listener);
	
	@Api
	void stop();
	
}
