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
import javax.microedition.media.MediaException;

@Api
public interface MIDIControl
	extends Control
{
	@Api
	int CONTROL_CHANGE =
		176;
	
	@Api
	int NOTE_ON =
		144;
	
	@Api
	int[] getBankList(boolean __a)
		throws MediaException;
	
	@Api
	int getChannelVolume(int __a);
	
	@Api
	String getKeyName(int __a, int __b, int __c)
		throws MediaException;
	
	@Api
	int[] getProgram(int __a)
		throws MediaException;
	
	@Api
	int[] getProgramList(int __a)
		throws MediaException;
	
	@Api
	String getProgramName(int __a, int __b)
		throws MediaException;
	
	@Api
	boolean isBankQuerySupported();
	
	@Api
	int longMidiEvent(byte[] __a, int __b, int __c);
	
	@Api
	void setChannelVolume(int __a, int __b);
	
	@Api
	void setProgram(int __a, int __b, int __c);
	
	@Api
	void shortMidiEvent(int __a, int __b, int __c);
}


