/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/*****************************************************************************
 * Core dumps utilize the CBOR format and dump the entirety of the
 * virtual machine state structure @c sjme_nvm to a stream. This is useful
 * for debugging the state of the virtual machine.
 *
 * Such created dumps can be reloaded and restored to a running virtual
 * machine potentially.
 *
 * https://www.rfc-editor.org/rfc/rfc8949
 ****************************************************************************/

#include "sjme/nvm/walk.h"
#include "sjme/nvm/walkCoreDump.h"

#define SJME_SOFT_OKAY

static sjme_errorCode sjme_nvm_walk_coreArrayClose(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState)
{
	if (coreState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: ],");
#endif

#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreMapClose(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState)
{
	if (coreState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: },");
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreMapOpen(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState)
{
	if (coreState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: {");
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreKeyPutI(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInValue sjme_jint inValue)
{
	if (coreState == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: \"%s\": %d,", inKey, inValue);
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreKeyPutP(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInValue sjme_intPointer inValue)
{
	if (coreState == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: \"%s\": %p,", inKey, (void*)inValue);
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreKeyPutArray(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState,
	sjme_attrInNotNull sjme_lpcstr inKey)
{
	if (coreState == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: \"%s\": [", inKey);
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_nvm_walk_coreKeyPutS(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNullable sjme_lpcstr inValue)
{
	if (coreState == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: \"%s\": %s%s%s,", inKey,
		(inValue == NULL ? "" : "\""),
		(inValue == NULL ? "null" : inValue),
		(inValue == NULL ? "" : "\""));
#endif
	
#if defined(SJME_SOFT_OKAY)
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

/** Close array. */
#define sjme_nvm_walk_coreArrayCloseR() \
	if (sjme_error_is(error = sjme_nvm_walk_coreArrayClose(coreState))) \
		return sjme_error_default(error)

/** Close map. */
#define sjme_nvm_walk_coreMapCloseR() \
	if (sjme_error_is(error = sjme_nvm_walk_coreMapClose(coreState))) \
		return sjme_error_default(error)

/** Open map. */
#define sjme_nvm_walk_coreMapOpenR() \
	if (sjme_error_is(error = sjme_nvm_walk_coreMapOpen(coreState))) \
		return sjme_error_default(error)

/** Put key array value. */
#define sjme_nvm_walk_coreKeyPutArrayR(name) \
	if (sjme_error_is(error = sjme_nvm_walk_coreKeyPutArray(coreState, \
		(name)))) \
		return sjme_error_default(error)

/** Put key value. */
#define sjme_nvm_walk_coreKeyPutIR(name, value) \
	if (sjme_error_is(error = sjme_nvm_walk_coreKeyPutI(coreState, \
		(name), (value)))) \
		return sjme_error_default(error)

/** Put key value. */
#define sjme_nvm_walk_coreKeyPutPR(name, value) \
	if (sjme_error_is(error = sjme_nvm_walk_coreKeyPutP(coreState, \
		(name), (sjme_intPointer)(value)))) \
		return sjme_error_default(error)

/** Put key value. */
#define sjme_nvm_walk_coreKeyPutSR(name, value) \
	if (sjme_error_is(error = sjme_nvm_walk_coreKeyPutS(coreState, \
		(name), (value)))) \
		return sjme_error_default(error)

typedef struct sjme_nvm_walk_coreHandler
{
	/** The handler type ID. */
	sjme_jint typeId;

	/** The function to use for handling. */
	sjme_nvm_walk_stepHandlerFunc function;
} sjme_nvm_walk_coreHandler;

#define SJME_WALK_CORE_END() \
	{ \
		sjme_sm(.typeId, 0), \
		sjme_sm(.function, NULL), \
	}

static const sjme_nvm_walk_coreHandler sjme_nvm_walk_coreHandlers[] =
{
	SJME_WALK_CORE_END()
};

static sjme_errorCode sjme_nvm_walk_coreRecordP(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState,
	sjme_attrInNotNull sjme_pointer pointer,
	sjme_attrInValue sjme_jint typeId)
{
	sjme_errorCode error;
	sjme_nvm_walk_pointerChain* chain;
	sjme_nvm_walk_pointerChain* freeChain;
	sjme_nvm_walk_pointerChain* lastChain;
	sjme_nvm_walk_pointerLink* link;
	sjme_jint i, lastFree;
	
	if (coreState == NULL || pointer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Used to quickly place in a free link slot. */
	freeChain = NULL;
	lastChain = NULL;
	lastFree = -1;

	/* Look in the pointer chain. */
	chain = coreState->chain;
	while (chain != NULL)
	{
		/* If this is found in the chain, skip it. */
		for (i = 0; i < SJME_NVM_WALK_CHAIN_SIZE; i++)
		{
			/* Is this the one? */
			if (chain->links[i].pointer == pointer)
			{
				/* If this is the same type then we added it. */
				if (chain->links[i].typeId == typeId)
					return SJME_ERROR_WALK_SKIP_ELEMENTS;

				/* Otherwise, indicate it exists under a different type. */
				return SJME_ERROR_ELEMENT_EXISTS;
			}

			/* Record free slot for quicker placement. */
			if (chain->links[i].pointer == NULL && freeChain == NULL)
			{
				freeChain = chain;
				lastFree = i;
			}
		}
		
		/* Go to the next chain. */
		lastChain = chain;
		chain = chain->next;
	}

	/* Need to add a new chain? */
	if (lastFree < 0 || freeChain == NULL)
	{
		/* Allocate new chain. */
		freeChain = NULL;
		if (sjme_error_is(error = sjme_alloc(coreState->allocPool,
			sizeof(*freeChain), (sjme_pointer*)&freeChain)) ||
			freeChain == NULL)
			return sjme_error_default(error);

		/* Free slot is always the first item! */
		lastFree = 0;

		/* Link at the end. */
		if (lastChain != NULL)
		{
			lastChain->next = freeChain;
			freeChain->prev = lastChain;
		}

		/* Freshly linked! */
		else
			coreState->chain = freeChain;
	}

	/* Store item here. */
	link = &freeChain->links[lastFree];
	link->pointer = pointer;
	link->typeId = typeId;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_coreStart(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
	sjme_errorCode error;
	sjme_nvm_walk_coreState* coreState;
	const sjme_nvm_walk_coreHandler* handler;
	sjme_jboolean shallowOpen;
	
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Parent got unbound somehow? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Recover state. */
	coreState = at->data;
	if (coreState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Did we fall out of a substructure? */
	if (at->depth < coreState->lastDepth)
	{
		/* Closing element list? */
		if (coreState->openElements)
		{
			/* Close the array. */
			sjme_nvm_walk_coreArrayCloseR();

			/* Elements no longer open. */
			coreState->openElements = SJME_JNI_FALSE;
		}
		
		/* Continue at parent. */
		sjme_nvm_walk_coreKeyPutIR("up",
			at->depth);

		/* Record new parent's depth. */
		coreState->lastDepth = at->depth;
	}

	/* Writing start of structure? */
	if (at->index < 0)
	{
		/* Writing end of skipped. */
		if (at->index == INT32_MIN)
		{
			/* Nothing is written here as the pointer record indicated a */
			/* skip, hence no new structure was ever declared. */
			
			/* Success! */
			return SJME_ERROR_NONE;
		}

		/* Record a new pointer. */
		shallowOpen = SJME_JNI_FALSE;
		if (sjme_error_is(error = sjme_nvm_walk_coreRecordP(
			coreState, at->base.raw, at->typeId)))
		{
			/* If this was already recorded, then we shallow open it. */
			if (error == SJME_ERROR_ELEMENT_EXISTS)
				shallowOpen = SJME_JNI_TRUE;
			else
				return sjme_error_default(error);
		}

		/* If the elements are open, we jumped into a sub-structure. */
		if (coreState->openElements)
		{
			/* Open map. */
			sjme_nvm_walk_coreMapOpenR();
			
			/* Write structure details. */
			sjme_nvm_walk_coreKeyPutIR("typeId",
				at->typeId);
			sjme_nvm_walk_coreKeyPutIR("down",
				at->uniqueId);
			
			/* Close map. */
			sjme_nvm_walk_coreMapCloseR();
			
			/* Close the array. */
			sjme_nvm_walk_coreArrayCloseR();
		
			/* Close map. */
			sjme_nvm_walk_coreMapCloseR();

			/* Elements no longer open. */
			coreState->openElements = SJME_JNI_FALSE;
		}
		
		/* Open map. */
		sjme_nvm_walk_coreMapOpenR();

		/* Write structure details. */
		sjme_nvm_walk_coreKeyPutIR("typeId",
			at->typeId);

		/* This is extra pointless information that we do not need. */
		if (!shallowOpen)
		{
			sjme_nvm_walk_coreKeyPutPR("pointer",
				at->base.raw);
			sjme_nvm_walk_coreKeyPutSR("typeName",
				at->inSelect->typeName);
			sjme_nvm_walk_coreKeyPutIR("itemId",
				++coreState->itemId);
		}
		
		/* Open array for entries. */
		sjme_nvm_walk_coreKeyPutArrayR("elements");
		coreState->openElements = SJME_JNI_TRUE;

		/* Detect depth changes. */
		coreState->lastDepth = at->depth;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Writing end of structure? */
	else if (at->index == INT32_MAX)
	{
		/* Close the elements array. */
		if (coreState->openElements)
		{
			/* Close the array. */
			sjme_nvm_walk_coreArrayCloseR();

			/* Elements no longer open. */
			coreState->openElements = SJME_JNI_FALSE;
		}
		
		/* Close map. */
		sjme_nvm_walk_coreMapCloseR();
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Find handler for the item. */
	handler = &sjme_nvm_walk_coreHandlers[0];
	while (handler->function != NULL)
		if (handler->typeId == at->typeId)
			break;

	/* Not found? */
	if (handler == NULL || handler->function == NULL)
	{
		sjme_todo("Impl? %d", at->typeId);
		return sjme_error_notImplemented(0);
	}

	/* Call handler. */
	if (sjme_error_is(error = handler->function(root, parent, at)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

const sjme_nvm_walk_functions sjme_nvm_walk_coreDumpFunctions =
{
	sjme_sm(.pre, NULL),
	sjme_sm(.step, sjme_nvm_walk_coreStart),
};

sjme_errorCode sjme_nvm_walk_coreDumpFile(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr filePath)
{
#define MINI_SIZE 8192
	sjme_errorCode error;
	sjme_jubyte mini[MINI_SIZE];
	sjme_alloc_pool miniPool;
	sjme_seekable seekable;
	sjme_stream_output outStream;
	
	if (inState == NULL || filePath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use the state NAL? */
	if (nal == NULL)
		nal = inState->nal;

	if (nal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Not supported? */
	if (nal->fileOpen == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;

	/* Allocate mini allocation pool. */
	memset(mini, 0, sizeof(mini));
	miniPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitStatic(&miniPool,
		mini, MINI_SIZE)) || miniPool == NULL)
		return sjme_error_default(error);

	/* Open native file. */
	seekable = NULL;
	if (sjme_error_is(error = nal->fileOpen(miniPool, filePath,
		&seekable, SJME_NAL_OPEN_WRITE_TRUNCATE)) || seekable == NULL)
		return sjme_error_default(error);

	/* Open output stream over the seekable. */
	outStream = NULL;
	if (sjme_error_is(error = sjme_stream_outputOpenSeekable(
		seekable, &outStream, 0, -1, SJME_JNI_TRUE)) ||
		outStream == NULL)
		goto fail_openSub;

	/* Perform the dump. */
	if (sjme_error_is(error = sjme_nvm_walk_coreDumpStream(
		miniPool, inState, outStream)))
		goto fail_dump;

	/* Close the seekable. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(seekable))))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_dump:
fail_openSub:
	if (seekable != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(seekable));
	
	return sjme_error_default(error);
#undef MINI_SIZE
}

sjme_errorCode sjme_nvm_walk_coreDumpStream(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_stream_output outStream)
{
	sjme_errorCode error;
	sjme_nvm_walk_coreState coreState;
	
	if (allocPool == NULL || inState == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Initialize state. */
	memset(&coreState, 0, sizeof(coreState));
	coreState.allocPool = allocPool;
	coreState.out = outStream;

	/* Perform the core dump. */
	if (sjme_error_is(error = sjme_nvm_walk_start(inState,
		SJME_NVM_STRUCT_STATE,
		&sjme_nvm_walk_coreDumpFunctions, &coreState)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}
