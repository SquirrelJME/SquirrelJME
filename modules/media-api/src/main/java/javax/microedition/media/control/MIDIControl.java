// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * This provides control over a MIDI device by permitting events and otherwise
 * to be transmitted. To request a control you must call
 * {@link Manager#createPlayer(String)} with
 * {@link Manager#MIDI_DEVICE_LOCATOR}. Then you must call
 * {@link Player#getControl(String)} with a value of
 * {@code "javax.microedition.media.control.MIDIControl"} to gain access to
 * this interface.
 * 
 * @since 2022/04/23
 */
@Api
public interface MIDIControl
	extends Control
{
	/** The control change event. */
	@Api
	int CONTROL_CHANGE =
		0b1011_0000;
	
	/** The note on event. */
	@Api
	int NOTE_ON =
		144;
	
	/**
	 * Returns the list of installed banks, which may optionally include
	 * custom ones.
	 * 
	 * @param __custom If set to {@code true} will return custom banks only,
	 * otherwise every blank including custom ones will be returned when
	 * {@code false}.
	 * @return The list of banks, each bank will be a value of [0, 16383].
	 * @throws IllegalStateException If the {@link Player} has not been
	 * {@link Player#PREFETCHED}.
	 * @throws MediaException If {@link #isBankQuerySupported()} is
	 * {@code false}.
	 * @since 2022/04/23
	 */
	@Api
	int[] getBankList(boolean __custom)
		throws IllegalStateException, MediaException;
	
	/**
	 * Returns the volume for the given channel if it is possible, the
	 * implementation may know the volume or keep track of it potentially.
	 * 
	 * @param __channel The channel to request, within [0, 15].
	 * @return Will return the channel volume [0, 127], or {@code -1} if not
	 * known.
	 * @throws IllegalArgumentException If the channel is out of range.
	 * @throws IllegalStateException If the {@link Player} has not been
	 * {@link Player#PREFETCHED}.
	 * @since 2022/04/23
	 */
	@Api
	int getChannelVolume(int __channel)
		throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Returns the name of the given key within a bank and program.
	 * 
	 * @param __bank The bank, within [0, 16383].
	 * @param __prog The program, within [0, 127].
	 * @param __key The key, within [0, 127].
	 * @return The name of the key or {@code null} if it is not mapped to
	 * a sound, melodic banks which only play a single sound at a different
	 * pitch will return {@code null}.
	 * @throws IllegalArgumentException If the bank, program, or key is not
	 * within bounds.
	 * @throws IllegalStateException If the {@link Player} has not been
	 * {@link Player#PREFETCHED}.
	 * @throws MediaException If the given bank and program is not installed;
	 * or if {@link #isBankQuerySupported()} is {@code false}.
	 * @since 2022/04/23
	 */
	@Api
	String getKeyName(int __bank, int __prog, int __key)
		throws IllegalArgumentException, IllegalStateException, MediaException;
	
	/**
	 * Returns the program that is currently assigned to the given channel.
	 * 
	 * If the device is not initialized, this may return a default 
	 * implementation defined value.
	 * 
	 * @param __channel The channel to query, within [0, 15].
	 * @return An array specifying {@code [bank, program]}.
	 * @throws IllegalArgumentException If the channel is out of range.
	 * @throws IllegalStateException If the {@link Player} has not been
	 * {@link Player#PREFETCHED}.
	 * @throws MediaException If {@link #isBankQuerySupported()} is
	 * {@code false}.
	 * @since 2022/04/23
	 */
	@Api
	int[] getProgram(int __channel)
		throws IllegalArgumentException, IllegalStateException, MediaException;
	
	@Api
	int[] getProgramList(int __bank)
		throws IllegalArgumentException, IllegalStateException, MediaException;
	
	@Api
	String getProgramName(int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException, MediaException;
	
	@Api
	boolean isBankQuerySupported();
	
	@Api
	int longMidiEvent(byte[] __b, int __o, int __l)
		throws IllegalArgumentException, IllegalStateException;
	
	@Api
	void setChannelVolume(int __channel, int __volume)
		throws IllegalArgumentException, IllegalStateException;
	
	@Api
	void setProgram(int __channel, int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException;
	
	@Api
	void shortMidiEvent(int __type, int __data1, int __data2)
		throws IllegalArgumentException, IllegalStateException;
}


