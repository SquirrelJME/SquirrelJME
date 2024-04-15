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

@Api
public interface FramePositioningControl
	extends Control
{
	@Api
	long mapFrameToTime(int __a);
	
	@Api
	int mapTimeToFrame(long __a);
	
	@Api
	int seek(int __a);
	
	@Api
	int skip(int __a);
}


