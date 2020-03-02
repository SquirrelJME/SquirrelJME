// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.micro.lcdui;

import javax.microedition.lcdui.Graphics;

public interface WatchInterface
{
    int kHourSelection = 1;
    int kMinuteSelection = 2;

    void initWatchValues(int var1, int var2);

    void paint(Graphics var1, int var2, int var3, int var4);

    String getBackCommand();

    String getSaveCommand();
}

