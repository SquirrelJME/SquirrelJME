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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.ui.NullAudioPresenter;

@Api
public class AudioPresenter
	implements MediaPresenter
{
	@Api
	@Override
	public MediaResource getMediaResource()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setAttribute(int __attribute, int __value)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setData(MediaData __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setMediaListener(MediaListener __listener)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSound(MediaSound __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void stop()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static AudioPresenter getAudioPresenter()
	{
		Debugging.todoNote("Implement DoJa sound.");
		return new NullAudioPresenter();
	}
	
	@Api
	public static AudioPresenter getAudioPresenter(int __port)
	{
		Debugging.todoNote("Implement DoJa sound.");
		return new NullAudioPresenter();
	}
}
