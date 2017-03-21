package zb.com.neu.jpcap.test;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class Main {

	public static void main(String[] args) {
		try{
			//获得网卡设备的实例列表 
			NetworkInterface[] devices = JpcapCaptor.getDeviceList();
			int index = 0;
			JpcapCaptor captor = JpcapCaptor.openDevice(devices[index], 65535, false, 20);
			captor.loopPacket(-1, new PackageReceiver()); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
