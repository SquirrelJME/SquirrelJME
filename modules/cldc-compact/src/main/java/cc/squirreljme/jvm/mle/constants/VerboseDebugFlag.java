// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Verbosity flags.
 *
 * @since 2020/07/11
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface VerboseDebugFlag
{
	/** All verbosity settings, except for exclusionary ones. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int ALL =
		~(0x4_0000 | 0x8_0000);
	
	/** Be verbose on the called instructions. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INSTRUCTIONS =
		0x01;
	
	/** Be verbose on the entered methods. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte METHOD_ENTRY =
		0x02;
	
	/** Be verbose on exited methods. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte METHOD_EXIT =
		0x04;
	
	/** Be verbose on MLE calls. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MLE_CALL =
		0x08;
	
	/** Be verbose on static invocations. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVOKE_STATIC =
		0x10;
	
	/** Be verbose on allocations. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALLOCATION =
		0x20;
	
	/** Be verbose on class initializations. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLASS_INITIALIZE =
		0x40;
	
	/** Virtual machine exceptions. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short VM_EXCEPTION =
		0x80;
	
	/** Class lookup failures. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MISSING_CLASS =
		0x100;
	
	/** Monitor entry. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MONITOR_ENTER =
		0x200;
	
	/** Monitor exit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MONITOR_EXIT =
		0x400;
	
	/** Wait on monitor. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MONITOR_WAIT =
		0x800;
	
	/** Notify on a monitor. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MONITOR_NOTIFY =
		0x1000;
	
	/** Inherit the current verbose checks to another thread. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INHERIT_VERBOSE_FLAGS =
		0x2000;
	
	/** New thread is created. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short THREAD_NEW =
		0x4000;
	
	/** Implicit exceptions being generated. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int IMPLICIT_EXCEPTION =
		0x8000;
	
	/** Method with many execution cycles. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int METHOD_CYCLES =
		0x1_0000;
	
	/** Ignored exception. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int IGNORED_EXCEPTION =
		0x2_0000;
	
	/** Not on the main thread. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int NOT_MAIN_THREAD =
		0x4_0000;
	
	/** Only in the default package. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int DEFAULT_PACKAGE =
		0x8_0000;
}
