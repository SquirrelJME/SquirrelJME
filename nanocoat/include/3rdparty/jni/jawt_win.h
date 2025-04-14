#ifndef _JAVASOFT_JAWT_MD_H_
#define _JAVASOFT_JAWT_MD_H_

#include <windows.h>
#include "jawt.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Microsoft Windows specific declarations for AWT native interface.
 * See notes in jawt.h for an example of use.
 */
typedef struct jawt_Win32DrawingSurfaceInfo {
    /* Native window, DDB, or DIB handle */
    union {
        HWND hwnd;
        HBITMAP hbitmap;
        void* pbits;
    };
    /*
     * This HDC should always be used instead of the HDC returned from
     * BeginPaint() or any calls to GetDC().
     */
    HDC hdc;
    HPALETTE hpalette;
} JAWT_Win32DrawingSurfaceInfo;

#ifdef __cplusplus
}
#endif

#endif /* !_JAVASOFT_JAWT_MD_H_ */
