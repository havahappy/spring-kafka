/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.kafka.listener.adapter;

import java.util.Iterator;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.listener.BatchMessageListener;

/**
 * A {@link BatchMessageListener} adapter that implements filter logic
 * via a {@link RecordFilterStrategy}.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 * @author Gary Russell
 *
 */
public class FilteringBatchMessageListenerAdapter<K, V>
		extends AbstractFilteringMessageListener<K, V, BatchMessageListener<K, V>>
		implements BatchMessageListener<K, V> {

	/**
	 * Create an instance with the supplied strategy and delegate listener.
	 * @param delegate the delegate.
	 * @param recordFilterStrategy the filter.
	 */
	public FilteringBatchMessageListenerAdapter(BatchMessageListener<K, V> delegate,
			RecordFilterStrategy<K, V> recordFilterStrategy) {
		super(delegate, recordFilterStrategy);
	}

	@Override
	public void onMessage(List<ConsumerRecord<K, V>> consumerRecords) {
		Iterator<ConsumerRecord<K, V>> iterator = consumerRecords.iterator();
		while (iterator.hasNext()) {
			if (filter(iterator.next())) {
				iterator.remove();
			}
		}
		if (consumerRecords.size() > 0) {
			this.delegate.onMessage(consumerRecords);
		}
	}

}
