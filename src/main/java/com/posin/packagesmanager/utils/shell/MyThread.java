package com.posin.packagesmanager.utils.shell;

import android.util.Log;

public abstract class MyThread implements Runnable {

	private static final String TAG = "[Thread]"; 
	
	protected final String mName;
	protected volatile boolean mRunning = false;
	protected volatile boolean mExitPending = false;
		
	public MyThread(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	public boolean exitPending() {
		return mExitPending;
	}
	
	public synchronized boolean isRunning() {
		return mRunning;
	}
	
	public synchronized void start() {
		if(mRunning) {
			return;
		}
		
		mExitPending = false;
		mRunning = true;
		(new Thread(this)).start();
	}
	
	public synchronized void stop() {
		if(!mRunning)
			return;
		
		mExitPending = true;
		onExitRequest();
	}

	@Override
	public void run() {
		Log.d(TAG, mName + " started.");

		mRunning = true;
		
		try {
			try {
				onRun();
			} catch (Throwable e) {
				e.printStackTrace();
				Log.e(TAG, e.toString());
			}

		} finally {
			mRunning = false;
			Log.d(TAG, mName + " stoped.");

			try {
				onTerminated();
			} catch (Throwable e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void onRun() throws Throwable;
	protected abstract void onExitRequest();
	
	protected void onTerminated() {
	}

}
