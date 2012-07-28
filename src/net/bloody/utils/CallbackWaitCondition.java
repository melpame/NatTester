package net.bloody.utils;

public class CallbackWaitCondition<T> {
	
	public interface Callback<T> {
		T check();
	}
	
	public boolean waitFor(Callback<T> callback, T condition, long timeout) {
		
		synchronized (this) {

			final long waitStartTime = System.currentTimeMillis();
			long currentTime = System.currentTimeMillis();
			do {
				if (callback.check().equals(condition))
					return true;
				try {
					this.wait(500);
				} catch (InterruptedException e) {
					if (callback.check().equals(condition))
						return true;
					return false;
				}

				currentTime = System.currentTimeMillis();
			} while ((currentTime - waitStartTime) < timeout);
		}
		
		return false;
	}
}
