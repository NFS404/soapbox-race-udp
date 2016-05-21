package br.com.soapboxrace.udplearn;

import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;
	private byte sessionClientIdx;
	private long timeStart = 0;
	private long pingTime = 0;
	private UdpWriter udpWriter;
	private int sessionId;
	private byte[] incomeSyncPacket;
	private boolean isSyncCompleted = false;
	private PacketProcessor packetProcessor = new PacketProcessor();

	public UdpTalk(byte sessionClientIdx, int sessionId, UdpWriter udpWriter) {
		this.sessionClientIdx = sessionClientIdx;
		this.sessionId = sessionId;
		this.udpWriter = udpWriter;
		timeStart = new Date().getTime();
		udpSession = UdpSessions.addUdpTalk(this);
	}

	public void syncCompleted() {
		isSyncCompleted = true;
		String incomePacket = new String(incomeSyncPacket);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("sync packet: [");
		stringBuilder.append(incomePacket.trim());
		stringBuilder.append("]\n");
		stringBuilder.append("Session started time: [");
		stringBuilder.append(getUdpSession().getDiffTime());
		stringBuilder.append("]\n");
		stringBuilder.append("pingTime: [");
		stringBuilder.append(pingTime);
		stringBuilder.append("]\n\n");
		getUdpWriter().sendPacket(stringBuilder.toString());
	}

	public UdpSession getUdpSession() {
		return udpSession;
	}

	public byte getSessionClientIdx() {
		return sessionClientIdx;
	}

	public UdpWriter getUdpWriter() {
		return udpWriter;
	}

	public void setPingTime(byte[] incomeSyncPacket) {
		this.incomeSyncPacket = incomeSyncPacket;
		pingTime = getDiffTime();
	}

	public long getPingTime() {
		return pingTime;
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

	public void sendFrom(UdpTalk udpTalk, byte[] sendData) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from [");
		stringBuffer.append(udpTalk.getSessionClientIdx());
		stringBuffer.append("] ");
		stringBuffer.append(new String(sendData));
		byte[] bytes = stringBuffer.toString().getBytes();
		byte[] processed = packetProcessor.getProcessed(bytes, sessionClientIdx);
		getUdpWriter().sendPacket(processed);
	}

	public int getSessionId() {
		return sessionId;
	}

	public boolean isSyncCompleted() {
		return isSyncCompleted;
	}

	public long getTimeStart() {
		return timeStart;
	}

}
