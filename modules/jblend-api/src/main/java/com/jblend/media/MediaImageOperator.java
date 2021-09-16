// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media;

public interface MediaImageOperator
{
    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setBounds(int var1, int var2, int var3, int var4);

    int getOriginX();

    int getOriginY();

    void setOrigin(int var1, int var2);

    int getMediaWidth();

    int getMediaHeight();
}
