/**
 * 
 */
package com.sist.spring.member.service.imple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sist.spring.cmn.DTO;
import com.sist.spring.cmn.SearchVO;
import com.sist.spring.member.service.Level;
import com.sist.spring.member.service.UserDao;
import com.sist.spring.member.service.UserVO;

/**
 * @author sist
 *
 */
//@Repository("userDao"): userDao?��?��?���? 객체�? ?��?���??��것임 (같�??��름의 ?��?��?���? ?��?��?��?��?��?��)	
@Repository
public class UserDaoImple implements UserDao {
	//Logger 
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	RowMapper<UserVO> rowMapper = new RowMapper<UserVO>() {
		public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserVO outData = new UserVO();	//?��?��?�� ?��각하면됨 - ?��?�� ?��?��?�� ?���? 반환
			//rs.getString같�?거는 ?��차피 db?�� ?��?�� 컬럼?��?���? �??��?��?�� 것이�?�? ???��문자 ?�� ??문자�? ?��
			outData.setU_id(rs.getString("u_id"));		
			outData.setName(rs.getString("name"));
			outData.setPasswd(rs.getString("passwd"));
			
			//-----------------------
			//2020/04/09 ?��?�� ?���? 추�?
			//-----------------------
			outData.setLevel(Level.valueOf(rs.getInt("u_level")));	//?��?��값을 받아?�� enum?�� level?�� 반환
			outData.setLogin(rs.getShort("login"));
			outData.setRecommend(rs.getInt("recommend"));
			outData.setEmail(rs.getString("mail"));
			outData.setRegDt(rs.getString("reg_dt"));
			outData.setNum(rs.getInt("rnum"));				//DTO?�� ?��?���??��?��(?��건조?��?��)
			outData.setTotalCnt(rs.getInt("total_cnt"));	//DTO?�� ?��?���??��?��(?��건조?��?��)
			
			return outData;
		}
	};
	
	
	//JDBCTemplate
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserDaoImple() {}
	



	public int doInsert(DTO dto) {
		int flag = 0;
		UserVO inVO = (UserVO) dto;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO hr_member (	\n");
		sb.append("    u_id,                \n");
		sb.append("    name,                \n");
		sb.append("    passwd,              \n");
		sb.append("    u_level,             \n");
		sb.append("    login,               \n");
		sb.append("    recommend,           \n");
		sb.append("    mail                 \n");
		sb.append(") VALUES (               \n");
		sb.append("    ?,                   \n");
		sb.append("    ?,                   \n");
		sb.append("    ?,                   \n");
		sb.append("    ?,                   \n");
		sb.append("    ?,                   \n");
		sb.append("    ?,                   \n");
		sb.append("    ?                    \n");
		sb.append(")                        \n");
		
		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		LOG.debug("Param:" + inVO.toString());
		LOG.debug("=====================");
		
		Object[] args = {inVO.getU_id()
						,inVO.getName()
						,inVO.getPasswd()
						,inVO.getLevel().intValue()			//db?�� ?��?��?��?�� int?��
						,inVO.getLogin()
						,inVO.getRecommend()
						,inVO.getEmail()
		};	//?��?��?��?�� inVO?�� ?��?��?���? �? ?��?��(배열args?�� �? ?��?��)
		flag = this.jdbcTemplate.update(sb.toString(), args);
		
		return flag;
	}

	public int doUpdate(DTO dto) {
		int flag = 0;
		UserVO inVO =(UserVO) dto;
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE hr_member			\n");
		sb.append("SET                      \n");
		sb.append("    name = ?,            \n");
		sb.append("    passwd = ?,          \n");
		sb.append("    u_level = ?,         \n");
		sb.append("    login = ?,           \n");
		sb.append("    recommend = ?,       \n");
		sb.append("    mail = ?,            \n");
		sb.append("    reg_dt = sysdate     \n");
		sb.append("WHERE                    \n");
		sb.append("    u_id = ?             \n");
		
		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		LOG.debug("Param:" + inVO.toString());
		LOG.debug("=====================");
		
		Object[] args = {inVO.getName()
					     ,inVO.getPasswd()
					     ,inVO.getLevel().intValue()
					     ,inVO.getLogin()
					     ,inVO.getRecommend()
					     ,inVO.getEmail()
					     ,inVO.getU_id()
					     };	//?��?��?��?�� inVO?�� ?��?��?���? �? ?��?��(배열args?�� �? ?��?��)
		LOG.debug("flag=" + flag);
		flag = this.jdbcTemplate.update(sb.toString(), args);
		
		
		return flag;
	}
	
	public int count(DTO dto) {
		int cnt = 0;
		UserVO inVO = (UserVO) dto;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) cnt	\n");
		sb.append("FROM hr_member       \n");
		sb.append("WHERE u_id like ?    \n");
		
		
		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		LOG.debug("Param:" + inVO.toString());
		LOG.debug("=====================");
		//required type??(받을 ???��) Integer.class�? 주면 queryForObject 반환?��?�� int�? 바�?? class?���?�? Integer.class?��
		//?��번째 ?��?��?�� required type?�� �??��?��?�� 방법
		cnt = this.jdbcTemplate.queryForObject(sb.toString(), new Object[] {"%"+inVO.getU_id()+"%" } 
				,Integer.class);
		
		LOG.debug("cnt:" + cnt);
		LOG.debug("=====================");
		
		return cnt;
	}
	
	public DTO doSelectOne(DTO dto) {
		UserVO outVO = null;				//return UserVO
		UserVO inVO = (UserVO) dto;			//Param  UserVO
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT														\n");
		sb.append("    u_id,                                                    \n");
		sb.append("    name,                                                    \n");
		sb.append("    passwd,                                                  \n");
		sb.append("    u_level,                                                 \n");
		sb.append("    login,                                                   \n");
		sb.append("    recommend,                                               \n");
		sb.append("    mail,                                                    \n");
		sb.append("    TO_CHAR(reg_dt, 'YYYY/MM/DD HH24:MI:SS') AS reg_dt,      \n");
		//rowmapper ?��?��?�� ?��?��?�� ?��?��.
		sb.append("    1 rnum,                  								\n");
		sb.append("    1 total_cnt              								\n");
		//--rowmapper ?��?��?�� ?��?��?�� ?��?��.
		sb.append("FROM                                                         \n");
		sb.append("    hr_member                                                \n");
		sb.append("WHERE u_id = ?                                               \n");
		
		
		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		//LOG.debug("Param:" + inVO);
		LOG.debug("Param:" + inVO.getU_id());	//u_id ?��?��?���? ?��?���? ?��?�� ?��
		LOG.debug("=====================");
		
		Object[] args = {inVO.getU_id()};
		
		//queryForObject?�� 반환값�? T???��?��?�� <UserVO>�? 줬으�?�? 반환?? UserVO?��			?��번째 ?��?��?�� RowMapper�? ??(?��건조?��?�� 경우)
		//jdbcTemplate.queryForObject?�� T???�� 객체�? 반환
		outVO=this.jdbcTemplate.queryForObject(sb.toString(), args,rowMapper);
		
		LOG.debug("outVO:" + outVO);
		LOG.debug("=====================");
		
		
		return outVO;
	}

	public int doDelete(DTO dto) {
		int flag = 0;
		UserVO inVO=(UserVO) dto;
		
		StringBuilder sb = new StringBuilder();
		sb.append("delete from hr_member \n");
		sb.append("WHERE u_id = ? 		 \n");

		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		LOG.debug("Param:" + inVO);
		LOG.debug("=====================");
		
		Object[] args = {inVO.getU_id()};
		
		flag=jdbcTemplate.update(sb.toString(), args);
		
		LOG.debug("flag:" + flag);
		LOG.debug("=====================");
		
		return flag;
	}
	
	/**
	 * 
	 *Method Name:getAll
	 *?��?��?��: 2020. 4. 8.
	 *?��?��?��: sist
	 *?���?: ?��체조?��
	 *@param dto
	 *@return
	 */
	public List<UserVO> getAll(DTO dto) {
		UserVO inVO = (UserVO) dto;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT														\n");
		sb.append("    u_id,                                                    \n");
		sb.append("    name,                                                    \n");
		sb.append("    passwd,                                                  \n");
		sb.append("    u_level,                                                 \n");
		sb.append("    login,                                                   \n");
		sb.append("    recommend,                                               \n");
		sb.append("    mail,                                                    \n");
		sb.append("    TO_CHAR(reg_dt, 'YYYY/MM/DD HH24:MI:SS') AS reg_dt,      \n");
		//rowmapper ?��?��?�� ?��?��?�� ?��?��.
		sb.append("    1 rnum,                  								\n");
		sb.append("    1 total_cnt              								\n");
		//--rowmapper ?��?��?�� ?��?��?�� ?��?��.
		sb.append("FROM                                                         \n");
		sb.append("    hr_member                                                \n");
		sb.append("WHERE u_id like ?   											\n");
		sb.append("ORDER BY u_id       											\n");
		LOG.debug("=====================");
		LOG.debug("Query:" + sb.toString());
		LOG.debug("Param:" + inVO);
		LOG.debug("=====================");
		
		
		//jdbcTemplate.query?�� list�? 반환
		List<UserVO> list =this.jdbcTemplate.query(sb.toString(), new Object[] {"%"+inVO.getU_id()+"%" }
				,rowMapper);
		LOG.debug("list:" + list);
		LOG.debug("=====================");
		
		return list;
	}
	
	
	public List<?> doRetrieve(DTO dto) {
		SearchVO inVO = (SearchVO) dto;
		//�??��구분
		  //ID : 10
		  //?���? : 20
		//�??��?��
		StringBuilder whereSb = new StringBuilder();
		
		if(null != inVO && !"".equals(inVO.getSearchDiv())) {
			if(inVO.getSearchDiv().equals("10")) {
				whereSb.append("WHERE U_ID LIKE '%'|| ? || '%'  \n");
			}else if(inVO.getSearchDiv().equals("20")) {
				whereSb.append("WHERE NAME LIKE '%'|| ? || '%'  \n");
			}
		}
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT T1.*,T2.*														\n");
		sb.append("FROM(                                                                \n");
		sb.append("    SELECT 	                                                        \n");
		sb.append("             B.U_ID,													\n");
		sb.append("             B.NAME,                                 				 \n");
		sb.append("             B.PASSWD,                                				\n");
		sb.append("             B.U_LEVEL,                               				\n");
		sb.append("             B.LOGIN,                                 				\n");
		sb.append("             B.RECOMMEND,                            				\n");
		sb.append("             B.MAIL,                                  				\n");
		sb.append("             TO_CHAR(B.REG_DT,'YYYY/MM/DD') REG_DT,   				\n");
		sb.append("             RNUM                                     				\n");
		sb.append("    FROM(                                                            \n");
		sb.append("        SELECT ROWNUM rnum,                                          \n");
		sb.append("               A.*                                                   \n");
		sb.append("        FROM (                                                       \n");
		sb.append("            SELECT *                                                 \n");
		sb.append("            FROM hr_member                                           \n");
		sb.append("            --�??��조건                                               						    \n");
		//--�??��-----------------------------------------------------------------------------
		sb.append(whereSb.toString());
		//--�??��-----------------------------------------------------------------------------
		sb.append("            ) A --?��?���? 10                                            \n");
		//sb.append("            WHERE ROWNUM<=(&PAGE_SIZE*(&PAGE_NUM-1) + &PAGE_SIZE)  \n");
		sb.append("            WHERE ROWNUM<=(?*(?-1) + ?)   							\n");
		sb.append("        )B --?��?�� NUM 1                                                \n");
		//sb.append("    WHERE B.RNUM >=(&PAGE_SIZE*(&PAGE_NUM-1)+1)                    \n");
		sb.append("    WHERE B.RNUM >=(?*(?-1)+1)           				            \n");
		sb.append(")T1 CROSS JOIN                                                       \n");
		sb.append("(                                                                    \n");
		sb.append("SELECT COUNT(*) total_cnt                                            \n");
		sb.append("FROM hr_member                                                       \n");
		sb.append("--�??��조건                                                            							    \n");
		//--�??��-----------------------------------------------------------------------------
		sb.append(whereSb.toString());
		//--�??��-----------------------------------------------------------------------------
		sb.append(")T2                                                                  \n");
		
		
		//param
		//list.add?��?�� ?���? ?��?��?���?�?
		List<Object> listArg = new ArrayList<Object>();
		
		
		//param set
		if(null != inVO && !"".equals(inVO.getSearchDiv())) {
			listArg.add(inVO.getSearchWord());
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageNum());
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageNum());
			listArg.add(inVO.getSearchWord());
		} else {
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageNum());
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageSize());
			listArg.add(inVO.getPageNum());
			
		}
		//listArg.toArray() list�? Object배열�? 바꿔�??��.
		
		List<UserVO> retList = this.jdbcTemplate.query(sb.toString(), listArg.toArray(), rowMapper);
		LOG.debug("query: \n"+sb.toString());
		LOG.debug("param: \n"+listArg);
		return retList;
	}

}
