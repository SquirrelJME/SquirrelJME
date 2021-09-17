// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.media;

public interface ResourceOperator
{
    int getResourceType();

    int getResourceCount();

    int getResourceID(int var1);

    String getResourceName(int var1);

    String[] getResourceNames();

    void setResourceByID(MediaPlayer var1, int var2);

    void setResourceByTitle(MediaPlayer var1, String var2);

    void setResource(MediaPlayer var1, int var2);

    int getIndexOfResource(int var1);
}
