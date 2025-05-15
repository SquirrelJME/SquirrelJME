/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal software mixer.
 * 
 * @since 2025/05/10
 */

#ifndef SJME_C_SOFTMIXINTERN_H
#define SJME_C_SOFTMIXINTERN_H

#include "lib/scritchaudio/softmix/softmix.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SOFTMIXINTERN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Software mixer wrapper functions. */
extern const sjme_scritchaudio_implFunctions
	sjme_scritchaudio_softmixFunctions;
	
sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState);

sjme_errorCode sjme_scritchaudio_softmix_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState);
	
sjme_errorCode sjme_scritchaudio_softmix_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list_sjme_scritchaudio_midiPort* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);
	
sjme_errorCode sjme_scritchaudio_softmix_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source);
	
sjme_errorCode sjme_scritchaudio_softmix_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SOFTMIXINTERN_H
}
#undef SJME_CXX_SOFTMIXINTERN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SOFTMIXINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SOFTMIXINTERN_H */
