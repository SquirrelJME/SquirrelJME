// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

public interface EventType
{
    int PRESSED = 1;
    int RELEASED = 2;
    int CLICKED = 3;
    int EXPIRED = 4;
    int STARTED = 5;
    int STOPPED = 6;
    int PAUSED = 7;
    int REQUEST_PAUSE = 8;
    int REQUEST_STOP = 9;
    int REQUEST_RESUME = 10;
    int CALLING = 11;
    int RECEIVED = 12;
    int SCHEDULED_ALARM = 13;
}
