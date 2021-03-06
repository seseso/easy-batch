/*
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.jeasy.batch.extensions.integration;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentBasedBlockingQueueRecordWriterTest {

    private ContentBasedBlockingQueueRecordWriter recordWriter;

    private BlockingQueue<Record> orangeQueue;

    private BlockingQueue<Record> defaultQueue;

    @Mock
    private Record orangeRecord, appleRecord;

    @Mock
    private Predicate orangePredicate;

    @Before
    public void setUp() {
        orangeQueue = new LinkedBlockingQueue<>();
        defaultQueue = new LinkedBlockingQueue<>();
        Map<Predicate, BlockingQueue<Record>> queueMap = new HashMap<>();
        queueMap.put(orangePredicate, orangeQueue);
        queueMap.put(new DefaultPredicate(), defaultQueue);
        recordWriter = new ContentBasedBlockingQueueRecordWriter(queueMap);

        when(orangePredicate.matches(orangeRecord)).thenReturn(true);
        when(orangePredicate.matches(appleRecord)).thenReturn(false);
    }

    @Test
    public void orangeRecordShouldBeWrittenToOrangeQueue() throws Exception {
        recordWriter.writeRecords(new Batch(orangeRecord, appleRecord));
        assertThat(orangeQueue).containsOnly(orangeRecord);
        assertThat(defaultQueue).containsOnly(appleRecord);
    }

}
