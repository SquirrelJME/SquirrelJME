// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

public interface UiEventType
{
    int CLICKED = 1;
    int SELECTED = 2;
    int DESELECTED = 3;
    int CHANGED = 4;
    int REPAINT = 5;
    int GOTDISPLAY = 6;
    int LOSTFOCUS = 7;
    int REPAINTCOMPONENT = 8;
    int RELEASEDISPLAY = 9;
}
