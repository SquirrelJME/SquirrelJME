// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface GUIControl
	extends Control
{
	@Api
	int USE_GUI_PRIMITIVE =
		0;
	
	@Api
	Object initDisplayMode(int __a, Object __b);
}


