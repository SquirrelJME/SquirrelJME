// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

public interface MIDIControl
	extends Control
{
	public static final int CONTROL_CHANGE =
		176;
	
	public static final int NOTE_ON =
		144;
	
	public abstract int[] getBankList(boolean __a)
		throws MediaException;
	
	public abstract int getChannelVolume(int __a);
	
	public abstract String getKeyName(int __a, int __b, int __c)
		throws MediaException;
	
	public abstract int[] getProgram(int __a)
		throws MediaException;
	
	public abstract int[] getProgramList(int __a)
		throws MediaException;
	
	public abstract String getProgramName(int __a, int __b)
		throws MediaException;
	
	public abstract boolean isBankQuerySupported();
	
	public abstract int longMidiEvent(byte[] __a, int __b, int __c);
	
	public abstract void setChannelVolume(int __a, int __b);
	
	public abstract void setProgram(int __a, int __b, int __c);
	
	public abstract void shortMidiEvent(int __a, int __b, int __c);
}


