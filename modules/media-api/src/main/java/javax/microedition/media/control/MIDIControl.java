// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

public interface MIDIControl
	extends Control
{
	int CONTROL_CHANGE =
		176;
	
	int NOTE_ON =
		144;
	
	int[] getBankList(boolean __a)
		throws MediaException;
	
	int getChannelVolume(int __a);
	
	String getKeyName(int __a, int __b, int __c)
		throws MediaException;
	
	int[] getProgram(int __a)
		throws MediaException;
	
	int[] getProgramList(int __a)
		throws MediaException;
	
	String getProgramName(int __a, int __b)
		throws MediaException;
	
	boolean isBankQuerySupported();
	
	int longMidiEvent(byte[] __a, int __b, int __c);
	
	void setChannelVolume(int __a, int __b);
	
	void setProgram(int __a, int __b, int __c);
	
	void shortMidiEvent(int __a, int __b, int __c);
}


