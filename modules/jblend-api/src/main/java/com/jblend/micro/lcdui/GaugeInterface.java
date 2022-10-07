// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.micro.lcdui;

import javax.microedition.lcdui.Graphics;

public interface GaugeInterface {
    int LOST_FOCUS = 1;
    int VALUE_CHANGED = 2;

    void init(boolean var1, int var2, int var3, int var4);

    void setValue(int var1);

    int getValue();

    void setMaxValue(int var1);

    int getMaxValue();

    void paint(boolean var1, Graphics var2, int var3, int var4);

    int processEvent(int var1, int var2, int var3, int var4);

    int getWidth();

    int getHeight();
}
