package zb.com.neu.jpcap.test;

import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class PackageReceiver implements PacketReceiver {

	@Override
	public void receivePacket(Packet packet) {
		System.out.println("接收到数据包：");
		IPPacket ipPacket = (IPPacket) packet;
		String srcIP = ipPacket.src_ip.toString();
		System.out.println("源IP：" + srcIP);
		String dstIP = ipPacket.dst_ip.toString();
		System.out.println("目的IP：" + dstIP);
		int hesdLength = ipPacket.header.length; // 首部长度
		int dataLength = ipPacket.data.length; // 数据长度
		//boolean dont_frag = ipPacket.dont_frag; // 是否不分段

		String header = "";
		for (int i = 0; i < hesdLength; i++) {
			header += Byte.toString(packet.header[i]);
		}
		System.out.println("首部：" + header);
		String data = "";
		for (int i = 0; i < dataLength; i++) {
			data += Byte.toString(packet.data[i]);
		}
		System.out.println("数据：" + data);
	}

}
