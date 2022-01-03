/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Engine scaffolding.
 * 
 * @since 2021/09/06
 */

#ifndef SQUIRRELJME_SCAFFOLD_H
#define SQUIRRELJME_SCAFFOLD_H

#include "sjmerc.h"
#include "error.h"
#include "video.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCAFFOLD_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The maximum number of terminals that can be displayed. */
#define SJME_CONFIG_MAX_TERMINALS 4

/** The maximum number of screens that can be displayed. */
#define SJME_CONFIG_MAX_SCREENS 4

/**
 * Indicates the reason why an engine is not available.
 * 
 * @since 2021/12/30
 */
typedef enum sjme_engineScaffoldUnavailableType
{
	/** It is actually available. */
	SJME_ENGINE_SCAFFOLD_IS_AVAILABLE = 0,
	
	/** The ROM is missing. */
	SJME_ENGINE_SCAFFOLD_MISSING_ROM,
	
	/** The ROM is not compatible with this engine. */
	SJME_ENGINE_SCAFFOLD_INCOMPATIBLE_ROM,
	
	/** The number of these types. */
	SJME_NUM_ENGINE_SCAFFOLD_UNAVAILABLE_TYPE
} sjme_engineScaffoldUnavailableType;

/**
 * The threading model for execution of the virtual machine.
 * 
 * @since 2022/01/03
 */
typedef enum sjme_engineThreadModel
{
	/** Cooperative threading in a single thread of execution. */
	SJME_ENGINE_THREAD_MODEL_COOP = 0,
	
	/** Simultaneous multi-threading. */
	SJME_ENGINE_THREAD_MODEL_SMT,
	
	/** The number of threading models that are available. */
	SJME_NUM_ENGINE_THREADING_MODELS
} sjme_engineThreadModel;

/**
 * Configuration for an engine within SquirrelJME, this is used to initialize
 * the virtual machine and everything it needs.
 * 
 * @since 2022/01/02
 */
typedef struct sjme_engineConfig
{
	/** The threading model of the engine. */
	sjme_engineThreadModel threadModel;
	
	/** The pointer to the ROM to use. */
	const sjme_jubyte* romPointer;
	
	/** The size of the ROM that exists. */
	sjme_jint romSize;
	
	/** Is the ROM dynamically loaded? */
	sjme_jboolean romIsAllocated;
	
	/** The number of active terminals, if @c then there are none. */
	sjme_jint numTerminals;
	
	/** The available terminals. */
	struct
	{
		/** The number of terminal columns. */
		sjme_jint columns;
		
		/** The number of terminal rows. */
		sjme_jint rows;
		
		/** Raw terminal display. */
		void* symbols;
	} terminals[SJME_CONFIG_MAX_TERMINALS];
	
	/** The number of active screens, if @c 0 then there are no displays. */
	sjme_jint numScreens;
	
	/** Individual video screen setups. */
	struct
	{
		/** Screen width. */
		sjme_jint width;
		
		/** Screen height. */
		sjme_jint height;
		
		/** The pixel format to use for the display. */
		sjme_pixelFormat pixelFormat;
		
		/** Screen pixel data, if using an already existing buffer. */
		void* pixels;
	} screens[SJME_CONFIG_MAX_SCREENS];
} sjme_engineConfig;

/**
 * The state of any given engine.
 * 
 * @since 2022/01/03
 */
typedef struct sjme_engineState sjme_engineState;

/**
 * The state of a single thread within the engine.
 * 
 * @since 2022/01/03
 */
typedef struct sjme_engineThread sjme_engineThread;

/**
 * This is the scaffold for an engine between the common engine layer and the
 * specific engine implementation.
 * 
 * @since 2021/12/19
 */
typedef struct sjme_engineScaffold
{
	/** The name of the engine. */
	const char* const name;
	
	/**
	 * Checks if this scaffold and engine are available.
	 * 
	 * @param config The configuration of the engine.
	 * @param why Why is this engine not available, this is optional and may
	 * be @c NULL .
	 * @param error The error state.
	 * @return Will return @c true if available, otherwise not. 
	 * @since 2021/12/30
	 */
	sjme_jboolean (*isAvailable)(sjme_engineConfig* config,
		sjme_engineScaffoldUnavailableType* why, sjme_error* error);
	
	/**
	 * Runs the engine in cooperative mode until a resting event potentially
	 * occurs.
	 * 
	 * @param state The state of the engine.
	 * @param limitCycles The number of cycles to limit executions to if a
	 * non-zero value otherwise a negative value effectively runs until a
	 * full rest occurs.
	 * @param error The error state.
	 * @return If execution was a success.
	 * @since 2022/01/03
	 */
	sjme_jboolean (*runCoopCycles)(sjme_engineState* state,
		sjme_jint limitCycles, sjme_error* error);
	
	/**
	 * Continuously runs the given thread from a multi-thread context, this
	 * is intended to be used by threading implementations.
	 * 
	 * @param state The state of the engine.
	 * @param thread The current thread to run, in a multi-threaded context.
	 * @param error The error state.
	 * @return If execution was a success.
	 * @since 2022/01/03
	 */
	sjme_jboolean (*runMultiThread)(sjme_engineState* state,
		sjme_engineThread* thread, sjme_error* error);
} sjme_engineScaffold;

/**
 * The state of any given engine.
 * 
 * @since 2022/01/03
 */
struct sjme_engineState
{
	/** A copy of the engine configuration that is used. */
	sjme_engineConfig config;
	
	/** The scaffold that this engine uses, contains all the engine logic. */
	const sjme_engineScaffold* scaffold;
};

/**
 * The state of a single thread within the engine.
 * 
 * @since 2022/01/03
 */
struct sjme_engineThread
{
	/** The thread identifier. */
	sjme_jint id;
};

/** Scaffolds which are available for use. */
extern const sjme_engineScaffold* const sjme_engineScaffolds[];

/** SpringCoat engine. */
extern const sjme_engineScaffold sjme_engineScaffoldSpringCoat;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCAFFOLD_H
}
#undef SJME_CXX_SQUIRRELJME_SCAFFOLD_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCAFFOLD_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCAFFOLD_H */
