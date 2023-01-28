// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Verbosity flags.
 *
 * @since 2020/07/11
 */
@Exported
public interface VerboseDebugFlag
{
	/** All verbosity settings. */
	@Exported
	int ALL =
		0xFFFF_FFFF;
	
	/** Be verbose on the called instructions. */
	@Exported
	byte INSTRUCTIONS =
		0x01;
	
	/** Be verbose on the entered methods. */
	@Exported
	byte METHOD_ENTRY =
		0x02;
	
	/** Be verbose on exited methods. */
	@Exported
	byte METHOD_EXIT =
		0x04;
	
	/** Be verbose on MLE calls. */
	@Exported
	byte MLE_CALL =
		0x08;
	
	/** Be verbose on static invocations. */
	@Exported
	byte INVOKE_STATIC =
		0x10;
	
	/** Be verbose on allocations. */
	@Exported
	byte ALLOCATION =
		0x20;
	
	/** Be verbose on class initializations. */
	@Exported
	byte CLASS_INITIALIZE =
		0x40;
	
	/** Virtual machine exceptions. */
	@Exported
	short VM_EXCEPTION =
		0x80;
	
	/** Class lookup failures. */
	@Exported
	short MISSING_CLASS =
		0x100;
	
	/** Monitor entry. */
	@Exported
	short MONITOR_ENTER =
		0x200;
	
	/** Monitor exit. */
	@Exported
	short MONITOR_EXIT =
		0x400;
	
	/** Wait on monitor. */
	@Exported
	short MONITOR_WAIT =
		0x800;
	
	/** Notify on a monitor. */
	@Exported
	short MONITOR_NOTIFY =
		0x1000;
	
	/** Inherit the current verbose checks to another thread. */
	@Exported
	short INHERIT_VERBOSE_FLAGS =
		0x2000;
	
	/** New thread is created. */
	@Exported
	short THREAD_NEW =
		0x4000;
}
