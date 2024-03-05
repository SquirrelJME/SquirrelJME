// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.media.control.VolumeControl;

/**
 * This is a volume control which does nothing.
 *
 * @since 2019/06/29
 */
@SquirrelJMEVendorApi
public final class NullVolumeControl
	implements VolumeControl
{
	/** Current volume level. */
	private int _level =
		100;
	
	/** Is muted? */
	private boolean _mute;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int getLevel()
	{
		return this._level;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	@SquirrelJMEVendorApi
	public final boolean isMuted()
	{
		return this._mute;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int setLevel(int __v)
	{
		this._level = (__v = (__v < 0 ? 0 : (Math.min(__v, 100))));
		return __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void setMute(boolean __v)
	{
		this._mute = __v;
	}
}

