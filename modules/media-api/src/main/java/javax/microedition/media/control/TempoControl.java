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

@Api
public interface TempoControl
	extends RateControl
{
	@Api
	int getTempo();
	
	@Api
	int setTempo(int __a);
}


