// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Verbosity flags.
 *
 * @since 2020/07/11
 */
public interface VerboseDebugFlag
{
	/** All verbosity settings. */
	int ALL =
		0xFFFF_FFFF;
	
	/** Be verbose on the called instructions. */
	byte INSTRUCTIONS =
		0x01;
	
	/** Be verbose on the entered methods. */
	byte METHOD_ENTRY =
		0x02;
	
	/** Be verbose on exited methods. */
	byte METHOD_EXIT =
		0x04;
	
	/** Be verbose on MLE calls. */
	byte MLE_CALL =
		0x08;
	
	/** Be verbose on static invocations. */
	byte INVOKE_STATIC =
		0x10;
	
	/** Be verbose on allocations. */
	byte ALLOCATION =
		0x20;
	
	/** Be verbose on class initializations. */
	byte CLASS_INITIALIZE =
		0x40;
	
	/** Virtual machine exceptions. */
	short VM_EXCEPTION =
		0x80;
	
	/** Class lookup failures. */
	short MISSING_CLASS =
		0x100;
	
	/** Monitor entry. */
	short MONITOR_ENTER =
		0x200;
	
	/** Monitor exit. */
	short MONITOR_EXIT =
		0x400;
	
	/** Wait on monitor. */
	short MONITOR_WAIT =
		0x800;
	
	/** Notify on a monitor. */
	short MONITOR_NOTIFY =
		0x1000;
	
	/** Inherit the current verbose checks to another thread. */
	short INHERIT_VERBOSE_FLAGS =
		0x2000;
	
	/** New thread is created. */
	short THREAD_NEW =
		0x4000;
	
	/** Implicit exceptions being generated. */
	int IMPLICIT_EXCEPTION =
		0x8000;
	
	/** Method with many execution cycles. */
	int METHOD_CYCLES =
		0x1_0000;
	
	/** Ignored exceptions. */
	int IGNORED_EXCEPTIONS =
		0x2_0000;
}
