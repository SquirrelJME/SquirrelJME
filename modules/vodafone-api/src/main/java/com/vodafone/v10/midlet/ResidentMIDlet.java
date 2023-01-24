// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.midlet;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.vodafone.v10.system.device.MailListener;
import com.vodafone.v10.system.device.RingStateListener;
import com.vodafone.v10.system.device.ScheduledAlarmListener;
import com.vodafone.v10.system.device.TelephonyListener;
import javax.microedition.midlet.MIDlet;

public abstract class ResidentMIDlet
	extends MIDlet
	implements TelephonyListener, MailListener, ScheduledAlarmListener,
		RingStateListener
{
	@Api
	protected ResidentMIDlet()
	{
		throw Debugging.todo();
	}
}

