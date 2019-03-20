package com.third.IntelPlat.service;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Component
public class SFTPChannel {
	
	private static Logger logger = LoggerFactory.getLogger(SFTPChannel.class);

	Session session = null;
	Channel channel = null;
	
	@Value("${ftp_host}")
    private String ftpHost;
	
	@Value("${ftp_port}")
    private String port;
	
	@Value("${ftp_userName}")
    private String ftpUserName;
	
	@Value("${ftp_password}")
    private String ftpPassword;
	
	@Value("${ftp_timeout}")
    private Integer timeout;
	
	public ChannelSftp getChannel() throws JSchException {

		int ftpPort = 22;

		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}

		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象

		logger.debug("Session created.");

		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		logger.debug("Session connected.");

		logger.debug("Opening Channel.");
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		logger.debug("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
				+ ", returning: " + channel);
		return (ChannelSftp) channel;
	}

	public void closeChannel() throws Exception {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}


}
