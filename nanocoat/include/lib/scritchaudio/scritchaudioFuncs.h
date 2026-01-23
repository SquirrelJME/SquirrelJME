/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchAudio Functions.
 *
 * @file
 * @since 2026/01/23
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHAUDIOFUNCS_H
#define SJME_C_SQUIRRELJME_SCRITCHAUDIOFUNCS_H

#include "sjme/stdTypes.h"
#include "lib/scritchaudio/scritchaudioConst.h"
#include "lib/scritchaudio/scritchaudioTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHAUDIOFUNCS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Allocates a single buffer.
 *
 * @param inState The ScritchAudio state.
 * @param headerSize The header size.
 * @param renderInfo The buffer render info.
 * @param outBuffer The output buffer.
 * @return Any resultant error, if any.
 * @since 2025/05/28
 */
typedef sjme_errorCode (*sjme_scritchaudio_allocBufferFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInPositive sjme_jint headerSize,
	sjme_attrInNotNull const sjme_scritchaudio_renderInfo* renderInfo,
	sjme_attrInNullable sjme_scritchaudio_streamBuffer* outBuffer);

/**
 * Internal implementation initialization function.
 *
 * @param inState The ScritchAudio state.
 * @return Any resultant error, if any.
 * @since 2025/05/12
 */
typedef sjme_errorCode (*sjme_scritchaudio_apiInitFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState);

/**
 * Calculates the render information.
 *
 * @param inState The ScritchAudio state.
 * @param inFormat The format used for the stream.
 * @param inLatency The latency information.
 * @param renderInfo The information needed for rendering.
 * @return Any resultant error, if any.
 * @since 2025/05/28
 */
typedef sjme_errorCode (*sjme_scritchaudio_calcRenderInfoFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull const sjme_scritchaudio_renderFormat* inFormat,
	sjme_attrInNotNull const sjme_scritchaudio_latency* inLatency,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo);

/**
 * Disconnects the given connection.
 *
 * @param inState The ScritchAudio state.
 * @param inConn The connection being disconnected.
 * @return Any resultant error, if any.
 * @since 2025/05/26
 */
typedef sjme_errorCode (*sjme_scritchaudio_disconnectFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn);

/**
 * Determines the next fallback to use.
 *
 * @param inState The input state.
 * @param origFormat The original format.
 * @param origRate The original rate.
 * @param origChannels The original channels.
 * @param adjustFormat The adjusted format.
 * @param adjustRate The adjusted rate.
 * @param adjustChannels The adjusted channels.
 * @return On any resultant error, if any.
 * @since 2025/05/28
 */
typedef sjme_errorCode (*sjme_scritchaudio_fallbackNextFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format origFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate origRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels origChannels,
	sjme_attrInOutNotNull sjme_scritchaudio_format* adjustFormat,
	sjme_attrInOutNotNull sjme_scritchaudio_rate* adjustRate,
	sjme_attrInOutNotNull sjme_scritchaudio_channels* adjustChannels);

/**
 * Loop iteration for audio processing.
 *
 * This may be called from a background through or the current thread.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to render in.
 * @return Any resultant error, if any.
 * @since 2025/05/28
 */
typedef sjme_errorCode (*sjme_scritchaudio_loopIterateFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream);

/**
 * Called when there are no peers.
 *
 * @param inState The ScritchAudio state.
 * @param inConn The connection with no peers.
 * @param explicit Is this an explicit no-peer?
 * @return Any resultant error, if any.
 * @since 2025/05/27
 */
typedef sjme_errorCode (*sjme_scritchaudio_peerNoneFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit);

/**
 * A polling function callback.
 *
 * @param rawStream The raw stream function.
 * @return The thread result.
 * @since 2026/01/09
 */
typedef sjme_thread_result (sjme_attrThreadCall *sjme_scritchaudio_pollFunc)(
	sjme_attrInNotNull sjme_thread_parameter rawStream);

/**
 * Called when the peer has been connected or disconnected.
 *
 * @param inState The ScritchAudio state.
 * @param inConn The connection being connected/disconnected.
 * @param inPeer The peer that connected/disconnected.
 * @param explicit Is this an explicit connection?
 * @return Any resultant error, if any.
 * @since 2025/05/26
 */
typedef sjme_errorCode (*sjme_scritchaudio_peerConnectFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit);

/**
 * Queries the MIDI ports and synths that are available to the system.
 *
 * @param inState The ScritchAudio state.
 * @param inOutPorts The ports which are available.
 * @param outNumPorts The number of ports that are available, this value may
 * be larger than the list if it is too small.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_queryMidiPortsFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list(sjme_scritchaudio_midiPort)* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);

/**
 * Callback function for when a render is occurring.
 *
 * @param inState The ScritchAudio state.
 * @param inSource The source being rendered.
 * @param renderInfo The information needed for rendering.
 * @param buf The buffer to render to.
 * @return Any resultant error, if any.
 * @since 2025/05/18
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceRenderFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* buf);

/**
 * Attaches a source renderer to the given stream, the renderer will use the
 * same format that the stream uses.
 *
 * If the target audio system does not support opening streams and attaching
 * sources in a different format
 * then @link SJME_ERROR_AUDIO_FORMAT_MISMATCH @endlink will be returned.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to attach to or detach from.
 * @param outSource The resultant source.
 * @param renderFunc The render function to use.
 * @param inFormat The audio format to use, @a -1 means to use the system
 * preferred format.
 * @param inRate The rate to use, @a -1 means to use the system preferred
 * rate.
 * @param inChannels The number of channels to use, @a -1 means to use the
 * system preferred channels.
 * @param initFrontEnd The front end used for the renderer.
 * @return Any resultant error, if any.
 * @since 2025/05/18
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceAttachFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc renderFunc,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd);

/**
 * Attaches a source renderer to the given stream, the renderer will use the
 * same format that the stream uses.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to attach to or detach from.
 * @param inSource The resultant source.
 * @return Any resultant error, if any.
 * @since 2025/05/25
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceAttachImplFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);

/**
 * Creates a new audio stream.
 *
 * @param inState The input state.
 * @param outStream The resultant audio stream.
 * @param inName The name of the stream.
 * @param inFormat The audio format to use, @c -1 means to use the system
 * preferred format.
 * @param inRate The rate to use, @c -1 means to use the system preferred
 * rate.
 * @param inChannels The number of channels to use, @c -1 means to use the
 * system preferred channels.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_streamCreateFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/**
 * Creates a new audio stream.
 *
 * @param inState The input state.
 * @param inOutStream The resultant audio stream.
 * @param inName The name of the stream.
 * @param inFormat The audio format to use, @c -1 means to use the system
 * preferred format.
 * @param inRate The rate to use, @c -1 means to use the system preferred
 * rate.
 * @param inChannels The number of channels to use, @c -1 means to use the
 * system preferred channels.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_streamCreateImplFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOFUNCS_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOFUNCS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOFUNCS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHAUDIOFUNCS_H */