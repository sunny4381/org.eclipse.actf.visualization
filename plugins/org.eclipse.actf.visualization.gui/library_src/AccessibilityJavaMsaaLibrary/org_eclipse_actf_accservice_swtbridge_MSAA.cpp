/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
#include "stdafx.h"
#include "org_eclipse_actf_accservice_swtbridge_MSAA.h"

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_AccessibleObjectFromPoint
  (JNIEnv *env, jclass that, jint x, jint y, jint pvarChild)
{
	IAccessible *pAcc = NULL;
	POINT ptScreen = {x,y};
	if( FAILED( AccessibleObjectFromPoint(ptScreen,&pAcc,(VARIANT*)pvarChild) ) ) {
		return NULL;
	}
	return (jint)pAcc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_AccessibleObjectFromWindow
  (JNIEnv *env, jclass that, jint hwnd)
{
	void *pObject = NULL;
//	if( FAILED( AccessibleObjectFromWindow((HWND)hwnd,OBJID_CLIENT,IID_IAccessible,&pObject) ) ) {
	if( FAILED( AccessibleObjectFromWindow((HWND)hwnd,OBJID_WINDOW,IID_IAccessible,&pObject) ) ) {
		return NULL;
	}
	return (jint)pObject;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_AccessibleChildren
  (JNIEnv *env, jclass that, jint paccContainer, jint iChildStart, jint cChildren, jint rgvarChildren)
{
	LONG count = 0;
	if( FAILED( AccessibleChildren((IAccessible*)paccContainer,iChildStart,cChildren,(VARIANT*)rgvarChildren,&count) ) ) {
		return 0;
	}
	return (jint)count;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_WindowFromAccessibleObject
  (JNIEnv *env, jclass that, jint pAcc)
{
	HWND hwnd = NULL;
	if( FAILED(WindowFromAccessibleObject((IAccessible*)pAcc,&hwnd)) ) {
		return NULL;
	}
	return (jint)hwnd;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_GetRoleText
  (JNIEnv *env, jclass that, jint lRole, jcharArray szRole, jint cchRoleMax)
{
	int count = 0;
	jchar *lpRole = NULL;
	if( szRole ) {
		lpRole = env->GetCharArrayElements(szRole,NULL);
	}
	count = (jint)GetRoleTextW(lRole, (LPWSTR)lpRole, cchRoleMax);
	if( lpRole ) {
		env->ReleaseCharArrayElements(szRole,lpRole,0);
	}
	return count;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_GetStateText
  (JNIEnv *env, jclass that, jint lStateBit, jcharArray szState, jint cchState)
{
	jint count = 0;
	jchar *lpState = NULL;
	if( szState ) {
		lpState = env->GetCharArrayElements(szState,NULL);
	}
	count = (jint)GetStateTextW(lStateBit, (LPWSTR)lpState, cchState);
	if( lpState ) {
		env->ReleaseCharArrayElements(szState,lpState,0);
	}
	return count;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_AccessibleObjectFromEvent
  (JNIEnv *env, jclass that, jint hwnd, jint dwId, jint dwChildId, jint pvarChild)
{
	IAccessible *pAcc = NULL;
	if( FAILED( AccessibleObjectFromEvent((HWND)hwnd, dwId, dwChildId, &pAcc, (VARIANT*)pvarChild) ) ) {
		return NULL;
	}
	return (jint)pAcc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_SetWinEventHook
  (JNIEnv *env, jclass that, jint eventMin, jint eventMax, jint hmodWinEventProc, jint lpfnWinEventProc, jint idProcess,jint idThread,jint dwFlags)
{
	jint rc = 0;
	rc = (jint)SetWinEventHook(eventMin, eventMax, (HMODULE)hmodWinEventProc, (WINEVENTPROC)lpfnWinEventProc, idProcess, idThread, dwFlags); 
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_UnhookWinEvent
  (JNIEnv *env, jclass that, jint hEvent)
{
	return (jint)UnhookWinEvent((HWINEVENTHOOK)hEvent);
}

static UINT MSG_GETOBJECT = RegisterWindowMessage(TEXT("WM_HTML_GETOBJECT"));

JNIEXPORT jint JNICALL Java_org_eclipse_actf_accservice_swtbridge_MSAA_HTMLDocumentFromWindow
  (JNIEnv *env, jclass that, jint hwnd)
{
	void *pObject = NULL;
	LRESULT lRes = NULL;

	SendMessageTimeout((HWND)hwnd, MSG_GETOBJECT, 0L, 0L, SMTO_ABORTIFHUNG, 1000, (DWORD*)&lRes );
	if( lRes==NULL || FAILED( ObjectFromLresult(lRes, IID_IHTMLDocument, 0, &pObject) ) ) {
		return NULL;
	}
	return (jint)pObject;
}

