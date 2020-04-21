package com.sist.spring.member.service;

import java.util.List;

import com.sist.spring.cmn.DTO;

public interface UserDao {
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
	 * ?��체조?��
	 * @param dto
	 * @return List<DTO>
	 */
	public List<?> getAll(DTO dto);
}
