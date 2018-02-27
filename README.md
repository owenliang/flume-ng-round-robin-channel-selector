# flume-ng-round-robin-channel-selector

## 用途

flume-ng默认source -> channel -> sink的流水线配置，会受到sink处理速率的影响，吞吐无法线性提升。

本插件通过实现自定义channel selector，实现了source均匀派发流量到多个channel，从而可以为每个channel配备一个独立的sink（线程），从而实现吞吐线性提升。

## 编译方法

* 下载对应版本flume源代码，以flume1.8为例：http://www.apache.org/dyn/closer.lua/flume/1.8.0/apache-flume-1.8.0-src.tar.gz
* 将RRChannelSelector.java文件拷贝到源码子路径：flume-ng-core/src/main/java/org/apache/flume/channel
* 回到flume源代码根目录，编译整个项目：mvn clean install -DskipTests
* 拷贝flume-ng-core/target/flume-ng-core-1.8.0.jar（其中1.8.0是你flume版本）到线上flume环境的lib目录下覆盖对应文件

## 配置方法

为source指定selector.type，例如：

agent.sources.src_taildir.selector.type = org.apache.flume.channel.RRChannelSelector

为source指定多个channel，例如：

agent.sources.src_taildir.channels = c1 c2 c3

为每个channel指定一个sink，例如：

agent.sinks.k1.channel = c1
agent.sinks.k2.channel = c2
agent.sinks.k3.channel = c3
