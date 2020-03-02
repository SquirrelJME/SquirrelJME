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

@SuppressWarnings({"FieldNamingConvention",
"InterfaceWithOnlyOneDirectInheritor"})
public interface CalendarInterface {
    int kYearSelectionMode = 1;
    int kMonthSelectionMode = 2;
    int kDaySelectionMode = 3;

    void initCalendarValues(int var1, int var2);

    void paint(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7);

    String getBackCommand();

    String getSaveCommand();
}
