/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/classy.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(InvokeStatic)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryMember* member;
	sjme_jclass classy;
	sjme_lpcstr binaryName;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_default(error);

	/* Extract member information. */
	member = &entry->member;
	binaryName = sjme_charSeq_asLpcTemp(&member->inClass->descriptor->seq);

	/* Debug. */
	sjme_message("invokestatic(into %s)", binaryName);
	
	/* Locate target class. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadB(
		inFrame->inThread->inTask->classLoader,
		&classy,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || classy == NULL)
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
