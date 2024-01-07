// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface MediaPresenter
{
	@Api
	MediaResource getMediaResource();
	
	@Api
	void play();
	
	@Api
	void setAttribute(int __attribute, int __value);
	
	@Api
	void setMediaListener(MediaListener __listener);
	
	@Api
	void stop();
	
}
