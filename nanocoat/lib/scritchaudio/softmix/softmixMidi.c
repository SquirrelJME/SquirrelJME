/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

sjme_errorCode sjme_scritchaudio_softmix_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list(sjme_scritchaudio_midiPort)* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
