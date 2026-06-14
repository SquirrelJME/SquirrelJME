// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import cc.squirreljme.runtime.nttdocomo.ui.LockFlush;
import com.nttdocomo.opt.ui.Graphics2;
import javax.microedition.lcdui.Graphics;

/**
 * Allows access to {@link Graphics2}.
 *
 * @since 2025/06/01
 */
@KeepWhenCompacting
class __Graphics2__
	extends Graphics2
{
	/**
	 * Wraps the given graphics object.
	 *
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}.
	 * @param __flush Optional flush callback to be executed when this
	 * occurs.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	@KeepWhenCompacting
	__Graphics2__(Graphics __g, BGColor __bgColor, LockFlush __flush)
		throws NullPointerException
	{
		super(__g, __bgColor, __flush);
	}
}
