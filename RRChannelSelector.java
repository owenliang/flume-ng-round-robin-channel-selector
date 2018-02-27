/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * 背景: Kafka Sink 单线程同步调用，吞吐无法继续提升。
 *
 * 解决方案：自定义实现channel selector, 将source的流量均匀分发到多个channel, 并让每个channel由一个独立的kafka sink消费
 *
 * 配置：为source指定selector.type=org.apache.flume.channel.RRChannelSelector
 *
 */
public class RRChannelSelector extends AbstractChannelSelector {
  private static final List<Channel> EMPTY_LIST = new ArrayList<>();

  private int rrIndex = 0;

  @Override
  public List<Channel> getRequiredChannels(Event event) {
    List<Channel> allChannels = getAllChannels();

    int index = rrIndex;
    rrIndex = (rrIndex + 1) % allChannels.size();

    List<Channel> result = new ArrayList<>();
    result.add(allChannels.get(index));
    return result;
  }

  @Override
  public List<Channel> getOptionalChannels(Event event) {
    return RRChannelSelector.EMPTY_LIST;
  }

  @Override
  public void configure(Context context) {

  }
}
