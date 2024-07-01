/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#if pencilPixelBits == 32 || pencilPixelBits == 16 || pencilPixelBits == 8
	#define pencilDecl \
		sjme_jint i	
	
	#define pencilMove(x, y) \
		i = (((y) * (sl)) + (x))
	#define pencilIncr() \
		i = i + 1;
#elif pencilPixelBits == 4 || pencilPixelBits == 2 || pencilPixelBits == 1
	#define pencilDecl \
		sjme_jint i; \
		sjme_jint im

	#define pencilMove(x, y) \
		do { i = (((y) * (sl)) + (x)); \
        im = pencilPixelMask * (\
		} while(0) 
	#define pencilIncr() \
		do { i = i + 1; \
        im = im << pencilPixelBits; \
        i = i + ( \
		} while(0)
#else
	#error No raw pencilPutPixel
#endif
