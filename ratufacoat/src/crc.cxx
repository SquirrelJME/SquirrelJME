/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "crc.h"

/** The initial ZIP reminder. */
#define SJME_ZIP_INITIAL_REMAINDER SJME_JUINT_C(0xFFFFFFFF)

/** The final XOR for the resultant value. */
#define SJME_ZIP_FINAL_XOR SJME_JUINT_C(0xFFFFFFFF)

#if defined(SJME_MEMORYPROFILE_MINIMAL)
	/** Offer a smaller set of bytes for CRC checks. */
	#define SJME_CRC_OFFER_SIZE 512
#else
	/** Offer more bytes for CRC checks. */
	#define SJME_CRC_OFFER_SIZE 4096
#endif

/**
 * CRC Table for standard ZIPs, so we need not calculate these over and
 * over accordingly.
 */
const sjme_juint sjme_crcTableZip[SJME_CRC_TABLE_SIZE] =
{
	SJME_JUINT_C(0x00000000), SJME_JUINT_C(0x04c11db7),
	SJME_JUINT_C(0x09823b6e), SJME_JUINT_C(0x0d4326d9),
	SJME_JUINT_C(0x130476dc), SJME_JUINT_C(0x17c56b6b),
	SJME_JUINT_C(0x1a864db2), SJME_JUINT_C(0x1e475005),
	SJME_JUINT_C(0x2608edb8), SJME_JUINT_C(0x22c9f00f),
	SJME_JUINT_C(0x2f8ad6d6), SJME_JUINT_C(0x2b4bcb61),
	SJME_JUINT_C(0x350c9b64), SJME_JUINT_C(0x31cd86d3),
	SJME_JUINT_C(0x3c8ea00a), SJME_JUINT_C(0x384fbdbd),
	SJME_JUINT_C(0x4c11db70), SJME_JUINT_C(0x48d0c6c7),
	SJME_JUINT_C(0x4593e01e), SJME_JUINT_C(0x4152fda9),
	SJME_JUINT_C(0x5f15adac), SJME_JUINT_C(0x5bd4b01b),
	SJME_JUINT_C(0x569796c2), SJME_JUINT_C(0x52568b75),
	SJME_JUINT_C(0x6a1936c8), SJME_JUINT_C(0x6ed82b7f),
	SJME_JUINT_C(0x639b0da6), SJME_JUINT_C(0x675a1011),
	SJME_JUINT_C(0x791d4014), SJME_JUINT_C(0x7ddc5da3),
	SJME_JUINT_C(0x709f7b7a), SJME_JUINT_C(0x745e66cd),
	SJME_JUINT_C(0x9823b6e0), SJME_JUINT_C(0x9ce2ab57),
	SJME_JUINT_C(0x91a18d8e), SJME_JUINT_C(0x95609039),
	SJME_JUINT_C(0x8b27c03c), SJME_JUINT_C(0x8fe6dd8b),
	SJME_JUINT_C(0x82a5fb52), SJME_JUINT_C(0x8664e6e5),
	SJME_JUINT_C(0xbe2b5b58), SJME_JUINT_C(0xbaea46ef),
	SJME_JUINT_C(0xb7a96036), SJME_JUINT_C(0xb3687d81),
	SJME_JUINT_C(0xad2f2d84), SJME_JUINT_C(0xa9ee3033),
	SJME_JUINT_C(0xa4ad16ea), SJME_JUINT_C(0xa06c0b5d),
	SJME_JUINT_C(0xd4326d90), SJME_JUINT_C(0xd0f37027),
	SJME_JUINT_C(0xddb056fe), SJME_JUINT_C(0xd9714b49),
	SJME_JUINT_C(0xc7361b4c), SJME_JUINT_C(0xc3f706fb),
	SJME_JUINT_C(0xceb42022), SJME_JUINT_C(0xca753d95),
	SJME_JUINT_C(0xf23a8028), SJME_JUINT_C(0xf6fb9d9f),
	SJME_JUINT_C(0xfbb8bb46), SJME_JUINT_C(0xff79a6f1),
	SJME_JUINT_C(0xe13ef6f4), SJME_JUINT_C(0xe5ffeb43),
	SJME_JUINT_C(0xe8bccd9a), SJME_JUINT_C(0xec7dd02d),
	SJME_JUINT_C(0x34867077), SJME_JUINT_C(0x30476dc0),
	SJME_JUINT_C(0x3d044b19), SJME_JUINT_C(0x39c556ae),
	SJME_JUINT_C(0x278206ab), SJME_JUINT_C(0x23431b1c),
	SJME_JUINT_C(0x2e003dc5), SJME_JUINT_C(0x2ac12072),
	SJME_JUINT_C(0x128e9dcf), SJME_JUINT_C(0x164f8078),
	SJME_JUINT_C(0x1b0ca6a1), SJME_JUINT_C(0x1fcdbb16),
	SJME_JUINT_C(0x018aeb13), SJME_JUINT_C(0x054bf6a4),
	SJME_JUINT_C(0x0808d07d), SJME_JUINT_C(0x0cc9cdca),
	SJME_JUINT_C(0x7897ab07), SJME_JUINT_C(0x7c56b6b0),
	SJME_JUINT_C(0x71159069), SJME_JUINT_C(0x75d48dde),
	SJME_JUINT_C(0x6b93dddb), SJME_JUINT_C(0x6f52c06c),
	SJME_JUINT_C(0x6211e6b5), SJME_JUINT_C(0x66d0fb02),
	SJME_JUINT_C(0x5e9f46bf), SJME_JUINT_C(0x5a5e5b08),
	SJME_JUINT_C(0x571d7dd1), SJME_JUINT_C(0x53dc6066),
	SJME_JUINT_C(0x4d9b3063), SJME_JUINT_C(0x495a2dd4),
	SJME_JUINT_C(0x44190b0d), SJME_JUINT_C(0x40d816ba),
	SJME_JUINT_C(0xaca5c697), SJME_JUINT_C(0xa864db20),
	SJME_JUINT_C(0xa527fdf9), SJME_JUINT_C(0xa1e6e04e),
	SJME_JUINT_C(0xbfa1b04b), SJME_JUINT_C(0xbb60adfc),
	SJME_JUINT_C(0xb6238b25), SJME_JUINT_C(0xb2e29692),
	SJME_JUINT_C(0x8aad2b2f), SJME_JUINT_C(0x8e6c3698),
	SJME_JUINT_C(0x832f1041), SJME_JUINT_C(0x87ee0df6),
	SJME_JUINT_C(0x99a95df3), SJME_JUINT_C(0x9d684044),
	SJME_JUINT_C(0x902b669d), SJME_JUINT_C(0x94ea7b2a),
	SJME_JUINT_C(0xe0b41de7), SJME_JUINT_C(0xe4750050),
	SJME_JUINT_C(0xe9362689), SJME_JUINT_C(0xedf73b3e),
	SJME_JUINT_C(0xf3b06b3b), SJME_JUINT_C(0xf771768c),
	SJME_JUINT_C(0xfa325055), SJME_JUINT_C(0xfef34de2),
	SJME_JUINT_C(0xc6bcf05f), SJME_JUINT_C(0xc27dede8),
	SJME_JUINT_C(0xcf3ecb31), SJME_JUINT_C(0xcbffd686),
	SJME_JUINT_C(0xd5b88683), SJME_JUINT_C(0xd1799b34),
	SJME_JUINT_C(0xdc3abded), SJME_JUINT_C(0xd8fba05a),
	SJME_JUINT_C(0x690ce0ee), SJME_JUINT_C(0x6dcdfd59),
	SJME_JUINT_C(0x608edb80), SJME_JUINT_C(0x644fc637),
	SJME_JUINT_C(0x7a089632), SJME_JUINT_C(0x7ec98b85),
	SJME_JUINT_C(0x738aad5c), SJME_JUINT_C(0x774bb0eb),
	SJME_JUINT_C(0x4f040d56), SJME_JUINT_C(0x4bc510e1),
	SJME_JUINT_C(0x46863638), SJME_JUINT_C(0x42472b8f),
	SJME_JUINT_C(0x5c007b8a), SJME_JUINT_C(0x58c1663d),
	SJME_JUINT_C(0x558240e4), SJME_JUINT_C(0x51435d53),
	SJME_JUINT_C(0x251d3b9e), SJME_JUINT_C(0x21dc2629),
	SJME_JUINT_C(0x2c9f00f0), SJME_JUINT_C(0x285e1d47),
	SJME_JUINT_C(0x36194d42), SJME_JUINT_C(0x32d850f5),
	SJME_JUINT_C(0x3f9b762c), SJME_JUINT_C(0x3b5a6b9b),
	SJME_JUINT_C(0x0315d626), SJME_JUINT_C(0x07d4cb91),
	SJME_JUINT_C(0x0a97ed48), SJME_JUINT_C(0x0e56f0ff),
	SJME_JUINT_C(0x1011a0fa), SJME_JUINT_C(0x14d0bd4d),
	SJME_JUINT_C(0x19939b94), SJME_JUINT_C(0x1d528623),
	SJME_JUINT_C(0xf12f560e), SJME_JUINT_C(0xf5ee4bb9),
	SJME_JUINT_C(0xf8ad6d60), SJME_JUINT_C(0xfc6c70d7),
	SJME_JUINT_C(0xe22b20d2), SJME_JUINT_C(0xe6ea3d65),
	SJME_JUINT_C(0xeba91bbc), SJME_JUINT_C(0xef68060b),
	SJME_JUINT_C(0xd727bbb6), SJME_JUINT_C(0xd3e6a601),
	SJME_JUINT_C(0xdea580d8), SJME_JUINT_C(0xda649d6f),
	SJME_JUINT_C(0xc423cd6a), SJME_JUINT_C(0xc0e2d0dd),
	SJME_JUINT_C(0xcda1f604), SJME_JUINT_C(0xc960ebb3),
	SJME_JUINT_C(0xbd3e8d7e), SJME_JUINT_C(0xb9ff90c9),
	SJME_JUINT_C(0xb4bcb610), SJME_JUINT_C(0xb07daba7),
	SJME_JUINT_C(0xae3afba2), SJME_JUINT_C(0xaafbe615),
	SJME_JUINT_C(0xa7b8c0cc), SJME_JUINT_C(0xa379dd7b),
	SJME_JUINT_C(0x9b3660c6), SJME_JUINT_C(0x9ff77d71),
	SJME_JUINT_C(0x92b45ba8), SJME_JUINT_C(0x9675461f),
	SJME_JUINT_C(0x8832161a), SJME_JUINT_C(0x8cf30bad),
	SJME_JUINT_C(0x81b02d74), SJME_JUINT_C(0x857130c3),
	SJME_JUINT_C(0x5d8a9099), SJME_JUINT_C(0x594b8d2e),
	SJME_JUINT_C(0x5408abf7), SJME_JUINT_C(0x50c9b640),
	SJME_JUINT_C(0x4e8ee645), SJME_JUINT_C(0x4a4ffbf2),
	SJME_JUINT_C(0x470cdd2b), SJME_JUINT_C(0x43cdc09c),
	SJME_JUINT_C(0x7b827d21), SJME_JUINT_C(0x7f436096),
	SJME_JUINT_C(0x7200464f), SJME_JUINT_C(0x76c15bf8),
	SJME_JUINT_C(0x68860bfd), SJME_JUINT_C(0x6c47164a),
	SJME_JUINT_C(0x61043093), SJME_JUINT_C(0x65c52d24),
	SJME_JUINT_C(0x119b4be9), SJME_JUINT_C(0x155a565e),
	SJME_JUINT_C(0x18197087), SJME_JUINT_C(0x1cd86d30),
	SJME_JUINT_C(0x029f3d35), SJME_JUINT_C(0x065e2082),
	SJME_JUINT_C(0x0b1d065b), SJME_JUINT_C(0x0fdc1bec),
	SJME_JUINT_C(0x3793a651), SJME_JUINT_C(0x3352bbe6),
	SJME_JUINT_C(0x3e119d3f), SJME_JUINT_C(0x3ad08088),
	SJME_JUINT_C(0x2497d08d), SJME_JUINT_C(0x2056cd3a),
	SJME_JUINT_C(0x2d15ebe3), SJME_JUINT_C(0x29d4f654),
	SJME_JUINT_C(0xc5a92679), SJME_JUINT_C(0xc1683bce),
	SJME_JUINT_C(0xcc2b1d17), SJME_JUINT_C(0xc8ea00a0),
	SJME_JUINT_C(0xd6ad50a5), SJME_JUINT_C(0xd26c4d12),
	SJME_JUINT_C(0xdf2f6bcb), SJME_JUINT_C(0xdbee767c),
	SJME_JUINT_C(0xe3a1cbc1), SJME_JUINT_C(0xe760d676),
	SJME_JUINT_C(0xea23f0af), SJME_JUINT_C(0xeee2ed18),
	SJME_JUINT_C(0xf0a5bd1d), SJME_JUINT_C(0xf464a0aa),
	SJME_JUINT_C(0xf9278673), SJME_JUINT_C(0xfde69bc4),
	SJME_JUINT_C(0x89b8fd09), SJME_JUINT_C(0x8d79e0be),
	SJME_JUINT_C(0x803ac667), SJME_JUINT_C(0x84fbdbd0),
	SJME_JUINT_C(0x9abc8bd5), SJME_JUINT_C(0x9e7d9662),
	SJME_JUINT_C(0x933eb0bb), SJME_JUINT_C(0x97ffad0c),
	SJME_JUINT_C(0xafb010b1), SJME_JUINT_C(0xab710d06),
	SJME_JUINT_C(0xa6322bdf), SJME_JUINT_C(0xa2f33668),
	SJME_JUINT_C(0xbcb4666d), SJME_JUINT_C(0xb8757bda),
	SJME_JUINT_C(0xb5365d03), SJME_JUINT_C(0xb1f740b4)
};

/**
 * Initializes the CRC calculator.
 *
 * @param outCrcState The output CRC state.
 * @param crcTable The CRC table to use.
 * @param reflectData Reflect the CRC data?
 * @param reflectRemainder Reflect the remainder value?
 * @param initialRemainder The initial remainder value.
 * @param finalXor The final XOR value.
 * @param error The error state, if any.
 * @return If initialization was a success or not.
 * @since 2021/11/13
 */
static sjme_jboolean sjme_crcInitInternal(sjme_crcState* outCrcState,
	const sjme_juint (*crcTable)[SJME_CRC_TABLE_SIZE],
	sjme_jboolean reflectData, sjme_jboolean reflectRemainder,
	sjme_juint initialRemainder, sjme_juint finalXor,
	sjme_error* error)
{
	if (outCrcState == NULL || crcTable == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Clear the initial state. */
	memset(outCrcState, 0, sizeof(*outCrcState));

	/* Setup values within. */
	outCrcState->crcTable = crcTable;
	outCrcState->reflectData = reflectData;
	outCrcState->reflectRemainder = reflectRemainder;
	outCrcState->finalXor = finalXor;
	outCrcState->currentRemainder = initialRemainder;

	return sjme_true;
}

/**
 * Reverses the bits in the given integer.
 *
 * @param val The value to reverse.
 * @return The value with the reversed bits.
 */
static inline sjme_juint sjme_reverseUInt(sjme_juint val)
{
	/* Move all the various bits around. */
	val = (((val & SJME_JUINT_C(0xAAAAAAAA)) >> SJME_JUINT_C(1)) |
		((val & SJME_JUINT_C(0x55555555)) << 1));
	val = (((val & SJME_JUINT_C(0xCCCCCCCC)) >> SJME_JUINT_C(2)) |
		((val & SJME_JUINT_C(0x33333333)) << 2));
	val = (((val & SJME_JUINT_C(0xF0F0F0F0)) >> SJME_JUINT_C(4)) |
		((val & SJME_JUINT_C(0x0F0F0F0F)) << 4));
	val = (((val & SJME_JUINT_C(0xFF00FF00)) >> SJME_JUINT_C(8)) |
		((val & SJME_JUINT_C(0x00FF00FF)) << 8));

	/* Perform final bit shifts. */
	return ((val >> SJME_JUINT_C(16)) | (val << SJME_JUINT_C(16)));
}

sjme_jboolean sjme_crcChecksum(sjme_crcState* crcState,
	sjme_juint* outChecksum, sjme_error* error)
{
	if (crcState == NULL || outChecksum == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Return the calculated checksum with any reflections accordingly. */
	*outChecksum = (crcState->reflectRemainder ?
		sjme_reverseUInt(crcState->currentRemainder) :
		crcState->currentRemainder) ^ crcState->finalXor;
	return sjme_true;
}

sjme_jboolean sjme_crcInitZip(sjme_crcState* outCrcState, sjme_error* error)
{
	return sjme_crcInitInternal(outCrcState, &sjme_crcTableZip,
		sjme_true, sjme_true,
		SJME_ZIP_INITIAL_REMAINDER, SJME_ZIP_FINAL_XOR,
		error);
}

sjme_jboolean sjme_crcOfferChunk(sjme_crcState* crcState,
	const sjme_memChunk* chunk, sjme_jint off, sjme_jint len,
	sjme_error* error)
{
	void* realPointer;

	if (crcState == NULL || chunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Has to be within bounds! */
	realPointer = NULL;
	if (!sjme_chunkCheckBound(chunk, off, len, error) ||
		!sjme_chunkRealPointer(chunk, off, &realPointer, error))
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS,
			sjme_getError(error, (sjme_errorCode)off));

		return sjme_false;
	}

	/* Call the direct offer. */
	return sjme_crcOfferDirect(crcState, realPointer, len, error);
}

sjme_jboolean sjme_crcOfferDirect(sjme_crcState* crcState,
	const void* data, sjme_jint len,
	sjme_error* error)
{
	sjme_jboolean reflectData;
	sjme_juint remainder, i, d, val;
	const sjme_jubyte* uData;
	const sjme_juint *crcTable;

	if (crcState == NULL || data == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (len < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, len);

		return sjme_false;
	}

	/* Setup state. */
	uData = (const sjme_jubyte*)data;
	reflectData = crcState->reflectData;
	remainder = crcState->currentRemainder;
	crcTable = &(*crcState->crcTable)[0];

	/* Read data into the work buffer. */
	for (i = 0; i < len; i++)
	{
		/* Read in data value */
		val = uData[i] & 0xFF;

		/* Reflect the data? */
		if (reflectData)
			val = sjme_reverseUInt(val) >> 24;

		d = (val ^ (remainder >> 24));
		remainder = crcTable[d] ^ (remainder << 8);
	}

	/* Set new remainder */
	crcState->currentRemainder = remainder;

	/* Calculated fragment okay. */
	return sjme_true;
}

sjme_jboolean sjme_crcOfferStream(sjme_crcState* crcState,
	sjme_dataStream* stream, sjme_jint len, sjme_jint* readLen,
	sjme_error* error)
{
	sjme_jubyte offerBuf[SJME_CRC_OFFER_SIZE];
	sjme_jint bufRead, readLimit, readTotal;

	if (crcState == NULL || stream == NULL || readLen == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (len < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, len);

		return sjme_false;
	}

	/* Continuously pump bytes in until EOF. */
	readTotal = 0;
	while (len > 0)
	{
		/* Setup for the next read. */
		bufRead = -1;
		readLimit = sjme_min(len, SJME_CRC_OFFER_SIZE);
		memset(offerBuf, 0, sizeof(offerBuf));

		/* Perform the read. */
		if (!sjme_streamRead(stream, offerBuf, readLimit,
			&bufRead, error))
		{
			sjme_setError(error, SJME_ERROR_READ_ERROR, 0);

			return sjme_false;
		}

		/* EOF? */
		if (bufRead < 0)
			break;

		/* Offer the data. */
		if (!sjme_crcOfferDirect(crcState, offerBuf, bufRead, error))
		{
			sjme_setError(error, SJME_ERROR_CALCULATE_ERROR, 0);

			return sjme_false;
		}

		/* Move data around. */
		readTotal += bufRead;
		len -= bufRead;
	}

	/* Report the number of bytes read. */
	*readLen = readTotal;
	return sjme_true;
}
