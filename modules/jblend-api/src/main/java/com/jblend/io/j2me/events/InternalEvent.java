// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

public interface InternalEvent
{
    int BEAM_RECEIVE_KVM_EVENT = 0;
    int PEN_DOWN_KVM_EVENT = 1;
    int PEN_UP_KVM_EVENT = 2;
    int PEN_MOVE_KVM_EVENT = 3;
    int KEY_DOWN_KVM_EVENT = 4;
    int KEY_UP_KVM_EVENT = 5;
    int LAST_KVM_EVENT = 6;
    int APP_STOP_KVM_EVENT = 7;
    int UI_KVM_EVENT = 8;
    int TIMER_KVM_EVENT = 9;
    int PLATFORM_KVM_EVENT = 10;
    int SOUND_KVM_EVENT = 11;
    int MEDIA_KVM_EVENT = 12;
    int DIAL_KVM_EVENT = 14;
    int APP_RESUME_KVM_EVENT = 100;
    int APP_SUSPEND_KVM_EVENT = 101;
    int GENERAL_NOTIFY_EVENT = 200;
    int ANI_CHAR_EVENT = 221;
    int USER_KVM_EVENT = 255;
    int VENDOR_KVM_EVENT = 1000;
    int OPTIONAL_EVENT_ORIGIN = 10000;
    int MAX_OPTIONAL_EVENT = 5;
}
