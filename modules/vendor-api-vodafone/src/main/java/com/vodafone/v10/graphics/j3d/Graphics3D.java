// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.graphics.j3d;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface Graphics3D
{
	@Api
	void drawFigure(Figure var1, int var2, int var3, FigureLayout var4,
		Effect3D var5);
}

