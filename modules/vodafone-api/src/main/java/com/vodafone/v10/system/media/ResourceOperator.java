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
    @Api
int getResourceType();

    @Api
int getResourceCount();

    @Api
int getResourceID(int var1);

    @Api
String getResourceName(int var1);

    @Api
String[] getResourceNames();

    @Api
void setResourceByID(MediaPlayer var1, int var2);

    @Api
void setResourceByTitle(MediaPlayer var1, String var2);

    @Api
void setResource(MediaPlayer var1, int var2);

    @Api
int getIndexOfResource(int var1);
}
