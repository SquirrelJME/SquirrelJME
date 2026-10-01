// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.BacklightControl;
import cc.squirreljme.runtime.lcdui.mle.Vibration;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import cc.squirreljme.runtime.nttdocomo.ui.VendorPhoneSystem;
import com.nttdocomo.system.StoreException;
import javax.microedition.lcdui.Displayable;
import org.jetbrains.annotations.Range;

@Api
public class PhoneSystem
{
	/** Maximum time for a steady vibration. */
	private static final int _MAX_VIBRATION_TIME = 2_000;

	/** Communication in progress, area information is currently unavailable. */
	@Api
	public static final int ATTR_AREAINFO_COMMUNICATING = 5;

	/** Device is within FOMA coverage but outside HSDPA coverage. */
	@Api
	public static final int ATTR_AREAINFO_FOMA = 0;

	/** Device is within HSDPA coverage. */
	@Api
	public static final int ATTR_AREAINFO_HSDPA = 1;

	/** Device is outside any coverage. */
	@Api
	public static final int ATTR_AREAINFO_OUTSIDE = 2;

	/** Device is within coverage, but is roaming. */
	@Api
	public static final int ATTR_AREAINFO_ROAMINGOUT = 3;

	/** Device is in self-mode, area information currently unavailable. */
	@Api
	public static final int ATTR_AREAINFO_SELFMODE = 4;

	/** Area information is currently unavailable due to an unknown reason. */
	@Api
	public static final int ATTR_AREAINFO_UNKNOWN = 99;

	/** Backlight is turned off. */
	@Api
	public static final int ATTR_BACKLIGHT_OFF = 0;

	/** Backlight is turned on. */
	@Api
	public static final int ATTR_BACKLIGHT_ON = 1;

	/** Device's battery is currently charging. */
	@Api
	public static final int ATTR_BATTERY_CHARGING = 2;

	/** Device's battery is fully charged. */
	@Api
	public static final int ATTR_BATTERY_FULL = 1;

	/** Device's battery is partially charged. */
	@Api
	public static final int ATTR_BATTERY_PARTIAL = 0;

	/** Device is folded (flip cover closed). */
	@Api
	public static final int ATTR_FOLDING_CLOSE = 0;

	/** Device is not folded (flip cover open). */
	@Api
	public static final int ATTR_FOLDING_OPEN = 1;

	/** There are unread e-mails in the device's e-mail client. */
	@Api
	public static final int ATTR_MAIL_AT_CENTER = 2;

	/** There device has no unread e-mails. */
	@Api
	public static final int ATTR_MAIL_NONE = 0;

	/** The device has unread e-mails. */
	@Api
	public static final int ATTR_MAIL_RECEIVED = 1;

	/** Device's silent mode is disabled. */
	@Api
	public static final int ATTR_MANNER_OFF = 0;

	/** Device's silent mode is enabled. */
	@Api
	public static final int ATTR_MANNER_ON = 1;

	/** There are unread messages in the device's messaging client. */
	@Api
	public static final int ATTR_MESSAGE_AT_CENTER = 2;

	/** There device has no unread messages. */
	@Api
	public static final int ATTR_MESSAGE_NONE = 0;

	/** There device has unread messages. */
	@Api
	public static final int ATTR_MESSAGE_RECEIVED = 1;

	/** The device's screen is not visible to the user. */
	@Api
	public static final int ATTR_SCREEN_INVISIBLE = 0;

	/** The device's screen is visible to the user. */
	@Api
	public static final int ATTR_SCREEN_VISIBLE = 1;

	/** Device is within coverage, deprecated for {@link #DEV_AREAINFO}. */
	@Api
	@ApiDefinedDeprecated
	public static final int ATTR_SERVICEAREA_INSIDE = 11;

	/** Device is outside coverage, deprecated for {@link #DEV_AREAINFO}. */
	@Api
	@ApiDefinedDeprecated
	public static final int ATTR_SERVICEAREA_OUTSIDE = 12;

	/** Device's Surround Sound feature is disabled. */
	@Api
	public static final int ATTR_SURROUND_OFF = 0;

	/** Device's Surround Sound feature is enabled. */
	@Api
	public static final int ATTR_SURROUND_ON = 1;

	/** Vibrator is turned off. */
	@Api
	public static final int ATTR_VIBRATOR_OFF = 0;

	/** Vibrator is turned on. */
	@Api
	public static final int ATTR_VIBRATOR_ON = 1;

	/** Device's area information status */
	@Api
	public static final int DEV_AREAINFO = 11;

	/** Device's surround sound status */
	@Api
	public static final int DEV_AUDIO_SURROUND = 10;

	/** Backlight device control. */
	@Api
	public static final int DEV_BACKLIGHT = 0;

	/** Device's battery level status. */
	@Api
	public static final int DEV_BATTERY = 5;

	/** Device's folding/flip cover status. */
	@Api
	public static final int DEV_FOLDING = 2;

	/** Device's keypad status */
	@Api
	public static final int DEV_KEYPAD = 8;

	/** Device's mailbox status */
	@Api
	public static final int DEV_MAILBOX = 3;

	/** Device's silent mode status */
	@Api
	public static final int DEV_MANNER = 7;

	/** Device's message reception status */
	@Api
	public static final int DEV_MESSAGEBOX = 4;

	/** Device screen visibility status */
	@Api
	public static final int DEV_SCREEN_VISIBLE = 9;

	/** Device coverage status, deprecated for {@code ATTR_AREAINFO_*}. */
	@Api
	@ApiDefinedDeprecated
	public static final int DEV_SERVICEAREA = 6;

	/** Vibrator device control. */
	@Api
	public static final int DEV_VIBRATOR = 1;

	/** The maximum value for optional device attributes */
	@Api
	public static final int MAX_OPTION_ATTR = 255;

	/** The maximum value for vendor-specific device attributes */
	@Api
	public static final int MAX_VENDOR_ATTR = 127;

	/** The minimum value for optional device attributes */
	@Api
	public static final int MIN_OPTION_ATTR = 128;

	/** The minimum value for vendor-specific device attributes */
	@Api
	public static final int MIN_VENDOR_ATTR = 64;

	/** System sound cue indicating an UI alarm. */
	@Api
	public static final int SOUND_ALARM = 3;

	/** System sound cue indicating an UI confirm action. */
	@Api
	public static final int SOUND_CONFIRM = 4;

	/** System sound cue indicating an UI informational popup. */
	@Api
	public static final int SOUND_INFO = 0;

	/** System sound cue indicating an UI error. */
	@Api
	public static final int SOUND_ERROR = 2;

	/** System sound cue indicating an UI warning. */
	@Api
	public static final int SOUND_WARNING = 1;

	/** Theme type that represents an incoming video call. */
	@Api
	public static final int THEME_AV_CALL_IN = 5;

	/** Theme type that represents an alternative incoming video call. */
	@Api
	public static final int THEME_AV_CALLING = 7;

	/** Theme type that represents an incoming voice call. */
	@Api
	public static final int THEME_CALL_IN = 2;

	/** Theme type that represents an outgoing voice call. */
	@Api
	public static final int THEME_CALL_OUT = 1;

	/** Theme type that represents an incoming message. */
	@Api
	public static final int THEME_CHAT_RECEIVED = 6;

	/** Theme type representing that a message, SMS, or e-mail was received. */
	@Api
	public static final int THEME_MESSAGE_RECEIVE = 4;

	/** Theme type representing that a message, SMS, or e-mail was sent. */
	@Api
	public static final int THEME_MESSAGE_SEND = 3;

	/** Theme type representing a standby device screen. */
	@Api
	public static final int THEME_STANDBY = 0;

	/**
	 * Retrieves the current state of the specified device attribute. If a
	 * non-existent attribute is specified, this method returns {@code -1}.
	 *
	 * @param __attr The attribute whose state has to be retrieved.
	 * @throws IllegalStateException In DoJa-3.0 and later if this method is
	 * called from an {@link IApplication#LAUNCHED_AFTER_DOWNLOAD} I-Appli with
	 * an attribute key that results in a {@link SecurityException} if said key
	 * is not present in that app's ADF.
	 * @throws SecurityException If the application that called this method does
	 * not have the specified attribute in its ADF, and is trying to get it
	 * anyway. For DoJa-2.x only, that includes e-mail or message icon
	 * information. For DoJa-3.0 and later it also includes e-mail pictogram
	 * information, messages, battery level and silent mode settings. For
	 * DoJa-5.1 and later, it also includes {@link #DEV_AREAINFO} attributes.
	 * @return The attribute's current state.
	 * @since 2026/08/28
	 */
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public final static int getAttribute(
		@Range(from = 0, to = 255) int __attr)
		throws IllegalStateException, SecurityException
	{
		throw Debugging.todo();
	}

	/**
	 * Returns whether the specified device attribute can be controlled by
	 * {@link PhoneSystem#setAttribute(int, int)} at this point in time.
	 *
	 * @param __attr The attribute whose availability must be checked.
	 * @return If the attribute can be controlled right now.
	 * @since 2026/08/28
	 */
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final boolean isAvailable(
		@Range(from = 0, to = 255) int __attr)
	{
		// Backlight control
		if (__attr == PhoneSystem.DEV_BACKLIGHT)
			return cc.squirreljme.runtime.lcdui.BacklightControl.available();

		// Vibration
		else if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_F503I_SO503I ||
			__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I ||
			__attr == PhoneSystem.DEV_VIBRATOR)
			return cc.squirreljme.runtime.lcdui.mle.Vibration.available();

		// Unsupported??
		throw Debugging.todo("isAvailable Attr = %d", __attr);
	}

	/**
	 * Plays a system-defined sound to indicate confirmations, errors, warnings,
	 * among others.
	 *
	 * @param __type The sound type to play. Valid values are
	 * {@link PhoneSystem#SOUND_ALARM}, {@link PhoneSystem#SOUND_CONFIRM},
	 * {@link PhoneSystem#SOUND_INFO}, {@link PhoneSystem#SOUND_ERROR}, and
	 * {@link PhoneSystem#SOUND_WARNING}.
	 * @throws IllegalArgumentException If {@code __type} is not a valid system
	 * sound.
	 * @throws IllegalStateException If this method is called during a phone
	 * call on DoJa 2.1 and later; or if the music player is playing in priority
	 * mode in DoJa 5.0 and later.
	 * @throws UIException If this method is called during a Push-to-talk call
	 * on DoJa-4.1 and later.
	 * @since 2026/08/28
	 */
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final void playSound(
		@Range(from = PhoneSystem.SOUND_INFO, to = PhoneSystem.SOUND_CONFIRM)
		int __type)
		throws IllegalArgumentException, IllegalStateException, UIException
	{
		/* {@squirreljme.error AH20 Invalid PhoneSystem sound value.} */
		if (__type < SOUND_INFO || __type > SOUND_CONFIRM)
			throw new IllegalArgumentException("AH20");

		// TODO: If ScritchUI is available, call the environment method to play
		// TODO: system-specific sounds, otherwise do nothing.
		throw Debugging.todo();
    }

	/**
	 * Allows controlling device resources such as vibration and backlight. If
	 * the resource cannot be controlled at this time, the request is ignored
	 * and no changes take place.
	 *
	 * @param __attr The device attribute to act upon.
	 * @param __value The value that the attribute should be set to.
	 * @throws IllegalArgumentException If {@code __value} is not a valid value
	 * for the specified {@code __attr} argument on DoJa-2.0 and later.
	 * @since 2021/11/30
	 */
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final void setAttribute(@Range(from = 0, to = 255) int __attr,
		int __value)
		throws IllegalArgumentException
	{
		// Backlight control
		if (__attr == PhoneSystem.DEV_BACKLIGHT)
		{
			// Change level
			if (__value == PhoneSystem.ATTR_BACKLIGHT_ON)
				BacklightControl.setLevel(BacklightControl.MAX_LEVEL);
			else if (__value == PhoneSystem.ATTR_BACKLIGHT_OFF)
				BacklightControl.setLevel(BacklightControl.MIN_LEVEL);

			// Some software such as Final Fantasy 1 for DoJa sets the
			// backlight constantly every frame to force the screen to refresh,
			// despite there being repaint()
			javax.microedition.lcdui.Display display = Display.__midpDisplay();
			if (display != null)
			{
				Displayable current = display.getCurrent();
				if (current instanceof javax.microedition.lcdui.Canvas)
					((javax.microedition.lcdui.Canvas)current).repaint();
			}

			return;
		}

		// Vibration
		else if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_F503I_SO503I ||
			__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I ||
			__attr == PhoneSystem.DEV_VIBRATOR)
		{
			// Vibration is not available before DoJa 2.0, if the standard
			// device vibration is selected then just ignore it
			if (__attr == PhoneSystem.DEV_VIBRATOR &&
				DoJaRuntime.versionBefore(2, 0))
				return;

			// Different phones have different means of turning on the shake
			boolean isOn;
			if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I ||
				__attr == PhoneSystem.DEV_VIBRATOR)
				isOn = (__value == PhoneSystem.ATTR_VIBRATOR_ON);
			else
				isOn = (__value == PhoneSystem.ATTR_VIBRATOR_ON ||
					__value == 64);

			// Perform the vibration
			Vibration.vibrate((isOn ? PhoneSystem._MAX_VIBRATION_TIME : 0));

			return;
		}

		// Unsupported??
		throw Debugging.todo("Attribute %d = %d", __attr, __value);
	}

	/**
	 * Sets an image, a video-only media track, or avatar data to be displayed
	 * for incoming and outgoing calls. These changes take effect on the system
	 * level and persist even after the application that called this method is
	 * closed. The entry ID of the media to be set can be retrieved with
	 * {@link ImageStore#addEntry(MediaImage)}, {@link ImageStore#getId()},
	 * or {@link ImageStore#selectEntryId()}.
	 *
	 * @param __target The target theme to apply the media to.
	 * @param __id The entry ID of the media to set.
	 * @throws IllegalArgumentException If {@code __target} is not a valid
	 * target theme.
	 * @throws IllegalStateException If this method is called while the
	 * application is on the background or the device is in standby.
	 * @throws SecurityException If the application does not have permission to
	 * manage settings.
	 * @throws StoreException If {@code __id} points to non-existent media data;
	 * or if {@code __id} itself is not within the [-256, 0] range.
	 * @throws UIException If the target does not support the format of the
	 * specified media, if its width or height are outside of the allowed range;
	 * or if the media's data size exceeds the device's limits; or if the media
	 * is a video file that has more than just a video-only track. In all of
	 * these cases, an {@link UIException#UNSUPPORTED_FORMAT} exception is
	 * thrown.
	 * @since 2026/08/28
	 */
	@Api
	public static void setImageTheme(
		@Range(from = PhoneSystem.THEME_STANDBY,
			to = PhoneSystem.THEME_AV_CALLING) int __target,
		@Range(from = 0, to = Integer.MAX_VALUE) int __id)
		throws StoreException
	{
		/* {@squirreljme.error AH21 Invalid PhoneSystem theme target.} */
		if (__target < PhoneSystem.THEME_STANDBY ||
			__target > PhoneSystem.THEME_AV_CALLING)
			throw new IllegalArgumentException("AH21");

		throw Debugging.todo();
	}

	/**
	 * Sets a sound or a video with audio-only tracks, for incoming and outgoing
	 * calls. These changes take effect on the system level and persist even
	 * after the application that called this method is closed. The entry ID of
	 * the media to be set may be retrieved with
	 * {@link SoundStore#addEntry(MediaSound)}.
	 *
	 * @param __target The target theme to apply the media to.
	 * @param __id The entry ID of the media to set.
	 * @throws IllegalArgumentException If {@code __target} is not a valid
	 * target theme.
	 * @throws IllegalStateException If this method is called while the
	 * application is on the background or the device is in standby.
	 * @throws SecurityException If the application does not have permission to
	 * manage settings.
	 * @throws StoreException If {@code __id} points to non-existent media data,
	 * or if {@code __id} itself is not within the [-256, 0] range.
	 * @throws UIException If the target does not support the format of the
	 * specified media; or if the media's data size exceeds the device's limits;
	 * or if the media is a video file that has more than just an audio-only
	 * track. In all of these cases, an {@link UIException#UNSUPPORTED_FORMAT}
	 * exception is thrown.
	 * @since 2026/08/28
	 */
	@Api
	public static void setSoundTheme(
		@Range(from = PhoneSystem.THEME_STANDBY,
			to = PhoneSystem.THEME_AV_CALLING) int __target,
		@Range(from = 0, to = Integer.MAX_VALUE) int __id)
		throws StoreException
	{
		/* {@squirreljme.error AH21 Invalid PhoneSystem theme target.} */
		if (__target < PhoneSystem.THEME_STANDBY ||
			__target > PhoneSystem.THEME_AV_CALLING)
			throw new IllegalArgumentException("AH21");

		throw Debugging.todo();
	}

	/**
	 * Sets a video to be displayed for incoming and outgoing calls. These
	 * changes take effect on the system level and persist even after the
	 * application that called this method is closed. The video file must
	 * contain both a video and an audio track. Their presence can be checked by
	 * calliing {@link MediaResource#getProperty(String)} with
	 * {@link MediaImage#MP4_VIDEOTRACK} and {@link MediaImage#MP4_AUDIOTRACK}
	 * as arguments.
	 *
	 * @param __target The target theme to apply the media to.
	 * @param __id The entry ID of the media to set.
	 * @throws IllegalArgumentException If {@code __target} is not a valid
	 * target theme.
	 * @throws IllegalStateException If this method is called while the
	 * application is on the background or the device is in standby.
	 * @throws SecurityException If the application does not have permission to
	 * manage settings.
	 * @throws StoreException If {@code __id} points to non-existent media data,
	 * or if {@code __id} itself is not within the [-256, 0] range.
	 * @throws UIException If the target does not support the format of the
	 * specified video; or if its width or height are outside of the allowed
	 * range; or if the video's data size exceeds the device's limits; or if the
	 * video does not have both a video and an audio track; or if the video has
	 * a text track. In all of these cases, an
	 * {@link UIException#UNSUPPORTED_FORMAT} exception is thrown.
	 * @since 2026/08/28
	 */
	@Api
	public static void setMovieTheme(
		@Range(from = PhoneSystem.THEME_STANDBY,
			to = PhoneSystem.THEME_AV_CALLING) int __target,
		@Range(from = 0, to = Integer.MAX_VALUE) int __id)
		throws StoreException
	{
		/* {@squirreljme.error AH21 Invalid PhoneSystem theme target.} */
		if (__target < PhoneSystem.THEME_STANDBY ||
			__target > PhoneSystem.THEME_AV_CALLING)
			throw new IllegalArgumentException("AH21");

		throw Debugging.todo();
	}
}
