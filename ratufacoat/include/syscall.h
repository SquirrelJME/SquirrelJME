/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Describe this.
 * 
 * @since 2021/02/28
 */

#ifndef SQUIRRELJME_SYSCALL_H
#define SQUIRRELJME_SYSCALL_H

#include "sjmerc.h"
#include "jvm.h"
#include "cpu.h"
#include "softmath.h"

/*--------------------------------------------------------------------------*/

/**
 * Handles system calls.
 *
 * @param jvm The JVM.
 * @param cpu The CPU.
 * @param error Error state.
 * @param callid The system call type.
 * @param args Arguments to the call.
 * @param rv The return value of the call.
 * @since 2019/06/09
 */
void sjme_syscall(sjme_jvm* jvm, sjme_cpu* cpu, sjme_error* error,
	sjme_jshort callid, sjme_jint* args, sjme_jlong* rv);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_SYSCALL_H */
