package com.sist.spring.member.service.imple;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sist.spring.cmn.DTO;
import com.sist.spring.member.service.Level;
import com.sist.spring.member.service.UserDao;
import com.sist.spring.member.service.UserService;
import com.sist.spring.member.service.UserVO;

import javax.mail.*;
import javax.mail.internet.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple implements UserService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * 		case BASIC: return user.getLogin()>=50;
			case SILVER: return user.getRecommend()>=30;
	 */
	public static final int MIN_LOGINCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMENDCOUNT_FOR_GOD = 30;
	
	//?��?��?��?��?���? ?��?��받는 경우 2�?�?
	//만약 ?��?��?��?��?��?��로만 객체 ?��?��(xml?��?��x) - ex)기본?��?��bean 객체같�?것�? Repository("�?")주고 ?���? ?��??곳에?�� 	
	//						   		  @Autowired ?���? �??�� ?��?��?�� ?�� "�?"?���? ?��?��	(?��로젝?�� eHR0504_WEB?�� TestWeb ?��?�� 참고)
	//xml?�� ?��?��?��?�� ?��?��방식?���? �??�� - ex)@Qualifier ?��?��
	
	@Autowired
	private UserDao userDao;		//?��?��?��?��?���? 받음 (결국 ?��?��?��?��?���? 구현?�� 객체�? 주입?��)
	
	@Autowired						//id값을 ?��?��주면 ?��객체�? ?��?��?��?��. 같�? ?��?��?��?��?�� 참조?��?��
	@Qualifier("mailSender")		//root-context.xml?�� bean?�� id값을 주면 �? 객체 ?��?��?��?��것임(dummyMailSender주면 그거 ?��?��)
	private MailSender mailSender;
	
	
	public UserServiceImple() {}
	
	
	
	
	
	/**
	 * Spring									javax.mail
	 * -------------------------------- 		-----------
	 * SimpleMailMessage		 				MimeMessage
	 * MailSender : 							Transport
	 * JavaMailSenderImpl(MailSender?�� 구현체이?��)
	 * -------------------------------- 		-----------
	 * ?��?��?�� ?��?��?��?���? 메일?��?��
	 * @param user
	 */
	private void sendUpgradeEmail(UserVO user) {
		/*
		 POP ?��버명 : pop.naver.com
		 SMTP ?��버명 : smtp.naver.com
		 POP ?��?�� : 995, 보안?���?(SSL) ?��?��
		 SMTP ?��?�� : 465, 보안 ?���?(SSL) 
		  ?��?��?��?��?�� : wogns_20
		  비�?번호 : ?��?���? 로그?�� 비�?번호
		*/
		
		//-----------------------------------
		//받는?��?��
		//?���?
		//?��?��
		//-----------------------------------
		
		String recipient = user.getEmail();//받는?��?��
		String title = user.getName() + "?�� ?��?��(https://cafe.naver.com/kndjang)"; //?���?
		//user.getLevel().name() enum?�� STring?���? �??��?��?�� ?��?���? name()
		String contents = user.getU_id() + "?�� ?��급이\n"+user.getLevel().name()+"�? ?��?��?��?��?��?��?��.";//?��?�� 
		
		//-----------------------------------
		//Message?�� 받는?��?��,?���?,?��?��,?���? ?��?�� ?�� ?��?��
		//?��?��: Java
		//-----------------------------------
		
		
		SimpleMailMessage mimeMessage = new SimpleMailMessage();	//?��?��?�� ?��?��, xml?��?�� 만듬
	
		mimeMessage.setFrom("wogns_20@naver.com"); //보내?�� ?��?��
		//받는?��?��
		mimeMessage.setTo(recipient);
		
		mimeMessage.setSubject(title);//?���?
		mimeMessage.setText(contents);//?��?��
		
		//?��?��
		mailSender.send(mimeMessage);
		
		LOG.debug("=====================");
		LOG.debug("mail send to =" + recipient);
		LOG.debug("=====================");
		
	}
	
	
	
//	/**
//	 * ?��?��?�� ?��?��?��?���? 메일?��?�� (javax.mail?��?��)
//	 * @param user
//	 */
//	private void sendUpgradeEmail(UserVO user) {
//		/*
//		 POP ?��버명 : pop.naver.com
//		 SMTP ?��버명 : smtp.naver.com
//		 POP ?��?�� : 995, 보안?���?(SSL) ?��?��
//		 SMTP ?��?�� : 465, 보안 ?���?(SSL) 
//		  ?��?��?��?��?�� : wogns_20
//		  비�?번호 : ?��?���? 로그?�� 비�?번호
//		*/
//		String smtpHost ="smtp.naver.com";
//		final String userId = "wogns_20";
//		final String userPass = "@chl1995314@";
//		int port = 465;
//		//-----------------------------------
//		//받는?��?��
//		//?���?
//		//?��?��
//		//SMTP?���? ?��?��
//		//?���?
//		//-----------------------------------		
//		String recipient = user.getEmail();//받는?��?��
//		String title = user.getName() + "?�� ?��?��(https://cafe.naver.com/kndjang)"; //?���?
//		//user.getLevel().name() enum?�� STring?���? �??��?��?�� ?��?���? name()
//		String contents = user.getU_id() + "?�� ?��급이\n"+user.getLevel().name()+"�? ?��?��?��?��?��?��?��.";//?��?�� 
//		Properties props = System.getProperties();//SMTP?���? ?��?��
//		props.put("mail.smtp.host", smtpHost);
//		props.put("mail.smtp.port", port);
//		props.put("mail.smtp.auth", true);
//		props.put("mail.smtp.ssl.enable", true);
//		props.put("mail.smtp.ssl.trust", smtpHost);
//		
//		//?���?
//		Session session = Session.getInstance(props, new Authenticator() {
//			String uName = userId;
//			String passwd = userPass;
//			
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(uName,passwd);
//			}
//			
//		});
//		
//		session.setDebug(true);
//		
//		//-----------------------------------
//		//Message?�� 받는?��?��,?���?,?��?��,?���? ?��?�� ?�� ?��?��
//		//?��?��: Java
//		//-----------------------------------
//		Message mimeMessage = new MimeMessage(session);
//		try {
//			mimeMessage.setFrom(new InternetAddress("wogns_20@naver.com")); //보내?�� ?��?��
//			//받는?��?��
//			mimeMessage.setRecipient(Message.RecipientType.TO
//					, new InternetAddress(recipient)); //CC?�� 참조/ BCC?�� ?��??참조 / TO?�� ?��?��?��
//			
//			mimeMessage.setSubject(title);//?���?
//			mimeMessage.setText(contents);//?��?��
//			
//			//?��?��
//			Transport.send(mimeMessage);
//			
//			
//		} catch (AddressException e) {
//			LOG.debug("=====================");
//			LOG.debug("AddressException=" + e.getMessage());
//			LOG.debug("=====================");
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			LOG.debug("=====================");
//			LOG.debug("MessagingException=" + e.getMessage());
//			LOG.debug("=====================");
//			e.printStackTrace();
//		}		
//		
//		LOG.debug("=====================");
//		LOG.debug("mail send to =" + recipient);
//		LOG.debug("=====================");
//		
//	}
	
	/**
	   * 최초�??��?�� : Level.BASIC
	   * @param user
	   */
	public int add(UserVO user) {
		  //Level null -> Level.BASIC
		if(null == user.getLevel()) {
			user.setLevel(Level.BASIC);
		}
		
	    return userDao.doInsert(user);
		
	}
	
	
	//?��?��조건				
	private boolean canUpgradeLevel(UserVO user){				
		Level currentLevel =user.getLevel();
		
		switch(currentLevel) {
			case BASIC: return user.getLogin()>= MIN_LOGINCOUNT_FOR_SILVER;
			case SILVER: return user.getRecommend()>= MIN_RECCOMENDCOUNT_FOR_GOD;
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level" + currentLevel);
			
		} 
		
	}	
	
	
	
	
	//?��벨업그레?��?�� 
	public void upgradeLevel(UserVO user) {
		//BASIC -> SILVER
		//SILVER -> GOLD
		
//		Level currentLevel =user.getLevel();
		
		//GOLD(3, null),SILVER(2, GOLD), BASIC(1,SILVER) ?��?���? enum �??��?��?��
		Level nextLevel =user.getLevel().getNextLevel();
		
		if(null == nextLevel) {
			//Gold�? ?��?��?���? ?��?��(마�?막꺼 j05_128) 그냥 콘솔�? 찍기?��
			LOG.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			LOG.debug(user.getLevel()+"?? ?��그레?��?�� 불�??�� ?��?��?��.");
			LOG.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			throw new IllegalArgumentException(user.getLevel() + "?? ?��그레?��?�� 불�??�� ?��?��?��.");
		} else {
			//?��머�? ?��?��?��?��
			//BASIC -> SILVER
			//SILVER -> GOLD
			user.setLevel(nextLevel);
		}
		
		//?��기까�? ?��?��?���? BASIC, SILVER, SILVER, GOLD, GOLD ?��?��?��?��
		
		//?��?��?��?��처리
		//GOLD?? ?��그레?��?�� 불�??�� sss?��?��?��.(j04_128)
		//?��?���? 롤백 �??��?�� 만든것임, Transaction ?��?�� 발생?��
		//ex)test-applicationContext.xml?��?�� pointcut?��?�� 메소?�� ?��??�? 주면 2�? ?��그레?��?��?���? 4번차�??��?�� 무조�? ?��?��걸리�? ?���?�?
		//   2�? 4�? ?��?�� ?��그레?��?�� ?��?��
		//ex)test-applicationContext.xml?��?�� pointcut?��?�� 메소?�� ?��르게 주면(?��?��?��?��?��??�?) 
		//   j04_128?��?��?�� ?��?��?��?�� ?��?��걸리�?�? ?��?��??�?(?��그레?��?��?��?��) ?��머�? ?��?��?��?�� 롤백처리?�� ?��?��(�? j02_128?�� ?��?��?�� ?��??�?�? upgrade?��)
		//?��?�� 주석?�� 막으�? rollback?��?�� ?��?���? 그냥 ?�� ?��그레?��?�� ?��!(마�?막에?�� 막으면됨 ?��?��?��?��?��?��?�� 배포?��?��?��것임)
		String id ="j04_128";
		if(user.getU_id().equals(id)) {
			LOG.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			LOG.debug(user.getLevel() + "?? ?��그레?��?�� 불�??�� ?��?��?��.");
			LOG.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			throw new IllegalArgumentException(user.getLevel() + "?? ?��그레?��?�� 불�??�� sss?��?��?��.");
		}

		userDao.doUpdate(user);
		
		//mail send
		sendUpgradeEmail(user);
		
		
	}
	
	/**
	 * ?��?��?��?�� 코드�? ?��?��?��?�� ?��?���? : UserServiceTx�? ?��?��?��?��?�� (DI)?��?�� 주입 �?�?기능 �??��.
	 * ?��?��?�� ?��?��
	 * 1. ?���? ?��?��?���? ?��?�� ?��?��?��.
	 * 2. ?��?�� ???��?�� ?��별한?��.
	 *   2.1. BASIC ?��?��?��: 로그?�� CNT 50  ?��?��(=?��?��)?���? -> SILVER
	 *   2.2. SILVER ?��?��?�� : 추천CNT 30?��?��?���? (=?��?��)?���? -> GOLD
	 *   2.3. GOLD???�� ?��?��
	 * 3. ?��?��?��?��.
	 * @throws SQLException 
	 * 
	 */
	public void upgradeLevels(UserVO userVO) throws Exception {
		//?��?��?��?��기능?�� ?���??��?�� ?��?���?�? UserServiceTx
		List<UserVO> list = (List<UserVO>) userDao.getAll(userVO);
		for(UserVO user:list) {
			if(canUpgradeLevel(user)==true ) {		//upgrade???�� ?���?
				upgradeLevel(user);					//upgrade?��?��
			}
		}//for
	
	}

	@Override
	public int doInsert(DTO dto) {
		return userDao.doInsert(dto);
	}

	@Override
	public int doUpdate(DTO dto) {
		return userDao.doUpdate(dto);
	}

	@Override
	public DTO doSelectOne(DTO dto) {
		return userDao.doSelectOne(dto);
	}

	@Override
	public int doDelete(DTO dto) {
		return userDao.doDelete(dto);
	}

	@Override
	public List<?> doRetrieve(DTO dto) {
		return userDao.doRetrieve(dto);
	}
	
	
}
