package dev.mvc.dcontents;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface DcontentsDAOInter {
  /**
   * 등록, 추상 메소드
   * @param dcontentsVO
   * @return
   */
  public int create(DcontentsVO dcontentsVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<DcontentsVO> list_all();

  /**
   * 카테고리별 등록된 글 목록
   * @param cateno
   * @return
   */
  public ArrayList<DcontentsVO> list_by_dogid(int dogid);

  /**
   * 조회
   * @param dogcontno
   * @return
   */
  public DcontentsVO read(int dogcontno);

  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);

  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */

  public ArrayList<DcontentsVO> list_by_dogid_search(HashMap<String, Object> hashMap);

  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_dogid_search_count(HashMap<String, Object> hashMap);

  /**
   * 카테고리별 검색 목록 + 페이징
   * @param contentsVO
   * @return
   */
  public ArrayList<DcontentsVO> list_by_dogid_search_paging(HashMap<String, Object> map);

  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);

  /**
   * 글 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(DcontentsVO dcontentsVO);

  /**
   * 파일 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(DcontentsVO dcontentsVO);

  /**
   * 삭제
   * @param contentsno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int dogcontno);

  /**
   * FK cateno 값이 같은 레코드 갯수 산출
   * @param cateno
   * @return
   */
  public int count_by_dogid(int dogid);

  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param cateno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_dogid(int dogid);

  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_usersno(int usersno);

  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param memberno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_usersno(int usersno);

  /**
   * 글 수 증가
   * @param
   * @return
   */
  public int increaseReplycnt(int dogcontno);

  /**
   * 글 수 감소
   * @param
   * @return
   */
  public int decreaseReplycnt(int dogcontno);
  
  public int update_cnt_up(int dogid);
  
  public int update_cnt_down(int dogid);
  
  public int update_cnt_breed_up(String breed);
  
  public int update_cnt_breed_down(String breed);
  
  public int increaseRecom(int dogcontno);
  
  public int decreaseRecom(int dogcontno);
}