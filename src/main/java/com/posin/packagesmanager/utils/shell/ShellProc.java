package com.posin.packagesmanager.utils.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public abstract class ShellProc extends MyThread {

	public ShellProc(String name) {
		super(name);
	}

	private volatile BufferedReader mReader = null;
	private volatile Process mProc = null;
	
	//private final List<String> mOutput = new ArrayList<String>();
	private final StringBuilder mOutput = new StringBuilder();
	
	public String getOutput() {
		synchronized(mOutput) {
			if(mOutput.length() == 0)
				return null;
			String result = mOutput.toString();
			mOutput.setLength(0);
			return result;
		}
	}
	
	public void getOutput(StringBuilder out) {
		synchronized(mOutput) {
			out.append(mOutput.toString());
			mOutput.setLength(0);
		}
	}
	
	public void getOutput(List<String> out) {
		String s = null;
		synchronized(mOutput) {
			s = mOutput.toString();
			mOutput.setLength(0);
		}

		if(s == null || s.isEmpty())
			return;
		
		String[] ss = s.split("\n");
		if(ss != null && ss.length > 0) {
			for(String t : ss)
				out.add(t);
		}
	}
	
	public void clearOutput() {
		synchronized(mOutput) {
			mOutput.setLength(0);
		}
	}
	
	public synchronized void exec(String cmd) throws IOException {
		if(mProc == null) {
			mProc = createSuProcess();
		}

		mProc.getOutputStream().write((cmd+"\n").getBytes());
		start();
	}

	public static Process createSuProcess() throws IOException {
		File f = new File("/system/xbin/ru");
		if(f.exists()) {
			return Runtime.getRuntime().exec("ru");
		} else {
			//return Runtime.getRuntime().exec("su");
			throw new IOException("error platform");
		}
	}
	
	protected abstract boolean onReceivedLine(String line);
	
	@Override
	protected void onRun() throws IOException {
		String r;
		try {
			mReader = new BufferedReader(new InputStreamReader(mProc.getInputStream(), "UTF-8"));
			while((r = mReader.readLine()) != null) {
				if(onReceivedLine(r)) {
					synchronized(mOutput) {
						//mOutput.add(r);
						mOutput.append(r).append('\n');
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if(mReader != null) {
				try {
					mReader.close();
				} catch (Throwable e) {
				}
				mReader = null;
			}
			
			synchronized(ShellProc.class) {
				mProc.destroy();
				mProc = null;
			}
		}
	}

	@Override
	protected void onExitRequest() {
		if(mReader != null) {
			try {
				mReader.close();
			} catch (Throwable e) {
			}
		}
	}
}
