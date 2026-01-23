/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Main ScritchAudio header.
 * 
 * @file
 * @since 2025/05/07
 */

#ifndef SJME_C_SCRITCHAUDIO_H
#define SJME_C_SCRITCHAUDIO_H

#include "sjme/config.h"
#include "sjme/alloc.h"
#include "sjme/list.h"
#include "sjme/native.h"
#include "lib/scritchany/scritchany.h"
#include "lib/scritchaudio/scritchaudioTypes.h"
#include "lib/scritchaudio/scritchaudioApiFuncs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SCRITCHAUDIO_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Cast to connection type. */
#define SJME_AS_AUDIO_CONN(x) ((sjme_scritchaudio_connection)(x))

/** Cast to stream type. */
#define SJME_AS_AUDIO_STREAM(x) ((sjme_scritchaudio_stream)(x))

/** Cast to source type. */
#define SJME_AS_AUDIO_SOURCE(x) ((sjme_scritchaudio_source)(x))

struct sjme_scritchaudioBase
{
	/** The lock for audio streams and otherwise. */
	sjme_thread_spinLock baseLock;
	
	/** The actual lock which should be used. */
	sjme_thread_spinLock* lock;
	
	/** The allocation pool to use. */
	sjme_alloc_pool pool;

	/** The front end. */
	sjme_frontEnd frontEnd;

	/** The audio clock. */
	sjme_scritchaudio_clock clock;

	/** The native abstraction layer to use. */
	const sjme_nal* nal;
	
	/** Api Functions. */
	const sjme_scritchaudio_apiFunctions* api;

	/** Implementation functions. */
	const sjme_scritchaudio_implFunctions* impl;

	/** Internal functions. */
	const sjme_scritchaudio_internFunctions* intern;
	
	/** Wrapped ScritchAudio state, if this is a wrapper. */
	sjme_scritchaudio wrappedState;
	
	/** Reference to the owning state. */
	sjme_alignPointer sjme_atomic(sjme_pointer) topState;

	/** Bugs. */
	sjme_scritchaudio_bugs bugs;

	/** The output audio stream. */
	sjme_scritchaudio_stream stream;

	/** Called to bind the audio thread. */
	sjme_thread_mainFunc bindAudioThread;
	
	/** Underlying streams/connections if this is double-layered. */
	struct
	{
		/** The underlying stream. */
		sjme_scritchaudio_stream stream;
		
		/** The underlying source. */
		sjme_scritchaudio_source source;
	} under;
};

struct sjme_scritchaudio_connectionBase
{
	/** The lock for this connection, used when rendering. */
	sjme_thread_spinLock* lock;

	/** The type of connection this is. */
	sjme_scritchaudio_connectionType type;
	
	/** The state this is in. */
	sjme_scritchaudio inState;
	
	/** Called when the peer disconnected. */
	sjme_scritchaudio_peerConnectFunc peerDisconnect;

	/** Called when there are no peers remaining. */
	sjme_scritchaudio_peerNoneFunc noPeers;

	/** The connections this is connected to. */
	sjme_list(sjme_scritchaudio_connection)* peers;

	/** Is this disconnecting? */
	sjme_atomic(sjme_jint) disconnecting;
};

struct sjme_scritchaudio_streamBase
{
	/** The connection. */
	sjme_scritchaudio_connectionBase connection;

	/** The lock for audio streams and otherwise. */
	sjme_thread_spinLock baseLock;

	/** The stream format. */
	sjme_scritchaudio_renderFormat format;

	/** The sources attached to this stream. */
	sjme_list(sjme_scritchaudio_source)* sources;

	/** Last callback error. */
	sjme_atomic(sjme_jint) lastError;

	/** The audio loop thread, if applicable. */
	sjme_thread loopThread;

	/** The current audio thread ID, if applicable. */
	sjme_thread_id loopThreadId;

	/** The loop thread is ready. */
	sjme_atomic(sjme_jint) loopThreadReady;

	/** Stream data. */
	sjme_scritchaudio_streamData data;
};

struct sjme_scritchaudio_sourceBase
{
	/** The connection. */
	sjme_scritchaudio_connectionBase connection;

	/** The stream this is attached to. */
	sjme_scritchaudio_stream inStream;

	/** The renderer function. */
	sjme_scritchaudio_sourceRenderFunc renderFunc;

	/** The front end data. */
	sjme_frontEnd frontEnd;

	/** The source format. */
	sjme_scritchaudio_renderFormat format;

	/** The source data. */
	struct
	{
		/** The wrapped source. */
		sjme_scritchaudio_source wrapped;
	} data;
};
	
/**
 * Dynamic library entry point for ScritchAudio.
 *
 * @param inPool The input pool.
 * @param outState The resultant audio state.
 * @param bindAudioThread Called if the event thread needs to be initialized.
 * @param initFrontEnd the initial front-end state.
 * @return Any resultant error, if any.
 * @since 2025/05/11
 */
typedef sjme_errorCode (sjme_attrExportCall *sjme_scritchaudio_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_thread_mainFunc bindAudioThread,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd);

/** The symbol used for default API export. */
#define SJME_SCRITCHAUDIO_DYLIB_API_EXPORT \
	sjme_scritchaudio_dylibApiExport

/** The default API entry export method. */
extern sjme_attrExport const sjme_scritchaudio_dylibApiFunc
	SJME_SCRITCHAUDIO_DYLIB_API_EXPORT;

#if defined(SJME_CONFIG_MULTILIB_IS_DYLIB)
	/** Set the value for the default dynamic library export. */
	#define SJME_SCRITCHAUDIO_DYLIB_API_EXPORT_SET(x) \
		sjme_attrExport const sjme_scritchaudio_dylibApiFunc \
			SJME_SCRITCHAUDIO_DYLIB_API_EXPORT = \
			SJME_SCRITCHAUDIO_DYLIB_SYMBOL(x);
#else
	/** Set the value for the default dynamic library export. */
	#define SJME_SCRITCHAUDIO_DYLIB_API_EXPORT_SET(x)
#endif
	
/** The base name for the ScritchAudio dynamic library. */
#define SJME_SCRITCHAUDIO_DYLIB_NAME_BASE \
	SJME_SCRITCHANY_DYLIB_NAME_BASE(audio)

/** The name of the dynamic library for ScritchAudio. */
#define SJME_SCRITCHAUDIO_DYLIB_NAME(x) \
	SJME_SCRITCHANY_DYLIB_NAME(audio, x)

/** The path name for the dynamic library for ScritchAudio. */
#define SJME_SCRITCHAUDIO_DYLIB_PATHNAME(x) \
	SJME_SCRITCHANY_DYLIB_PATHNAME(audio, x)

/** The prefix for the dynamic library. */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL_PREFIX \
	SJME_SCRITCHANY_DYLIB_SYMBOL_PREFIX(audio)

/** The symbol to use with @link sjme_scritchaudio_dylibApiFunc @endlink . */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL(audio, x)

/** Declares the API export . */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL_DECLARE(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL_DECLARE(audio, x)

/** Casts to a @link sjme_scritchaudio_connection @endlink . */
#define SJME_SAU_CAST_CONNECTION(x) \
	((sjme_scritchaudio_connection)(x))

/** Casts to a @link sjme_scritchaudio_stream @endlink . */
#define SJME_SAU_CAST_STREAM(x) \
	((sjme_scritchaudio_stream)(x))
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SCRITCHAUDIO_H
}
#undef SJME_CXX_SCRITCHAUDIO_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SCRITCHAUDIO_H */
#endif /* #ifdef __cplusplus */

#endif /* SCRITCHAUDIO_H */
