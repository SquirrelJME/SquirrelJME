// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.samsung.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Samsung API for audio playback, supports MP3, MIDI, and MMF/SMAF media.
 * 
 * As opposed to some vendor-specific APIs like SKT's, Samsung's AudioClip has
 * non-blocking audio playback.
 *
 * @since 2026/04/07
 */
@Api
public class AudioClip
{
	/** Constant that depicts that media is of the MMF/SMAF audio format. */
	@Api
	public static final int TYPE_MMF = 1;

	/** Constant that depicts that media is of the MP3 audio format. */
	@Api
	public static final int TYPE_MP3 = 2;

	/** Constant that depicts that media is of the MIDI audio format. */
	@Api
	public static final int TYPE_MIDI = 3;

	/** Actual Player for MIDI, SMAF and MP3 audio. */
	@SquirrelJMEVendorApi
	volatile Player _player;

	/** Parsed player format, used to adjust internal playback logic. */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = AudioClip.class)
	private int _playerFormat;

	/**
	 * Creates a new AudioClip with the given media type. Data is read from the
	 * {@code __audioData} array starting from {@code __audioOffset}, with its
	 * size being dictated by {@code __audioLength}.
	 *
	 * @param __type The audio format in {@code __audioData}, usually should be
	 * one of {@link AudioClip#TYPE_MMF}, {@link AudioClip#TYPE_MP3}, or
	 * {@link AudioClip#TYPE_MIDI}; however, apps that actually use
	 * {@link AudioClip#TYPE_MMF} tend to go out of spec here and send wrong
	 * values. Thus, this implementation manually checks the incoming data type.
	 * @param __audioData The data array to prepare for playback
	 * @param __audioOffset The initial position of the audio data within
	 * {@code __audioData} to load.
	 * @param __audioLength The size of the audio data within
	 * {@code __audioData} to load.
	 * @throws ArrayIndexOutOfBoundsException If {@code __audioOffset} or
	 * {@code __audioLength} resolve to an out of bounds access on the
	 * {@code __audioData} array.
	 * @throws IllegalArgumentException If {@code __type} is invalid, though
	 * wonky MMF usage on some jars pretty much means that this will never
	 * be thrown, as they pass anything for {@code __type} value.
	 * @throws NullPointerException If {@code __audioData} is null.
	 * @since 2026/04/07
	 */
	@Api
	public AudioClip(
		@MagicConstant(valuesFromClass = AudioClip.class) int __type,
		@NotNull byte[] __audioData,
		@Range(from = 0, to = Integer.MAX_VALUE) int __audioOffset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __audioLength)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		if (__audioData == null)
			throw new NullPointerException("NARG");

		if (__audioOffset < 0 || __audioLength < 0 ||
			__audioOffset + __audioLength < 0 ||
			__audioOffset + __audioLength > __audioData.length)
			throw new ArrayIndexOutOfBoundsException("OOB");

		try
		{
			this.__initialize(new ByteArrayInputStream(__audioData,
				__audioOffset, __audioLength));
		}

		// IOExceptions shouldn't happen here
		catch (IOException e)
		{
			throw Debugging.oops();
		}
	}

	/**
	 * Creates a new AudioClip with the given media type. Data is read from
	 * {@code __rcName}, which must be a valid resource.
	 *
	 * @param __type The audio format in {@code __audioData}, usually should be
	 * one of {@link AudioClip#TYPE_MMF}, {@link AudioClip#TYPE_MP3}, or
	 * {@link AudioClip#TYPE_MIDI}; however, apps that actually use
	 * {@link AudioClip#TYPE_MMF} tend to go out of spec here and send wrong
	 * values. Thus, values out of range are resolved to MMF.
	 * @param __rcName The location and name of the data resource that must
	 * be loaded.
	 * @throws IllegalArgumentException If {@code __type} is invalid, though
	 * wonky MMF usage on some jars pretty much means that this will never
	 * be thrown, as they pass anything for {@code __type} value.
	 * @throws IOException If the data given by {@code __rcName} does not
	 * exist.
	 * @throws NullPointerException If {@code __rcName} is null.
	 * @since 2026/04/07
	 */
	@Api
	public AudioClip(
		@MagicConstant(valuesFromClass = AudioClip.class) int __type,
		@NotNull String __rcName)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__rcName == null)
			throw new NullPointerException("NARG");

		try (InputStream input = ActiveMidlet.get().getClass().
			getResourceAsStream(__rcName))
		{
			this.__initialize(input);
		}
	}

	/**
	 * Returns whether the device supports AudioClip.
	 *
	 * @return Whether AudioClip is supported.
	 * @since 2026/04/07
	 */
	@Api
	public static boolean isSupported()
	{
		return cc.squirreljme.runtime.media.AudioSystem.available();
	}

	/**
	 * Pauses the currently playing AudioClip. If the clip is already paused, or
	 * has already been closed, this call does nothing.
	 *
	 * @throws IllegalStateException If this device does not support AudioClip.
	 * @since 2026/04/07
	 */
	@Api
	public void pause()
		throws IllegalStateException
	{
		/* {@squirreljme.error SS3u AudioClip is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS3u");

		synchronized (this)
		{
			// Pause is pretty much equivalent to MIDP2's Player.stop()
			Player player = this._player;
			if (player == null || player.getState() <= Player.PREFETCHED)
				return;

			try
			{
				player.stop();
			}

			// We can't throw MediaException in AudioClip (it shouldn't happen
			// anyway), so we must check why it happened, and fix it.
			catch (MediaException e)
			{
				throw Debugging.oops();
			}
		}
	}

	/**
	 * Starts playing this AudioClip, only one AudioClip is played at a time.
	 * The amount of loops are given by {@code __loop}, and the playback volume
	 * is given by {@code __volume}.
	 *
	 * @param __loop The amount of loops to be done. The official spec allows
	 * a 0 to 255 range (0 meaning no loops); however, apps that use MMF often
	 * use the same range allowed by MIDP2's {@link Player}, thus both kinds are
	 * allowed here.
	 * @param __volume The volume that this AudioClip should play with. The
	 * official spec allows a 0 to 5 range; however, apps that use MMF often
	 * use the same range allowed by MIDP2's {@link Player}, thus both kinds are
	 * allowed here.
	 * @throws IllegalArgumentException If {@code __loop} or {@code __volume}
	 * are invalid.
	 * @throws IllegalStateException If this device does not support AudioClip.
	 * @since 2026/04/07
	 */
	@Api
	public void play(
		@Range(from = -1, to = Integer.MAX_VALUE) int __loop,
		@Range(from = 0, to = 100) int __volume)
		throws IllegalArgumentException, IllegalStateException
	{
		/* {@squirreljme.error SS3u AudioClip is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS3u");

		int playerFormat = this._playerFormat;

		// MMF apparently accepts looping to -1 in AudioClip. Not stated on the
		// documentation, but some jars like ClickMan use it for MMF files.
		// TODO: Add a compatibility flag for MMF AudioClip quirks.
		if (__loop < ((playerFormat == TYPE_MMF) ? -1 : 0) || __loop > 255 ||
			__volume < 0 || __volume > ((playerFormat == TYPE_MMF) ? 100 : 5))
			throw new IllegalArgumentException("INVARG");

		synchronized (this)
		{
			try
			{
				Player player = this._player;

				if (player.getState() == Player.STARTED)
					player.stop();

				// Received volume varies from 1 to 5 normally, so adapt to
				// 20-100, except for MMF of course, where Snowball Fight among
				// other jars use the 0-100 range, which is not covered by the
				// documentation.
				// TODO: Add a compatibility flag for MMF AudioClip quirks.
				this.__setVolume((playerFormat == TYPE_MMF) ? (__volume <= 5 ?
					__volume * 20 : __volume) : __volume * 20);

				// Treat 0 and 255 loops as infinite looping to support both MMF
				// and MIDI/MP3 AudioClip conventions.
				// TODO: Add a compatibility flag for MMF AudioClip quirks.
				player.setLoopCount((__loop == 255 || __loop == 0) ? -1 :
					__loop);

				// play() always plays media from beginning, like Nokia Sound
				player.setMediaTime(0);
				player.start();
			}
			catch (MediaException e)
			{
				throw Debugging.oops();
			}
		}
	}

	/**
	 * Resumes a previously paused AudioClip. Effectively the same as calling
	 * {@link Player#start()} in MIDP2. If the player is already running, or
	 * has already reached End-Of-Media, this call does nothing.
	 *
	 * @throws IllegalStateException If this device does not support AudioClip.
	 * @since 2026/04/07
	 */
	@Api
	public void resume()
		throws IllegalStateException
	{
		/* {@squirreljme.error SS3u AudioClip is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS3u");

		synchronized (this)
		{
			Player player = this._player;

			// Resume only if paused, AND its current saved position is not at
			// the end of the media. Otherwise, this may result in infinite
			// playback loops. Otherwise, it's pretty much just a standard MIDP2
			// Player.start() call.
			if (player.getState() != Player.PREFETCHED ||
				(player.getMediaTime() >= player.getDuration()))
				return;

			try
			{
				player.start();
			}
			catch (MediaException e)
			{
				throw Debugging.oops();
			}
		}
	}

	/**
	 * Stops an AudioClip and releases all of its resources, effectively
	 * deallocating it by MIDP2's {@link Player} convention. Note that this
	 * implementation does not actually deallocate data here, instead, releasing
	 * data is only done in {@link AudioClip#__close()} as memory savings are
	 * likely not worth the burden of constantly de/re-allocating data on apps
	 * that routinely call for {@link AudioClip#play(int, int)} and
	 * {@link AudioClip#stop()}, instead, the media marker is just moved back
	 * to the beginning.
	 *
	 * @throws IllegalStateException If this device does not support AudioClip.
	 * @since 2026/04/07
	 */
	@Api
	public void stop()
		throws IllegalStateException
	{
		/* {@squirreljme.error SS3u AudioClip is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS3u");

		synchronized (this)
		{
			Player player = this._player;
			if (player == null || player.getState() <= Player.PREFETCHED)
				return;

			try
			{
				player.stop();

				// AudioClip stop forces the media time to go back to the start
				player.setMediaTime(0);
			}
			catch (MediaException e)
			{
				throw Debugging.oops();
			}
		}
	}

	/**
	 * Releases the data currently held by this AudioClip. If the clip is
	 * already closed, or has not been created, this call does nothing.
	 *
	 * @since 2026/04/07
	 */
	@SquirrelJMEVendorApi
	void __close()
	{
		synchronized (this)
		{
			Player player = this._player;

			if (player == null ||
				player.getState() <= Player.CLOSED)
				return;

			if (player.getState() >= Player.STARTED)
			{
				try
				{
					player.stop();
				}
				catch (MediaException e)
				{
					throw Debugging.oops();
				}
			}

			player.close();
		}
	}

	/**
	 * Initializes the AudioClip with the received audio data.
	 *
	 * @param __input The audio data to use for initialization
	 * @throws IllegalArgumentException If the resolved media type is not one of
	 * {@link AudioClip#TYPE_MMF}, {@link AudioClip#TYPE_MP3} or
	 * {@link AudioClip#TYPE_MIDI}.
	 * @throws IOException If {@code __input} could not be opened.
	 * @since 2026/04/07
	 */
	@SquirrelJMEVendorApi
	private void __initialize(@NotNull InputStream __input)
		throws IllegalArgumentException, IOException
	{
		/* {@squirreljme.error SS3n received InputStream is invalid.} */
		if (__input == null)
			throw new IOException("SS3n");

		// Some jars actually try to pass streams with a different clip type
		// from what they should be, so check their header and ignore whatever
		// the jar is passing here. A few Sonic 1 versions especially love to
		// pass MIDI as MP3 here for whatever reason. Samsung really should have
		// enforced this part of the spec more heavily, but seems like their
		// actual devices do something similar to this, as the apps work fine
		// on them.
		String format = ContentTypeUtil.guess(__input);
		int type = -1;

		if (format.equals("application/x-smaf"))
			type = TYPE_MMF;

		else if (format.equals("audio/midi"))
			type = TYPE_MIDI;

		else if (format.equals("audio/mpeg"))
			type = TYPE_MP3;

		/* {@squirreljme.error SS3i invalid media type for AudioClip.} */
		else
			throw new IllegalArgumentException("SS3i");


		synchronized (this)
		{
			try
			{
				Player player = this._player;
				if (player != null)
					this.__close();

				// Offload actual player creation to MIDP2's Manager, Samsung
				// doesn't have any proprietary formats like Nokia or Siemens.
				player = Manager.createPlayer(__input, format);

				this._playerFormat = type;

				// AudioClip is initialized in a state that's ready to play
				player.prefetch();

				this._player = player;
			}
			catch (MediaException e)
			{
				throw Debugging.oops();
			}
		}
	}

	/**
	 * Sets the player's volume before playback.
	 *
	 * @param __volume The volume to be set.
	 * @since 2026/04/07
	 */
	@SquirrelJMEVendorApi
	private void __setVolume(
		@Range(from = 0, to = 100) int __volume)
	{
		synchronized (this)
		{
			Player player = this._player;

			if (player == null)
				return;

			VolumeControl control = (VolumeControl)player
				.getControl("VolumeControl");
			if (control != null)
				control.setLevel(__volume);
		}
	}
}
