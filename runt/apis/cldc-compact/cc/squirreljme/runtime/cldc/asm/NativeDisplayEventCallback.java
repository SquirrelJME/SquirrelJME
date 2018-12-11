// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * For any native display event that occurs, this method is called back and
 * executed accordingly so that any events may be processed accordingly. This
 * is needed by some operating systems and devices where any access to the
 * native display must be done through this event loop. Most operating systems
 * have a UI event loop
 *
 * @since 2018/12/03
 */
public interface NativeDisplayEventCallback
{
	/** Key pressed. */
	public static final int KEY_PRESSED =
		1;
	
	/** Key repeated. */
	public static final int KEY_REPEATED =
		2;
	
	/** Key released. */
	public static final int KEY_RELEASED =
		3;
	
	/** Pointer pressed. */
	public static final int POINTER_PRESSED =
		4;
	
	/** Pointer dragged. */
	public static final int POINTER_DRAGGED =
		5;
	
	/** Pointer released. */
	public static final int POINTER_RELEASED =
		6;
	
	/**
	 * Executes the numbered command on the given display.
	 *
	 * @param __d The display to run the command on.
	 * @param __c The command to execute.
	 * @since 2018/12/03
	 */
	public abstract void command(int __d, int __c);
	
	/**
	 * Exit request generated for a display, this likely means the close
	 * button was pressed.
	 *
	 * Note that an exit might not happen if it is caught (like an exit
	 * dialog question or similar).
	 *
	 * @param __d The display which the close was performed on.
	 * @since 2018/12/03
	 */
	public abstract void exitRequest(int __d);
	
	/**
	 * Key action has been performed.
	 *
	 * @param __d The display ID.
	 * @param __ty The type of key event.
	 * @param __kc The key code.
	 * @param __ch The key character, {@code -1} is not valid.
	 * @param __time Timecode.
	 * @since 2018/12/03
	 */
	public abstract void keyEvent(int __d, int __ty, int __kc, int __ch,
		int __time);
	
	/**
	 * This is called when the callback has been lost, another task has claimed
	 * access to the display event handler. This should be called before
	 * a registration occurs.
	 *
	 * @since 2018/12/10
	 */
	public abstract void lostCallback();
	
	/**
	 * Paints the display.
	 *
	 * @param __d The display.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/12/03
	 */
	public abstract void paintDisplay(int __d, int __x, int __y,
		int __w, int __h);
	
	/**
	 * Pointer event has occured.
	 *
	 * @param __d The display to have the end.
	 * @param __ty The type of pointer event.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __time Timecode.
	 * @since 2018/12/03
	 */
	public abstract void pointerEvent(int __d, int __ty, int __x, int __y,
		int __time);
	
	/**
	 * Display has been shown or hidden.
	 *
	 * @param __d The display.
	 * @param __shown If this is non-zero the display is shown.
	 * @since 2018/12/03
	 */
	public abstract void shown(int __d, int __shown);
	
	/**
	 * Display size has changed.
	 *
	 * @param __d The display which has changed.
	 * @param __w The width of the display.
	 * @param __h The height of the display.
	 * @since 2018/12/03
	 */
	public abstract void sizeChanged(int __d, int __w, int __h);
}

