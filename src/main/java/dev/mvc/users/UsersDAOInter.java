package dev.mvc.users;
import java.util.ArrayList;
import java.util.HashMap;  // class

import jakarta.servlet.http.HttpSession;

public interface UsersDAOInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String id);

  /**
   * 회원 가입
   * @param memberVO
   * @return 추가한 레코드 갯수
   */
  public int create(UsersVO usersVO);

  /**
   * 회원 전체 목록
   * @return
   */
  public ArrayList<UsersVO> list();

  /**
   * memberno로 회원 정보 조회
   * @param memberno
   * @return
   */
  public UsersVO read(int usersno);

  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public UsersVO readById(String id);

  /**
   * 수정 처리
   * @param memberVO
   * @return
   */
  public int update(UsersVO usersVO);

  /**
   * 회원 삭제 처리
   * @param memberno
   * @return
   */
  public int delete(int usersno);

  /**
   * 현재 패스워드 검사
   * @param map
   * @return 0: 일치하지 않음, 1: 일치함
   */
  public int passwd_check(HashMap<String, Object> map);

  /**
   * 패스워드 변경
   * @param map
   * @return 변경된 패스워드 갯수
   */
  public int passwd_update(HashMap<String, Object> map);

  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);

  /**
   * 로그인된 회원 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isUsers(HttpSession session);

  /**
   * 로그인된 회원 관리자 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isAdmin(HttpSession session);

}