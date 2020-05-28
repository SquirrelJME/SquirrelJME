// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.midlet;

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
    protected ResidentMIDlet()
	{
		throw new todo.TODO();
	}

    @Override
	public abstract void ring();

    @Override
	public abstract void hungup();

    @Override
	public abstract void received(int var1);

    @Override
	public abstract void notice(String var1);

    @Override
	public abstract void ringStarted();

    @Override
	public abstract void ringStopped();
}

