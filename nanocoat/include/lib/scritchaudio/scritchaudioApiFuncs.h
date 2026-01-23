/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchAudio API function structures.
 *
 * @file
 * @since 2026/01/23
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H
#define SJME_C_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H

#include "lib/scritchaudio/scritchaudioFuncs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Functions for operating on ScritchAudio.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudio_apiFunctions
{
	/** Disconnects a connection. */
	sjme_scritchaudio_disconnectFunc disconnect;

	/** Iterates the audio loop. */
	sjme_scritchaudio_loopIterateFunc loopIterate;

	/** Queries the MIDI ports and synths available. */
	sjme_scritchaudio_queryMidiPortsFunc queryMidiPorts;

	/** Attaches or detaches a source. */
	sjme_scritchaudio_sourceAttachFunc sourceAttach;

	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateFunc streamCreate;
} sjme_scritchaudio_apiFunctions;

/**
 * ScritchAudio implementation functions.
 *
 * @since 2025/05/10
 */
typedef struct sjme_scritchaudio_implFunctions
{
	/** The driver name. */
	sjme_lpcstr driverName;

	/** Supports every format and can handle its own mixing. */
	sjme_jboolean allFormatsOwnMixing;

	/** Supports more than one stream opened at once. */
	sjme_jboolean supportsMultiStream;

	/** Api initialization. */
	sjme_scritchaudio_apiInitFunc apiInit;

	/** Notification that a disconnection is about to occur. */
	sjme_scritchaudio_disconnectFunc disconnect;

	/** Iterates the audio loop. */
	sjme_scritchaudio_loopIterateFunc loopIterate;

	/** Queries the MIDI ports and synths available. */
	sjme_scritchaudio_queryMidiPortsFunc queryMidiPorts;

	/** Attaches or detaches a source. */
	sjme_scritchaudio_sourceAttachImplFunc sourceAttach;

	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateImplFunc streamCreate;

	/** Native callback procedure. */
	sjme_undefinedFunction nativeCallback;
} sjme_scritchaudio_implFunctions;

/**
 * Internal functions.
 *
 * @since 2025/05/26
 */
typedef struct sjme_scritchaudio_internFunctions
{
	/** Allocates buffers. */
	sjme_scritchaudio_loopIterateFunc allocBuffers;

	/** Calculate the rendering information. */
	sjme_scritchaudio_calcRenderInfoFunc calcRenderInfo;

	/** Determines the next fallback. */
	sjme_scritchaudio_fallbackNextFunc fallbackNext;

	/** Iterates the audio loop, while locked. */
	sjme_scritchaudio_loopIterateFunc loopIterateLocked;

	/** Connect two peers. */
	sjme_scritchaudio_peerConnectFunc peerConnect;

	/** Disconnect two peers. */
	sjme_scritchaudio_peerConnectFunc peerDisconnect;

	/** Dispatch peer none. */
	sjme_scritchaudio_peerNoneFunc peerNoneDispatch;

	/** Event based polling loop. */
	sjme_scritchaudio_pollFunc pollEvent;

	/** Manual polling loop. */
	sjme_scritchaudio_pollFunc pollManual;

	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateFunc streamCreate;
} sjme_scritchaudio_internFunctions;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHAUDIOAPIFUNCS_H */