package com.gome.clover.common.zeromq;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/25
 * Time: 20:55
 */
public class AsyncZMQPull {
  /*  protected ModuleZeromq mq;
    protected ZMQ.Socket jobSocket = null;
    protected ZMQ.Socket socketPullBroadcast = null;
    private boolean alive = true;
    private int index = 0;
    private long jobCount = 0L;
    private Thread t = null;
    protected static boolean debugThread = false;
    protected static Logger logger = LoggerFactory.getLogger(SyncZMQPull.class);

    public SyncZMQPull(ModuleZeromq mq, int index) {
        this.mq = mq;
        this.index = index;
    }

    public void attach(Thread t) {
        this.t = t;
    }

    private ZMsg packClientMsg(ModuleZeromq.MessageSource source, String clientAddr, MessageVersion version, byte[] msgTag, String content) {
        byte[] bytes = StringUtil.toBytes(content);
        return packClientMsg(source, clientAddr, version, msgTag, bytes);
    }

    private ZMsg packClientMsg(ModuleZeromq.MessageSource source, String clientAddr, MessageVersion version, byte[] msgTag, byte[] bytes) {
        ZMsg msg = new ZMsg();
        msg.addFirst(String.valueOf(source.ordinal()));
        msg.add(clientAddr);
        msg.add(ZeromqUtil.EMPTY);
        msg.add(msgTag);
        CompressMode compress = null;
        byte[] compressed = bytes;
        if (bytes.length > 1024) {
            compress = CompressMode.SNAPPY;
            compressed = StringUtil.compress(bytes);
        }
        if (compressed == bytes) {
            compress = CompressMode.NONE;
        }
        msg.add(compress.getData());
        msg.add(compressed);
        return msg;
    }

    private ZMsg packRouterMsg(Device device, ServiceName service, MessageScope scope, MessageType type, MessageVersion version, String msgId, String msgTag, String msgTo, long timestamp, byte[] bytes, String address)
    {
        ZMsg msg = new ZMsg();
        msg.add(String.valueOf(ModuleZeromq.MessageSource.fromRouter.ordinal()));
        msg.add(ZeromqUtil.EMPTY);
        msg.add(device.getData());
        msg.add(service.getData());
        msg.add(String.valueOf(service.ordinal()));
        msg.add(scope.getData());
        msg.add(type.getData());
        msg.add(version.getData());
        msg.add(ZeromqUtil.getBytes(msgId));
        msg.add(ZeromqUtil.getBytes(msgTag));
        msg.add(ZeromqUtil.getBytes(msgTo));
        msg.add(String.valueOf(timestamp));
        CompressMode compress = null;
        byte[] compressed = bytes;
        if (bytes.length > 1024) {
            compress = CompressMode.SNAPPY;
            compressed = StringUtil.compress(bytes);
        }
        if (compressed == bytes) {
            compress = CompressMode.NONE;
        }
        msg.add(compress.getData());
        msg.add(compressed);
        msg.add(StringUtil.toBytes(address));
        return msg;
    }

    protected void changeRouter(String clientAddr, MessageVersion version, byte[] msgTag, String content) {
        packRouterMsg(Device.SERVER, ServiceName.Server, MessageScope.ROUTER, MessageType.DISCONNECT, MessageVersion.MQ, this.mq.getId(), "change", "TO", System.currentTimeMillis(), ZeromqUtil.EMPTY,
                this.mq.getId());
        sendJob(packRouterMsg(Device.SERVER, ServiceName.Server, MessageScope.ROUTER, MessageType.DISCONNECT, MessageVersion.MQ, "ID", msgTag.toString(), "TO", System.currentTimeMillis(),
                ZeromqUtil.EMPTY, this.mq.getId()));
        this.mq.stop();
        this.mq.routerNode = ServerDict.self.routerBy(content);
        this.mq.start(true);
        ModuleIntf subscribe = ModuleFactory.moduleInstanceBy("subscribe");
        subscribe.stop();
        subscribe.start(true);
        byte[] bytes = StringUtil.toBytes("{state:" + this.mq.isAlive() + "}");
        sendJob(packClientMsg(ModuleZeromq.MessageSource.fromApp, clientAddr, version, msgTag, bytes));
    }

    protected void switchTo(boolean online, String sender, MessageVersion version, ZFrame msgTag) {
        boolean exist = ZooUtil.exists(ServerDict.self.zoo(), "/appservers/" + this.mq.getId());
        String name = online ? "online" : "offline";
        if (exist != online) {
            if (online) {
                this.mq.restart();
                sendJob(packClientMsg(ModuleZeromq.MessageSource.fromApp, sender, version, msgTag.getData(), "{\"" + name + "\":true}"));
            } else {
                sendJob(packRouterMsg(Device.SERVER, ServiceName.Server, MessageScope.ROUTER, MessageType.DISCONNECT, MessageVersion.MQ, "ID", msgTag.toString(), "TO", System.currentTimeMillis(),
                        ZeromqUtil.EMPTY, this.mq.getId()));
                sendJob(packClientMsg(ModuleZeromq.MessageSource.fromApp, sender, version, msgTag.getData(), "{\"" + name + "\":true}"));
                this.mq.serverNode.removeField("routerId");
                ZooUtil.delete(ServerDict.self.zoo(), "/appservers/" + this.mq.getId());
            }
        }
        logger.info("AppServer  [{}] is [{}]  at  ServerIP={} & Port={} & PublisherPort={}", new Object[] { this.mq.getId(), name, this.mq.serverNode.getString("ip"), Integer.valueOf(Global.MQRouterPort),
                Integer.valueOf(Global.MQRouterPublishPort) });
    }

    public void sendJob(ZMsg msg) {
        ZeroSocket oSocket = null;
        String uri = "inproc://jobs-app";
        try {
            oSocket = ZeroSocketFactory.get(uri, this.mq.ctx, 5);
            if ((oSocket == null) || (!oSocket.isAlive)) {
                return;
            }
            msg.send(oSocket.socket);
        } catch (Exception e) {
            oSocket.isAlive = false;
            logger.error("sendJob", e);
        } finally {
            ZeromqUtil.free(msg);
            ZeroSocketFactory.ret(uri, oSocket);
        }
    }

    public void sendSocketEvent(String senderId, Object oResult) {
        SocketEvent event = this.mq.event.removeSocket(senderId);
        if ((event == null) || (oResult == null)) {
            return;
        }
        if ((oResult instanceof String))
            event.onMessage(Global.SocketDataType.string, oResult);
        else if ((oResult instanceof BasicDBObject))
            event.onMessage(Global.SocketDataType.json, oResult);
    }

    private void createSocket()
    {
        this.jobSocket = this.mq.ctx.createSocket(7);
        this.jobSocket.setIdentity(StringUtil.toBytes("jobs-push@" + this.index));
        this.jobSocket.connect("inproc://jobs-push");

        this.socketPullBroadcast = this.mq.ctx.createSocket(7);
        this.socketPullBroadcast.setIdentity(StringUtil.toBytes("broadcast-pull@" + this.index));
        this.socketPullBroadcast.connect("inproc://broadcast-app-push");
    }

    public void run()
    {
        createSocket();
        ZMQ.Poller poller = new ZMQ.Poller(2);
        poller.register(this.jobSocket, 1);
        poller.register(this.socketPullBroadcast, 1);

        while ((this.alive) && (!this.t.isInterrupted())) {
            ZMsg msg = null;
            if (poller.poll(3000L) < 1) {
                continue;
            }
            if (poller.pollin(0))
                msg = ZMsg.recvMsg(this.jobSocket);
            else if (poller.pollin(1)) {
                msg = ZMsg.recvMsg(this.socketPullBroadcast);
            }
            ZFrame c = msg.pop();
            ModuleZeromq.MessageSource messageFrom = ModuleZeromq.MessageSource.values()[Integer.parseInt(c.toString())];
            c.destroy();
            ZFrame sender = msg.pop();
            ZFrame empty = msg.pop();
            ZFrame device = msg.pop();
            ZFrame serviceName = msg.pop();
            ZFrame serviceIndex = msg.pop();
            ZFrame scope = msg.pop();
            ZFrame messageType = msg.pop();
            ZFrame messageVersion = msg.pop();
            MessageVersion version = MessageVersion.values()[Integer.parseInt(messageVersion.toString())];
            ZFrame messageId = msg.pop();
            ZFrame messageTag = msg.pop();
            ZFrame messageTo = msg.pop();
            ZFrame timestamp = msg.pop();
            ZFrame compress = msg.pop();

            String content = null;
            String msgTag = messageTag.toString();
            try {
                c = msg.pop();
                byte[] bytes = c.getData();
                c.destroy();
                if (Integer.parseInt(compress.toString()) == CompressMode.SNAPPY.ordinal()) {
                    bytes = StringUtil.uncompress(bytes);
                }
                content = StringUtil.toString(bytes);
                ServiceName service = ServiceName.values()[Integer.parseInt(serviceIndex.toString())];
                MessageType msgType = MessageType.values()[Integer.parseInt(messageType.toString())];
                switch (service) {
                    case Activity:
                        switch ($SWITCH_TABLE$org$x$cloud$dict$MessageType()[msgType.ordinal()]) {
                            case 2:
                                if (StringUtil.isEmpty(content)) break;
                                this.mq.registerServer(content);

                                break;
                            case 15:
                                switchTo(false, sender.toString(), version, messageTag);
                                break;
                            case 14:
                                switchTo(true, sender.toString(), version, messageTag);
                                break;
                            case 5:
                                if (msgTag.equalsIgnoreCase("runtime")) {
                                    content = JVMUtil.readRuntime().append("jobCount", Long.valueOf(this.jobCount)).toString();
                                }
                                sendJob(packClientMsg(ModuleZeromq.MessageSource.fromApp, sender.toString(), version, messageTag.getData(), content));
                                break;
                            case 9:
                                sendJob(packClientMsg(ModuleZeromq.MessageSource.fromSelf, sender.toString(), version, messageTag.getData(), content));
                        }

                        break;
                    default:
                        BasicDBObject oResult = new BasicDBObject().append("server", this.mq.getId());
                        ZFrame jobNo = msg.pop();
                        this.jobCount = Long.parseLong(jobNo.toString());
                        oResult.append("@job", Long.valueOf(this.jobCount));
                        jobNo.destroy();
                        oResult.append("@from", messageFrom.name()).append("@thread", Integer.valueOf(this.index));
                        MessageScope msgScope = MessageScope.values()[Integer.parseInt(scope.toString())];
                        DBObject oReq = (DBObject)JSON.parse(content);
                        try {
                            DBObject oResponse = ModuleFactory.processor().execute(this.jobCount, sender.toString(), Integer.parseInt(device.toString()), Integer.parseInt(serviceIndex.toString()), msgScope,
                                    Integer.parseInt(messageType.toString()), Integer.parseInt(messageVersion.toString()), messageId.toString(), messageTag.toString(), messageTo.toString(),
                                    Long.parseLong(timestamp.toString()), oReq);
                            oResult.append("response", oResponse);
                            oResult.append("state", Boolean.valueOf(true));
                        } catch (Exception e) {
                            oResult.append("state", Boolean.valueOf(false));
                            oResult.append("message", e.getMessage());
                            logger.error("ModuleFactory.processor().execute" + StringUtil.stringifyException(e));
                        }

                        oResult.append("@time", Long.valueOf(System.currentTimeMillis() - Long.parseLong(timestamp.toString())));
                        switch (messageFrom) {
                            case fromApp:
                                sendJob(packRouterMsg(Device.values()[Integer.parseInt(device.toString())], ServiceName.Server, msgScope, MessageType.REPLY, version, messageId.toString(),
                                        messageTag.toString(), messageTo.toString(), Long.parseLong(timestamp.toString()), StringUtil.toBytes(oResult.toString()), sender.toString()));
                                break;
                            case fromRouter:
                                sendJob(packClientMsg(ModuleZeromq.MessageSource.fromApp, sender.toString(), version, messageTag.getData(), oResult.toString()));
                                break;
                            case fromSelf:
                                sendSocketEvent(sender.toString(), oResult);
                        }
                }
            }
            catch (Exception e)
            {
                logger.error("JobPull[" + messageType.toString() + "&" + serviceName.toString() + "&" + content + "]" + StringUtil.stringifyException(e));
            }
            ZeromqUtil.free(empty);
            sender.destroy();
            device.destroy();
            serviceName.destroy();
            serviceIndex.destroy();
            scope.destroy();
            messageType.destroy();
            messageVersion.destroy();
            messageTag.destroy();
            timestamp.destroy();
            compress.destroy();
            msg.destroy();
        }
        stop();
    }

    public void stop() {
        this.alive = false;
        try {
            this.t.join();
            if (this.jobSocket == null) {
                return;
            }
            this.mq.ctx.destroySocket(this.jobSocket);
        } catch (Exception e) {
            logger.error("stop", e);
        }
    }*/
}
