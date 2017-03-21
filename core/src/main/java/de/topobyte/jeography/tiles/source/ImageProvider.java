// Copyright 2016 Sebastian Kuerten
//
// This file is part of jeography.
//
// jeography is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// jeography is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with jeography. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.jeography.tiles.source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.tiles.LoadListener;

/**
 * @param <T>
 *            the type of things.
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public abstract class ImageProvider<T, D> implements ImageSource<T, D>
{

	final static Logger logger = LoggerFactory.getLogger(ImageProvider.class);

	Object lock = new Object();

	List<Message> toProvideList = new ArrayList<>();
	Set<T> toProvideSet = new HashSet<>();
	Set<T> providing = new HashSet<>();

	Set<LoadListener<T, D>> listeners = new HashSet<>();

	private int nThreads;

	/**
	 * This is the method that this ImageProvider will use to get an Image.
	 * Implement this in order to create a special purpose provider.
	 * 
	 * @param thing
	 *            the thing that shall be loaded.
	 * @return the loaded data.
	 */
	@Override
	public abstract D load(T thing);

	/**
	 * Provide the data for this thing. Return to the caller immediately. Notify
	 * the listeners as soon as loading is done.
	 * 
	 * @param thing
	 *            the thing to provide data for.
	 */
	public void provide(T thing)
	{
		synchronized (lock) {
			if (toProvideSet.contains(thing) || providing.contains(thing))
				return;
			toProvideList.add(new Message(MessageType.Data, thing));
			toProvideSet.add(thing);
			lock.notify();
		}
	}

	/**
	 * Stop producing new elements.
	 */
	public void stopRunning()
	{
		synchronized (lock) {
			for (int i = 0; i < nThreads; i++) {
				toProvideList.add(new Message(MessageType.Kill, null));
			}
			lock.notifyAll();
		}
	}

	/**
	 * Add a listener to be notified on load / fail events.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addLoadListener(LoadListener<T, D> listener)
	{
		listeners.add(listener);
	}

	/**
	 * Remove a listener from the set of listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeLoadListener(LoadListener<T, D> listener)
	{
		listeners.remove(listener);
	}

	void notifyLoaded(T thing, D data)
	{
		for (LoadListener<T, D> listener : listeners) {
			listener.loaded(thing, data);
		}
	}

	void notifyFailed(T thing)
	{
		for (LoadListener<T, D> listener : listeners) {
			listener.loadFailed(thing);
		}
	}

	/**
	 * Create a new ImageProvider.
	 * 
	 * @param nThreads
	 *            the number of threads to use for loading.
	 */
	public ImageProvider(int nThreads)
	{
		this.nThreads = nThreads;
		for (int i = 0; i < nThreads; i++) {
			LoadThread loadThread = new LoadThread();
			Thread thread = new Thread(loadThread);
			thread.start();
		}
	}

	class LoadThread implements Runnable
	{

		@Override
		public void run()
		{
			while (true) {
				T provide = null;
				synchronized (lock) {
					if (toProvideList.isEmpty()) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// do nothing here
						}
					} else {
						Message message = toProvideList
								.remove(toProvideList.size() - 1);
						if (message.type == MessageType.Kill) {
							logger.debug("thread stopped");
							return;
						}
						provide = message.data;
						toProvideSet.remove(provide);
						providing.add(provide);
					}
				}
				if (provide == null)
					continue;

				D loaded = load(provide);

				synchronized (lock) {
					if (loaded == null) {
						notifyFailed(provide);
					} else {
						notifyLoaded(provide, loaded);
					}
					providing.remove(provide);
				}
			}
		}
	}

	private class Message
	{

		MessageType type;
		T data;

		public Message(MessageType type, T data)
		{
			this.type = type;
			this.data = data;
		}
	}

	private enum MessageType {
		Data,
		Kill
	}

}
