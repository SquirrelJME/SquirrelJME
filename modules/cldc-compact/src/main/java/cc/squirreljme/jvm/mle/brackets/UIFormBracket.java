// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents a form as a whole, it is displayed with
 * {@link UIDisplayBracket} and contains {@link UIItemBracket}.
 * 
 * Every form has a title, a ticker, command buttons, and/or a menu.
 *
 * @since 2020/07/01
 */
@SquirrelJMEVendorApi
@GhostObject
public interface UIFormBracket
	extends UIWidgetBracket
{
}
