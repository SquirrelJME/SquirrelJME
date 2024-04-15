// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents an item within a {@link UIFormShelf}, it has one of the
 * {@link UIItemType} for its type.
 *
 * @since 2020/07/01
 */
@SquirrelJMEVendorApi
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@GhostObject
public interface UIItemBracket
	extends UIWidgetBracket
{
}
