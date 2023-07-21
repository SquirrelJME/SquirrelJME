// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

public interface MediaPresenter
{
	MediaResource getMediaResource();
	
	void play();
	
	void setAttribute(int __attribute, int __value);
	
	void setMediaListener(MediaListener __listener);
	
	void stop();
	
}
