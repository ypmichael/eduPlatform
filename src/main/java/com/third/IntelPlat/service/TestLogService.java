package com.third.IntelPlat.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.third.IntelPlat.common.ExportTextUtil;
import com.third.IntelPlat.common.PDFReport;
import com.third.IntelPlat.entity.TestLogEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.repository.TestLogRepository;

@Component
public class TestLogService {
	private static Logger logger = LoggerFactory.getLogger(TestLogService.class);

	@Autowired
	private TestLogRepository testLogRepository;

	@Autowired
	private UserService userService;

	@Value("${file.folder}")
	private String filePath;

	@Value("${ftp_dst}")
	private String ftpDst;

	@Autowired
	private SFTPChannel ftpChannel;

	/**
	 * 检查错误题目数量
	 * 
	 * @param userName
	 * @return
	 */
	public Integer checkWrongTopic(String userName) {

		int num = testLogRepository.checkWrongTopic(userName);

		return num;
	}

	/**
	 * 获取最近一道题目的信息
	 * 
	 * @param userName
	 */
	public TestLogEntity findTopic(String userName) {
		TestLogEntity TestLogEntity = testLogRepository.findTopic(userName);

		return TestLogEntity;
	}

	/**
	 * 保存答题信息
	 * 
	 * @param testLog
	 */
	public void save(TestLogEntity testLog) {
		testLogRepository.save(testLog);

	}

	/**
	 * 获取学生正确答题数据
	 * 
	 * @param uid
	 * @return
	 */
	public Integer queryCorrectAnswer(Integer uid) {
		return testLogRepository.queryCorrectAnswer(uid);
	}

	public int checkTitleNumber(String userName, Integer tutorialUid) {

		int num = testLogRepository.checkTitleNumber(userName, tutorialUid);

		return num;
	}

	public List<TestLogEntity> queryQstLog() {
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();

		UserEntity u = userService.findByAccount(userName);

		List<TestLogEntity> testLogs = testLogRepository.findByUserUid(u.getUid());

		return testLogs;
	}

	public List<TestLogEntity> exportText() {
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();

		UserEntity u = userService.findByAccount(userName);

		List<TestLogEntity> testLogs = testLogRepository.findByUserUid(u.getUid());

		return testLogs;
	}

	public String exportTextLog(HttpServletRequest request) throws FileNotFoundException {
		List<TestLogEntity> testLogs = (List<TestLogEntity>) testLogRepository.findAll();

		String path = filePath + "log.txt";

		File filenameShip = new File(path);
		ExportTextUtil.createFile(path);// 检测路径，没有就创建

		List<Map<String, String>> content = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("1", "序号");
		map.put("2", "学生姓名");
		map.put("3", "知识点编号");
		map.put("4", "知识点名称");
		map.put("5", "试题内容");
		map.put("6", "试题类型");
		map.put("7", "正确答案");
		map.put("8", "开始答题时间");
		map.put("9", "结束答题时间");
		map.put("10", "学生答题时长");
		map.put("11", "学生答案");
		map.put("12", "试题结果");

		content.add(map);

		for (TestLogEntity log : testLogs) {
			HashMap<String, String> logMap = new HashMap<String, String>();

			logMap.put("1", String.valueOf(log.getUid()));
			logMap.put("2", log.getUserName());
			logMap.put("3", log.getTutorialNo());
			logMap.put("4", log.getTutorialName());
			logMap.put("5", log.getChoiceContent());
			logMap.put("6", log.getQuestionType());
			logMap.put("7", log.getAnsw());
			logMap.put("8", toStringByFormat(log.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("9", toStringByFormat(log.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("10", new BigDecimal(log.getDuration()).toString());
			logMap.put("11", log.getStudentOptAnsw());
			logMap.put("12", String.valueOf(log.getResult()));

			content.add(logMap);
		}

		String url = null;

		try {
			ExportTextUtil.writeTxtFileFor(content, filenameShip);

			url = generateFile(new FileInputStream(filenameShip), "log.txt");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/log/" + url;
	}

	/**
	 * 生成文件
	 * 
	 * @param in
	 * @param fileName
	 * @return
	 */
	private String generateFile(InputStream in, String fileName) {

		String url = fileName;

		try {
			ChannelSftp channel = ftpChannel.getChannel();
			channel.put(in, ftpDst + url, ChannelSftp.OVERWRITE);

			Vector<LsEntry> v = channel.ls("/edu/production/nginx/read");

			for (LsEntry e : v) {
				System.out.println(e.getFilename());
			}

			channel.quit();
		} catch (Exception e) {
			throw new ServiceException("文件上传到文件服务器出错.");
		}
		return url;
	}

	public boolean checkFileExists(String fileName) {

		try {
			ChannelSftp channel = ftpChannel.getChannel();

			Vector<LsEntry> v = channel.ls("/edu/production/nginx/read");

			for (LsEntry e : v) {

				if (e.getFilename().equals(fileName)) {
					channel.quit();
					return true;
				}
			}

			channel.quit();
		} catch (Exception e) {
			throw new ServiceException("文件上传到文件服务器出错.");
		}

		return false;
	}

	public static String toStringByFormat(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd
																// HH:mm:ss"
			return sdf.format(date);
		}
		return "";
	}

	public String exportPdfLog(List<String> userNames, HttpServletRequest request) throws Exception {
		List<TestLogEntity> testLogs = new ArrayList();
		for (String userName : userNames) {
			List<TestLogEntity> testLog = this.testLogRepository.findByUserName(userName);
			testLogs.addAll(testLog);
		}
		String path = this.filePath + "log.pdf";

		File filenameShip = new File(path);
		ExportTextUtil.createFile(path);

		String url = null;
		try {
			new PDFReport(filenameShip).generatePDF(testLogs);

			url = generateFile(new FileInputStream(filenameShip), "log.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/log/" + url;
	}

	public String queryQstLogs(List<String> userNames, HttpServletRequest request) throws FileNotFoundException {
		List<TestLogEntity> testLogs = new ArrayList();
		for (String userName : userNames) {
			List<TestLogEntity> testLog = this.testLogRepository.findByUserName(userName);
			testLogs.addAll(testLog);
		}
		String path = this.filePath + "logs.txt";

		File filenameShip = new File(path);
		ExportTextUtil.createFile(path);

		List<Map<String, String>> content = new ArrayList();
		HashMap<String, String> map = new HashMap();
		map.put("1", "序号");
		map.put("2", "学生姓名");
		map.put("3", "知识点编号");
		map.put("4", "知识点名称");
		map.put("5", "试题内容");
		map.put("6", "试题类型");
		map.put("7", "正确答案");
		map.put("8", "开始答题时间");
		map.put("9", "结束答题时间");
		map.put("10", "学生答题时长");
		map.put("11", "学生答案");
		map.put("12", "试题结果");

		content.add(map);
		for (TestLogEntity log : testLogs) {
			HashMap<String, String> logMap = new HashMap();

			logMap.put("1", String.valueOf(log.getUid()));
			logMap.put("2", log.getUserName());
			logMap.put("3", log.getTutorialNo());
			logMap.put("4", log.getTutorialName());
			logMap.put("5", log.getChoiceContent());
			logMap.put("6", log.getQuestionType());
			logMap.put("7", log.getAnsw());
			logMap.put("8", toStringByFormat(log.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("9", toStringByFormat(log.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("10", new BigDecimal(log.getDuration().longValue()).toString());
			logMap.put("11", log.getStudentOptAnsw());
			logMap.put("12", String.valueOf(log.getResult()));

			content.add(logMap);
		}
		String url = null;
		try {
			ExportTextUtil.writeTxtFileFor(content, filenameShip);

			url = generateFile(new FileInputStream(filenameShip), "logs.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/log/" + url;
	}
	
	
	/**
	 * 清除学生分数
	 * @param userNames
	 */
	public void deleteStudentScore(List<String> userNames) 
	{
		testLogRepository.deleteStudentScore(userNames);
	}

}
