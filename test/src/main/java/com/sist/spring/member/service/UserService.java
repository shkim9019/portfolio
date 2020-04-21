package com.sist.spring.member.service;

import java.sql.SQLException;
import java.util.List;

import com.sist.spring.cmn.DTO;



public interface UserService {

	/**
	 * ?���?
	 * @param dto
	 * @return int
	 */
	public int doInsert(DTO dto);
	
	/**
	 * ?��?��
	 * @param dto
	 * @return int
	 */
	public int doUpdate(DTO dto);
	
	/**
	 * ?��건조?��
	 * @param dto
	 * @return DTO
	 */
	public DTO doSelectOne(DTO dto);
	
	/**
	 * ?��?��
	 * @param dto
	 * @return int
	 */
	public int doDelete(DTO dto);
	
	/**
	 * 목록조회
	 * @param dto
	 * @return List<DTO>
	 */
	public List<?> doRetrieve(DTO dto);
	
	/**
     * 최초�??��?�� : Level.BASIC
     * @param user
     */
	int add(UserVO user);

	/**
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
	void upgradeLevels(UserVO userVO) throws Exception;

}