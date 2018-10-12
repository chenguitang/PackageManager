package com.posin.packagesmanager.utils.shell;

import android.util.Log;

import java.io.IOException;

public class SuShell extends ShellProc {

	private static final String TAG = "SuShell";
	
	protected static final String CMD_FINISH = "@@result=";
	
	protected static Boolean mExecuteDone = false;
	protected static int mExecuteResult = 0;

	protected static volatile SuShell mInstance = new SuShell();
	
	protected SuShell() {
		super("SuShell");
	}
	
	public static SuShell get() {
		return mInstance;
	}

	@Override
	protected boolean onReceivedLine(String line) {
		//Log.d("** su shell", line);
		if(line.startsWith(CMD_FINISH)) {
			try {
				mExecuteResult = Integer.parseInt(line.substring(CMD_FINISH.length()));
			} catch (Throwable e) {
			}
			
			synchronized(mExecuteDone) {
				mExecuteDone.notify();
			}
			return false; // do not add to mOutput
		}
		return true; // add to mOutput
	}

	public static int exec(String cmd, StringBuilder output, int timeout) throws IOException {
		if(timeout > 0) {
			mExecuteResult = 1;
			mInstance.clearOutput();
		}
		
		Log.d(TAG, "exec cmd : " + cmd);
		Log.d(TAG, "timeout : " + timeout);
		mInstance.exec(cmd + "\necho " + CMD_FINISH + "$?\n");

		if(timeout > 0) {
			synchronized(mExecuteDone) {
				if(!mExecuteDone) {
					try {
						mExecuteDone.wait(timeout);
					} catch (Throwable e) {
					}
				}
			}

			if(output != null)
				mInstance.getOutput(output);

			return mExecuteResult;
		}
		return 0;
	}

	/*
	public static int exec(String cmd, List<String> output, int timeout) throws IOException {
		if(timeout > 0) {
			mExecuteResult = 1;
			mInstance.clearOutput();
		}
		
		mInstance.exec(cmd + "\n" + CMD_FINISH + "$?\n");

		if(timeout > 0) {
			synchronized(mExecuteDone) {
				if(!mExecuteDone) {
					try {
						mExecuteDone.wait(timeout);
					} catch (Throwable e) {
					}
				}
			}

			if(output != null)
				mInstance.getOutput(output);

			return mExecuteResult;
		}
		return 0;
	}
	*/
}
