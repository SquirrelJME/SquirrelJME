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

/**
 * Handles mixing.
 *
 * @param sourceInfo The source information.
 * @param sourceBuf The source buffer.
 * @param destInfo The destination info.
 * @param destBuf The destination buffer.
 * @return Any resultant error.
 * @since 2025/05/31
 */
typedef sjme_errorCode (*sjme_scritchaudio_softmix_mixer)(
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* sourceInfo,
	sjme_attrInNotNull const sjme_scritchaudio_buffer* sourceBuf,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf);
	
/** Software mixer wrapper functions. */
extern const sjme_scritchaudio_implFunctions
	sjme_scritchaudio_softmixFunctions;

/** Available software mixer functions. */
extern const sjme_scritchaudio_softmix_mixer
	sjme_scritchaudio_softmix_mixers[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS]
	[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS];
	
sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState);
	
sjme_errorCode sjme_scritchaudio_softmix_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn);

sjme_errorCode sjme_scritchaudio_softmix_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo);
	
sjme_errorCode sjme_scritchaudio_softmix_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list_sjme_scritchaudio_midiPort* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);
	
sjme_errorCode sjme_scritchaudio_softmix_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);
	
sjme_errorCode sjme_scritchaudio_softmix_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
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
