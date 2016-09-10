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

package de.topobyte.jeography.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * @param <T>
 *            the type of keys.
 * @param <D>
 *            the type of data.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ImageManagerSourceRam<T, D> extends
		AbstractImageManagerWithMemoryCachePlus<T, D> implements
		PriorityImageManager<T, D, Integer>
{

	private final int nThreads;

	Object lock = new Object();

	// set view on the things in the queue
	Set<T> toProvideSet = new HashSet<>();
	// priority queue view on the things in the queue
	PriorityQueue<PriorityEntry> toProvideQueue = new PriorityQueue<>();
	// set of things that are currently being produced
	Set<T> providing = new HashSet<>();
	// list of messages to the workers
	List<Message> messages = new ArrayList<>();

	Set<LoadListener<T, D>> listeners = new HashSet<>();

	ImageSource<T, D> imageSource = null;

	/**
	 * Create an ImageSource based ImageManager implementation that does in-RAM
	 * caching.
	 * 
	 * @param nThreads
	 *            the number of threads to use for providing images.
	 * @param cacheSize
	 *            the number of data elements to store in the RAM cache.
	 * @param source
	 *            the ImageSource to use for creation of unknown requests.
	 */
	public ImageManagerSourceRam(int nThreads, int cacheSize,
			ImageSource<T, D> source)
	{
		super(cacheSize);
		this.nThreads = nThreads;
		imageSource = source;

		for (int i = 0; i < nThreads; i++) {
			LoadThread loadThread = new LoadThread();
			Thread thread = new Thread(loadThread);
			thread.start();
		}
	}

	/**
	 * Get the ImageSource object
	 * 
	 * @return the image source.
	 */
	public ImageSource<T, D> getImageSource()
	{
		return imageSource;
	}

	@Override
	public D get(T thing)
	{
		return get(thing, 0);
	}

	@Override
	public D get(T thing, Integer priority)
	{
		D image = memoryCache.get(thing);
		if (image != null) {
			return image;
		}

		produce(thing, priority);

		return null;
	}

	@Override
	public void cancelJobs()
	{
		synchronized (lock) {
			toProvideQueue.clear();
			toProvideSet.clear();
		}
	}

	@Override
	public void willNeed(T thing)
	{
		memoryCache.refresh(thing);
	}

	private void produce(T thing, int priority)
	{
		synchronized (lock) {
			if (toProvideSet.contains(thing) || providing.contains(thing)) {
				return;
			}
			toProvideQueue.add(new PriorityEntry(priority, thing));
			toProvideSet.add(thing);
			lock.notify();
		}
	}

	int settingsId = 1;

	@Override
	public void setIgnorePendingProductions()
	{
		synchronized (lock) {
			settingsId += 1;
			providing.clear();
		}
	}

	@Override
	public void destroy()
	{
		stopRunning();
	}

	/**
	 * Stop producing new elements.
	 */
	public void stopRunning()
	{
		synchronized (lock) {
			for (int i = 0; i < nThreads; i++) {
				messages.add(new Message(MessageType.Kill));
			}
			lock.notifyAll();
		}
	}

	/**
	 * Ensure that the denoted thing will be removed from the cache.
	 * 
	 * @param thing
	 *            the thing to remove.
	 */
	public void unchache(T thing)
	{
		memoryCache.remove(thing);
	}

	/**
	 * Clear the underlying cache.
	 */
	public void clearCache()
	{
		memoryCache.clear();
	}

	@Override
	public void setCacheHintMinimumSize(int size)
	{
		if (memoryCache.getSize() < size) {
			memoryCache.setSize(size);
		} else if (size < desiredCacheSize) {
			memoryCache.setSize(desiredCacheSize);
		}
	}

	/*
	 * Worker thread implementation
	 */

	void notifyLoaded(T thing, D data)
	{
		memoryCache.put(thing, data);
		notifyListeners(thing, data);
	}

	void notifyFailed(T thing)
	{
		notifyListenersFail(thing);
	}

	class LoadThread implements Runnable
	{

		@Override
		public void run()
		{
			while (true) {
				int mySettingsId = 0;
				T provide = null;
				synchronized (lock) {
					if (messages.isEmpty() && toProvideQueue.isEmpty()) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// do nothing here
						}
					} else {
						if (!messages.isEmpty()) {
							Message message = messages
									.remove(messages.size() - 1);
							if (message.type == MessageType.Kill) {
								System.out.println("stopped!!!!");
								return;
							}
						}
						PriorityEntry entry = toProvideQueue.remove();
						provide = entry.thing;
						mySettingsId = settingsId;
						toProvideSet.remove(provide);
						providing.add(provide);
					}
				}
				if (provide == null)
					continue;

				D data = imageSource.load(provide);

				synchronized (lock) {
					if (mySettingsId != settingsId) {
						continue;
					}
					if (data == null) {
						notifyFailed(provide);
					} else {
						notifyLoaded(provide, data);
					}
					providing.remove(provide);
				}
			}
		}
	}

	private class Message
	{

		final MessageType type;

		public Message(MessageType type)
		{
			this.type = type;
		}
	}

	private enum MessageType {
		Kill
	}

	/*
	 * Priority management data type
	 */

	private class PriorityEntry implements Comparable<PriorityEntry>
	{

		final int priority;
		final T thing;

		PriorityEntry(int priority, T thing)
		{
			this.priority = priority;
			this.thing = thing;
		}

		@Override
		public int compareTo(PriorityEntry o)
		{
			return priority - o.priority;
		}

	}

}
