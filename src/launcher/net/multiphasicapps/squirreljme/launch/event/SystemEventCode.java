// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.event;

/**
 * The list of system event codes that may occur.
 *
 * @since 2016/05/15
 */
public interface SystemEventCode
{
	/** A controller was attached to a port. */
	public static final int CONTROLLER_ATTACHED =
		1;
	
	/** A controller was removed from a port. */
	public static final int CONTROLLER_DETACHED =
		2;
	
	/** A block device was inserted. */
	public static final int BLOCK_DEVICE_INSERTED =
		3;
	
	/** A block device was removed. */
	public static final int BLOCK_DEVICE_REMOVED =
		4;
	
	/** The system is about to enter low power mode. */
	public static final int ENTERING_LOW_POWER_MODE =
		5;
	
	/** The system is thinking about entering low power mode. */
	public static final int DECIDING_ON_LOW_POWER_MODE =
		6;
	
	/** The system time has be changed (by the user). */
	public static final int CLOCK_CHANGED =
		7;
	
	/** HotSync operation is about to start. */
	public static final int HOTSYNC_STARTING =
		8;
	
	/** HotSync operation has complete. */
	public static final int HOTSYNC_FINISHED =
		9;
	
	/** The system has been locked. */
	public static final int SYSTEM_LOCKED =
		10;
	
	/** The system has been unlocked. */
	public static final int SYSTEM_UNLOCKED =
		11;
	
	/** The screen saver started. */
	public static final int SCREEN_SAVER_STARTED =
		12;
	
	/** The screen save has ended. */
	public static final int SCREEN_SAVER_FINISHED =
		13;
	
	/** The user is idle (screen saver likely to start). */
	public static final int IDLE =
		14;
	
	/** The system is about to power down. */
	public static final int IN_SHUTDOWN_MODE =
		15;
	
	/** The system has just left low power mode. */
	public static final int LEFT_LOW_POWER_MODE =
		16;
	
	/** Network interfaces have changed. */
	public static final int NETWORK_INTERFACE_CHANGED =
		17;
	
	/** The modem/device has an incoming call. */
	public static final int INCOMING_PHONE_CALL =
		18;
	
	/** The modem/device has started a call. */
	public static final int STARTED_PHONE_CALL =
		19;
	
	/** The modem/device has ended a call. */
	public static final int ENDED_PHONE_CALL =
		20;
	
	/** A new voice mail has arrived. */
	public static final int NEW_VOICE_MAIL =
		21;
	
	/** A new text message has arrived. */
	public static final int NEW_TEXT_MESSAGE =
		22;
	
	/** A new video message has arrived. */
	public static final int NEW_VIDEO_MESSAGE =
		23;
	
	/** Headphones inserted. */
	public static final int HEADPHONES_INSERTED =
		24;
	
	/** Headphones removed. */
	public static final int HEADPHONES_REMOVED =
		25;
	
	/** External interrupt triggered. */
	public static final int EXTERNAL_INTERRUPT =
		26;
	
	/** JVM made visible. */
	public static final int JVM_VISIBLE =
		27;
	
	/** JVM made invisible. */
	public static final int JVM_INVISIBLE =
		28;
	
	/** Battery is low warning. */
	public static final int BATTERY_LOW_WARNING =
		29;
	
	/** Battery is critical warning. */
	public static final int BATTERY_CRITICAL_WARNING =
		30;
	
	/** System is charging. */
	public static final int BATTERY_CHARGING =
		31;
	
	/** Charging complete. */
	public static final int BATTERY_CHARGE_COMPLETE =
		32;
	
	/** System is discharging. */
	public static final int BATTERY_DISCHARGING =
		33;
	
	/** A problem has occured with charging. */
	public static final int BATTERY_CHARGE_ERROR =
		34;
	
	/** The battery charge level has changed. */
	public static final int BATTERY_LEVEL_CHANGED =
		35;
	
	/** A battery has been removed. */
	public static final int BATTERY_REMOVED =
		36;
	
	/** A battery has been inserted. */
	public static final int BATTERY_INSERTED =
		37;
	
	/** Battery not full, but not charging. */
	public static final int BATTERY_NOT_CHARGING =
		38;
	
	/** External power attached. */
	public static final int POWER_ATTACHED =
		39;
	
	/** External power removed. */
	public static final int POWER_REMOVED =
		40;
	
	/** Display attached. */
	public static final int DISPLAY_ATTACHED =
		41;
	
	/** Display removed. */
	public static final int DISPLAY_REMOVED =
		42;
}

