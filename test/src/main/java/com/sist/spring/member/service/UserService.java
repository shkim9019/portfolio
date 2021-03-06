package com.sist.spring.member.service;

import java.sql.SQLException;
import java.util.List;

import com.sist.spring.cmn.DTO;



public interface UserService {

	/**
	 * ?±λ‘?
	 * @param dto
	 * @return int
	 */
	public int doInsert(DTO dto);
	
	/**
	 * ?? 
	 * @param dto
	 * @return int
	 */
	public int doUpdate(DTO dto);
	
	/**
	 * ?¨κ±΄μ‘°?
	 * @param dto
	 * @return DTO
	 */
	public DTO doSelectOne(DTO dto);
	
	/**
	 * ?­? 
	 * @param dto
	 * @return int
	 */
	public int doDelete(DTO dto);
	
	/**
	 * λͺ©λ‘μ‘°ν
	 * @param dto
	 * @return List<DTO>
	 */
	public List<?> doRetrieve(DTO dto);
	
	/**
     * μ΅μ΄κ°??? : Level.BASIC
     * @param user
     */
	int add(UserVO user);

	/**
	 * ?¬?©? ?±?
	 * 1. ? μ²? ?¬?©?λ₯? ?½?΄ ?€?Έ?€.
	 * 2. ?±? ???? ? λ³ν?€.
	 *   2.1. BASIC ?¬?©?: λ‘κ·Έ?Έ CNT 50  ?΄?(=?¬?¨)?΄λ©? -> SILVER
	 *   2.2. SILVER ?¬?©? : μΆμ²CNT 30?΄??΄λ©? (=?¬?¨)?΄λ©? -> GOLD
	 *   2.3. GOLD??? ??
	 * 3. ?±???€.
	 * @throws SQLException 
	 * 
	 */
	void upgradeLevels(UserVO userVO) throws Exception;

}