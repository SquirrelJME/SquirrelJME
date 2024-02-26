// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.ui.AudioPresenter;
import com.nttdocomo.ui.MediaSound;

/**
 * Nullary audio presenter which generates no sound.
 *
 * @since 2024/01/14
 */
public class NullAudioPresenter
	extends AudioPresenter
{
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void play()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setAttribute(int __attribute, int __value)
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setSound(MediaSound __data)
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void stop()
	{
		// Does nothing
	}
}
