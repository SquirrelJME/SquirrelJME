// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * Abstract implementation of volume control.
 *
 * @since 2025/06/03
 */
@SquirrelJMEVendorApi
public class AbstractVolumeControl
	extends AbstractControl<VolumeControl>
	implements VolumeControl
{
	/** The player to reference. */
	@SquirrelJMEVendorApi
	protected final Reference<AbstractPlayer> player;
	
	/** The last volume set. */
	private volatile int _volume =
		100;
	
	/** The last mute set. */
	private volatile boolean _muted =
		false;
	
	/** The last logical volume set. */
	private volatile int _logical =
		100;
	
	/**
	 * AbstractVolumeControl the volume control.
	 *
	 * @param __player The player to reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public AbstractVolumeControl(AbstractPlayer __player)
		throws NullPointerException
	{
		super(VolumeControl.class);
		
		if (__player == null)
			throw new NullPointerException("NARG");
		
		this.player = new WeakReference<>(__player); 
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public final int getLevel()
	{
		return this._volume;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public final boolean isMuted()
	{
		return this._muted;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public final int setLevel(int __volume)
	{
		// Use a default level if GCed
		AbstractPlayer player = this.player.get();
		if (player == null)
			return 100;
		
		// Clamp
		if (__volume < 0 || __volume > 100)
			__volume = Math.max(0, Math.min(100, __volume));
		
		// Set new volume
		synchronized (this)
		{
			// Set new volume
			this._volume = __volume;
			
			// Set logical volume
			this.__logical(__volume);
			return __volume;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public final void setMute(boolean __muted)
	{
		// Use a default mute if GCed
		AbstractPlayer player = this.player.get();
		if (player == null)
			return;
		
		// Set mute flag
		this._muted = __muted;
		
		// Set logical volume
		this.__logical((__muted ? 0 : this._volume));
	}
	
	/**
	 * Sets the logical volume.
	 *
	 * @param __volume The logical volume.
	 * @since 2025/06/03
	 */
	private void __logical(int __volume)
	{
		// Ignore if GCed
		AbstractPlayer player = this.player.get();
		if (player == null)
			return;
		
		// Did the logical volume actually change?
		int lastLogical = this._logical;
		if (lastLogical == __volume)
			return;
		
		// Tell the player to use this volume
		this._logical = __volume;
		player.useVolume(__volume);
		
		// Dispatch volume change event
		player.dispatchEvent(PlayerListener.VOLUME_CHANGED, __volume);
	}
}
