package zb.com.ui.test;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

public class TestJpcap {
	public static void main(String[] args) {
		//获得网卡设备的实例列表 
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		//循环输出全部网卡设备对象相应的信息      
		for(int i = 0; i< devices.length; i++){
			//设备号 ,网卡名,网卡描述 
			System.out.println(i+":"+devices[i].name + "("+devices[i].description+")");
			//网卡所处数据链路层的名称与其描述  
			System.out.println(" 数据链路层名称和描述: "+devices[i].datalink_name+"("+devices[i].datalink_description+")");
			//网卡MAC地址   
			System.out.print("MAC address:");
			for(byte b : devices[i].mac_address)
				 System.out.print(Integer.toHexString(b&0xff) + ":"); 
			System.out.println(); 
			 //输出网卡IP地址 IPV4 IPV6 子网地址 扩播地址 
		    for (NetworkInterfaceAddress a : devices[i].addresses)    
		        System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
		}
	}
}
