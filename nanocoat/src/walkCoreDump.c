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

static sjme_errorCode sjme_nvm_walk_coreArrayOpen(
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState)
{
	if (coreState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageB("JSON: [");
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
	sjme_messageB("JSON: \"%s\": %lld,", inKey,
		(long long)inValue);
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

/** Open array. */
#define sjme_nvm_walk_coreArrayOpenR() \
	if (sjme_error_is(error = sjme_nvm_walk_coreArrayOpen(coreState))) \
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

static sjme_errorCode sjme_nvm_walk_coreMetaType(
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_coreState* coreState)
{
	sjme_errorCode error;
	sjme_jboolean bothPrimitive;
	sjme_javaTypeId stepJavaType;
	
	if (at == NULL || coreState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Both primitive? */
	stepJavaType = (at->inStep != NULL ? at->inStep->javaType :
		SJME_NUM_JAVA_TYPE_IDS);
	bothPrimitive = ((at->typeId == SJME_NVM_WALK_PSEUDO_PRIMITIVE) &&
		(coreState->currentType == SJME_NVM_WALK_PSEUDO_PRIMITIVE));
	
	/* Changing of the structure type? */
	/* Can only switch while in a map state. */
	if ((at->typeId != coreState->currentType ||
		(bothPrimitive && stepJavaType !=
			coreState->currentJavaType)) && coreState->inStructure)
	{
		/* Record the type change always, since this determines */
		/* how the data is to be interpreted, that is what structure it */
		/* goes into ultimately. Order wise, this should always be last! */
		sjme_nvm_walk_coreKeyPutIR("~typeId",
			at->typeId);

		/* Record the Java type as well, if needed. */
		if (at->typeId == SJME_NVM_WALK_PSEUDO_PRIMITIVE)
		{
			sjme_nvm_walk_coreKeyPutIR("~typeIdJava",
				at->inStep->javaType);
		}
		
		/* Now set it, since we are at that type. */
		coreState->currentType = at->typeId;
		coreState->currentJavaType = stepJavaType;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_coreDoGeneric(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
	sjme_errorCode error;
	sjme_nvm_walk_coreState* coreState;
	const sjme_nvm_walk_step* inStep;
	
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover state. */
	coreState = at->data;
	if (coreState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Not a stepped member? */
	inStep = at->inStep;
	if (inStep == NULL)
		return SJME_ERROR_NONE;

	/* Set stored values. */
	sjme_nvm_walk_coreKeyPutSR("memberName", inStep->memberName);

	/* If this is a pointer value, store the pointer value. */
	if (inStep->typeId == SJME_NVM_WALK_PSEUDO_LPSTR)
	{
		sjme_nvm_walk_coreKeyPutSR("string",
			(sjme_lpcstr)at->valueP.intPointer[0]);
	}
	else if (inStep->isPointer)
	{
		sjme_nvm_walk_coreKeyPutPR("pointer", at->valueP.intPointer[0]);
	}

	return SJME_ERROR_NONE;
}

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
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInNotNull sjme_pointer baseStruct,
	sjme_attrInValue sjme_jint typeId)
{
	sjme_errorCode error;
	sjme_nvm_walk_pointerChain* chain;
	sjme_nvm_walk_pointerChain* freeChain;
	sjme_nvm_walk_pointerChain* lastChain;
	sjme_nvm_walk_pointerLink* link;
	sjme_jint i, lastFree;
	
	if (coreState == NULL || base == NULL || baseStruct == NULL)
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
			if (chain->links[i].base == base &&
				chain->links[i].baseStruct == baseStruct)
			{
				/* If this is the same type then we processed it already. */
				if (chain->links[i].typeId == typeId)
					return SJME_ERROR_WALK_SKIP_ELEMENTS;

				/* Otherwise, indicate it exists under a different type. */
				return SJME_ERROR_ELEMENT_EXISTS;
			}

			/* Record free slot for quicker placement. */
			if (chain->links[i].base == NULL && freeChain == NULL)
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
	link->base = base;
	link->baseStruct = baseStruct;
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
	sjme_nvm_walk_stepHandlerFunc handlerFunc;
	
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover state. */
	coreState = at->data;
	if (coreState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("step(%p, %d, %d, %d)",
		at->base.pointer, at->typeId, at->index, at->breadth);
#endif

	/* End of current structure? */
	if (at->index == INT32_MIN || at->index == INT32_MAX)
	{
		coreState->currentBase = NULL;
		coreState->currentDepth = -1;
	}

	/* Handling individual element? */
	if (at->index >= 0 && at->index < INT32_MAX)
	{
		/* Do nothing at the dive level here. */
		if (at->breadth == SJME_NVM_WALK_BREADTH_DIVE)
			return SJME_ERROR_NONE;
		
		/* Find handler for the item. */
		handler = &sjme_nvm_walk_coreHandlers[0];
		while (handler->function != NULL)
			if (handler->typeId == at->typeId)
				break;

		/* Open map for value. */
		sjme_nvm_walk_coreMapOpenR();
		
		/* Did the structure type change? */
		if (sjme_error_is(error = sjme_nvm_walk_coreMetaType(at, coreState)))
			return sjme_error_default(error);

		/* Not found? Use a generic handler which keeps the data opaque. */
		if (handler == NULL || handler->function == NULL)
			handlerFunc = sjme_nvm_walk_coreDoGeneric;
		else
			handlerFunc = handler->function;
		
		/* Call handler. */
		if (sjme_error_is(error = handlerFunc(root, parent, at)))
			return sjme_error_default(error);
		
		/* Close map. */
		sjme_nvm_walk_coreMapCloseR();

		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Did the current base change? We need to wind a new tape head */
	/* so that we can write information on this structure. */
	if (at->base.pointer != coreState->currentBase ||
		at->depth != coreState->currentDepth)
	{
		/* Close the elements and map of the previous item. */
		if (coreState->inStructure)
		{
			/* Close structure. */
			sjme_nvm_walk_coreArrayCloseR();
			sjme_nvm_walk_coreMapCloseR();

			/* No longer in a structure. */
			coreState->inStructure = SJME_JNI_FALSE;
		}

		/* We ended at a structure, do not new/recall an existing one. */
		if (at->index == INT32_MIN || at->index == INT32_MAX)
			return SJME_ERROR_NONE;
		
		/* Record a new pointer, or use pre-existing one. */
		/* Note that we are _IN_ this structure! */
		shallowOpen = SJME_JNI_FALSE;
		if (sjme_error_is(error = sjme_nvm_walk_coreRecordP(
			coreState, at->base.pointer,
			at->baseStruct.pointer, at->typeId)))
		{
			/* If this was already recorded, then we shallow open it. */
			/* We always want to process this at the level point. */
			if (error == SJME_ERROR_ELEMENT_EXISTS ||
				at->breadth == SJME_NVM_WALK_BREADTH_LEVEL)
				shallowOpen = SJME_JNI_TRUE;
			else
				return sjme_error_default(error);
		}

		/* We are in a structure now. */
		coreState->inStructure = SJME_JNI_TRUE;

		/* Regardless of the mode, a new map is opened. */
		sjme_nvm_walk_coreMapOpenR();

		/* We are defining a shiny new structure that we have not seen */
		/* before. */
		if (!shallowOpen)
		{
			/* Need to be more descriptive about what this is. */
			sjme_nvm_walk_coreKeyPutPR("new",
				at->base.pointer);
			sjme_nvm_walk_coreKeyPutIR("itemId",
				++coreState->itemId);

			/* If we are in a list/array, this is a variant. */
			if (at->inSelect != NULL)
				sjme_nvm_walk_coreKeyPutSR("typeName",
					at->inSelect->typeName);
		}

		/* Otherwise, a shallow open means we are working on a structure */
		/* we already know about. */
		else
		{
			/* Recall this pointer specifically. */
			sjme_nvm_walk_coreKeyPutPR("recall",
				at->base.pointer);

			/* If this is recalling to store data into a known structure */
			/* member's data, then recall it. This is a hint */
			if (at->inStep != NULL)
				sjme_nvm_walk_coreKeyPutSR("basis",
					at->inStep->memberName);
		}

		/* Which sub-structure is this for? */
		sjme_nvm_walk_coreKeyPutPR("struct",
			at->baseStruct.pointer);
		
		/* Did the structure type change? */
		if (sjme_error_is(error = sjme_nvm_walk_coreMetaType(at, coreState)))
			return sjme_error_default(error);

		/* Regardless of the mode, we need to store data on the structure. */
		sjme_nvm_walk_coreKeyPutArrayR("data");
		
		/* Store current info. */
		coreState->currentBase = at->base.pointer;
		coreState->currentDepth = at->depth;
	}
	
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
	sjme_nvm_walk_coreState initState;
	sjme_nvm_walk_coreState* coreState;
	
	if (allocPool == NULL || inState == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Initialize state. */
	memset(&initState, 0, sizeof(initState));
	coreState = &initState;
	initState.allocPool = allocPool;
	initState.out = outStream;
	initState.currentBase = 0;
	initState.currentDepth = -1;

	/* Open an array to store all wound data. */
	sjme_nvm_walk_coreArrayOpenR();

	/* Perform the core dump. */
	if (sjme_error_is(error = sjme_nvm_walk_start(inState,
		SJME_NVM_STRUCT_STATE,
		&sjme_nvm_walk_coreDumpFunctions, &initState)))
		return sjme_error_default(error);

	/* Close data winding. */
	sjme_nvm_walk_coreArrayCloseR();

	/* Success! */
	return SJME_ERROR_NONE;
}
