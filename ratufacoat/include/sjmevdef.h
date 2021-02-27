/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME Virtual Machine Definitions.
 *
 * @since 2019/10/06
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMFHSJMEVDEFH
#define SJME_hGRATUFACOATSJMFHSJMEVDEFH

#include "sjmerc.h"
#include "sjmecon.h"

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMFHSJMEVDEFH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/**
 * Long return value result
 *
 * @since 2019/12/07
 */
typedef struct sjme_jlong_combine
{
	/** Low. */
	sjme_jint lo;
	
	/** High. */
	sjme_jint hi;
} sjme_jlong_combine;

/**
 * Division result.
 *
 * @since 2019/06/20
 */
typedef struct sjme_jint_div
{
	/** Quotient. */
	sjme_jint quot;

	/** Remainder. */
	sjme_jint rem;
} sjme_jint_div;

/**
 * This contains the state of a single CPU frame.
 *
 * @since 2019/06/27
 */
typedef struct sjme_cpuframe sjme_cpuframe;
struct sjme_cpuframe
{
	/** PC. */
	sjme_vmemptr pc;
	
	/** Registers. */
	sjme_jint r[SJME_MAX_REGISTERS];
	
	/** Debug: Class name. */
	sjme_vmemptr debugclassname;
	
	/** Debug: Method name. */
	sjme_vmemptr debugmethodname;
	
	/** Debug: Method type. */
	sjme_vmemptr debugmethodtype;
	
	/** Debug: Source file. */
	sjme_vmemptr debugsourcefile;
	
	/** Debug: Current line. */
	sjme_jint debugline;
	
	/** Debug: Java Operation. */
	sjme_jint debugjop;
	
	/** Debug: Java Address. */
	sjme_jint debugjpc;
	
	/** Debug: The Task ID. */
	sjme_jint taskid;
	
	/** The parent CPU state. */
	sjme_cpuframe* parent;
};

/** Virtual CPU. */
typedef struct sjme_cpu sjme_cpu;
struct sjme_cpu
{
	/** The state of this thread. */
	sjme_jint threadstate;
	
	/* System call arguments. */
	sjme_jint syscallargs[SJME_MAX_SYSCALLARGS];
	
	/* System call error numbers. */
	sjme_jint syscallerr[SJME_SYSCALL_NUM_SYSCALLS];
	
	/* Supervisor properties. */
	sjme_jint supervisorprops[SJME_SUPERPROP_NUM_PROPERTIES];
	
	/* Current CPU state. */
	sjme_cpuframe state;
	
	/** Stored IPC Exception. */
	sjme_jint ipcexception;
};

/** Virtual machine state. */
struct sjme_jvm
{
	/** Virtual memory information. */
	sjme_vmem* vmem;
	
	/** RAM. */
	sjme_vmemmap* ram;
	
	/** ROM. */
	sjme_vmemmap* rom;
	
	/** Configuration space. */
	sjme_vmemmap* config;
	
	/** Framebuffer. */
	sjme_vmemmap* framebuffer;
	
	/** OptionJAR. */
	sjme_vmemmap* optionjar;
	
	/** Preset ROM. */
	void* presetrom;
	
	/** Framebuffer info. */
	sjme_framebuffer* fbinfo;
	
	/** Native functions. */
	sjme_nativefuncs* nativefuncs;
	
	/** Linearly fair CPU execution engine. */
	sjme_jint fairthreadid;
	
	/** Threads. */
	sjme_cpu threads[SJME_THREAD_MAX];
	
	/** Total instruction count. */
	sjme_jint totalinstructions;
	
	/** Did the supervisor boot okay? */
	sjme_jint supervisorokay;
	
	/** Console X position. */
	sjme_jint conx;
	
	/** Console Y position. */
	sjme_jint cony;
	
	/** Console width. */
	sjme_jint conw;
	
	/** Console height. */
	sjme_jint conh;
	
	/** System call static field pointer. */
	sjme_vmemptr syscallsfp;
	
	/** System call code pointer. */
	sjme_vmemptr syscallcode;
	
	/** System call pool pointer. */
	sjme_vmemptr syscallpool;
	
	/** Is debugging enabled? */
	sjme_jint enabledebug;
	
	/** Squelch the framebuffer console? */
	sjme_jint squelchfbconsole;
};

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMFHSJMEVDEFH
}
#undef SJME_cXRATUFACOATSJMFHSJMEVDEFH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMFHSJMEVDEFH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMFHSJMEVDEFH */

