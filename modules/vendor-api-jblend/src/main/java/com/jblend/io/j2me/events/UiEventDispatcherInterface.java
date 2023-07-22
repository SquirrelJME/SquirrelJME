// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface UiEventDispatcherInterface
{
	@Api
	void dispatchUi(int var1, int var2, int var3, int var4, int var5);
}
